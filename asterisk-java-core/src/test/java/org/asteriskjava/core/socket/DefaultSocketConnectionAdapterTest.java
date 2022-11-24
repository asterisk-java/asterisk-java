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

import org.asteriskjava.core.socket.scanner.SocketScanner;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

import static java.net.InetAddress.getByName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link DefaultSocketConnectionAdapter}.
 *
 * @author Piotr Olaszewski
 */
class DefaultSocketConnectionAdapterTest {
    private final Socket socket = mock(Socket.class);
    private final SocketScanner socketScanner = mock(SocketScanner.class);
    private final Writer writer = mock(Writer.class);

    private final DefaultSocketConnectionAdapter defaultSocketConnectionAdapter = new DefaultSocketConnectionAdapter(socket, socketScanner, writer);

    @Test
    void shouldReadLineUsingScanner() throws IOException {
        //given
        when(socketScanner.next()).thenReturn("new line from socket");

        //when
        String line = defaultSocketConnectionAdapter.read();

        //then
        assertThat(line).isEqualTo("new line from socket");
    }

    @Test
    void shouldHandleWhenReadLineThrowsIllegalStateException() throws IOException {
        //given
        when(socketScanner.next()).thenThrow(new IllegalStateException("illegal state"));

        //when/then
        assertThatThrownBy(defaultSocketConnectionAdapter::read)
            .isInstanceOf(IOException.class)
            .hasMessage("No more lines available.");
    }

    @Test
    void shouldHandleWhenReadLineThrowsNoSuchElementException() throws IOException {
        //given
        when(socketScanner.next()).thenThrow(new NoSuchElementException("no more elements"));

        //when/then
        assertThatThrownBy(defaultSocketConnectionAdapter::read)
            .isInstanceOf(IOException.class)
            .hasMessage("No more lines available.");
    }

    @Test
    void shouldWriteToSocket() throws IOException {
        //when
        defaultSocketConnectionAdapter.write("some string to write");

        //then
        verify(writer).write("some string to write");
    }

    @Test
    void shouldFlusDataToSocket() throws IOException {
        //when
        defaultSocketConnectionAdapter.flush();

        //then
        verify(writer).flush();
    }

    @Test
    void shouldReturnIsSocketConnected() {
        //given
        when(socket.isConnected()).thenReturn(true);

        //when
        boolean connected = defaultSocketConnectionAdapter.isConnected();

        //then
        assertThat(connected).isTrue();
    }

    @Test
    void shouldReturnLocalAddressAndLocalPort() throws UnknownHostException {
        //given
        when(socket.getLocalAddress()).thenReturn(getByName("localhost"));
        when(socket.getLocalPort()).thenReturn(1234);

        //when
        InetAddress localAddress = defaultSocketConnectionAdapter.getLocalAddress();
        int localPort = defaultSocketConnectionAdapter.getLocalPort();

        //then
        assertThat(localAddress).isEqualTo(getByName("localhost"));
        assertThat(localPort).isEqualTo(1234);
    }

    @Test
    void shouldReturnRemoteAddressAndRemotePort() throws UnknownHostException {
        //given
        when(socket.getLocalAddress()).thenReturn(getByName("172.10.10.1"));
        when(socket.getLocalPort()).thenReturn(4321);

        //when
        InetAddress localAddress = defaultSocketConnectionAdapter.getLocalAddress();
        int localPort = defaultSocketConnectionAdapter.getLocalPort();

        //then
        assertThat(localAddress).isEqualTo(getByName("172.10.10.1"));
        assertThat(localPort).isEqualTo(4321);
    }
}
