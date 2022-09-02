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
package org.asteriskjava.fastagi;

import org.asteriskjava.util.ServerSocketFacade;
import org.asteriskjava.util.SocketConnectionFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultAgiServerTest {
    private DefaultAgiServer server;
    private MockedServerSocketFacade serverSocket;
    private SocketConnectionFacade socket;

    @BeforeEach
    void setUp() {
        serverSocket = new MockedServerSocketFacade();
        server = new MockedDefaultAgiServer();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    void testDummy() {

    }

    void XtestStartup() throws Exception {
        socket = mock(SocketConnectionFacade.class);

        when(socket.readLine())
                .thenReturn(null)
                .thenReturn(null);
        when(socket.getLocalPort()).thenReturn(1);
        when(socket.getRemoteAddress()).thenReturn(null);
        when(socket.getRemotePort()).thenReturn(2);
        socket.write("VERBOSE \"No script configured for null\" 1\n");
        socket.flush();
        when(socket.readLine()).thenReturn(null);
        socket.close();

        try {
            server.startup();
        } catch (IOException e) {
            // swallow
        }
        Thread.sleep(500);

        assertEquals(2, serverSocket.acceptCalls, "serverSocket.accept() not called 2 times");
        assertEquals(1, serverSocket.closeCalls, "serverSocket.close() not called");
    }

    class MockedDefaultAgiServer extends DefaultAgiServer {
        @Override
        protected ServerSocketFacade createServerSocket() {
            return serverSocket;
        }
    }

    class MockedServerSocketFacade implements ServerSocketFacade {
        public int acceptCalls = 0;
        public int closeCalls = 0;

        public SocketConnectionFacade accept() throws IOException {
            acceptCalls++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (acceptCalls == 1) {
                return socket;
            }
            throw new IOException("Provoked IOException");
        }

        public void close() throws IOException {
            closeCalls++;
        }

        public void setSocketReadTimeout(int socketReadTimeout) {
        }
    }

    @Test
    void testLoadConfigWithDefaultPort() {
        DefaultAgiServer defaultAgiServer;

        defaultAgiServer = new DefaultAgiServer();
        assertEquals(4573, defaultAgiServer.getPort(), "Invalid default port");
    }

    @Test
    void testLoadConfigWithPort() {
        DefaultAgiServer defaultAgiServer;

        defaultAgiServer = new DefaultAgiServer("test1-fastagi");
        assertEquals(1234, defaultAgiServer.getPort(), "Port property not recognized");

    }

    @Test
    void testLoadConfigWithBindPort() {
        DefaultAgiServer defaultAgiServer;

        defaultAgiServer = new DefaultAgiServer("test2-fastagi");
        assertEquals(2345, defaultAgiServer.getPort(), "BindPort property not recognized");
    }
}
