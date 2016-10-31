package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;

public class StatusCompleteEvent extends ResponseEvent
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(StatusCompleteEvent.class);

	public StatusCompleteEvent(final org.asteriskjava.manager.event.StatusCompleteEvent event)
	{
		super(event);
	}
}
