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

import java.io.IOException;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Runs an AgiServer in a separate Thread.<br>
 * You can use this class to run an AgiServer in the background of your
 * application or run it in your servlet container or application server. 
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class AgiServerThread
{
    private final Log logger = LogFactory.getLog(getClass()); 
    private AgiServer agiServer;
    private Thread thread;
    private boolean daemon = true;

    public AgiServerThread()
    {
        
    }

    /**
     * Creates a new AgiServerThread that runs the given {@link AgiServer}.
     * 
     * @param agiServer the AgiServer to run.
     */
    public AgiServerThread(AgiServer agiServer)
    {
        this.agiServer = agiServer;
    }

    /**
     * Sets the AgiServer to run.<br>
     * This property must be set before starting the AgiServerThread by calling startup.
     * 
     * @param agiServer the AgiServer to run.
     */
    public void setAgiServer(AgiServer agiServer)
    {
        this.agiServer = agiServer;
    }

    /**
     * Marks the thread as either a daemon thread or a user thread.<br>
     * Default is <code>true</code>.
     * 
     * @param daemon if <code>false</code>, marks the thread as a user thread.
     * @see Thread#setDaemon(boolean)
     * @since 0.3
     */
    public void setDaemon(boolean daemon)
    {
        this.daemon = daemon;
    }

    /**
     * Starts the AgiServer in its own thread.<br>
     * Note: The AgiServerThread is designed to handle one AgiServer instance at
     * a time so calling this method twice without stopping the AgiServer in
     * between will result in a RuntimeException.
     * 
     * @throws IllegalStateException if the mandatory property agiServer has
     *         not been set or the AgiServer had already been started.
     * @throws RuntimeException if the AgiServer can't be started due to IO problems, for
     *         example because the socket has already been bound by another process.
     */
    public synchronized void startup() throws IllegalStateException, RuntimeException
    {
        if (agiServer == null)
        {
            throw new IllegalStateException("Mandatory property agiServer is not set.");
        }

        if (thread != null)
        {
            throw new IllegalStateException("AgiServer is already started");
        }

        thread = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    agiServer.startup();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Unable to start AgiServer.", e);
                }
            }
        });
        thread.setName("AgiServer Thread");
        thread.setDaemon(daemon);
        thread.start();
    }

    /**
     * Stops the {@link AgiServer}.<br>
     * The AgiServer must have been started by calling {@link #startup()} before you
     * can stop it.
     * 
     * @see AgiServer#shutdown()
     * @throws IllegalStateException if the mandatory property agiServer has
     *         not been set or the AgiServer had already been shut down.
     */
    public synchronized void shutdown() throws IllegalStateException
    {
        if (agiServer == null)
        {
            throw new IllegalStateException("Mandatory property agiServer is not set.");
        }

        agiServer.shutdown();

        if (thread != null)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                logger.warn("Interrupted while waiting for AgiServer to shutdown.");
            }
            thread = null;
        }
    }
}
