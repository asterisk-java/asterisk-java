package org.asteriskjava.pbx.internal.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.asterisk.InvalidChannelName;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelEventHelper;
import org.asteriskjava.pbx.internal.core.ChannelProxy;

public class DialEvent extends ChannelEventHelper
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(DialEvent.class);

    /**
     * The name of the destination channel.
     */
    private final ChannelProxy destination;

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

    public ChannelProxy getDestination()
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
