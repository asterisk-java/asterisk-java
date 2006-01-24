/*
 *  Copyright  2004-2006 Stefan Reuter
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

/**
 * Runs an AGIServer in a separate Thread.<br>
 * You can use this class to run an AGIServer in the background of your
 * application or run it in your webcontainer or application server. 
 * 
 * @author srt
 * @version $Id: AGIServerThread.java,v 1.2 2005/10/25 22:26:21 srt Exp $
 * @since 0.2
 */
public class AGIServerThread
{
    private AGIServer agiServer;
    private Thread thread;

    /**
     * Sets the AGIServer to run.
     * 
     * @param agiServer the AGIServer to run.
     */
    public void setAgiServer(AGIServer agiServer)
    {
        this.agiServer = agiServer;
    }

    /**
     * Starts the AGIServer in its own thread.<br>
     * Note: The AGIServerThread is designed to handle on AGIServer instance at
     * a time so calling this method twice without stopping the AGIServer in
     * between will result in a RuntimeException.
     */
    public synchronized void startup()
    {
        if (agiServer == null)
        {
            throw new RuntimeException(
                    "Mandatory property agiServer is not set.");
        }

        if (thread != null)
        {
            throw new RuntimeException("AGIServer is already started");
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
                    throw new RuntimeException("Unable to start AGIServer.", e);
                }
            }
        });
        thread.setName("AGIServer Thread");
        thread.start();
    }

    /**
     * Stops the AGIServer.
     */
    public synchronized void shutdown()
    {
        if (agiServer == null)
        {
            throw new RuntimeException(
                    "Mandatory property agiServer is not set.");
        }

        try
        {
            agiServer.shutdown();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to stop AGIServer.", e);
        }

        thread = null;
    }
}
