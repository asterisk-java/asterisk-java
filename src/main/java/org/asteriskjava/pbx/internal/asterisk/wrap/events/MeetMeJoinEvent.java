package org.asteriskjava.pbx.internal.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.asterisk.InvalidChannelName;

public class MeetMeJoinEvent extends AbstractMeetMeEvent
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(MeetMeJoinEvent.class);

	CallerID callerID;

	public MeetMeJoinEvent(final org.asteriskjava.manager.event.MeetMeJoinEvent event) throws InvalidChannelName
	{
		super(event);
		final PBX pbx = PBXFactory.getActivePBX();

		pbx.buildCallerID(event.getCallerIdNum(), event.getCallerIdName());

	}

	public CallerID getCallerID()
	{
		return this.callerID;
	}

}
