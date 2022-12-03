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
package org.asteriskjava.ami.databind;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ComaJoiningSerializer}.
 *
 * @author Piotr Olaszewski
 */
class ComaJoiningSerializerTest {
    @Test
    void shouldSerializeList() {
        //given
        ComaJoiningSerializer comaJoiningSerializer = new ComaJoiningSerializer();

        List<String> value = new ArrayList<>();
        value.add("string1");
        value.add("string2");
        value.add("string3");

        //when
        String serialize = comaJoiningSerializer.serialize(value);

        //then
        assertThat(serialize).isEqualTo("string1,string2,string3");
    }

    @Test
    void shouldSerializeListOfEnums() {
        //given
        ComaJoiningSerializer comaJoiningSerializer = new ComaJoiningSerializer();

        EnumSet<SampleEnum> enums = EnumSet.of(SampleEnum.value1, SampleEnum.value2, SampleEnum.value3);

        //when
        String serialize = comaJoiningSerializer.serialize(enums);

        //then
        assertThat(serialize).isEqualTo("value1,value2,value3");
    }

    private enum SampleEnum {
        value1,
        value2,
        value3,
    }
}
