package org.asteriskjava.manager.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.util.AstUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.ReflectionUtil;

/**
 * Abstract base class for reflection based builders.
 */
abstract class AbstractBuilder
{
    protected final Log logger = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
    protected void setAttributes(Object target, Map<String, Object> attributes, Set<String> ignoredAttributes)
    {
        Map<String, Method> setters;

        setters = ReflectionUtil.getSetters(target.getClass());
        for (Map.Entry<String, Object> entry : attributes.entrySet())
        {
            Object value;
            final Class< ? > dataType;
            Method setter;
            String setterName;

            if (ignoredAttributes != null && ignoredAttributes.contains(entry.getKey()))
            {
                continue;
            }

            setterName = ReflectionUtil.stripIllegalCharacters(entry.getKey());

            /*
             * The source property needs special handling as it is already
             * defined in java.util.EventObject (the base class of
             * ManagerEvent), so we have to translate it.
             */
            if ("source".equals(setterName))
            {
                setterName = "src";
            }
            else if ("class".equals(setterName))
            {
                setterName = "clazz";
            }

            setter = setters.get(setterName);

            if (setter == null && !setterName.endsWith("s")) // no exact match
                                                             // => try plural
            {
                setter = setters.get(setterName + "s");
                // but only for maps
                if (setter != null && !(setter.getParameterTypes()[0].isAssignableFrom(Map.class)))
                {
                    setter = null;
                }
            }

            // it seems silly to warn if it's a user event -- maybe it was
            // intentional
            if (setter == null && !(target instanceof UserEvent))
            {
                logger.warn("Unable to set property '" + entry.getKey() + "' to '" + entry.getValue() + "' on "
                        + target.getClass().getName()
                        + ": no setter. Please report at https://github.com/asterisk-java/asterisk-java/issues");
            }

            if (setter == null)
            {
                continue;
            }

            dataType = setter.getParameterTypes()[0];

            if (dataType == Boolean.class)
            {
                value = AstUtil.isTrue(entry.getValue());
            }
            else if (dataType.isAssignableFrom(String.class))
            {
                value = entry.getValue();
                if (AstUtil.isNull(value))
                {

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
            }
            else if (dataType.isAssignableFrom(Map.class))
            {
                if (entry.getValue() instanceof List)
                {
                    List<String> list = (List<String>) entry.getValue();
                    value = buildMap(list.toArray(new String[list.size()]));
                }
                else if (entry.getValue() instanceof String)
                {
                    value = buildMap((String) entry.getValue());
                }
                else
                {
                    value = null;
                }
            }
            else
            {
                try
                {
                    Constructor< ? > constructor = dataType.getConstructor(String.class);
                    value = constructor.newInstance(entry.getValue());
                }
                catch (Exception e)
                {
                    logger.error("Unable to convert value: Called the constructor of " + dataType + " with value '"
                            + entry.getValue() + "' for the attribute '" + entry.getKey() + "'\n of event type "
                            + target.getClass().getName() + " with resulting error: " + e.getMessage(), e);
                    continue;
                }
            }

            try
            {
                setter.invoke(target, value);
            }
            catch (Exception e)
            {
                logger.error("Unable to set property '" + entry.getKey() + "' to '" + entry.getValue() + "' on "
                        + target.getClass().getName(), e);
            }
        }
    }

    private Map<String, String> buildMap(String... lines)
    {
        if (lines == null)
        {
            return null;
        }

        final Map<String, String> map = new LinkedHashMap<>();
        for (String line : lines)
        {
            final int index = line.indexOf('=');
            if (index > 0)
            {
                final String key = line.substring(0, index);
                final String value = line.substring(index + 1, line.length());
                map.put(key, value);
            }
            else
            {
                logger.warn("Malformed line '" + line + "' for a map property");
            }
        }
        return map;
    }
}
