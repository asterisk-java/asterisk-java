package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class DialEvent extends ChannelEventHelper
{
    private static final long serialVersionUID = 1L;

    /**
     * The name of the destination channel.
     */
    private final Channel destination;

    private final String dialString;
    private final String dialStatus;

    public DialEvent(final org.asteriskjava.manager.event.DialEvent event) throws InvalidChannelName
    {
        super(event.getChannel(), event.getUniqueId(), event.getCallerIdNum(), event.getCallerIdName());

        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        if (event.getDestination() != null)
            this.destination = pbx.internalRegisterChannel(event.getDestination(), event.getDestUniqueId());
        else
            this.destination = null;

        this.dialString = event.getDialString();
        this.dialStatus = event.getDialStatus();

    }

    public Channel getDestination()
    {
        return this.destination;
    }

    public String getDialString()
    {
        return this.dialString;
    }

    public String getDialStatus()
    {
        return this.dialStatus;
    }

}
