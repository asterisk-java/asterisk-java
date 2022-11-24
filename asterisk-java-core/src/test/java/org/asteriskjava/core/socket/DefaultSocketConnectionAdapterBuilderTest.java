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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link DefaultSocketConnectionAdapterBuilder}.
 *
 * @author Piotr Olaszewski
 */
class DefaultSocketConnectionAdapterBuilderTest {
    @Test
    void shouldBuildDefaultSocketConnectionAdapter() throws IOException {
        //given
        DefaultSocketConnectionAdapterBuilder defaultSocketConnectionAdapterBuilder = new DefaultSocketConnectionAdapterBuilder();

        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream("a".getBytes()));
        when(socket.getOutputStream()).thenReturn(new ByteArrayOutputStream(1));

        //when
        SocketConnectionAdapter socketConnectionAdapter = defaultSocketConnectionAdapterBuilder
            .socket(socket)
            .build();

        //then
        assertThat(socketConnectionAdapter).isInstanceOf(DefaultSocketConnectionAdapter.class);
    }
}
