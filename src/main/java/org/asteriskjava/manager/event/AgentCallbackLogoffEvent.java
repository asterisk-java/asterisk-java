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
 * An AgentCallbackLogoffEvent is triggered when an agent that previously logged in using
 * AgentCallbackLogin is logged of.<br>
 * It is implemented in <code>channels/chan_agent.c</code>
 * 
 * @see org.asteriskjava.manager.event.AgentCallbackLoginEvent
 * @author srt
 * @version $Id: AgentCallbackLogoffEvent.java,v 1.2 2005/02/23 22:50:58 srt Exp $
 */
public class AgentCallbackLogoffEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 8458799161502800576L;
    private String agent;
    private String loginChan;
    private String loginTime;
    private String reason;
    private String uniqueId;

    /**
     * @param source
     */
    public AgentCallbackLogoffEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the agent that logged off.
     */
    public String getAgent()
    {
        return agent;
    }

    /**
     * Sets the name of the agent that logged off.
     */
    public void setAgent(String agent)
    {
        this.agent = agent;
    }

    public String getLoginChan()
    {
        return loginChan;
    }

    public void setLoginChan(String loginChan)
    {
        this.loginChan = loginChan;
    }

    public String getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(String loginTime)
    {
        this.loginTime = loginTime;
    }

    /**
     * Returns the reason for the logoff. The reason is set to Autologoff if the agent has been
     * logged off due to not answering the phone in time. Autologoff is configured by setting
     * <code>autologoff</code> to the appropriate number of seconds in <code>agents.conf</code>.
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * Sets the reason for the logoff.
     */
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }
}
