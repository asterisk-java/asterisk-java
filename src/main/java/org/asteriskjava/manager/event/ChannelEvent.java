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
 * Abstract base class providing common properties for HangupEvent, NewChannelEvent and
 * NewStateEvent.
 * 
 * @author srt
 * @version $Id$
 */
public abstract class ChannelEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 5906599407896179295L;

    /**
     * The name of the channel.
     */
    private String channel;

    /**
     * The state of the channel.
     */
    private String state;

    /**
     * This Caller*ID of the channel.
     */
    private String callerId;

    /**
     * The Caller*ID Name of the channel.
     */
    private String callerIdName;

    /**
     * The unique id of the channel.
     */
    private String uniqueId;

    /**
     * @param source
     */
    public ChannelEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel.
     * 
     * @return the name of the channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     * 
     * @param channel the name of the channel.
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
     * Returns the Caller*ID of the channel if set or "&lg;unknown&gt;" if none has been set.
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the Caller*ID of the channel.
     * 
     * @param callerId the Caller*ID of the channel.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the Caller*ID Name of the channel if set or "&lg;unknown&gt;" if none has been set.
     * 
     * @return the Caller*ID Name of the channel.
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID Name of the channel.
     * 
     * @param callerIdName the Caller*ID Name of the channel.
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }

    /**
     * Returns the (new) state of the channel.<p>
     * The following states are used:<p>
     * <ul>
     * <li>Down</li>
     * <li>OffHook</li>
     * <li>Dialing</li>
     * <li>Ring</li>
     * <li>Ringing</li>
     * <li>Up</li>
     * <li>Busy</li>
     * <ul>
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the (new) state of the channel.
     */
    public void setState(String state)
    {
        this.state = state;
    }
}
