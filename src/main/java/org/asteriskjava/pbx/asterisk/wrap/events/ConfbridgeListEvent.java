package org.asteriskjava.pbx.asterisk.wrap.events;

public class ConfbridgeListEvent extends ResponseEvent  
{

    private static final long serialVersionUID = 1L;
    private String conference;
    private String channel;

    public ConfbridgeListEvent(org.asteriskjava.manager.event.ConfbridgeListEvent source)
    {
        super(source);
        conference = source.getConference();
        channel = source.getChannel();
    }
 
    public String getChannel()
    {
        return channel;
    }

    public String getConference()
    {
        return conference;
    }
}
