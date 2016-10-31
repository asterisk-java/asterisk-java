package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class PeerStatusEvent extends ManagerEvent
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PeerStatusEvent.class);

	private final EndPoint peer;

	public PeerStatusEvent(final org.asteriskjava.manager.event.PeerStatusEvent event)
	{
		super(event);
		final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

		this.peer = pbx.buildEndPoint(event.getPeer());// registerChannel(event.getPeer(),
														// Channel.UNKNOWN_UNIQUE_ID);
	}

	public EndPoint getPeer()
	{
		return this.peer;
	}

}
