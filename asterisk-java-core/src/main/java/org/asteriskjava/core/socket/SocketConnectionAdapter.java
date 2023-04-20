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

import org.asteriskjava.core.NewlineDelimiter;
import org.asteriskjava.core.socket.scanner.SocketScanner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketOptions;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * The adapter provides read and write operation for communication over TCP/IP sockets.<p>
 * It hides the details of the underlying I/O system used for socket communication.
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 */
public interface SocketConnectionAdapter extends AutoCloseable {
    /**
     * Reads a line of text from the socket connection. The current thread is blocked until either the next line is
     * received or an IOException encounters.<p>
     * Depending on the implementation different newline delimiters are used
     * ({@code \r\n} for the AMI and {@code \n} for the AGI).
     *
     * @return the line of text received excluding the newline delimiter
     * @throws IOException if the connection has been closed
     * @see SocketScanner
     */
    String read() throws IOException;

    /**
     * Sends a given string to the socket connection.
     *
     * @param str the string to send
     * @throws IOException if the string cannot be sent, maybe because the connection has already been closed
     */
    void write(String str) throws IOException;

    /**
     * Flushes the socket connection by sending any buffered but yet unsent data.
     *
     * @throws IOException if the connection cannot be flushed
     */
    void flush() throws IOException;

    /**
     * Returns the connection state of the socket.
     *
     * @return <code>true</code> if the socket successfully connected to a server
     */
    boolean isConnected();

    /**
     * Returns the local address this socket connection.
     *
     * @since 0.2
     */
    InetAddress getLocalAddress();

    /**
     * Returns the local port of this socket connection.
     *
     * @since 0.2
     */
    int getLocalPort();

    /**
     * Returns the remote address of this socket connection.
     *
     * @since 0.2
     */
    InetAddress getRemoteAddress();

    /**
     * Returns the remote port of this socket connection.
     *
     * @since 0.2
     */
    int getRemotePort();

    /**
     * Obtain a {@link SocketConnectionAdapter} builder, using created (and connected) {@link Socket} object.
     */
    static Builder builder(Socket socket) {
        return new DefaultSocketConnectionAdapterBuilder()
            .socket(socket);
    }

    /**
     * A builder for creating a {@link SocketConnectionAdapter}.
     */
    interface Builder {
        /**
         * Configure a connected socket.
         */
        Builder socket(Socket socket);

        /**
         * Configure a newline delimiter used for different purposes.<p>
         * AMI uses CRLF {@code \r\n} as a newline delimiter.<br>
         * AGI uses LF {@code \n} as a newline delimiter.
         */
        Builder newlineDelimiter(NewlineDelimiter newlineDelimiter);

        /**
         * Configure how long wait for read operation.
         *
         * @see SocketOptions#SO_TIMEOUT
         */
        Builder readTimeout(Duration readTimeout);

        /**
         * Configure a charset for sent and received messages via socket.<p>
         * Default charset is: {@link StandardCharsets#UTF_8}.
         */
        Builder charset(Charset charset);

        /**
         * Build the {@link SocketConnectionAdapter} instance.
         */
        SocketConnectionAdapter build() throws IOException;
    }
}
