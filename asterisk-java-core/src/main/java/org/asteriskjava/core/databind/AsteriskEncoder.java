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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.asteriskjava.core.NewlineDelimiter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.asteriskjava.core.databind.CodersConsts.NAME_VALUE_SEPARATOR;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskEncoder {
    private final NewlineDelimiter newlineDelimiter;
    private final JsonMapper jsonMapper;

    public AsteriskEncoder(NewlineDelimiter newlineDelimiter) {
        this.newlineDelimiter = newlineDelimiter;
        jsonMapper = AsteriskJacksonFactory.create();
    }

    public String encode(Object source) {
        Map<String, Object> map = jsonMapper.convertValue(source, new TypeReference<>() {
        });
        return encode(map);
    }

    public String encode(Map<String, Object> source) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!source.isEmpty()) {
            for (Entry<String, Object> entry : source.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                if (value instanceof Map<?, ?> valueMap) {
                    encodeMap(valueMap, name, stringBuilder);
                } else if (value instanceof List<?> valueList) {
                    encodeList(valueList, name, stringBuilder);
                } else {
                    appendFieldNameAndValue(name, value, stringBuilder);
                }
            }
            // Trailing delimiter.
            stringBuilder.append(newlineDelimiter.getPattern());
        }
        return stringBuilder.toString();
    }

    private void encodeMap(Map<?, ?> valueMap, String name, StringBuilder stringBuilder) {
        valueMap.entrySet()
                .stream()
                .map(entry -> "%s=%s".formatted(entry.getKey(), entry.getValue()))
                .forEach(value -> appendFieldNameAndValue(name, value, stringBuilder));
    }

    private void encodeList(List<?> valueList, String name, StringBuilder stringBuilder) {
        int i = 0;
        for (Object value : valueList) {
            if (value instanceof Map<?, ?> valueMap) {
                for (Entry<?, ?> entry : valueMap.entrySet()) {
                    if (entry.getValue() != null) {
                        appendFieldNameAndValue("%s-%06d".formatted(entry.getKey(), i), entry.getValue(), stringBuilder);
                    }
                }
                i++;
            } else {
                appendFieldNameAndValue(name, value, stringBuilder);
            }
        }
    }

    private void appendFieldNameAndValue(String name, Object value, StringBuilder stringBuilder) {
        stringBuilder.append(name);
        stringBuilder.append(NAME_VALUE_SEPARATOR);
        stringBuilder.append(value);
        stringBuilder.append(newlineDelimiter.getPattern());
    }
}
