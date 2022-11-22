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

class RecordFileCommandTest {
    @Test
    void shouldBuildRecordFileCommand() {
        //given
        RecordFileCommand recordFileCommand = new RecordFileCommand("some-file", "wav", "4", 10);

        //when
        String actual = recordFileCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("RECORD FILE \"some-file\" \"wav\" \"4\" 10 0 s=0");
    }

    @Test
    void shouldBuildRecordFileCommandWithOffsetBeepAndMaxSilence() {
        //given
        RecordFileCommand recordFileCommand = new RecordFileCommand("some-file", "wav", "4", 10, 10, true, 15);

        //when
        String actual = recordFileCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("RECORD FILE \"some-file\" \"wav\" \"4\" 10 10 BEEP s=15");
    }
}
