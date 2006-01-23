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
package net.sf.asterisk.manager.event;

/**
 * An AgentCalledEvent is triggered when an agent is rung.<br>
 * To enable AgentCalledEvents you have to set
 * <code>eventwhencalled = yes</code> in <code>queues.conf</code>.<br>
 * This event is implemented in <code>apps/app_queue.c</code>
 * 
 * @author srt
 * @version $Id: AgentCalledEvent.java,v 1.3 2005/08/27 03:22:32 srt Exp $
 */
public class AgentCalledEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -8736410893935374606L;
    private String agentCalled;
    private String channelCalling;
    private String callerId;
    private String callerIdName;
    private String context;
    private String extension;
    private String priority;

    /**
     * @param source
     */
    public AgentCalledEvent(Object source)
    {
        super(source);
    }

    public String getAgentCalled()
    {
        return agentCalled;
    }

    public void setAgentCalled(String agentCalled)
    {
        this.agentCalled = agentCalled;
    }

    public String getChannelCalling()
    {
        return channelCalling;
    }

    public void setChannelCalling(String channelCalling)
    {
        this.channelCalling = channelCalling;
    }

    public String getCallerId()
    {
        return callerId;
    }

    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the Caller*ID name of the calling channel.
     * 
     * @return the Caller*ID name of the calling channel or "unknown" if no
     *         Caller*Id has been set.
     * @since 0.2
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID name of the calling channel.
     * 
     * @param callerIdName the Caller*ID name of the calling channel.
     * @since 0.2
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
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

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }
}
