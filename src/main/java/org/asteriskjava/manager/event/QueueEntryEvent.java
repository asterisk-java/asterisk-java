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
 * A QueueEntryEvent is triggered in response to a QueueStatusAction and
 * contains information about an entry in a queue.
 * <p>
 * It is implemented in <code>apps/app_queue.c</code>
 *
 * @author srt
 * @version $Id: QueueEntryEvent.java 1360 2009-09-04 01:08:57Z srt $
 * @see org.asteriskjava.manager.action.QueueStatusAction
 */
public class QueueEntryEvent extends ResponseEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;
    private String queue;
    private Integer position;
    private String uniqueId;
    private String channel;
    private String callerId;
    private Long wait;

    /**
     * @param source
     */
    public QueueEntryEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the queue that contains this entry.
     */
    public String getQueue()
    {
        return queue;
    }

    /**
     * Sets the name of the queue that contains this entry.
     */
    public void setQueue(String queue)
    {
        this.queue = queue;
    }

    /**
     * Returns the position of this entry in the queue.
     */
    public Integer getPosition()
    {
        return position;
    }

    /**
     * Sets the position of this entry in the queue.
     */
    public void setPosition(Integer position)
    {
        this.position = position;
    }

    /**
     * Returns the name of the channel of this entry.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Returns the unique id of the channel of this entry.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return the unique id of the channel of this entry.
     * @since 1.0.0
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /**
     * Sets the name of the channel of this entry.
     *
     * @param channel the name of the channel of this entry.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the the Caller*ID number of this entry.
     *
     * @return the the Caller*ID number of this entry.
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the the Caller*ID number of this entry.
     *
     * @param callerId the the Caller*ID number of this entry.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the number of seconds this entry has spent in the queue.
     */
    public Long getWait()
    {
        return wait;
    }

    /**
     * Sets the number of seconds this entry has spent in the queue.
     */
    public void setWait(Long wait)
    {
        this.wait = wait;
    }

}
