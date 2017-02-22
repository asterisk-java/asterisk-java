package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.InvalidChannelName;

public abstract class AbstractChannelStateEvent extends ChannelEventHelper
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AbstractChannelStateEvent.class);

	private final ChannelState channelState;
	private final String channelStateDesc;

	AbstractChannelStateEvent(final org.asteriskjava.manager.event.AbstractChannelStateEvent event)
			throws InvalidChannelName
	{
		super(event);
		this.channelState = ChannelState.valueOfDesc(event.getChannelStateDesc());
		this.channelStateDesc = event.getChannelStateDesc();
	}

	public ChannelState getChannelState()
	{
		return this.channelState;
	}

	public String getChannelStateDesc()
	{
		return this.channelStateDesc;
	}

}
