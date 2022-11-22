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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetDataCommandTest {
    private GetDataCommand getDataCommand;

    @Test
    void shouldBuildGetDataCommand() {
        //given
        GetDataCommand getDataCommand = new GetDataCommand("VAR1");

        //when
        String actual = getDataCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("GET DATA \"VAR1\"");
    }

    @Test
    void shouldSetTimeoutWhenBuildGetDataCommand() {
        //given
        GetDataCommand getDataCommand = new GetDataCommand("VAR1");
        getDataCommand.setTimeout(10000);

        //when
        String actual = getDataCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("GET DATA \"VAR1\" 10000");
        assertThat(getDataCommand.getTimeout()).isEqualTo(10000);
        assertThat(getDataCommand.getMaxDigits()).isEqualTo(1024);
    }

    @Test
    void shouldSetMaxDigitsWhenBuildGetDataCommand() {
        //given
        GetDataCommand getDataCommand = new GetDataCommand("VAR1");
        getDataCommand.setMaxDigits(10);

        //when
        String actual = getDataCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("GET DATA \"VAR1\" 0 10");
        assertThat(getDataCommand.getTimeout()).isEqualTo(0);
        assertThat(getDataCommand.getMaxDigits()).isEqualTo(10);
    }

    @Test
    void shouldSetTimeoutAndSetMaxDigitsWhenBuildGetDataCommand() {
        //given
        GetDataCommand getDataCommand = new GetDataCommand("VAR1");
        getDataCommand.setTimeout(10000);
        getDataCommand.setMaxDigits(20);

        //when
        String actual = getDataCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("GET DATA \"VAR1\" 10000 20");
        assertThat(getDataCommand.getTimeout()).isEqualTo(10000);
        assertThat(getDataCommand.getMaxDigits()).isEqualTo(20);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1025})
    void shouldThrowExceptionWhenMaxDigitsIsOutOfRange(int maxDigits) {
        //when/then
        assertThatThrownBy(() -> new GetDataCommand("VAR1", 10, maxDigits))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("maxDigits must be in [1..1024]");
    }
}
