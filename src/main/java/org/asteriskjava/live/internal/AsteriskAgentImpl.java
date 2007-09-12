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
 * @since 0.1
 * @author <a href="mailto:patrick.breucking{@nospam}gonicus.de">Patrick
 *         Breucking</a>
 * @version $Id$
 */
public class AsteriskAgentImpl extends AbstractLiveObject implements
	AsteriskAgent
{

    public String name;
    public String agentId;
    public AgentState status;

    /**
     * @param server
     * @param loggedinchan
     * @param status
     * @param agentId
     * @param name
     */
    AsteriskAgentImpl(AsteriskServerImpl server, String name, String agentId,
	    AgentState status)
    {
	super(server);
	if (server == null || name == null || agentId == null)
	{
	    throw new NullPointerException(
		    "Parameters passed to AsteriskAgentImpl() must not be null.");
	}
	this.name = name;
	this.agentId = agentId;
	this.status = status;
    }

    @Override
    public String toString()
    {
	final StringBuffer sb;

	sb = new StringBuffer("AsteriskAgent[");
	sb.append("agentId='").append(getAgentId()).append("',");
	sb.append("name='").append(getName()).append("',");
	sb.append("state='").append(getStatus()).append("',");
	sb.append("systemHashcode=").append(System.identityHashCode(this));
	sb.append("]");

	return sb.toString();
    }

    /**
     * @return the name
     */
    public String getName()
    {
	return name;
    }

    /**
     * @return the agentId
     */
    public String getAgentId()
    {
	return agentId;
    }

    /**
     * @return the status
     */
    public AgentState getStatus()
    {
	return status;
    }

    /**
     * @param agentState the status to set
     */
    synchronized void updateStatus(AgentState agentState)
    {
	final AgentState oldStatus = status;
	this.status = agentState;
	firePropertyChange(PROPERTY_STATUS, oldStatus, status);
    }
}
