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
package org.asteriskjava.fastagi.internal;

import java.io.IOException;

import org.asteriskjava.fastagi.AgiChannelFactory;
import org.asteriskjava.fastagi.AgiReader;
import org.asteriskjava.fastagi.AgiWriter;
import org.asteriskjava.fastagi.MappingStrategy;
import org.asteriskjava.util.SocketConnectionFacade;

/**
 * An AgiConnectionHandler for FastAGI.
 * <br>
 * It reads the request using a FastAgiReader and runs the AgiScript configured to
 * handle this type of request. Finally it closes the socket connection.
 *
 * @author srt
 * @version $Id$
 */
public class FastAgiConnectionHandler extends AgiConnectionHandler
{
    /**
     * The socket connection.
     */
    private final SocketConnectionFacade socket;

    /**
     * Creates a new FastAGIConnectionHandler to handle the given FastAGI socket connection.
     *
     * @param mappingStrategy   the strategy to use to determine which script to run.
     * @param socket            the socket connection to handle.
     * @param agiChannelFactory The factory to use for creating new AgiChannel instances.
     */
    public FastAgiConnectionHandler(MappingStrategy mappingStrategy, SocketConnectionFacade socket, AgiChannelFactory agiChannelFactory)
    {
        super(mappingStrategy, agiChannelFactory);
        this.socket = socket;
    }

    @Override
    protected AgiReader createReader()
    {
        return new FastAgiReader(socket);
    }

    @Override
    protected AgiWriter createWriter()
    {
        return new FastAgiWriter(socket);
    }

    @Override
    public void release()
    {
        try
        {
            socket.close();
        }
        catch (IOException e) // NOPMD
        {
            // swallow
        }
    }
}