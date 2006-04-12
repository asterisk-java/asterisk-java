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
 * A StatusEvent is triggered for each active channel in response to a StatusAction.
 * 
 * @see org.asteriskjava.manager.action.StatusAction
 * 
 * @author srt
 * @version $Id: StatusEvent.java,v 1.3 2005/03/16 09:49:49 srt Exp $
 */
public class StatusEvent extends ResponseEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = -3619197512835308812L;
    private String channel;
    private String callerIdNum;
    private String callerIdName;
    private String account;
    private String state;
    private String context;
    private String extension;
    private Integer priority;
    private Integer seconds;
    private String link;
    private String uniqueId;

    /**
     * @param source
     */
    public StatusEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of this channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of this channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the Caller*ID Number of this channel.<br>
     * This property is deprecated as of Asterisk 1.4, use {@link #getCallerIdNum()} instead.
     * @return the Caller*ID Number of this channel or "&lt;unknown&gt;" if none is available.
     * @deprecated
     */
    public String getCallerId()
    {
        return callerIdNum;
    }

    /**
     * Sets the Caller*ID Number of this channel.<br>
     * This property is deprecated as of Asterisk 1.4.
     * @param callerId the Caller*ID Number to set.
     */
    public void setCallerId(String callerIdNum)
    {
        this.callerIdNum = callerIdNum;
    }

    /**
     * Returns the Caller*ID Number of this channel.
     * @return the Caller*ID Number of this channel or "&lt;unknown&gt;" if none is available.
     * @since 0.3
     */
    public String getCallerIdNum()
    {
        return callerIdNum;
    }

    /**
     * Sets the Caller*ID Number of this channel.
     * @param callerIdNum the Caller*ID Number to set.
     * @since 0.3
     */
    public void setCallerIdNum(String callerIdNum)
    {
        this.callerIdNum = callerIdNum;
    }

    /**
     * Returns the Caller*ID Name of this channel.
     * @return the Caller*ID Name of this channel or "&lt;unknown&gt;" if none is available.
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID Name of this channel.
     * @param callerIdName the Caller*ID Name of this channel.
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }

    /**
     * Returns the account code of this channel.
     */
    public String getAccount()
    {
        return account;
    }

    /**
     * Sets the account code of this channel.
     */
    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public Integer getPriority()
    {
        return priority;
    }

    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    /**
     * Returns the number of elapsed seconds.
     */
    public Integer getSeconds()
    {
        return seconds;
    }

    /**
     * Sets the number of elapsed seconds.
     */
    public void setSeconds(Integer seconds)
    {
        this.seconds = seconds;
    }

    /**
     * Returns the name of the linked channel if this channel is bridged.
     */
    public String getLink()
    {
        return link;
    }

    /**
     * Sets the name of the linked channel.
     */
    public void setLink(String link)
    {
        this.link = link;
    }

    /**
     * Returns the unique id of this channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of this channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }
}
