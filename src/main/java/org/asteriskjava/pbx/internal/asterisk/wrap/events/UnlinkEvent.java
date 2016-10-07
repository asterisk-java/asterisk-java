package org.asteriskjava.pbx.internal.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.internal.asterisk.InvalidChannelName;

public class UnlinkEvent extends BridgeEvent
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UnlinkEvent.class);

	@SuppressWarnings("deprecation")
	public UnlinkEvent(final org.asteriskjava.manager.event.UnlinkEvent event) throws InvalidChannelName
	{
		super(event);
	}
}
