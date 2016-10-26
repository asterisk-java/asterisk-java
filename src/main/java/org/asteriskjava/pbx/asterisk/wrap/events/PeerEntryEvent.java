package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.TechType;

public class PeerEntryEvent extends ResponseEvent
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PeerEntryEvent.class);

	private final EndPoint peer;

	public PeerEntryEvent(final org.asteriskjava.manager.event.PeerEntryEvent event)
	{
		super(event);

		PBX pbx = PBXFactory.getActivePBX();
		this.peer = pbx.buildEndPoint(TechType.valueOf(event.getChannelType()), event.getObjectName());
	}

	public EndPoint getPeer()
	{
		return this.peer;
	}
}
