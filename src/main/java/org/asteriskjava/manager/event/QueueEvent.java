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
package org.asteriskjava.manager.event;

/**
 * Abstract base class providing common properties for JoinEvent and LeaveEvent.
 * 
 * @author srt
 * @version $Id: QueueEvent.java,v 1.2 2005/02/23 22:50:58 srt Exp $
 */
public abstract class QueueEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = -8554382298783676181L;

    private String channel;
    private String queue;
    private Integer count;

    /**
     * @param source
     */
    public QueueEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel that joines or leaves a queue.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel that joines or leaves a queue.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the number of elements in the queue, i.e. the number of calls waiting to be answered
     * by an agent.
     */
    public Integer getCount()
    {
        return count;
    }

    /**
     * Sets the number of elements in the queue.
     */
    public void setCount(Integer count)
    {
        this.count = count;
    }

    /**
     * Returns the name of the queue.
     */
    public String getQueue()
    {
        return queue;
    }

    /**
     * Sets the name of the queue.
     */
    public void setQueue(String queue)
    {
        this.queue = queue;
    }
}
