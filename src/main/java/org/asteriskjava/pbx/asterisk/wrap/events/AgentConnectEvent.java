package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.InvalidChannelName;

public class AgentConnectEvent extends ChannelEventHelper
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AgentConnectEvent.class);

	private String queue;

	private String agentInterface;

	public AgentConnectEvent(final org.asteriskjava.manager.event.AgentConnectEvent event) throws InvalidChannelName
	{
		super(event.getChannel(), event.getUniqueId(), event.getCallerIdNum(), event.getCallerIdName());

		agentInterface = event.getInterface();
		queue = event.getQueue();
	}

	public String getQueueName()
	{
		return queue;
	}

	public String getAgentInterface()
	{
		return agentInterface;
	}

}
