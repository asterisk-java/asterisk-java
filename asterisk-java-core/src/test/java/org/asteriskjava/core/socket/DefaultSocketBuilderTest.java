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
package org.asteriskjava.core.socket;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link DefaultSocketBuilder}.
 *
 * @author Piotr Olaszewski
 */
class DefaultSocketBuilderTest {
    @Test
    void shouldCreateConnectedSocket() throws IOException {
        //given
        new ServerSocket(1234);

        DefaultSocketBuilder defaultSocketBuilder = new DefaultSocketBuilder();

        //when
        Socket socket = defaultSocketBuilder
            .hostname("localhost")
            .port(1234)
            .connectionTimeout(ofSeconds(2))
            .ssl(true)
            .build();

        //then
        assertThat(socket.isConnected()).isTrue();
    }
}
