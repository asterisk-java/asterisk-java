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

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.time.Duration;

/**
 * A builder for creating a {@link Socket}.
 *
 * @author Piotr Olaszewski
 */
public interface SocketBuilder {
    /**
     * Configure a hostname which socket is connected.
     */
    SocketBuilder hostname(String hostname);

    /**
     * Configure a port used by socket to connect.
     */
    SocketBuilder port(int port);

    /**
     * Configure that socket should (or not) use an SSL.
     *
     * @see SSLSocketFactory
     * @see SocketFactory
     */
    SocketBuilder ssl(boolean ssl);

    /**
     * Configure a connection timeout for socket.
     */
    SocketBuilder connectionTimeout(Duration connectionTimeout);

    /**
     * Build the {@link Socket} which is connected.
     */
    Socket build() throws IOException;

    /**
     * Obtain a {@link SocketBuilder} builder.<p>
     * Allows to create a {@link Socket} object.
     */
    static SocketBuilder builder() {
        return new DefaultSocketBuilder();
    }
}
