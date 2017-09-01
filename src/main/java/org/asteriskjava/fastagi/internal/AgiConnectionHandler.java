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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiChannelFactory;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiReader;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.AgiWriter;
import org.asteriskjava.fastagi.MappingStrategy;
import org.asteriskjava.fastagi.NamedAgiScript;
import org.asteriskjava.fastagi.command.VerboseCommand;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * An AgiConnectionHandler is created and run by the AgiServer whenever a new
 * AGI connection from an Asterisk Server is received. <br>
 * It reads the request using an AgiReader and runs the AgiScript configured to
 * handle this type of request. Finally it closes the AGI connection.
 *
 * @author srt
 * @version $Id$
 */
public abstract class AgiConnectionHandler implements Runnable
{
    private static final String AJ_AGISTATUS_VARIABLE = "AJ_AGISTATUS";
    private static final String AJ_AGISTATUS_NOT_FOUND = "NOT_FOUND";
    private static final String AJ_AGISTATUS_SUCCESS = "SUCCESS";
    private static final String AJ_AGISTATUS_FAILED = "FAILED";
    private static final ThreadLocal<AgiChannel> channel = new ThreadLocal<>();
    private final Log logger = LogFactory.getLog(getClass());
    private boolean ignoreMissingScripts = false;
    private AgiScript script = null;
    private AgiChannelFactory agiChannelFactory;

    public static final ConcurrentMap<AgiConnectionHandler, AgiChannel> AGI_CONNECTION_HANDLERS = new ConcurrentHashMap<>(
            32);

    /**
     * The strategy to use to determine which script to run.
     */
    private final MappingStrategy mappingStrategy;

    /**
     * Creates a new AGIConnectionHandler to handle the given socket connection.
     *
     * @param mappingStrategy the strategy to use to determine which script to
     *            run.
     * @param agiChannelFactory the AgiFactory, that is used to create new
     *            AgiChannel objects.
     */
    protected AgiConnectionHandler(MappingStrategy mappingStrategy, AgiChannelFactory agiChannelFactory)
    {
        if (mappingStrategy == null)
        {
            throw new IllegalArgumentException("MappingStrategy must not be null");
        }
        if (agiChannelFactory == null)
        {
            throw new IllegalArgumentException("AgiChannelFactory must not be null");
        }

        this.agiChannelFactory = agiChannelFactory;
        this.mappingStrategy = mappingStrategy;
    }

    protected boolean isIgnoreMissingScripts()
    {
        return ignoreMissingScripts;
    }

    protected void setIgnoreMissingScripts(boolean ignoreMissingScripts)
    {
        this.ignoreMissingScripts = ignoreMissingScripts;
    }

    protected AgiScript getScript()
    {
        return script;
    }

    protected abstract AgiReader createReader();

    protected abstract AgiWriter createWriter();

    /**
     * Release any open resources like closing a socket.
     */
    public abstract void release();

    @Override
    public void run()
    {
        AgiChannel channel = null;

        try
        {
            AgiReader reader;
            AgiWriter writer;
            AgiRequest request;

            reader = createReader();
            writer = createWriter();

            request = reader.readRequest();
            channel = this.agiChannelFactory.createAgiChannel(request, writer, reader);

            AgiConnectionHandler.channel.set(channel);

            if (mappingStrategy != null)
            {
                script = mappingStrategy.determineScript(request, channel);
            }

            if (script == null && !ignoreMissingScripts)
            {
                final String errorMessage;

                errorMessage = "No script configured for URL '" + request.getRequestURL() + "' (script '"
                        + request.getScript() + "')";
                logger.error(errorMessage);

                setStatusVariable(channel, AJ_AGISTATUS_NOT_FOUND);
                logToAsterisk(channel, errorMessage);
            }
            else if (script != null)
            {
                AGI_CONNECTION_HANDLERS.put(this, channel);
                runScript(script, request, channel);
            }
        }
        catch (AgiException e)
        {
            setStatusVariable(channel, AJ_AGISTATUS_FAILED);
            logger.error("AgiException while handling request", e);
        }
        catch (Exception e)
        {
            setStatusVariable(channel, AJ_AGISTATUS_FAILED);
            logger.error("Unexpected Exception while handling request", e);
        }
        finally
        {
            AGI_CONNECTION_HANDLERS.remove(this);
            AgiConnectionHandler.channel.set(null);
            release();
        }
    }// run

    private void runScript(AgiScript script, AgiRequest request, AgiChannel channel)
    {
        String threadName;
        threadName = Thread.currentThread().getName();

        logger.debug("Begin AgiScript " + getScriptName(script) + " on " + threadName);
        try
        {
            script.service(request, channel);
            setStatusVariable(channel, AJ_AGISTATUS_SUCCESS);
        }
        catch (AgiException e)
        {
            logger.error("AgiException running AgiScript " + getScriptName(script) + " on " + threadName, e);
            setStatusVariable(channel, AJ_AGISTATUS_FAILED);
        }
        catch (Exception e)
        {
            logger.error("Exception running AgiScript " + getScriptName(script) + " on " + threadName, e);
            setStatusVariable(channel, AJ_AGISTATUS_FAILED);
        }
        logger.debug("End AgiScript " + getScriptName(script) + " on " + threadName);
    }

    protected String getScriptName(AgiScript script)
    {
        if (script == null)
        {
            return null;
        }

        if (script instanceof NamedAgiScript)
        {
            return ((NamedAgiScript) script).getName();
        }
        return script.getClass().getName();
    }

    private void setStatusVariable(AgiChannel channel, String value)
    {
        if (channel == null)
        {
            return;
        }

        try
        {
            channel.setVariable(AJ_AGISTATUS_VARIABLE, value);
        }
        catch (Exception e) // NOPMD
        {
            // swallow
        }
    }

    private void logToAsterisk(AgiChannel channel, String message)
    {
        if (channel == null)
        {
            return;
        }

        try
        {
            channel.sendCommand(new VerboseCommand(message, 1));
        }
        catch (Exception e) // NOPMD
        {
            // swallow
        }
    }

    /**
     * Returns the AgiChannel associated with the current thread.
     *
     * @return the AgiChannel associated with the current thread or
     *         <code>null</code> if none is associated.
     */
    public static AgiChannel getChannel()
    {
        return AgiConnectionHandler.channel.get();
    }
}
