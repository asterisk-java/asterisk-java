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
package org.asteriskjava.core.databind;

import org.apache.commons.lang3.tuple.Pair;
import org.asteriskjava.core.NewlineDelimiter;
import org.asteriskjava.core.databind.TypeConversionRegister.Converter;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Locale.ENGLISH;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.asteriskjava.core.databind.CodersConsts.nameValueSeparator;
import static org.asteriskjava.core.databind.TypeConversionRegister.TYPE_CONVERTERS;
import static org.asteriskjava.core.databind.utils.AnnotationUtils.getBucketMethod;
import static org.asteriskjava.core.databind.utils.AnnotationUtils.getName;
import static org.asteriskjava.core.databind.utils.Invoker.invokeOn;
import static org.asteriskjava.core.databind.utils.ReflectionUtils.getSetters;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskDecoder {
    private static final Logger logger = getLogger(AsteriskDecoder.class);

    /**
     * Handles values that look like a list. E.g.:
     * <pre>
     * Category-000001: cat1
     * Category-000002: cat2
     * Category-000003: cat3
     * </pre>
     */
    private static final Pattern LIST_DETECTOR = compile("-\\d\\d\\d\\d\\d\\d$");

    public <T> T decode(String source, NewlineDelimiter newlineDelimiter, Class<T> target) {
        String[] content = source.split(newlineDelimiter.getPattern());
        return decode(content, target);
    }

    public <T> T decode(String[] source, Class<T> target) {
        return decode(toMap(source), target);
    }

    public <T> T decode(Map<String, Object> source, Class<T> target) {
        Map<String, Method> setters = getSetters(target)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> getName(e.getValue(), e.getKey()).toLowerCase(ENGLISH), Entry::getValue));

        Method bucketMethod = getBucketMethod(setters.values());

        Map<String, String> bucket = bucketMethod != null ? new LinkedHashMap<>() : null;

        T result = instantiateResultClass(target);
        for (Entry<String, Object> entry : source.entrySet()) {
            handleEntry(entry, setters, result, bucket);
        }

        if (bucketMethod != null) {
            invokeOn(result).method(bucketMethod).withParameter(bucket);
        }

        return result;
    }

    public static Map<String, Object> toMap(String[] source) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String line : source) {
            if (isBlank(line)) {
                continue;
            }

            String[] split = line.split(nameValueSeparator);
            String name = split[0].trim();
            Object value = null;
            if (split.length == 1) {
                name = name.replace(nameValueSeparator.trim(), EMPTY);
            } else {
                value = stream(split)
                        .skip(1)
                        .collect(joining(nameValueSeparator))
                        .trim();
            }

            name = cleanupNameWhenIsList(name);

            if (map.containsKey(name)) {
                Object currenValue = map.get(name);
                //noinspection rawtypes
                if (currenValue instanceof List list) {
                    //noinspection unchecked
                    list.add(value);
                } else {
                    List<Object> list = new LinkedList<>();
                    list.add(currenValue);
                    list.add(value);
                    map.put(name, list);
                }
            } else {
                map.put(name, value);
            }
        }
        return map;
    }

    private static <T> T instantiateResultClass(Class<T> target) {
        try {
            return target.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate class %s".formatted(target), e);
        }
    }

    private static <T> void handleEntry(Entry<String, Object> entry, Map<String, Method> setters, T result, Map<String, String> bucket) {
        Object value = entry.getValue();

        String key = entry.getKey().toLowerCase(ENGLISH);
        Method method = setters.getOrDefault(key, null);
        if (method == null) {
            if (bucket == null) {
                logger.warn("Unable to set the '{}' property to the value '{}' in the '{}' class. There is no setter method available. " +
                                "Please report at https://github.com/asterisk-java/asterisk-java/issues.",
                        entry.getKey(), value, result.getClass().getName());
                return;
            }

            bucket.put(entry.getKey(), String.valueOf(value));
            return;
        }

        Object targetValue = getValue(value, method);
        invokeOn(result).method(method).withParameter(targetValue);
    }

    private static Object getValue(Object value, Method method) {
        Class<?> targetDataType = method.getParameterTypes()[0];
        if (value == null) {
            return null;
        } else if (targetDataType.isEnum()) {
            return parseEnum(method, value);
        } else if (targetDataType.isAssignableFrom(List.class)) {
            return parseList(method, value);
        } else if (targetDataType.isAssignableFrom(Map.class)) {
            return parseMap(method, value);
        } else {
            return parseOtherType(method, value);
        }
    }

    private static Enum<?> parseEnum(Method method, Object value) {
        Class<?> targetDataType = method.getParameterTypes()[0];
        return (Enum<?>) stream(targetDataType.getEnumConstants())
                .filter(t -> t.toString().equalsIgnoreCase(String.valueOf(value)))
                .findFirst()
                .orElse(null);
    }

    private static List<Object> parseList(Method method, Object value) {
        ParameterizedType genericParameter = (ParameterizedType) method.getGenericParameterTypes()[0];
        Class<?> listElementType = (Class<?>) genericParameter.getActualTypeArguments()[0];

        Map<Class<?>, Converter<?, ?>> conversions = TYPE_CONVERTERS.get(listElementType);

        List<?> list = value instanceof List<?> ? (List<?>) value : List.of(value);
        return list
                .stream()
                .map(object -> {
                    @SuppressWarnings("rawtypes")
                    Converter converter = conversions.get(String.class);
                    //noinspection unchecked
                    return converter.apply(object.toString());
                })
                .toList();
    }

    private static Map<Object, Object> parseMap(Method method, Object value) {
        ParameterizedType genericParameter = (ParameterizedType) method.getGenericParameterTypes()[0];
        Class<?> mapKeyType = (Class<?>) genericParameter.getActualTypeArguments()[0];
        Class<?> mapValueType = (Class<?>) genericParameter.getActualTypeArguments()[1];

        Map<Class<?>, Converter<?, ?>> keyConversions = TYPE_CONVERTERS.get(mapKeyType);
        Map<Class<?>, Converter<?, ?>> valueConversions = TYPE_CONVERTERS.get(mapValueType);

        if (value instanceof List<?> values) {
            Map<Object, Object> map = new LinkedHashMap<>();
            for (Object item : values) {
                Pair<Object, Object> converted = convertMapItem(item, keyConversions, valueConversions);
                map.put(converted.getKey(), converted.getValue());
            }
            return map;
        } else {
            Pair<Object, Object> converted = convertMapItem(value, keyConversions, valueConversions);
            return Map.of(converted.getKey(), converted.getValue());
        }
    }

    private static Object parseOtherType(Method method, Object value) {
        Class<?> sourceType = value.getClass();
        Class<?> targetType = method.getParameterTypes()[0];

        Map<Class<?>, Converter<?, ?>> conversions = TYPE_CONVERTERS.get(targetType);

        if (conversions == null) {
            throw new RuntimeException("Cannot find conversions for type [%s]".formatted(targetType));
        }

        @SuppressWarnings("rawtypes")
        Converter converter = conversions.get(sourceType);

        if (converter == null) {
            throw new RuntimeException("Cannot find converter from [%s] type to type [%s]".formatted(sourceType, targetType));
        }

        //noinspection unchecked
        return converter.apply(value);
    }

    private static Pair<Object, Object> convertMapItem(
            Object object,
            Map<Class<?>, Converter<?, ?>> keyConversions,
            Map<Class<?>, Converter<?, ?>> valueConversions
    ) {
        String[] split = String.valueOf(object).split("=");
        Pair<String, String> deserialize = Pair.of(split[0], split[1]);

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

    private static String cleanupNameWhenIsList(String name) {
        Matcher matcher = LIST_DETECTOR.matcher(name);
        return matcher.find() ? matcher.replaceAll(EMPTY) : name;
    }
}
