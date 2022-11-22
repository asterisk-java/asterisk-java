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

import org.asteriskjava.AsteriskVersion;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.AsteriskVersion.ASTERISK_18;
import static org.asteriskjava.AsteriskVersion.ASTERISK_1_4;

class AbstractAgiCommandTest {
    @Test
    void shouldBuildCommand() {
        //given
        MyCommand myCommand = new MyCommand("just a string");

        //when
        String actual = myCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("MY \"just a string\"");
    }

    @Test
    void shouldHandleNull() {
        //given
        MyCommand myCommand = new MyCommand(null);

        //when
        String actual = myCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("MY \"\"");
    }

    @Test
    void shouldHandleEmptyString() {
        //given
        MyCommand myCommand = new MyCommand("");

        //when
        String actual = myCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("MY \"\"");
    }

    @Test
    void shouldEscapeAndQuoteWithStringContainingQuotes() {
        //given
        MyCommand myCommand = new MyCommand("\"John Doe\" is calling");

        //given
        String actual = myCommand.buildCommand();

        //when
        assertThat(actual).isEqualTo("MY \"\\\"John Doe\\\" is calling\"");
    }

    @Test
    void shouldEscapeAndQuoteWithStringContainingNewline() {
        //given
        MyCommand myCommand = new MyCommand("Caller is:\nJohn Doe");

        //when
        String actual = myCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("MY \"Caller is:John Doe\"");
    }

    @Test
    void shouldEscapedQuotesAJ192() {
        //given
        MyCommand myCommand = new MyCommand("first \\\" second \\\" third");

        //when
        String actual = myCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("MY \"first \\\\\\\" second \\\\\\\" third\"");
    }

    @Test
    void shouldFormatToString() {
        //given
        MyCommand myCommand = new MyCommand("just a string");

        //when
        String actual = myCommand.toString();

        //then
        assertThat(actual).contains("command='MY \"just a string\"'");
    }

    @Test
    void shouldHandleCommandWithMultipleStrings() {
        //given
        MyMultipleCommand myMultipleCommand = new MyMultipleCommand(new String[]{"first string", "another string"});

        //when
        String actual = myMultipleCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("MY \"first string,another string\"");
    }

    @Test
    void shouldHandleNullForArrayStrings() {
        //given
        MyMultipleCommand myMultipleCommand = new MyMultipleCommand(null);

        //when
        String actual = myMultipleCommand.buildCommand();

        //then
        assertThat(actual).isEqualTo("MY \"\"");
    }

    @Test
    void shouldReturnDefaultAsteriskVersion() {
        //given
        MyCommand myCommand = new MyCommand("string");

        //when
        AsteriskVersion asteriskVersion = myCommand.getAsteriskVersion();

        //then
        assertThat(asteriskVersion).isEqualTo(ASTERISK_1_4);
    }

    @Test
    void shouldSetAsteriskVersion() {
        //given
        MyCommand myCommand = new MyCommand("string");

        //when
        myCommand.setAsteriskVersion(ASTERISK_18);

        //then
        assertThat(myCommand.getAsteriskVersion()).isEqualTo(ASTERISK_18);
    }

    private static class MyCommand extends AbstractAgiCommand {
        private static final long serialVersionUID = 3976731484641833012L;
        private final String command;

        public MyCommand(String command) {
            this.command = command;
        }

        @Override
        public String buildCommand() {
            return format("MY %s", escapeAndQuote(command));
        }
    }

    private static class MyMultipleCommand extends AbstractAgiCommand {
        private static final long serialVersionUID = 3976731484641833012L;
        private final String[] commands;

        public MyMultipleCommand(String[] commands) {
            this.commands = commands;
        }

        @Override
        public String buildCommand() {
            return format("MY %s", escapeAndQuote(commands));
        }
    }
}
