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
import static org.asteriskjava.AsteriskVersion.ASTERISK_10;

class ConfbridgeCommandTest {
    @Test
    void shouldBuildConfbridgeCommand() {
        //given
        ConfbridgeCommand confbridgeCommand = new ConfbridgeCommand("room", "fancybridge");

        //when
        String actual = confbridgeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("EXEC \"confbridge\" \"room\"|\"fancybridge\"");
    }

    @Test
    void shouldBuildConfbridgeCommandForAsteriskGreaterThan10() {
        //given
        ConfbridgeCommand confbridgeCommand = new ConfbridgeCommand("room", "fancybridge");
        confbridgeCommand.setAsteriskVersion(ASTERISK_10);

        //when
        String actual = confbridgeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("EXEC \"confbridge\" \"room\",\"fancybridge\"");
    }
}
