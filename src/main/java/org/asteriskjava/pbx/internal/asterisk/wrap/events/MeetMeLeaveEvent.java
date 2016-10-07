package org.asteriskjava.pbx.internal.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.asterisk.InvalidChannelName;

public class MeetMeLeaveEvent extends AbstractMeetMeEvent
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(MeetMeLeaveEvent.class);

	private final CallerID callerID;
	private final Long duration;

	public MeetMeLeaveEvent(final org.asteriskjava.manager.event.MeetMeLeaveEvent event) throws InvalidChannelName
	{
		super(event);

		final PBX pbx = PBXFactory.getActivePBX();

		this.callerID = pbx.buildCallerID(event.getCallerIdNum(), event.getCallerIdName());
		this.duration = event.getDuration();
	}

	public CallerID getCallerID()
	{
		return this.callerID;
	}

	public Long getDuration()
	{
		return this.duration;
	}

}
