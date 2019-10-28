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
package org.asteriskjava.util;

import java.io.IOException;

/**
 * The ServerSocketFacade provides operations to accept client connections over
 * TCP/IP sockets.<p>
 * It hides the details of the underlying I/O system used for server socket
 * communication.
 * 
 * @author srt
 * @version $Id$
 */
public interface ServerSocketFacade
{
    /**
     * Waits for a new incoming connection.
     * 
     * @return the new connection.
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    SocketConnectionFacade accept() throws IOException;

    /**
     * Unbinds and closes the server socket.
     * 
     * @throws IOException if the server socket cannot be closed.
     */
    void close() throws IOException;

    /**
     * Connection is dropped if it stales on read longer than the timeout.
     *
     * @param socketReadTimeout the read timeout value to be used in
     *            milliseconds.
     * @see java.net.Socket#setSoTimeout(int)
     * @since 3.0.0
     */
    void setSocketReadTimeout(int socketReadTimeout);
}
