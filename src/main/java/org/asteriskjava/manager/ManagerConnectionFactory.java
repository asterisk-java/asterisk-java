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
package org.asteriskjava.manager;

import java.io.IOException;

/**
 * This factory is used to obtain new ManagerConnections.
 * 
 * @see org.asteriskjava.manager.ManagerConnection
 * @author srt
 * @version $Id$
 */
public class ManagerConnectionFactory
{
    private static final int DEFAULT_PORT = 5038;

    private final String hostname;
    private final int port;
    private final String username;
    private final String password;

    /**
     * Creates a new ManagerConnectionFactory with the given connection data and
     * the default port 5038.
     * 
     * @param hostname the hostname of the Asterisk server to connect to.
     * @param username the username to use for login as defined in Asterisk's <code>manager.conf</code>.
     * @param password the password to use for login as defined in Asterisk's <code>manager.conf</code>.
     * @since 0.3
     */
    public ManagerConnectionFactory(String hostname, String username, String password)
    {
        this.hostname = hostname;
        this.port = DEFAULT_PORT;
        this.username = username;
        this.password = password;
    }
    
    /**
     * Creates a new ManagerConnectionFactory with the given connection data.
     * 
     * @param hostname the hostname of the Asterisk server to connect to.
     * @param port the port where Asterisk listens for incoming Manager API
     *            connections, usually 5038.
     * @param username the username to use for login as defined in Asterisk's <code>manager.conf</code>.
     * @param password the password to use for login as defined in Asterisk's <code>manager.conf</code>.
     * @since 0.3
     */
    public ManagerConnectionFactory(String hostname, int port, String username, String password)
    {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns a new ManagerConnection.
     * 
     * @return the created connection to the Asterisk server.
     * @throws IOException if the connection cannot be established.
     * @since 0.3
     */
    public ManagerConnection createManagerConnection() throws IOException
    {
        return new DefaultManagerConnection(this.hostname, this.port,
                this.username, this.password);
    }
}
