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
 * Abstract base class for several call parking related events.
 * 
 * @author srt
 * @version $Id: AbstractParkedCallEvent.java,v 1.1 2005/08/27 16:16:45 srt Exp $
 * @since 0.2
 */
public abstract class AbstractParkedCallEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -7437833328723536814L;
    private String exten;
    private String channel;
    private String callerId;
    private String callerIdName;

    /**
     * @param source
     */
    public AbstractParkedCallEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the extension the channel is or was parked at.
     */
    public String getExten()
    {
        return exten;
    }

    /**
     * Sets the extension the channel is or was parked at.
     */
    public void setExten(String exten)
    {
        this.exten = exten;
    }

    /**
     * Returns the name of the channel that is or was parked.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel that is or was parked.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the Caller*ID number of the parked channel.
     * 
     * @return the Caller*ID number of the parked channel.
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the Caller*ID number of the parked channel.
     * 
     * @param callerId the Caller*ID number of the parked channel.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the Caller*ID name of the parked channel.
     * 
     * @return the Caller*ID name of the parked channel.
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID name of the parked channel.
     * 
     * @param callerIdName the Caller*ID name of the parked channel.
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }
}
