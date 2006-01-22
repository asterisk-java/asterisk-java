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
 * An AgentConnectEvent is triggered when a caller is connected to an agent.<br>
 * It is implemented in <code>apps/app_queue.c</code>.<br>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id: AgentConnectEvent.java,v 1.2 2005/08/28 09:45:19 srt Exp $
 * @since 0.2
 */
public class AgentConnectEvent extends AbstractAgentEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 2108033737226142194L;

    private Long holdTime;

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
}
