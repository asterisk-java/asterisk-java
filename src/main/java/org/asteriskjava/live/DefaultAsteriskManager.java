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
package org.asteriskjava.live;

import java.util.Collection;
import java.util.Map;

import org.asteriskjava.live.internal.AsteriskManagerImpl;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;

/**
 * Default implementation of the AsteriskManager interface.
 * 
 * @see org.asteriskjava.live.AsteriskManager
 * @author srt
 * @version $Id$
 */
public class DefaultAsteriskManager implements AsteriskManager
{
    private AsteriskManagerImpl asteriskManager;

    /**
     * Creates a new instance.
     */
    public DefaultAsteriskManager()
    {
        asteriskManager = new AsteriskManagerImpl();
    }

    /**
     * Creates a new instance.
     * 
     * @param eventConnection the ManagerConnection to use for receiving events from Asterisk.
     */
    public DefaultAsteriskManager(ManagerConnection eventConnection)
    {
        asteriskManager = new AsteriskManagerImpl(eventConnection);
    }

    /**
     * Determines if queue status is retrieved at startup. If you don't need
     * queue information and still run Asterisk 1.0.x you can set this to
     * <code>true</code> to circumvent the startup delay caused by the missing
     * QueueStatusComplete event.<br>
     * Default is <code>false</code>.
     * 
     * @param skipQueues <code>true</code> to skip queue initialization,
     *            <code>false</code> to not skip.
     * @since 0.2
     */
    public void setSkipQueues(boolean skipQueues)
    {
        asteriskManager.setSkipQueues(skipQueues);
    }

    public void setManagerConnection(ManagerConnection eventConnection)
    {
        asteriskManager.setManagerConnection(eventConnection);
    }

    public void initialize() throws AuthenticationFailedException, ManagerCommunicationException
    {
        asteriskManager.initialize();
    }
    
    /* Implementation of the AsteriskManager interface */

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout) throws ManagerCommunicationException
    {
        return asteriskManager.originateToExtension(channel, context, exten, priority, timeout);
    }

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout, Map<String, String> variables) throws ManagerCommunicationException
    {
        return asteriskManager.originateToExtension(channel, context, exten, priority, timeout, variables);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout) throws ManagerCommunicationException
    {
        return asteriskManager.originateToApplication(channel, application, data, timeout);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout, Map<String, String> variables) throws ManagerCommunicationException
    {
        return asteriskManager.originateToApplication(channel, application, data, timeout, variables);
    }

    public Collection<AsteriskChannel> getChannels()
    {
        return asteriskManager.getChannels();
    }

    public AsteriskChannel getChannelByName(String name)
    {
        return asteriskManager.getChannelByName(name);
    }

    public AsteriskChannel getChannelById(String id)
    {
        return asteriskManager.getChannelById(id);
    }

    public Collection<AsteriskQueue> getQueues()
    {
        return asteriskManager.getQueues();
    }

    public String getVersion() throws ManagerCommunicationException
    {
        return asteriskManager.getVersion();
    }

    public int[] getVersion(String file)
    {
        return asteriskManager.getVersion(file);
    }
}
