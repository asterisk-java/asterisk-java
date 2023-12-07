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
package org.asteriskjava.core.databind.deserializer.custom;

import org.apache.commons.lang3.tuple.Pair;
import org.asteriskjava.core.databind.deserializer.AsteriskDeserializer;

/**
 * Deserializer for splitting collection elements using the {@code =} sign.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class EqualsSignDeserializer implements AsteriskDeserializer<Pair<String, String>> {
    private static final String EQUALS_SIGN_SEPARATOR = "=";

    @Override
    public Pair<String, String> deserialize(String value) {
        String[] split = value.split(EQUALS_SIGN_SEPARATOR);
        return Pair.of(split[0], split[1]);
    }
}
