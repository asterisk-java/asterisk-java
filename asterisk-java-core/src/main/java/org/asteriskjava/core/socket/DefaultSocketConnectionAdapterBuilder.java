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

import org.asteriskjava.core.socket.SocketConnectionAdapter.Builder;
import org.asteriskjava.core.socket.scanner.SocketScanner;
import org.asteriskjava.core.socket.scanner.SocketScanner.NewlineDelimiter;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.time.Duration;

import static java.lang.Math.toIntExact;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Default implementation of {@link SocketConnectionAdapter.Builder}.
 *
 * @author Piotr Olaszewski
 */
class DefaultSocketConnectionAdapterBuilder implements SocketConnectionAdapter.Builder {
    private Socket socket;
    private NewlineDelimiter newlineDelimiter;
    private Duration readTimeout;
    private Charset charset = UTF_8;

    @Override
    public Builder socket(Socket socket) {
        this.socket = socket;
        return this;
    }

    @Override
    public Builder newlineDelimiter(NewlineDelimiter newlineDelimiter) {
        this.newlineDelimiter = newlineDelimiter;
        return this;
    }

    @Override
    public Builder readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @Override
    public Builder charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public SocketConnectionAdapter build() throws IOException {
        if (readTimeout != null) {
            socket.setSoTimeout(toIntExact(readTimeout.toMillis()));
        }

        SocketScanner socketScanner = getSocketScanner();
        Writer writer = getWriter();
        return new DefaultSocketConnectionAdapter(socket, socketScanner, writer);
    }

    private SocketScanner getSocketScanner() throws IOException {
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        return SocketScanner.create(inputStreamReader, newlineDelimiter);
    }

    private Writer getWriter() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);
        return new BufferedWriter(outputStreamWriter);
    }
}
