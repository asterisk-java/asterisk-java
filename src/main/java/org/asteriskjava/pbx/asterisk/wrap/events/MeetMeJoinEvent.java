package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;

public class MeetMeJoinEvent extends AbstractMeetMeEvent
{
    private static final long serialVersionUID = 1L;

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
