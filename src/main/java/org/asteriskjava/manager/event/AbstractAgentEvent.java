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
 * Abstract base class for several agent related events.
 * 
 * @author srt
 * @author martins
 * @version $Id$
 * @since 0.2
 */
public abstract class AbstractAgentEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -7437833328723536814L;
    private String channel;
    private String uniqueId;
    private String queue;
    private String member;
    private String memberName;

    /**
     * @param source
     */
    protected AbstractAgentEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the unique id of the channel.
     * 
     * @return the unique id of the channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel.
     * 
     * @param uniqueId the unique id of the channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /**
     * Returns the name of the queue.
     * 
     * @return the name of the queue.
     */
    public String getQueue()
    {
        return queue;
    }

    /**
     * Sets the name of the queue.
     * 
     * @param queue the name of the queue.
     */
    public void setQueue(String queue)
    {
        this.queue = queue;
    }

    /**
     * Returns the name of the member's interface.
     * 
     * @return the name of the member's interface.
     */
    public String getMember()
    {
        return member;
    }

    /**
     * Sets the name of the member's interface.
     * 
     * @param member the name of the member's interface.
     */
    public void setMember(String member)
    {
        this.member = member;
    }

    /**
     * @return the member name supplied for logging when the member is added
     */
    public String getMemberName()
    {
        return memberName;
    }

    /**
     * @param memberName the member name supplied for logging when the member is added
     */
    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }
}
