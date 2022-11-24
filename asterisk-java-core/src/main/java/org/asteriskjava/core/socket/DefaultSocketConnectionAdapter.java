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

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;

/**
 * Default implementation of {@link SocketConnectionAdapter}.
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 */
class DefaultSocketConnectionAdapter implements SocketConnectionAdapter {
    private final Socket socket;
    private final SocketScanner socketScanner;
    private final Writer writer;

    DefaultSocketConnectionAdapter(Socket socket, SocketScanner socketScanner, Writer writer) {
        this.socket = socket;
        this.socketScanner = socketScanner;
        this.writer = writer;
    }

    @Override
    public String read() throws IOException {
        String line;
        try {
            line = socketScanner.next();
        } catch (IllegalStateException | NoSuchElementException e) {
            throw new IOException("No more lines available.", e);
        }
        return line;
    }

    public void write(String str) throws IOException {
        writer.write(str);
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        socket.close();
        socketScanner.close();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public InetAddress getRemoteAddress() {
        return socket.getInetAddress();
    }

    public int getRemotePort() {
        return socket.getPort();
    }
}
