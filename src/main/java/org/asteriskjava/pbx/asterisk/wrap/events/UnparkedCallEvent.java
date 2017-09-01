package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelImpl;

public class UnparkedCallEvent extends AbstractParkedCallEvent
{
    private static final long serialVersionUID = 1L;

    private final Channel fromChannel;

    public UnparkedCallEvent(final org.asteriskjava.manager.event.UnparkedCallEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.fromChannel = pbx.internalRegisterChannel(event.getParkerDialString(), ChannelImpl.UNKNOWN_UNIQUE_ID);
    }

    public Channel getFromChannel()
    {
        return this.fromChannel;
    }

}
