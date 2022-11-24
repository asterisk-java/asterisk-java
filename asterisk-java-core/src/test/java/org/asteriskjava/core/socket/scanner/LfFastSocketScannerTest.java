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
package org.asteriskjava.core.socket.scanner;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Unit tests for {@link LfFastSocketScanner}.
 *
 * @author Piotr Olaszewski
 */
class LfFastSocketScannerTest {
    @ParameterizedTest
    @MethodSource("linesProvider")
    void shouldScanForLines(String lines, String expected2) throws IOException {
        //given
        byte[] data = lines.getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        InputStreamReader reader = new InputStreamReader(inputStream);

        LfFastSocketScanner socketScanner = new LfFastSocketScanner(reader);

        //when
        StringBuilder actual = new StringBuilder();
        String actualLine;
        while ((actualLine = socketScanner.next()) != null) {
            actual.append(actualLine).append("|");
        }

        //then
        assertThat(actual.toString()).isEqualTo(expected2);
    }

    private static Stream<Arguments> linesProvider() {
        return Stream.of(
            arguments("text1\ntext2\ntext3\r\ntext4", "text1|text2|text3\r|text4|"),
            arguments("\ntext1", "text1|"),
            arguments(" text1", " text1|"),
            arguments(" \ntext1", " |text1|"),
            arguments("", ""),
            arguments("text1\r\n", "text1\r|"),
            arguments("text1\n\r", "text1|\r|"),
            arguments("\rtext1\n\r", "\rtext1|\r|"),
            arguments("\n\ntext1", "|text1|"),
            arguments("text1\n\n\ntext2\ntext3\r\ntext4", "text1|||text2|text3\r|text4|"),
            arguments("text1", "text1|")
        );
    }
}
