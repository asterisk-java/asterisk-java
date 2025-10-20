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

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.asteriskjava.core.NewlineDelimiter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.asteriskjava.core.databind.CodersConsts.NAME_VALUE_SEPARATOR;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskDecoder {
    /**
     * Handles values that look like a list. E.g.:
     * <pre>
     * Category-000001: cat1
     * Category-000002: cat2
     * Category-000003: cat3
     * </pre>
     */
    private static final Pattern LIST_DETECTOR = compile("-\\d\\d\\d\\d\\d\\d$");

    private final JsonMapper jsonMapper;

    public AsteriskDecoder() {
        jsonMapper = AsteriskJacksonFactory.create();
    }

    public <T> T decode(String source, NewlineDelimiter newlineDelimiter, Class<T> target) {
        String[] content = source.split(newlineDelimiter.getPattern());
        return decode(content, target);
    }

    public <T> T decode(String[] source, Class<T> target) {
        return decode(toMap(source), target);
    }

    public <T> T decode(Map<String, Object> source, Class<T> target) {
        return jsonMapper.convertValue(source, target);
    }

    public static Map<String, Object> toMap(String[] source) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String line : source) {
            if (isBlank(line)) {
                continue;
            }

            String[] split = line.split(NAME_VALUE_SEPARATOR);
            String name = split[0].trim();
            Object value = null;
            if (split.length == 1) {
                name = name.replace(NAME_VALUE_SEPARATOR.trim(), EMPTY);
            } else {
                value = stream(split)
                        .skip(1)
                        .collect(joining(NAME_VALUE_SEPARATOR))
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

    private static String cleanupNameWhenIsList(String name) {
        Matcher matcher = LIST_DETECTOR.matcher(name);
        return matcher.find() ? matcher.replaceAll(EMPTY) : name;
    }
}
