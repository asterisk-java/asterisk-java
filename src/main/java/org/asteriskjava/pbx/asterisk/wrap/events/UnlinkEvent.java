package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.InvalidChannelName;

public class UnlinkEvent extends BridgeEvent
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("deprecation")
    public UnlinkEvent(final org.asteriskjava.manager.event.UnlinkEvent event) throws InvalidChannelName
    {
        super(event);
    }
}
