package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;

public class MeetMeLeaveEvent extends AbstractMeetMeEvent
{
    private static final long serialVersionUID = 1L;

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
