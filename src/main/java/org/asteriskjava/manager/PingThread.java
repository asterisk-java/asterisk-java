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
package org.asteriskjava.manager;

import org.asteriskjava.manager.action.PingAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;


/**
 * A Thread that pings the Asterisk server at a given interval.<br>
 * You can use this to prevent the connection being shut down when there is no
 * traffic.
 * 
 * @author srt
 * @version $Id: PingThread.java,v 1.2 2005/07/16 20:07:49 srt Exp $
 */
public class PingThread extends Thread
{
    /**
     * Default value for the interval attribute.
     */
    private static final long DEFAULT_INTERVAL = 2000;

    /**
     * Instance logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    private long interval;
    private boolean die;
    private ManagerConnection connection;

    /**
     * Creates a new PingThread that uses the given ManagerConnection.
     * 
     * @param connection ManagerConnection that is pinged
     */
    public PingThread(ManagerConnection connection)
    {
        this.connection = connection;
        this.interval = DEFAULT_INTERVAL;
        this.die = false;
        setName("Ping");
    }

    /**
     * Adjusts how often a PingAction is sent.<br>
     * Default is 2000ms.
     * 
     * @param interval the interval in milliseconds
     */
    public void setInterval(long interval)
    {
        this.interval = interval;
    }

    /**
     * Terminates this PingThread.
     */
    public void die()
    {
        this.die = true;
        interrupt();
    }

    public void run()
    {
        ManagerResponse response;

        while (!die)
        {
            try
            {
                sleep(interval);
            }
            catch (InterruptedException e)
            {
                // swallow
            }

            if (die)
            {
                break;
            }

            try
            {
                response = connection.sendAction(new PingAction());
                logger.debug("Ping response: " + response);
            }
            catch (Exception e)
            {
                logger.warn("Exception on sending Ping", e);
            }
        }
    }
}
