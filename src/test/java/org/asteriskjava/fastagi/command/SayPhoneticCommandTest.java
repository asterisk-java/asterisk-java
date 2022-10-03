/*
 * Copyright 2004-2022 Asterisk-Java contributors
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
package org.asteriskjava.fastagi.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SayPhoneticCommandTest {
    @Test
    void shouldBuildSayPhonetic() {
        //given
        SayPhoneticCommand sayPhoneticCommand = new SayPhoneticCommand("some text");

        //when
        String actual = sayPhoneticCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("SAY PHONETIC \"some text\" \"\"");
    }

    @Test
    void shouldBuildSayPhoneticWithEscapeDigits() {
        //given
        SayPhoneticCommand sayPhoneticCommand = new SayPhoneticCommand("some text", "89");

        //when
        String actual = sayPhoneticCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("SAY PHONETIC \"some text\" \"89\"");
    }
}
