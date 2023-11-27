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

import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;

class ComaJoiningSerializerTest {
    private final AsteriskGenerator asteriskGenerator = new AsteriskGenerator(CRLF);

    @Test
    void shouldSerializeList() {
        //given
        ComaJoiningSerializer comaJoiningSerializer = new ComaJoiningSerializer();

        List<String> value = List.of("string1", "string2", "string3");

        //when
        comaJoiningSerializer.serialize("fieldName", value, asteriskGenerator);

        //then
        assertThat(asteriskGenerator.generate().trim()).isEqualTo("string1,string2,string3");
    }

    @Test
    void shouldSerializeListOfEnums() {
        //given
        ComaJoiningSerializer comaJoiningSerializer = new ComaJoiningSerializer();

        EnumSet<SampleEnum> enums = EnumSet.of(SampleEnum.value1, SampleEnum.value2, SampleEnum.value3);

        //when
        comaJoiningSerializer.serialize("fieldName", enums, asteriskGenerator);

        //then
        assertThat(asteriskGenerator.generate().trim()).isEqualTo("value1,value2,value3");
    }

    private enum SampleEnum {
        value1,
        value2,
        value3,
    }
}
