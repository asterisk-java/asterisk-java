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

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SayDateTimeCommandTest {
    private final long time = Instant.parse("2022-09-03T11:48:30.00Z").getEpochSecond();

    @Test
    void shouldBuildSayDateTimeCommand() {
        //given
        SayDateTimeCommand sayDateTimeCommand = new SayDateTimeCommand(time);

        //when
        String actual = sayDateTimeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("SAY DATETIME 1662205710 \"\"");
    }

    @Test
    void shouldBuildSayDateTimeCommandWithEscapeDigits() {
        //given
        SayDateTimeCommand sayDateTimeCommand = new SayDateTimeCommand(time, "1");

        //when
        String actual = sayDateTimeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("SAY DATETIME 1662205710 \"1\"");
    }

    @Test
    void shouldBuildSayDateTimeCommandWithEscapeDigitsAndFormat() {
        //given
        SayDateTimeCommand sayDateTimeCommand = new SayDateTimeCommand(time, "1", "Y k");

        //when
        String actual = sayDateTimeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("SAY DATETIME 1662205710 \"1\" \"Y k\"");
    }

    @Test
    void shouldBuildSayDateTimeCommandWithEscapeDigitsAndDefaultFormatAndTimezons() {
        //given
        SayDateTimeCommand sayDateTimeCommand = new SayDateTimeCommand(time, "1", null, "Poland");

        //when
        String actual = sayDateTimeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("SAY DATETIME 1662205710 \"1\" \"ABdY 'digits/at' IMp\" \"Poland\"");
    }
}
