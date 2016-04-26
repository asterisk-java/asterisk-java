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
 * An AgentConnectEvent is triggered when a caller is connected to an agent.
 * <p>
 * It is implemented in <code>apps/app_queue.c</code>.
 * <p>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class AgentConnectEvent extends AbstractAgentEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 0L;

    private Long holdTime;
    private String bridgedChannel;
    private Long ringtime;

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
    private String linkedId;
    private String destLinkedId;
    private String destLanguage;
    private String language;
    
    private String accountcode;

    public AgentConnectEvent(Object source)
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
     * Returns the unique ID of the queue member channel that is taking the
     * call. This is useful when trying to link recording filenames back to a
     * particular call from the queue.
     * <p>
     * Available since Asterisk 1.4.
     * 
     * @return the unique ID of the queue member channel that is taking the
     *         call.
     */
    public String getBridgedChannel()
    {
        return bridgedChannel;
    }

    /**
     * Sets the unique ID of the queue member channel that is taking the call.
     * 
     * @param bridgedChannel the unique ID of the queue member channel that is
     *            taking the call.
     */
    public void setBridgedChannel(String bridgedChannel)
    {
        this.bridgedChannel = bridgedChannel;
    }

    /**
     * Returns the amount of time the agent's channel was ringing before
     * answered.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return the amount of time the agent's channel was ringing before
     *         answered in seconds.
     * @since 1.0.0
     */
    public Long getRingtime()
    {
        return ringtime;
    }

    public void setRingtime(Long ringtime)
    {
        this.ringtime = ringtime;
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
    
    public String getDestAccountCode()
    {
        return destAccountCode;
    }

    public void setDestAccountCode(String destAccountCode)
    {
        this.destAccountCode = destAccountCode;
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

    public String getDestLanguage()
    {
        return destLanguage;
    }

    public void setDestLanguage(String destLanguage)
    {
        this.destLanguage = destLanguage;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

	public String getAccountcode()
	{
		return accountcode;
	}

	public void setAccountcode(String accountcode)
	{
		this.accountcode = accountcode;
	}
}
