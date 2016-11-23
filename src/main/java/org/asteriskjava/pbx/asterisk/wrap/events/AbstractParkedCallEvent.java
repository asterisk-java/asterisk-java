package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.InvalidChannelName;

public class AbstractParkedCallEvent extends ChannelEventHelper
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AbstractParkedCallEvent.class);

	private final String exten;
	private final String parkingLot;

	AbstractParkedCallEvent(final org.asteriskjava.manager.event.AbstractParkedCallEvent event)
			throws InvalidChannelName
	{
		super(event.getParkeeChannel(), event.getUniqueId(), event.getCallerIdNum(), event.getCallerIdName());
		this.exten = event.getExten();
		this.parkingLot = event.getParkingLot();
	}

	public String getExten()
	{
		return this.exten;
	}

	public String getParkingLot()
	{
		return this.parkingLot;
	}
}
