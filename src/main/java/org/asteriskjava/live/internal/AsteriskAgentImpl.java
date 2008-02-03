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
package org.asteriskjava.live.internal;

import org.asteriskjava.live.AgentState;
import org.asteriskjava.live.AsteriskAgent;

/**
 * Default implementation of the AsteriskAgent interface.
 *
 * @author <a href="mailto:patrick.breucking{@nospam}gonicus.de">Patrick Breucking</a>
 * @version $Id$
 * @since 0.1
 */
public class AsteriskAgentImpl extends AbstractLiveObject implements AsteriskAgent
{
    public String name;
    public String agentId;
    public AgentState status;

    AsteriskAgentImpl(AsteriskServerImpl server, String name, String agentId, AgentState status)
    {
        super(server);
        if (server == null || name == null || agentId == null)
        {
            throw new IllegalArgumentException("Parameters passed to AsteriskAgentImpl() must not be null.");
        }
        this.name = name;
        this.agentId = agentId;
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public String getAgentId()
    {
        return agentId;
    }

    public AgentState getStatus()
    {
        return status;
    }

    synchronized void updateStatus(AgentState agentState)
    {
        final AgentState oldStatus = status;
        this.status = agentState;
        firePropertyChange(PROPERTY_STATUS, oldStatus, status);
    }

    @Override
    public String toString()
    {
        final StringBuffer sb;

        sb = new StringBuffer("AsteriskAgent[");
        sb.append("agentId='").append(getAgentId()).append("',");
        sb.append("name='").append(getName()).append("',");
        sb.append("state=").append(getStatus()).append(",");
        sb.append("systemHashcode=").append(System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }
}