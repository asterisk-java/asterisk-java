package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.InvalidChannelName;

public class LinkEvent extends BridgeEvent
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(LinkEvent.class);

	@SuppressWarnings("deprecation")
	public LinkEvent(final org.asteriskjava.manager.event.LinkEvent event) throws InvalidChannelName
	{
		super(event);
	}
}
