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
 * @version $Id: ManagerConnectionFactory.java,v 1.3 2005/08/05 05:03:14 srt Exp $
 */
public class ManagerConnectionFactory
{
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final int DEFAULT_PORT = 5038;
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private String hostname;
    private int port;
    private String username;
    private String password;

    /**
     * Creates a new ManagerConnectionFactory.
     */
    public ManagerConnectionFactory()
    {
        this.hostname = DEFAULT_HOSTNAME;
        this.port = DEFAULT_PORT;
        this.username = DEFAULT_USERNAME;
        this.password = DEFAULT_PASSWORD;
    }

    /**
     * Sets the default hostname.<br>
     * Default is "localhost".
     * 
     * @param hostname the default hostname
     * @since 0.2
     */
    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    /**
     * Sets the default port.<br>
     * Default is 5038.
     * 
     * @param port the default port
     * @since 0.2
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Sets the default username.<br>
     * Default is "admin".
     * 
     * @param username the default username
     * @since 0.2
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Sets the default password.<br>
     * Default is "admin".
     * 
     * @param username the default password
     * @since 0.2
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Returns a new ManagerConnection with the default values for hostname,
     * port, username and password. It uses either the built-in defaults
     * ("localhost", 5038, "admin", "admin") or the custom default values you
     * set via {@link #setHostname(String)}, {@link #setPort(int)},
     * {@link #setUsername(String)} and {@link #setPassword(String)}.
     * 
     * @return the created connection to the Asterisk call manager
     * @throws IOException if the connection cannot be established.
     * @since 0.2
     */
    public ManagerConnection getManagerConnection() throws IOException
    {
        return new DefaultManagerConnection(this.hostname, this.port,
                this.username, this.password);
    }

    /**
     * Returns a new ManagerConnection to an Asterisk server running on default
     * host ("localhost" if you didn't change that via
     * {@link #setHostname(String)}) with the call manager interface listening
     * on the default port (5038 if you didn't change that via
     * {@link #setPort(int)}).
     * 
     * @param username the username as specified in Asterisk's
     *            <code>manager.conf</code>
     * @param password the password as specified in Asterisk's
     *            <code>manager.conf</code>
     * @return the created connection to the Asterisk call manager
     * @throws IOException if the connection cannot be established.
     */
    public ManagerConnection getManagerConnection(String username,
            String password) throws IOException
    {
        return new DefaultManagerConnection(this.hostname, this.port, username,
                password);
    }

    /**
     * Returns a new ManagerConnection to an Asterisk server running on given
     * host with the call manager interface listening on the default port (5038
     * if you didn't change that via {@link #setPort(int)}).
     * 
     * @param hostname the name of the host the Asterisk server is running on
     * @param username the username as specified in Asterisk's
     *            <code>manager.conf</code>
     * @param password the password as specified in Asterisk's
     *            <code>manager.conf</code>
     * @return the created connection to the Asterisk call manager
     * @throws IOException if the connection cannot be established.
     */
    public ManagerConnection getManagerConnection(String hostname,
            String username, String password) throws IOException
    {
        return new DefaultManagerConnection(hostname, this.port, username,
                password);
    }

    /**
     * Returns a new ManagerConnection to an Asterisk server running on given
     * host with the call manager interface listening on the given port.
     * 
     * @param hostname the name of the host the Asterisk server is running on
     * @param port the port the call manager interface is listening on
     * @param username the username as specified in Asterisk's
     *            <code>manager.conf</code>
     * @param password the password as specified in Asterisk's
     *            <code>manager.conf</code>
     * @return the created connection to the Asterisk call manager
     * @throws IOException if the connection cannot be established.
     */
    public ManagerConnection getManagerConnection(String hostname, int port,
            String username, String password) throws IOException
    {
        return new DefaultManagerConnection(hostname, port, username, password);
    }
}
