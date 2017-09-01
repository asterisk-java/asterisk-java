package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.InvalidChannelName;

public class LinkEvent extends BridgeEvent
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("deprecation")
    public LinkEvent(final org.asteriskjava.manager.event.LinkEvent event) throws InvalidChannelName
    {
        super(event);
    }
}
