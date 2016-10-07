package org.asteriskjava.pbx.internal.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.internal.asterisk.InvalidChannelName;
import org.asteriskjava.pbx.internal.core.ChannelEventHelper;

public class AbstractMeetMeEvent extends ChannelEventHelper
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AbstractMeetMeEvent.class);

	private final String meetMe;
	private final Integer userNum;

	public AbstractMeetMeEvent(final org.asteriskjava.manager.event.AbstractMeetMeEvent event) throws InvalidChannelName
	{
		super(event.getChannel(), event.getUniqueId());
		this.meetMe = event.getMeetMe();
		this.userNum = event.getUser();
	}

	public String getMeetMe()
	{
		return this.meetMe;
	}

	public Integer getUserNum()
	{
		return this.userNum;
	}

}
