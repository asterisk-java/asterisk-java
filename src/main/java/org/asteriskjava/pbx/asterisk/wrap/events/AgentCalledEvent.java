package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class AgentCalledEvent extends ChannelEventHelper
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(AgentCalledEvent.class);

    private String queue;

    private String agentInterface;

    public AgentCalledEvent(final org.asteriskjava.manager.event.AgentCalledEvent event) throws InvalidChannelName
    {
        super(event.getChannelCalling(), event.getUniqueId(), event.getCallerIdNum(), event.getCallerIdName());

        agentInterface = event.getAgentCalled();
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
