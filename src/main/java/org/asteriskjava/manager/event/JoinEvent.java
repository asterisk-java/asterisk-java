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
package org.asteriskjava.manager.event;

/**
 * A JoinEvent is triggered when a channel joines a queue.<p>
 * It is implemented in <code>apps/app_queue.c</code>
 * 
 * @author srt
 * @version $Id$
 */
public class JoinEvent extends QueueEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 4961288508235470985L;

    protected String callerId;
    protected String callerIdName;
    protected Integer position;

    /**
     * @param source
     */
    public JoinEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the Caller*ID number of the channel that joined the queue if set.
     * If the channel has no caller id set "unknown" is returned.
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the Caller*ID number of the channel that joined the queue.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the Caller*ID name of the channel that joined the queue if set.
     * If the channel has no caller id set "unknown" is returned.
     * @since 0.2
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID name of the channel that joined the queue.
     * @since 0.2
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }

    /**
     * Returns the position of the joined channel in the queue.
     */
    public Integer getPosition()
    {
        return position;
    }

    /**
     * Sets the position of the joined channel in the queue.
     */
    public void setPosition(Integer position)
    {
        this.position = position;
    }
}
