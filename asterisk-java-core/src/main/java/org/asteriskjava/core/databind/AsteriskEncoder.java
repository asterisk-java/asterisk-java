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

import org.asteriskjava.core.NewlineDelimiter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;
import static org.asteriskjava.core.NewlineDelimiter.LF;
import static org.asteriskjava.core.databind.AsteriskEncoder.EncoderConsts.*;
import static org.asteriskjava.core.databind.utils.AnnotationUtils.getName;
import static org.asteriskjava.core.databind.utils.Invoker.invokeOn;
import static org.asteriskjava.core.databind.utils.ReflectionUtils.getGetters;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskEncoder {
    private final NewlineDelimiter newlineDelimiter;
    private final Comparator<String> fieldNamesComparator;

    public AsteriskEncoder(NewlineDelimiter newlineDelimiter, Comparator<String> fieldNamesComparator) {
        this.newlineDelimiter = newlineDelimiter;
        this.fieldNamesComparator = fieldNamesComparator;
    }

    public AsteriskEncoder(NewlineDelimiter newlineDelimiter) {
        this(newlineDelimiter, null);
    }

    public String encode(Object source) {
        return encode(toMap(source));
    }

    public String encode(Map<String, Object> source) {
        source = sortIfNeeded(source);
        StringBuilder stringBuilder = new StringBuilder();
        if (!source.isEmpty()) {
            for (Entry<String, Object> entry : source.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof List<?> values) {
                    values.forEach(v -> appendFieldNameAndValue(name, v, stringBuilder));
                } else {
                    appendFieldNameAndValue(name, value, stringBuilder);
                }
            }
            // Trailing delimiter.
            stringBuilder.append(newlineDelimiter.getPattern());
        }
        return stringBuilder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    private Map<String, Object> sortIfNeeded(Map<String, Object> source) {
        if (fieldNamesComparator == null) {
            return source;
        }

        Map<String, Object> sourceSortedIfNeeded = new TreeMap<>(fieldNamesComparator);
        sourceSortedIfNeeded.putAll(source);
        return sourceSortedIfNeeded;
    }

    private void appendFieldNameAndValue(String name, Object value, StringBuilder stringBuilder) {
        stringBuilder.append(name);
        stringBuilder.append(nameValueSeparator);
        stringBuilder.append(value);
        stringBuilder.append(newlineDelimiter.getPattern());
    }

    private static Map<String, Object> toMap(Object source) {
        Map<String, Object> map = new LinkedHashMap<>();

        Class<?> clazz = source.getClass();
        Map<String, Method> getters = getGetters(clazz);
        for (Entry<String, Method> entry : getters.entrySet()) {
            Method method = entry.getValue();

            String name = getName(method, entry.getKey());
            Object value = invokeOn(source).method(method).withoutParameter();

            if (value instanceof List<?> values) {
                value = values
                        .stream()
                        .map(Object::toString)
                        .collect(joining(listSeparator));
            }

            if (value instanceof Map<?, ?> values) {
                value = values
                        .entrySet()
                        .stream()
                        .map(e -> mapTemplate.formatted(e.getKey(), e.getValue()))
                        .toList();
            }

            map.put(name, value);
        }

        return map;
    }

    public static class Builder {
        private NewlineDelimiter newlineDelimiter = CRLF;
        private Comparator<String> fieldNamesComparator;

        public Builder newlineDelimiter(NewlineDelimiter newlineDelimiter) {
            this.newlineDelimiter = requireNonNull(newlineDelimiter, "newlineDelimiter cannot be null");
            return this;
        }

        public Builder crlfNewlineDelimiter() {
            return newlineDelimiter(CRLF);
        }

        public Builder lfNewlineDelimiter() {
            return newlineDelimiter(LF);
        }

        public Builder fieldNamesComparator(Comparator<String> fieldNamesComparator) {
            this.fieldNamesComparator = requireNonNull(fieldNamesComparator, "fieldNamesComparator cannot be null");
            return this;
        }

        public AsteriskEncoder build() {
            return new AsteriskEncoder(newlineDelimiter, fieldNamesComparator);
        }
    }

    static class EncoderConsts {
        final static String nameValueSeparator = ": ";
        final static String listSeparator = ",";
        final static String mapTemplate = "%s=%s";
    }
}
