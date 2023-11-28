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
package org.asteriskjava.core.databind.serializer.custom;

import org.asteriskjava.core.databind.AsteriskGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;

class VariableSerializerTest {
    private final AsteriskGenerator asteriskGenerator = new AsteriskGenerator(CRLF);

    @Test
    void shouldSerializeValues() {
        //given
        VariableSerializer variableSerializer = new VariableSerializer();

        Map<String, String> map = Map.of(
                "key1", "value1",
                "key2", "value2",
                "key3", "value3"
        );

        //when
        variableSerializer.serialize("fieldName", map, asteriskGenerator);

        //then
        assertThat(asteriskGenerator.generate())
                .contains(
                        "fieldName: key1=value1",
                        "fieldName: key2=value2",
                        "fieldName: key3=value3"
                );
    }
}
