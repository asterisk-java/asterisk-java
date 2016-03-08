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
 * An AgentCompleteEvent is triggered when at the end of a call if the caller
 * was connected to an agent.
 * <p>
 * It is implemented in <code>apps/app_queue.c</code>.
 * <p>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class AgentCompleteEvent extends AbstractAgentEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 2108033737226142194L;

    private Long holdTime;
    private Long talkTime;
    private String reason;

    private String destExten;
    private String destChannelStateDesc;
    private String destUniqueId;
    private String destConnectedLineNum;
    private String destConnectedLineName;
    private String destCallerIdName;
    private String destCallerIdNum;
    private String destContext;
    private String destPriority;
    private String destChannel;
    private String destChannelState;
    private String iface;
    
    private String destAccountCode;
    private String language;
    private String destLanguage;
    private String linkedId;
    private String destLinkedId;
    private String accountCode;

    public AgentCompleteEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the amount of time the caller was on hold.
     * 
     * @return the amount of time the caller was on hold in seconds.
     */
    public Long getHoldTime()
    {
        return holdTime;
    }

    /**
     * Sets the amount of time the caller was on hold.
     * 
     * @param holdtime the amount of time the caller was on hold in seconds.
     */
    public void setHoldTime(Long holdtime)
    {
        this.holdTime = holdtime;
    }

    /**
     * Returns the amount of time the caller talked to the agent.
     * 
     * @return the amount of time the caller talked to the agent in seconds.
     */
    public Long getTalkTime()
    {
        return talkTime;
    }

    /**
     * Sets the amount of time the caller talked to the agent.
     * 
     * @param talkTime the amount of time the caller talked to the agent in
     *            seconds.
     */
    public void setTalkTime(Long talkTime)
    {
        this.talkTime = talkTime;
    }

    /**
     * Returns if the agent or the caller terminated the call.
     * 
     * @return "agent" if the agent terminated the call, "caller" if the caller
     *         terminated the call.
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * Sets if the agent or the caller terminated the call.
     * 
     * @param reason "agent" if the agent terminated the call, "caller" if the
     *            caller terminated the call.
     */
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    /**
     * @return the destExten
     */
    public String getDestExten()
    {
        return destExten;
    }

    /**
     * @param destExten the destExten to set
     */
    public void setDestExten(String destExten)
    {
        this.destExten = destExten;
    }

    /**
     * @return the destChannelStateDesc
     */
    public String getDestChannelStateDesc()
    {
        return destChannelStateDesc;
    }

    /**
     * @param destChannelStateDesc the destChannelStateDesc to set
     */
    public void setDestChannelStateDesc(String destChannelStateDesc)
    {
        this.destChannelStateDesc = destChannelStateDesc;
    }

    /**
     * @return the destUniqueId
     */
    public String getDestUniqueId()
    {
        return destUniqueId;
    }

    /**
     * @param destUniqueId the destUniqueId to set
     */
    public void setDestUniqueId(String destUniqueId)
    {
        this.destUniqueId = destUniqueId;
    }

    /**
     * @return the destConnectedLineNum
     */
    public String getDestConnectedLineNum()
    {
        return destConnectedLineNum;
    }

    /**
     * @param destConnectedLineNum the destConnectedLineNum to set
     */
    public void setDestConnectedLineNum(String destConnectedLineNum)
    {
        this.destConnectedLineNum = destConnectedLineNum;
    }

    /**
     * @return the destConnectedLineName
     */
    public String getDestConnectedLineName()
    {
        return destConnectedLineName;
    }

    /**
     * @param destConnectedLineName the destConnectedLineName to set
     */
    public void setDestConnectedLineName(String destConnectedLineName)
    {
        this.destConnectedLineName = destConnectedLineName;
    }

    /**
     * @return the destCallerIdName
     */
    public String getDestCallerIdName()
    {
        return destCallerIdName;
    }

    /**
     * @param destCallerIdName the destCallerIdName to set
     */
    public void setDestCallerIdName(String destCallerIdName)
    {
        this.destCallerIdName = destCallerIdName;
    }

    /**
     * @return the destCallerIdNum
     */
    public String getDestCallerIdNum()
    {
        return destCallerIdNum;
    }

    /**
     * @param destCallerIdNum the destCallerIdNum to set
     */
    public void setDestCallerIdNum(String destCallerIdNum)
    {
        this.destCallerIdNum = destCallerIdNum;
    }

    /**
     * @return the destContext
     */
    public String getDestContext()
    {
        return destContext;
    }

    /**
     * @param destContext the destContext to set
     */
    public void setDestContext(String destContext)
    {
        this.destContext = destContext;
    }

    /**
     * @return the destPriority
     */
    public String getDestPriority()
    {
        return destPriority;
    }

    /**
     * @param destPriority the destPriority to set
     */
    public void setDestPriority(String destPriority)
    {
        this.destPriority = destPriority;
    }

    /**
     * @return the destChannel
     */
    public String getDestChannel()
    {
        return destChannel;
    }

    /**
     * @param destChannel the destChannel to set
     */
    public void setDestChannel(String destChannel)
    {
        this.destChannel = destChannel;
    }

    /**
     * @return the destChannelState
     */
    public String getDestChannelState()
    {
        return destChannelState;
    }

    /**
     * @param destChannelState the destChannelState to set
     */
    public void setDestChannelState(String destChannelState)
    {
        this.destChannelState = destChannelState;
    }

    /**
     * @return the iface
     */
    public String getInterface()
    {
        return iface;
    }

    /**
     * @param iface the iface to set
     */
    public void setInterface(String iface)
    {
        this.iface = iface;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getDestAccountCode()
    {
        return destAccountCode;
    }

    public void setDestAccountCode(String destAccountCode)
    {
        this.destAccountCode = destAccountCode;
    }

    public String getDestLanguage()
    {
        return destLanguage;
    }

    public void setDestLanguage(String destLanguage)
    {
        this.destLanguage = destLanguage;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

    public String getDestLinkedId()
    {
        return destLinkedId;
    }

    public void setDestLinkedId(String destLinkedId)
    {
        this.destLinkedId = destLinkedId;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }
}
