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
import static org.asteriskjava.AsteriskVersion.ASTERISK_12;
import static org.asteriskjava.AsteriskVersion.ASTERISK_13;

class BridgeCommandTest {
    @Test
    void shouldBuildBridgeCommand() {
        //given
        BridgeCommand bridgeCommand = new BridgeCommand("SIP/1234", "p");
        bridgeCommand.setAsteriskVersion(ASTERISK_13);

        //when
        String actual = bridgeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("EXEC \"bridge\" \"SIP/1234\",\"p\"");
    }

    @Test
    void shouldBuildBridgeCommandForAsteriskLessThan13() {
        //given
        BridgeCommand bridgeCommand = new BridgeCommand("SIP/1234", "p");
        bridgeCommand.setAsteriskVersion(ASTERISK_12);

        //when
        String actual = bridgeCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("EXEC \"bridge\" \"SIP/1234\"|\"p\"");
    }
}
