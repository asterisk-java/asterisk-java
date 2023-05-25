/*
 * Copyright 2004-2022 Asterisk-Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.manager.util;

import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.AstUtil;
import org.asteriskjava.util.ReflectionUtil;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Piotr Olaszewski <piotrooo>
 */
public class EventAttributesHelper {
    private static final Logger logger = getLogger(EventAttributesHelper.class);

    private EventAttributesHelper() {
    }

    public static void setAttributes(Object target, Map<String, Object> attributes, Set<String> ignoredAttributes) {
        Map<String, Method> setters;

        setters = ReflectionUtil.getSetters(target.getClass());
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            Object value;
            final Class<?> dataType;
            Method setter;
            String setterName;

            if (ignoredAttributes != null && ignoredAttributes.contains(entry.getKey())) {
                continue;
            }

            setterName = ReflectionUtil.stripIllegalCharacters(entry.getKey());

            /*
             * The source property needs special handling as it is already
             * defined in java.util.EventObject (the base class of
             * ManagerEvent), so we have to translate it.
             */
            if ("source".equals(setterName)) {
                setterName = "src";
            } else if ("class".equals(setterName)) {
                setterName = "clazz";
            }

            setter = setters.get(setterName);

            if (setter == null && !setterName.endsWith("s")) // no exact match
            // => try plural
            {
                setter = setters.get(setterName + "s");
                // but only for maps
                if (setter != null && !(setter.getParameterTypes()[0].isAssignableFrom(Map.class))) {
                    setter = null;
                }
            }

            // it seems silly to warn if it's a user event -- maybe it was
            // intentional
            if (setter == null && !(target instanceof UserEvent) && !target.getClass().equals(ManagerResponse.class)) {

                //CDR has dynamic properties
                if (target instanceof CdrEvent) {
                    try {
                        Method addproperty= CdrEvent.class.getMethod("addDynamicProperties", String.class, String.class);
                        addproperty.invoke(target, entry.getKey(), entry.getValue().toString());
                    } catch (Exception e) {
                        logger.error("Unable to set Dynamic CDR Property '" + entry.getKey() + "' to '" + entry.getValue(), e);
                        continue;
                    }

                } else {
                    logger.warn("Unable to set property '" + entry.getKey() + "' to '" + entry.getValue() + "' on "
                        + target.getClass().getName()
                        + ": no setter. Please report at https://github.com/asterisk-java/asterisk-java/issues");

                    for (Map.Entry<String, Object> entry2 : attributes.entrySet()) {
                        logger.debug("Key: " + entry2.getKey() + " Value: " + entry2.getValue());
                    }
                }
            }

            if (setter == null) {
                continue;
            }

            try {
                dataType = setter.getParameterTypes()[0];

                if (dataType == Boolean.class) {
                    value = AstUtil.isTrue(entry.getValue());
                } else if (dataType.isAssignableFrom(String.class)) {
                    value = parseString(entry);
                } else if (dataType.isAssignableFrom(Map.class)) {
                    value = parseMap(entry);
                } else if (dataType.isAssignableFrom(double.class) || dataType.isAssignableFrom(Double.class)) {
                    value = parseDouble(entry);
                } else if (dataType.isAssignableFrom(long.class) || dataType.isAssignableFrom(Long.class)) {
                    value = parseLong(entry);
                } else if (dataType.isAssignableFrom(int.class) || dataType.isAssignableFrom(Integer.class)) {
                    value = parseInteger(entry);
                } else {
                    try {
                        Constructor<?> constructor = dataType.getConstructor(String.class);
                        // Asterisk sometimes uses yes/no instead of True/False for boolean.  java.lang.Boolean(String) doesn't handle this.
                        if (dataType.isAssignableFrom(Boolean.class)) {
                            value = constructor.newInstance(AstUtil.convertAsteriskBooleanStringToStandardBooleanString((String) entry.getValue()));
                        } else value = constructor.newInstance(entry.getValue());
                    } catch (Exception e) {
                        logger.error("Unable to convert value: Called the constructor of " + dataType + " with value '"
                            + entry.getValue() + "' for the attribute '" + entry.getKey() + "'\n of event type "
                            + target.getClass().getName() + " with resulting error: " + e.getMessage(), e);
                        continue;
                    }
                }

                setter.invoke(target, value);
            } catch (Exception e) {
                logger.error("Unable to set property '" + entry.getKey() + "' to '" + entry.getValue() + "' on "
                    + target.getClass().getName() + " " + e.getMessage(), e);
            }
        }
    }

    private static Integer parseInteger(Map.Entry<String, Object> entry) {
        Integer value;
        String stringValue = (String) entry.getValue();
        if (stringValue != null && stringValue.length() > 0) {
            value = Integer.parseInt(stringValue);
        } else {
            value = null;
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> parseMap(Map.Entry<String, Object> entry) {
        Map<String, String> value;
        if (entry.getValue() instanceof List) {
            List<String> list = (List<String>) entry.getValue();
            value = buildMap(list.toArray(new String[list.size()]));
        } else if (entry.getValue() instanceof String) {
            value = buildMap((String) entry.getValue());
        } else {
            value = null;
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Object parseString(Map.Entry<String, Object> entry) {
        Object value;
        value = entry.getValue();
        if (AstUtil.isNull(value)) {

            value = null;
        }
        if (value instanceof List) {
            StringBuilder strBuff = new StringBuilder();
            for (String tmp : (List<String>) value) {
                if (tmp != null && tmp.length() != 0) {
                    strBuff.append(tmp).append('\n');
                }
            }
            value = strBuff.toString();
        }
        return value;
    }

    private static Double parseDouble(Map.Entry<String, Object> entry) {
        Double value;
        String stringValue = (String) entry.getValue();
        if (stringValue != null && stringValue.length() > 0) {
            value = Double.parseDouble(stringValue);
        } else {
            value = null;
        }
        return value;
    }

    private static Long parseLong(Map.Entry<String, Object> entry) {
        Long value;
        String stringValue = (String) entry.getValue();
        if (stringValue != null && stringValue.length() > 0) {
            value = Long.parseLong(stringValue);
        } else {
            value = null;
        }
        return value;
    }

    private static Map<String, String> buildMap(String... lines) {
        if (lines == null) {
            return null;
        }

        final Map<String, String> map = new LinkedHashMap<>();
        for (String line : lines) {
            final int index = line.indexOf('=');
            if (index > 0) {
                final String key = line.substring(0, index);
                final String value = line.substring(index + 1, line.length());
                map.put(key, value);
            } else {
                logger.warn("Malformed line '" + line + "' for a map property");
            }
        }
        return map;
    }
}
