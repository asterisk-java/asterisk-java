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

import java.io.Serializable;

/**
 * Represents an Asterisk server that is connected via the Manager API.
 * 
 * @author PY
 * @version $Id$
 */
public class AsteriskServer implements Serializable
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 3257284738393125176L;

    /**
     * The hostname to use if none is provided.
     */
    private static final String DEFAULT_HOSTNAME = "localhost";

    /**
     * The port to use if none is provided.
     */
    private static final int DEFAULT_PORT = 5038;

    /**
     * The hostname of the Asterisk server.
     */
    private String hostname;

    /**
     * The port on the Asterisk server the Asterisk Manager API is listening on.
     */
    private int port;

    /**
     * Creates a new AsteriskServer with default hostname and default port.
     */
    public AsteriskServer()
    {
        this.hostname = DEFAULT_HOSTNAME;
        this.port = DEFAULT_PORT;
    }

    /**
     * Creates a new Asterisk server with the given hostname and port.
     * 
     * @param hostname the hostname of the Asterisk server
     * @param port the port on the Asterisk server the Asterisk Manager API is listening on
     */
    public AsteriskServer(final String hostname, final int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Returns the hostname.
     * 
     * @return the hostname
     */
    public final String getHostname()
    {
        return hostname;
    }

    /**
     * Sets the hostname.
     * 
     * @param hostname the hostname to set
     */
    public final void setHostname(final String hostname)
    {
        this.hostname = hostname;
    }

    /**
     * Returns the port.
     * 
     * @return the port
     */
    public final int getPort()
    {
        return port;
    }

    /**
     * Sets the port.
     * 
     * @param port the port to set.
     */
    public final void setPort(final int port)
    {
        this.port = port;
    }

    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof AsteriskServer))
        {
            return false;
        }

        AsteriskServer s = (AsteriskServer) o;
        if (this.getHostname() != null)
        {
            if (!this.getHostname().equals(s.getHostname()))
            {
                return false;
            }
        }
        else
        {
            if (s.getHostname() != null)
            {
                return false;
            }
        }

        if (this.getPort() != s.getPort())
        {
            return false;
        }

        return true;
    }

    public String toString()
    {
        return "AsteriskServer[hostname='" + hostname + "',port=" + port + "]";
    }
}
