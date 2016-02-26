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
 * An AgentRingNoAnswerEvent is triggered when a call is routed to an agent but
 * the agent does not answer the call.
 * <p>
 * It is implemented in <code>apps/app_queue.c</code>.
 * <p>
 * Available since Asterisk 1.6
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
public class AgentRingNoAnswerEvent extends AbstractAgentEvent
{
    private static final long serialVersionUID = 1L;

    private Long ringtime;

    String destExten;
    String destChannelStateDesc;
    String destUniqueId;
    String destConnectedLineNum;
    String destConnectedLineName;
    String destCallerIdName;
    String destCallerIdNum;
    String destContext;
    String destPriority;
    String destChannel;
    String destChannelState;
    String iface;

    public AgentRingNoAnswerEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the amount of time the agent's channel was ringing.
     *
     * @return the amount of time the agent's channel was ringing in seconds.
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
}
