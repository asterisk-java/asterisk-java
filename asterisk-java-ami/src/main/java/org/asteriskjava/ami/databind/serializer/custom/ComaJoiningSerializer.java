/*
 * Copyright 2004-2022 Asterisk Java contributors
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
package org.asteriskjava.ami.databind.serializer.custom;

import org.asteriskjava.ami.databind.AsteriskGenerator;
import org.asteriskjava.ami.databind.serializer.AsteriskSerializer;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

public class ComaJoiningSerializer implements AsteriskSerializer<Collection<?>> {
    private static final String COMA_SEPARATOR = ",";

    @Override
    public void serialize(String field, Collection<?> value, AsteriskGenerator asteriskGenerator) {
        String v = value.stream()
            .map(Object::toString)
            .collect(joining(COMA_SEPARATOR));

        asteriskGenerator.writeFieldValue(v);
    }
}
