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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;

import static java.lang.Math.toIntExact;

/**
 * Default implementation of {@link SocketBuilder}.
 *
 * @author Piotr Olaszewski
 */
class DefaultSocketBuilder implements SocketBuilder {
    private String hostname;
    private int port;
    private boolean ssl;
    private Duration connectionTimeout;

    @Override
    public SocketBuilder hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    @Override
    public SocketBuilder port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public SocketBuilder ssl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    @Override
    public SocketBuilder connectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    @Override
    public Socket build() throws IOException {
        SocketFactory socketFactory = ssl ? SSLSocketFactory.getDefault() : SocketFactory.getDefault();
        try (Socket socket = socketFactory.createSocket()) {
            InetSocketAddress endpoint = new InetSocketAddress(hostname, port);
            int connectionTimeout = toIntExact(this.connectionTimeout.toMillis());
            socket.connect(endpoint, connectionTimeout);

            return socket;
        }
    }
}
