/*
 * Copyright  2004-2005 Stefan Reuter
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

import java.io.IOException;

import org.asteriskjava.fastagi.command.VerboseCommand;
import org.asteriskjava.fastagi.impl.AGIChannelImpl;
import org.asteriskjava.fastagi.impl.AGIReaderImpl;
import org.asteriskjava.fastagi.impl.AGIWriterImpl;
import org.asteriskjava.io.SocketConnectionFacade;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;



/**
 * An AGIConnectionHandler is created and run by the AGIServer whenever a new
 * socket connection from an Asterisk Server is received.<br>
 * It reads the request using an AGIReader and runs the AGIScript configured to
 * handle this type of request. Finally it closes the socket connection.
 * 
 * @author srt
 * @version $Id: AGIConnectionHandler.java,v 1.13 2005/11/04 21:49:01 srt Exp $
 */
public class AGIConnectionHandler implements Runnable
{
    private final Log logger = LogFactory.getLog(getClass());
    private static final ThreadLocal<AGIChannel> channel = new ThreadLocal<AGIChannel>();

    /**
     * The socket connection.
     */
    private SocketConnectionFacade socket;

    /**
     * The strategy to use to determine which script to run.
     */
    private MappingStrategy mappingStrategy;

    /**
     * Creates a new AGIConnectionHandler to handle the given socket connection.
     * 
     * @param socket the socket connection to handle.
     * @param mappingStrategy the strategy to use to determine which script to
     *            run.
     */
    public AGIConnectionHandler(SocketConnectionFacade socket,
            MappingStrategy mappingStrategy)
    {
        this.socket = socket;
        this.mappingStrategy = mappingStrategy;
    }

    protected AGIReader createReader()
    {
        return new AGIReaderImpl(socket);
    }

    protected AGIWriter createWriter()
    {
        return new AGIWriterImpl(socket);
    }

    public void run()
    {
        try
        {
            AGIReader reader;
            AGIWriter writer;
            AGIRequest request;
            AGIChannel channel;
            AGIScript script;
            Thread thread;
            String threadName;

            reader = createReader();
            writer = createWriter();

            request = reader.readRequest();
            channel = new AGIChannelImpl(writer, reader);

            script = mappingStrategy.determineScript(request);

            thread = Thread.currentThread();
            threadName = thread.getName();
            AGIConnectionHandler.channel.set(channel);

            if (script != null)
            {
                logger.info("Begin AGIScript " + script.getClass().getName()
                        + " on " + threadName);
                script.service(request, channel);
                logger.info("End AGIScript " + script.getClass().getName()
                        + " on " + threadName);
            }
            else
            {
                String error;

                error = "No script configured for URL '"
                        + request.getRequestURL() + "' (script '"
                        + request.getScript() + "')";
                channel.sendCommand(new VerboseCommand(error, 1));
                logger.error(error);
            }
        }
        catch (AGIException e)
        {
            logger.error("AGIException while handling request", e);
        }
        catch (Exception e)
        {
            logger.error("Unexpected Exception while handling request", e);
        }
        finally
        {
            AGIConnectionHandler.channel.set(null);
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                // swallow
            }
        }
    }

    /**
     * Returns the AGIChannel associated with the current thread.<br>
     * 
     * @return the AGIChannel associated with the current thread or
     *         <code>null</code> if none is associated.
     */
    static AGIChannel getChannel()
    {
        return (AGIChannel) AGIConnectionHandler.channel.get();
    }
}
