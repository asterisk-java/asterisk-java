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

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskGenerator {
    private static final String FIELD_NAME_VALUE_DELIMITER = ": ";

    private final StringBuilder stringBuilder = new StringBuilder();

    private final NewlineDelimiter newlineDelimiter;

    public AsteriskGenerator(NewlineDelimiter newlineDelimiter) {
        this.newlineDelimiter = newlineDelimiter;
    }

    public void writeFieldName(String name) {
        stringBuilder.append(name);
        stringBuilder.append(FIELD_NAME_VALUE_DELIMITER);
    }

    public void writeFieldValue(String value) {
        stringBuilder.append(value);
        stringBuilder.append(newlineDelimiter.getPattern());
    }

    public String generate() {
        stringBuilder.append(newlineDelimiter.getPattern());
        return stringBuilder.toString();
    }
}
