/*
 * Copyright 2004-2023 Asterisk Java contributors
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
package org.asteriskjava.core.databind.reader;

import org.apache.commons.lang3.tuple.Pair;
import org.asteriskjava.core.databind.TypeConversionRegister;
import org.asteriskjava.core.databind.TypeConversionRegister.Converter;
import org.asteriskjava.core.databind.annotation.AsteriskDeserialize;
import org.asteriskjava.core.databind.deserializer.AsteriskDeserializer;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static org.asteriskjava.core.databind.utils.AnnotationUtils.getName;
import static org.asteriskjava.core.databind.utils.Invoker.invokeOn;
import static org.asteriskjava.core.databind.utils.ReflectionUtils.getSetters;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reader class to read content and transform for the desired object.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskObjectReader {
    private static final Logger logger = getLogger(AsteriskObjectReader.class);

    public <T> T read(Map<String, Object> content, Class<T> clazz) {
        Map<String, Method> setters = getSetters(clazz)
                .entrySet()
                .stream()
                .collect(toMap(e -> getName(e.getValue(), e.getKey()), Entry::getValue));

        T resultObject = instantiateResultClass(clazz);
        for (Entry<String, Object> entry : content.entrySet()) {
            handleEntry(entry, setters, resultObject);
        }
        return resultObject;
    }

    private static <T> T instantiateResultClass(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate result class %s".formatted(clazz), e);
        }
    }

    private static <T> void handleEntry(Entry<String, Object> entry, Map<String, Method> setters, T resultObject) {
        Object currentValue = entry.getValue();

        Method method = setters.getOrDefault(entry.getKey(), null);
        if (method == null) {
            logger.warn("Unable to set the '{}' property to the value '{}' in the '{}' class. There is no setter method available. " +
                            "Please report at https://github.com/asterisk-java/asterisk-java/issues.",
                    entry.getKey(), currentValue, resultObject.getClass().getName());
            return;
        }

        Object value = getValue(currentValue, method);
        invokeOn(resultObject).method(method).withParameter(value);
    }

    private static Object getValue(Object currentValue, Method method) {
        Class<?> targetDataType = method.getParameterTypes()[0];
        if (targetDataType.isEnum()) {
            return parseEnum(method, currentValue);
        } else if (targetDataType.isAssignableFrom(List.class)) {
            return parseList(method, currentValue);
        } else if (targetDataType.isAssignableFrom(Map.class)) {
            return parseMap(method, currentValue);
        } else {
            return parseOtherType(method, currentValue);
        }
    }

    private static Enum<?> parseEnum(Method method, Object currentValue) {
        Class<?> targetDataType = method.getParameterTypes()[0];
        return (Enum<?>) stream(targetDataType.getEnumConstants())
                .filter(t -> t.toString().equals(String.valueOf(currentValue)))
                .findFirst()
                .orElse(null);
    }

    private static List<Object> parseList(Method method, Object currentValue) {
        ParameterizedType genericParameter = (ParameterizedType) method.getGenericParameterTypes()[0];
        Class<?> listElementType = (Class<?>) genericParameter.getActualTypeArguments()[0];

        Map<Class<?>, Converter<?, ?>> conversions = TypeConversionRegister.TYPE_CONVERTERS.get(listElementType);

        return ((List<?>) currentValue)
                .stream()
                .map(object -> {
                    @SuppressWarnings("rawtypes")
                    Converter converter = conversions.get(object.getClass());
                    //noinspection unchecked
                    return converter.apply(object);
                })
                .toList();
    }

    private static Map<Object, Object> parseMap(Method method, Object currentValue) {
        AsteriskDeserializer<Pair<String, String>> asteriskDeserializer = getAsteriskDeserializer(method);

        ParameterizedType genericParameter = (ParameterizedType) method.getGenericParameterTypes()[0];
        Class<?> mapKeyType = (Class<?>) genericParameter.getActualTypeArguments()[0];
        Class<?> mapValueType = (Class<?>) genericParameter.getActualTypeArguments()[1];

        Map<Class<?>, Converter<?, ?>> keyConversions = TypeConversionRegister.TYPE_CONVERTERS.get(mapKeyType);
        Map<Class<?>, Converter<?, ?>> valueConversions = TypeConversionRegister.TYPE_CONVERTERS.get(mapValueType);

        if (currentValue instanceof List) {
            Map<Object, Object> map = new LinkedHashMap<>();
            for (Object item : (List<?>) currentValue) {
                Pair<Object, Object> converted = convertMapItem(item, asteriskDeserializer, keyConversions, valueConversions);
                map.put(converted.getKey(), converted.getValue());
            }
            return map;
        } else {
            Pair<Object, Object> converted = convertMapItem(currentValue, asteriskDeserializer, keyConversions, valueConversions);
            return Map.of(converted.getKey(), converted.getValue());
        }
    }

    private static Object parseOtherType(Method method, Object currentValue) {
        Class<?> sourceType = currentValue.getClass();
        Class<?> targetType = method.getParameterTypes()[0];

        Map<Class<?>, Converter<?, ?>> conversions = TypeConversionRegister.TYPE_CONVERTERS.get(targetType);
        @SuppressWarnings("rawtypes")
        Converter converter = conversions.get(sourceType);
        //noinspection unchecked
        return converter.apply(currentValue);
    }

    private static AsteriskDeserializer<Pair<String, String>> getAsteriskDeserializer(Method method) {
        AsteriskDeserialize asteriskDeserialize = method.getAnnotation(AsteriskDeserialize.class);
        if (asteriskDeserialize == null) {
            throw new RuntimeException("Map should have defined deserializer");
        }

        try {
            Class<? extends AsteriskDeserializer<?>> asteriskDeserializerClass = asteriskDeserialize.value();
            //noinspection unchecked
            return (AsteriskDeserializer<Pair<String, String>>) asteriskDeserializerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new instance of deserializer %s".formatted(asteriskDeserialize), e);
        }
    }

    private static Pair<Object, Object> convertMapItem(
            Object object,
            AsteriskDeserializer<Pair<String, String>> asteriskDeserializer,
            Map<Class<?>, Converter<?, ?>> keyConversions,
            Map<Class<?>, Converter<?, ?>> valueConversions
    ) {
        Pair<String, String> deserialize = asteriskDeserializer.deserialize(String.valueOf(object));

        @SuppressWarnings("rawtypes")
        Converter keyConverter = keyConversions.get(String.class);
        @SuppressWarnings("rawtypes")
        Converter valueConverter = valueConversions.get(String.class);

        String key = String.valueOf(deserialize.getKey());
        @SuppressWarnings("unchecked")
        Object keyValue = keyConverter.apply(key);

        String value = String.valueOf(deserialize.getValue());
        @SuppressWarnings("unchecked")
        Object valueValue = valueConverter.apply(value);

        return Pair.of(keyValue, valueValue);
    }
}
