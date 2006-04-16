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
package org.asteriskjava.live.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;


/**
 * Manages queue events on behalf of an AsteriskManager.
 * 
 * @author srt
 * @version $Id$
 */
public class QueueManager
{
    private final Log logger = LogFactory.getLog(this.getClass());

    private final ChannelManager channelManager;
    
    /**
     * A map of ACD queues by there name.
     */
    private final Map<String, AsteriskQueueImpl> queues;

    /**
     * Creates a new instance.
     */
    public QueueManager(ChannelManager channelManager)
    {
        this.channelManager = channelManager;
        this.queues = new HashMap<String, AsteriskQueueImpl>();
    }

    public void clear()
    {
        synchronized (queues)
        {
            queues.clear();
        }
    }
    
    public Collection<AsteriskQueue> getQueues()
    {
        Collection<AsteriskQueue> copy;
        
        synchronized (queues)
        {
            copy = new ArrayList<AsteriskQueue>(queues.values());
        }
        return copy;
    }

    protected void addQueue(AsteriskQueueImpl queue)
    {
        synchronized (queues)
        {
            queues.put(queue.getName(), queue);
        }
    }

    protected void removeQueue(AsteriskQueue queue)
    {
        synchronized (queues)
        {
            queues.remove(queue.getName());
        }
    }

    public void handleQueueParamsEvent(QueueParamsEvent event)
    {
        AsteriskQueueImpl queue;
        boolean isNew = false;

        queue = queues.get(event.getQueue());

        if (queue == null)
        {
            queue = new AsteriskQueueImpl(event.getQueue());
            isNew = true;
        }

        synchronized (queue)
        {
            queue.setMax(event.getMax());
        }

        if (isNew)
        {
            logger.info("Adding new queue " + queue.getName());
            addQueue(queue);
        }
    }

    public void handleQueueMemberEvent(QueueMemberEvent event)
    {

    }

    public void handleQueueEntryEvent(QueueEntryEvent event)
    {
        AsteriskQueueImpl queue = queues.get(event.getQueue());
        AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

        if (queue == null)
        {
            logger.error("Ignored QueueEntryEvent for unknown queue "
                    + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("Ignored QueueEntryEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        if (!queue.getEntries().contains(channel))
        {
            queue.addEntry(channel);
        }
    }

    public void handleJoinEvent(JoinEvent event)
    {
        AsteriskQueueImpl queue = queues.get(event.getQueue());
        AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

        if (queue == null)
        {
            logger.error("Ignored JoinEvent for unknown queue "
                    + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("Ignored JoinEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        if (!queue.getEntries().contains(channel))
        {
            queue.addEntry(channel);
        }
    }

    public void handleLeaveEvent(LeaveEvent event)
    {
        AsteriskQueueImpl queue = queues.get(event.getQueue());
        AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

        if (queue == null)
        {
            logger.error("Ignored LeaveEvent for unknown queue "
                    + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("Ignored LeaveEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        if (queue.getEntries().contains(channel))
        {
            queue.removeEntry(channel);
        }
    }
}
