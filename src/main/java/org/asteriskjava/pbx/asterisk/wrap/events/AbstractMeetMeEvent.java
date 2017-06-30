package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class AbstractMeetMeEvent extends ChannelEventHelper
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(AbstractMeetMeEvent.class);

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
