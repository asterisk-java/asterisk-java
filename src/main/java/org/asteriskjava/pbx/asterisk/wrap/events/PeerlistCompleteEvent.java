package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;

public class PeerlistCompleteEvent extends ResponseEvent
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PeerlistCompleteEvent.class);

	public PeerlistCompleteEvent(final org.asteriskjava.manager.event.PeerlistCompleteEvent event)
	{
		super(event);
	}
}
