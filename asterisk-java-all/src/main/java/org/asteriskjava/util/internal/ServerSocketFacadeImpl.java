/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.util.internal;

import org.asteriskjava.core.socket.SocketConnectionAdapter;
import org.asteriskjava.util.ServerSocketFacade;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

import static org.asteriskjava.core.NewlineDelimiter.LF;


/**
 * Default implementation of the ServerSocketFacade interface using standard
 * java.io classes (ServerSocket in this case).
 *
 * @author srt
 * @version $Id$
 */
public class ServerSocketFacadeImpl implements ServerSocketFacade {
    private ServerSocket serverSocket;

    /**
     * 3 hrs = 3 * 3660 * 1000
     */
    private int socketReadTimeout = 10800000;

    public ServerSocketFacadeImpl(int port, int backlog, InetAddress bindAddress)
        throws IOException {
        this.serverSocket = new ServerSocket(port, backlog, bindAddress);
    }

    public SocketConnectionAdapter accept() throws IOException {
        Socket socket;

        socket = serverSocket.accept();

        int timeout;
        if (socketReadTimeout == -1) {
            timeout = 0;
        } else {
            timeout = socketReadTimeout;
        }

        return SocketConnectionAdapter
            .builder(socket)
            .readTimeout(Duration.ofMillis(timeout))
            .newlineDelimiter(LF)
            .build();
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public void setSocketReadTimeout(int socketReadTimeout) {
        this.socketReadTimeout = socketReadTimeout;
    }
}
