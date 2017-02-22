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

import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskQueueMember;
import org.asteriskjava.live.InvalidPenaltyException;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchInterfaceException;
import org.asteriskjava.live.QueueMemberState;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.asteriskjava.manager.action.QueuePenaltyAction;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.AstUtil;

/**
 * Default implementation of a queue member.
 *
 * @author Patrick Breucking
 * @version $Id$
 * @see AsteriskQueueMember
 * @since 0.3.1
 */
class AsteriskQueueMemberImpl extends AbstractLiveObject implements AsteriskQueueMember
{
    private AsteriskQueue queue;
    private QueueMemberState state;
    private String location;
    private Integer penalty;
    private boolean paused;
    private Integer callsTaken;
    private Long lastCall;
    private String membership;

    /**
     * Creates a new queue member.
     *
     * @param server server this channel belongs to.
     * @param queue queue this member is registered to.
     * @param location location of member.
     * @param state state of this member.
     * @param paused <code>true</code> if this member is currently paused,
     *            <code>false</code> otherwise.
     * @param penalty penalty of this member.
     * @param membership "dynamic" if the added member is a dynamic queue
     *            member, "static" if the added member is a static queue member.
     */
    AsteriskQueueMemberImpl(final AsteriskServerImpl server, final AsteriskQueueImpl queue, String location,
            QueueMemberState state, boolean paused, Integer penalty, String membership, Integer callsTaken, Long lastCall)
    {
        super(server);
        this.queue = queue;
        this.location = location;
        this.state = state;
        this.penalty = penalty;
        this.paused = paused;
        this.callsTaken = callsTaken;
        this.lastCall = lastCall;
        this.membership = membership;
    }

    public AsteriskQueue getQueue()
    {
        return queue;
    }

    public String getLocation()
    {
        return location;
    }

    public QueueMemberState getState()
    {
        return state;
    }

    @Override
    public Integer getCallsTaken()
    {
        return callsTaken;
    }

    public void setCallsTaken(Integer callsTaken)
    {
        this.callsTaken = callsTaken;
    }

    /**
     * Returns the time the last successful call answered by the added member
     * was hungup.
     *
     * @return the time (in seconds since 01/01/1970) the last successful call
     *         answered by the added member was hungup.
     */
    @Override
    public Long getLastCall()
    {
        return lastCall;
    }

    public void setLastCall(Long lastCall)
    {
        this.lastCall = lastCall;
    }

    @Deprecated
    public boolean getPaused()
    {
        return isPaused();
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused) throws ManagerCommunicationException, NoSuchInterfaceException
    {
        sendPauseAction(new QueuePauseAction(location, queue.getName(), paused));
    }

    public void setPausedAll(boolean paused) throws ManagerCommunicationException, NoSuchInterfaceException
    {
        sendPauseAction(new QueuePauseAction(location, paused));
    }

    private void sendPauseAction(QueuePauseAction action) throws ManagerCommunicationException, NoSuchInterfaceException
    {
        final ManagerResponse response = server.sendAction(action);

        if (response instanceof ManagerError)
        {
            // Message: Interface not found
            if (action.getQueue() != null)
            {
                // Message: Interface not found
                throw new NoSuchInterfaceException("Unable to change paused state for '" + action.getInterface() + "' on '"
                        + action.getQueue() + "': " + response.getMessage());
            }
            throw new NoSuchInterfaceException("Unable to change paused state for '" + action.getInterface()
                    + "' on all queues: " + response.getMessage());
        }
    }

    public String getMembership()
    {
        return membership;
    }

    public boolean isStatic()
    {
        return membership != null && "static".equals(membership);
    }

    public boolean isDynamic()
    {
        return membership != null && "dynamic".equals(membership);
    }

    public Integer getPenalty()
    {
        return penalty;
    }

    public void setPenalty(int penalty)
            throws IllegalArgumentException, ManagerCommunicationException, InvalidPenaltyException
    {
        if (penalty < 0)
        {
            throw new IllegalArgumentException("Penalty must not be negative");
        }

        final ManagerResponse response = server.sendAction(new QueuePenaltyAction(location, penalty, queue.getName()));
        if (response instanceof ManagerError)
        {
            throw new InvalidPenaltyException(
                    "Unable to set penalty for '" + location + "' on '" + queue.getName() + "': " + response.getMessage());
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder sb;

        sb = new StringBuilder("AsteriskQueueMember[");
        sb.append("location='").append(location).append("'");
        sb.append("state='").append(state).append("'");
        sb.append("paused='").append(paused).append("'");
        sb.append("membership='").append(membership).append("'");
        sb.append("queue='").append(queue.getName()).append("'");
        sb.append("callsTaken='").append(getCallsTaken()).append("'");
        sb.append("lastCall='").append(getLastCall()).append("'");
        sb.append("systemHashcode=").append(System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }

    synchronized boolean stateChanged(QueueMemberState state)
    {
        if (!AstUtil.isEqual(this.state, state))
        {
            QueueMemberState oldState = this.state;
            this.state = state;
            firePropertyChange(PROPERTY_STATE, oldState, state);
            return true;
        }
        return false;
    }

    synchronized boolean penaltyChanged(Integer penalty)
    {
        if (!AstUtil.isEqual(this.penalty, penalty))
        {
            Integer oldPenalty = this.penalty;
            this.penalty = penalty;
            firePropertyChange(PROPERTY_PENALTY, oldPenalty, penalty);
            return true;
        }

        return false;
    }

    synchronized boolean pausedChanged(boolean paused)
    {
        if (!AstUtil.isEqual(this.paused, paused))
        {
            boolean oldPaused = this.paused;
            this.paused = paused;
            firePropertyChange(PROPERTY_PAUSED, oldPaused, paused);
            return true;
        }
        return false;
    }

    synchronized boolean callsTakenChanged(Integer callsTaken)
    {
        if (!AstUtil.isEqual(this.callsTaken, callsTaken))
        {
            Integer oldcallsTaken = this.callsTaken;
            this.callsTaken = callsTaken;
            firePropertyChange(PROPERTY_CALLSTAKEN, oldcallsTaken, callsTaken);
            return true;
        }
        return false;
    }

    synchronized boolean lastCallChanged(Long lastCall)
    {
        if (!AstUtil.isEqual(this.lastCall, lastCall))
        {
            Long oldlastCall = this.lastCall;
            this.lastCall = lastCall;
            firePropertyChange(PROPERTY_LASTCALL, oldlastCall, lastCall);
            return true;
        }
        return false;
    }
}
