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
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.QueueMemberState;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Manages queue events on behalf of an AsteriskServer.
 * 
 * @author srt
 * @version $Id$
 */
class QueueManager
{
    private final Log logger = LogFactory.getLog(this.getClass());

    private final AsteriskServerImpl server;
    private final ChannelManager channelManager;

    /**
     * A map of ACD queues by there name.
     */
    private final Map<String, AsteriskQueueImpl> queues;

    /**
     * Creates a new instance.
     */
    QueueManager(AsteriskServerImpl server, ChannelManager channelManager)
    {
	this.server = server;
	this.channelManager = channelManager;
	this.queues = new HashMap<String, AsteriskQueueImpl>();
    }

    void initialize() throws ManagerCommunicationException
    {
	ResponseEvents re;

	try
	{
	    re = server.sendEventGeneratingAction(new QueueStatusAction());
	} catch (ManagerCommunicationException e)
	{
	    final Throwable cause = e.getCause();

	    if (cause instanceof EventTimeoutException)
	    {
		// this happens with Asterisk 1.0.x as it doesn't send a
		// QueueStatusCompleteEvent
		re = ((EventTimeoutException) cause).getPartialResult();
	    } else
	    {
		throw e;
	    }
	}

	for (ManagerEvent event : re.getEvents())
	{
	    if (event instanceof QueueParamsEvent)
	    {
		handleQueueParamsEvent((QueueParamsEvent) event);
	    } else if (event instanceof QueueMemberEvent)
	    {
		handleQueueMemberEvent((QueueMemberEvent) event);
	    } else if (event instanceof QueueEntryEvent)
	    {
		handleQueueEntryEvent((QueueEntryEvent) event);
	    }
	}
    }

    void disconnected()
    {
	synchronized (queues)
	{
	    queues.clear();
	}
    }

    Collection<AsteriskQueue> getQueues()
    {
	Collection<AsteriskQueue> copy;

	synchronized (queues)
	{
	    copy = new ArrayList<AsteriskQueue>(queues.values());
	}
	return copy;
    }

    private void addQueue(AsteriskQueueImpl queue)
    {
	synchronized (queues)
	{
	    queues.put(queue.getName(), queue);
	}
    }

    void handleQueueParamsEvent(QueueParamsEvent event)
    {
	AsteriskQueueImpl queue;
	final String name;
	final Integer max;
	final String strategy;
	final Integer serviceLevel;
	final Integer weight;

	name = event.getQueue();
	max = event.getMax();
	strategy = event.getStrategy();
	serviceLevel = event.getServiceLevel();
	weight = event.getServiceLevel();

	queue = queues.get(name);

	if (queue == null)
	{
	    queue = new AsteriskQueueImpl(server, name, max, strategy,
		    serviceLevel, weight);
	    logger.info("Adding new queue " + queue);
	    addQueue(queue);
	} else
	{
	    synchronized (queue)
	    {
		queue.setMax(max);
		queue.setServiceLevel(serviceLevel);
		queue.setWeight(weight);
	    }
	}
    }

    void handleQueueMemberEvent(QueueMemberEvent event)
    {
	final AsteriskQueueImpl queue = queues.get(event.getQueue());
	if (queue == null)
	{
	    logger.error("Ignored QueueEntryEvent for unknown queue "
		    + event.getQueue());
	    return;
	}
	AsteriskQueueMemberImpl member = queue.getMember(event.getLocation());
	if (member == null)
	{
	    member = new AsteriskQueueMemberImpl(server, queue, event
		    .getLocation(), QueueMemberState.DEVICE_NOT_INUSE);
	}
	queue.addMember(member);

    }

    void handleQueueEntryEvent(QueueEntryEvent event)
    {
	final AsteriskQueueImpl queue = queues.get(event.getQueue());
	final AsteriskChannelImpl channel = channelManager
		.getChannelImplByName(event.getChannel());

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

	queue.addEntry(channel);
    }

    void handleJoinEvent(JoinEvent event)
    {
	final AsteriskQueueImpl queue = queues.get(event.getQueue());
	final AsteriskChannelImpl channel = channelManager
		.getChannelImplByName(event.getChannel());

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

	queue.addEntry(channel);
    }

    void handleLeaveEvent(LeaveEvent event)
    {
	final AsteriskQueueImpl queue = queues.get(event.getQueue());
	final AsteriskChannelImpl channel = channelManager
		.getChannelImplByName(event.getChannel());

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

	queue.removeEntry(channel);
    }

    /**
     * Challange a QueueMemberStatusEvent.
     * @param event that was triggered by Asterisk server.
     */
    void handleQueueMemberStatusEvent(QueueMemberStatusEvent event)
    {
	AsteriskQueueImpl queue = getQueueByName(event.getQueue());
	AsteriskQueueMemberImpl member = queue.getMemberByLocation(event
		.getLocation());

	if (queue == null)
	{
	    logger.error("Ignored QueueMemberStatusEvent for unknown queue "
		    + event.getQueue());
	    return;
	}
	if (member == null)
	{
	    logger.error("Ignored QueueMemberStatusEvent for unknown member "
		    + event.getLocation());
	    return;
	}

	member.stateChanged(QueueMemberState.valueOf(event.getStatus()));
	queue.fireMemberStateChanged(member);
    }

    /**
     * Retrieves a queue by its name.
     * @param queue - name of the queue.
     * @return the requested queue.
     */
    private AsteriskQueueImpl getQueueByName(String queue)
    {
	synchronized (queues)
	{
	    for (AsteriskQueueImpl asteriskQueue : queues.values())
	    {
		if (asteriskQueue.getName().equals(queue))
		{
		    return asteriskQueue;
		}
	    }
	    logger.error("Requested queue " + queue + " not found!");
	    return null;
	}
    }

}
