package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;

public class DisconnectEvent extends ManagerEvent
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DisconnectEvent.class);

	public DisconnectEvent(final org.asteriskjava.manager.event.DisconnectEvent event)
	{
		super(event);
	}
}
