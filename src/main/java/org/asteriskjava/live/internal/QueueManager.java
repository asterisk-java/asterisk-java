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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

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
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberPenaltyEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
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
    private boolean queuesMonitorForced = false;
    private long queueMonitorLastTimeReloaded;
    private long queuesMonitorLeaseTime = 1000;

    /**
     * A map of ACD queues by there name. 101119 OLB: Modified to act as a LRU
     * Cache to optimize updates
     */
    private final Map<String, AsteriskQueueImpl> queuesLRU = new LinkedHashMap<>();

    QueueManager(AsteriskServerImpl server, ChannelManager channelManager)
    {
        this.server = server;
        this.channelManager = channelManager;
    }

    void initialize() throws ManagerCommunicationException
    {
        ResponseEvents re;

        try
        {
            re = server.sendEventGeneratingAction(new QueueStatusAction());
        }
        catch (ManagerCommunicationException e)
        {
            final Throwable cause = e.getCause();

            if (cause instanceof EventTimeoutException)
            {
                // this happens with Asterisk 1.0.x as it doesn't send a
                // QueueStatusCompleteEvent
                re = ((EventTimeoutException) cause).getPartialResult();
            }
            else
            {
                throw e;
            }
        }

        for (ManagerEvent event : re.getEvents())
        {
            if (event instanceof QueueParamsEvent)
            {
                handleQueueParamsEvent((QueueParamsEvent) event);
            }
            else if (event instanceof QueueMemberEvent)
            {
                handleQueueMemberEvent((QueueMemberEvent) event);
            }
            else if (event instanceof QueueEntryEvent)
            {
                handleQueueEntryEvent((QueueEntryEvent) event);
            }
        }

    }

    /**
     * Method to ask for a Queue data update
     * 
     * @author Octavio Luna
     * @param queue
     * @throws ManagerCommunicationException
     */
    void updateQueue(String queue) throws ManagerCommunicationException
    {
        ResponseEvents re;

        try
        {
            QueueStatusAction queueStatusAction = new QueueStatusAction();
            queueStatusAction.setQueue(queue);
            re = server.sendEventGeneratingAction(queueStatusAction);
        }
        catch (ManagerCommunicationException e)
        {
            final Throwable cause = e.getCause();

            if (cause instanceof EventTimeoutException)
            {
                // this happens with Asterisk 1.0.x as it doesn't send a
                // QueueStatusCompleteEvent
                re = ((EventTimeoutException) cause).getPartialResult();
            }
            else
            {
                throw e;
            }
        }

        for (ManagerEvent event : re.getEvents())
        {
            // 101119 OLB: solo actualizamos el QUEUE por ahora
            if (event instanceof QueueParamsEvent)
            {
                handleQueueParamsEvent((QueueParamsEvent) event);
            }

            else if (event instanceof QueueMemberEvent)
            {
                handleQueueMemberEvent((QueueMemberEvent) event);
            }
            else if (event instanceof QueueEntryEvent)
            {
                handleQueueEntryEvent((QueueEntryEvent) event);
            }

        }
    }

    void disconnected()
    {
        synchronized (queuesLRU)
        {
            for (AsteriskQueueImpl queue : queuesLRU.values())
            {
                queue.cancelServiceLevelTimer();
            }
            queuesLRU.clear();
        }
    }

    /**
     * Gets (a copy of) the list of the queues.
     * 
     * @return a copy of the list of the queues.
     */
    Collection<AsteriskQueue> getQueues()
    {
        refreshQueuesIfForced();

        Collection<AsteriskQueue> copy;

        synchronized (queuesLRU)
        {
            copy = new ArrayList<AsteriskQueue>(queuesLRU.values());
        }
        return copy;
    }

    public List<AsteriskQueue> getQueuesUpdatedAfter(Date date)
    {
        refreshQueuesIfForced();

        List<AsteriskQueue> copy = new ArrayList<>();
        synchronized (queuesLRU)
        {
            List<Entry<String, AsteriskQueueImpl>> list = new ArrayList<>(queuesLRU.entrySet());
            ListIterator<Entry<String, AsteriskQueueImpl>> iter = list.listIterator(list.size());

            Entry<String, AsteriskQueueImpl> entry;
            while (iter.hasPrevious())
            {
                entry = iter.previous();
                AsteriskQueueImpl astQueue = entry.getValue();
                if (astQueue.getLastUpdateMillis() <= date.getTime())
                {
                    break;
                }
                copy.add(astQueue);
            }

        }

        return copy;
    }

    /**
     * Adds a queue to the internal map, keyed by name.
     * 
     * @param queue the AsteriskQueueImpl to be added
     */
    private void addQueue(AsteriskQueueImpl queue)
    {
        synchronized (queuesLRU)
        {
            queuesLRU.put(queue.getName(), queue);
        }
    }

    /**
     * Called during initialization to populate the list of queues.
     * 
     * @param event the event received
     */
    private void handleQueueParamsEvent(QueueParamsEvent event)
    {
        AsteriskQueueImpl queue;

        final String name = event.getQueue();
        final Integer max = event.getMax();
        final String strategy = event.getStrategy();
        final Integer serviceLevel = event.getServiceLevel();
        final Integer weight = event.getWeight();
        final Integer calls = event.getCalls();
        final Integer holdTime = event.getHoldTime();
        final Integer talkTime = event.getTalkTime();
        final Integer completed = event.getCompleted();
        final Integer abandoned = event.getAbandoned();
        final Double serviceLevelPerf = event.getServiceLevelPerf();

        queue = getInternalQueueByName(name);

        if (queue == null)
        {
            queue = new AsteriskQueueImpl(server, name, max, strategy, serviceLevel, weight, calls, holdTime, talkTime,
                    completed, abandoned, serviceLevelPerf);
            logger.info("Adding new queue " + queue);
            addQueue(queue);
        }
        else
        {
            // We should never reach that code as this method is only called for
            // initialization
            // So the queue should never be in the queues list
            synchronized (queue)
            {
                synchronized (queuesLRU)
                {

                    if (queue.setMax(max) | queue.setServiceLevel(serviceLevel) | queue.setWeight(weight)
                            | queue.setCalls(calls) | queue.setHoldTime(holdTime) | queue.setTalkTime(talkTime)
                            | queue.setCompleted(completed) | queue.setAbandoned(abandoned)
                            | queue.setServiceLevelPerf(serviceLevelPerf))
                    {

                        queuesLRU.remove(queue.getName());
                        queuesLRU.put(queue.getName(), queue);
                    }
                }
            }
        }
    }

    /**
     * Called during initialization to populate the members of the queues.
     * 
     * @param event the QueueMemberEvent received
     */
    private void handleQueueMemberEvent(QueueMemberEvent event)
    {
        final AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());
        if (queue == null)
        {
            logger.error("Ignored QueueEntryEvent for unknown queue " + event.getQueue());
            return;
        }

        AsteriskQueueMemberImpl member = queue.getMember(event.getLocation());
        if (member == null)
        {
            member = new AsteriskQueueMemberImpl(server, queue, event.getLocation(), QueueMemberState.valueOf(event
                    .getStatus()), event.getPaused(), event.getPenalty(), event.getMembership(), event.getCallsTaken(),
                    event.getLastCall());

            queue.addMember(member);
        }
        else
        {
            manageQueueMemberChange(queue, member, event);
        }

    }

    private void manageQueueMemberChange(AsteriskQueueImpl queue, AsteriskQueueMemberImpl member, QueueMemberEvent event)
    {
        if (member.stateChanged(QueueMemberState.valueOf(event.getStatus())) | member.pausedChanged(event.getPaused())
                | member.penaltyChanged(event.getPenalty()) | member.callsTakenChanged(event.getCallsTaken())
                | member.lastCallChanged(event.getLastCall()))
        {
            queue.stampLastUpdate();
        }
    }

    /**
     * Called during initialization to populate entries of the queues. Currently
     * does the same as handleJoinEvent()
     * 
     * @param event - the QueueEntryEvent received
     */
    private void handleQueueEntryEvent(QueueEntryEvent event)
    {
        final AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());
        final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

        if (queue == null)
        {
            logger.error("Ignored QueueEntryEvent for unknown queue " + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("Ignored QueueEntryEvent for unknown channel " + event.getChannel());
            return;
        }

        if (queue.getEntry(event.getChannel()) != null)
        {
            logger.debug("Ignored duplicate queue entry during population in queue " + event.getQueue() + " for channel "
                    + event.getChannel());
            return;
        }

        // Asterisk gives us an initial position but doesn't tell us when he
        // shifts the others
        // We won't use this data for ordering until there is a appropriate
        // event in AMI.
        // (and refreshing the whole queue is too intensive and suffers
        // incoherencies
        // due to asynchronous shift that leaves holes if requested too fast)
        int reportedPosition = event.getPosition();

        queue.createNewEntry(channel, reportedPosition, event.getDateReceived());
    }

    /**
     * Called from AsteriskServerImpl whenever a new entry appears in a queue.
     * 
     * @param event the JoinEvent received
     */
    void handleJoinEvent(JoinEvent event)
    {
        final AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());
        final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

        if (queue == null)
        {
            logger.error("Ignored JoinEvent for unknown queue " + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("Ignored JoinEvent for unknown channel " + event.getChannel());
            return;
        }

        if (queue.getEntry(event.getChannel()) != null)
        {
            logger.error("Ignored duplicate queue entry in queue " + event.getQueue() + " for channel " + event.getChannel());
            return;
        }

        // Asterisk gives us an initial position but doesn't tell us when he
        // shifts the others
        // We won't use this data for ordering until there is a appropriate
        // event in AMI.
        // (and refreshing the whole queue is too intensive and suffers
        // incoherencies
        // due to asynchronous shift that leaves holes if requested too fast)
        int reportedPosition = event.getPosition();

        queue.createNewEntry(channel, reportedPosition, event.getDateReceived());
    }

    /**
     * Called from AsteriskServerImpl whenever an enty leaves a queue.
     * 
     * @param event - the LeaveEvent received
     */
    void handleLeaveEvent(LeaveEvent event)
    {
        final AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());
        final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

        if (queue == null)
        {
            logger.error("Ignored LeaveEvent for unknown queue " + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("Ignored LeaveEvent for unknown channel " + event.getChannel());
            return;
        }

        final AsteriskQueueEntryImpl existingQueueEntry = queue.getEntry(event.getChannel());
        if (existingQueueEntry == null)
        {
            logger.error("Ignored leave event for non existing queue entry in queue " + event.getQueue() + " for channel "
                    + event.getChannel());
            return;
        }

        queue.removeEntry(existingQueueEntry, event.getDateReceived());
    }

    /**
     * Challange a QueueMemberStatusEvent. Called from AsteriskServerImpl
     * whenever a member state changes.
     * 
     * @param event that was triggered by Asterisk server.
     */
    void handleQueueMemberStatusEvent(QueueMemberStatusEvent event)
    {
        AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());

        if (queue == null)
        {
            logger.error("Ignored QueueMemberStatusEvent for unknown queue " + event.getQueue());
            return;
        }

        AsteriskQueueMemberImpl member = queue.getMemberByLocation(event.getLocation());
        if (member == null)
        {
            logger.error("Ignored QueueMemberStatusEvent for unknown member " + event.getLocation());
            return;
        }

        manageQueueMemberChange(queue, member, event);
        queue.fireMemberStateChanged(member);
    }

    void handleQueueMemberPausedEvent(QueueMemberPausedEvent event)
    {
        AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());

        if (queue == null)
        {
            logger.error("Ignored QueueMemberPausedEvent for unknown queue " + event.getQueue());
            return;
        }

        AsteriskQueueMemberImpl member = queue.getMemberByLocation(event.getLocation());
        if (member == null)
        {
            logger.error("Ignored QueueMemberPausedEvent for unknown member " + event.getLocation());
            return;
        }

        member.pausedChanged(event.getPaused());
    }

    void handleQueueMemberPenaltyEvent(QueueMemberPenaltyEvent event)
    {
        AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());

        if (queue == null)
        {
            logger.error("Ignored QueueMemberStatusEvent for unknown queue " + event.getQueue());
            return;
        }

        AsteriskQueueMemberImpl member = queue.getMemberByLocation(event.getLocation());
        if (member == null)
        {
            logger.error("Ignored QueueMemberStatusEvent for unknown member " + event.getLocation());
            return;
        }

        member.penaltyChanged(event.getPenalty());
    }

    /**
     * Retrieves a queue by its name.
     * 
     * @param queueName name of the queue.
     * @return the requested queue or <code>null</code> if there is no queue
     *         with the given name.
     */
    AsteriskQueueImpl getQueueByName(String queueName)
    {
        refreshQueueIfForced(queueName);

        AsteriskQueueImpl queue = getInternalQueueByName(queueName);
        if (queue == null)
        {
            logger.error("Requested queue '" + queueName + "' not found!");
        }

        return queue;
    }

    private AsteriskQueueImpl getInternalQueueByName(String queueName)
    {
        AsteriskQueueImpl queue;

        synchronized (queuesLRU)
        {
            queue = queuesLRU.get(queueName);
        }
        return queue;
    }

    private void refreshQueueIfForced(String queueName)
    {
        if (queuesMonitorForced)
        {
            updateQueue(queueName);
        }
    }

    private void refreshQueuesIfForced()
    {
        if (queuesMonitorForced && (System.currentTimeMillis() - queueMonitorLastTimeReloaded) > queuesMonitorLeaseTime)
        {
            initialize();
            queueMonitorLastTimeReloaded = System.currentTimeMillis();
        }
    }

    /**
     * Challange a QueueMemberAddedEvent.
     * 
     * @param event - the generated QueueMemberAddedEvent.
     */
    public void handleQueueMemberAddedEvent(QueueMemberAddedEvent event)
    {
        final AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());
        if (queue == null)
        {
            logger.error("Ignored QueueMemberAddedEvent for unknown queue " + event.getQueue());
            return;
        }

        AsteriskQueueMemberImpl member = queue.getMember(event.getLocation());
        if (member == null)
        {
            member = new AsteriskQueueMemberImpl(server, queue, event.getLocation(), QueueMemberState.valueOf(event
                    .getStatus()), event.getPaused(), event.getPenalty(), event.getMembership(), event.getCallsTaken(),
                    event.getLastCall());
        }

        queue.addMember(member);
    }

    /**
     * Challange a QueueMemberRemovedEvent.
     * 
     * @param event - the generated QueueMemberRemovedEvent.
     */
    public void handleQueueMemberRemovedEvent(QueueMemberRemovedEvent event)
    {
        final AsteriskQueueImpl queue = getInternalQueueByName(event.getQueue());
        if (queue == null)
        {
            logger.error("Ignored QueueMemberRemovedEvent for unknown queue " + event.getQueue());
            return;
        }

        final AsteriskQueueMemberImpl member = queue.getMember(event.getLocation());
        if (member == null)
        {
            logger.error("Ignored QueueMemberRemovedEvent for unknown agent name: " + event.getMemberName() + " location: "
                    + event.getLocation() + " queue: " + event.getQueue());
            return;
        }

        queue.removeMember(member);
    }

    public void forceQueuesMonitor(boolean force)
    {
        queuesMonitorForced = force;
    }

    public boolean isQueuesMonitorForced()
    {
        return queuesMonitorForced;
    }
}
