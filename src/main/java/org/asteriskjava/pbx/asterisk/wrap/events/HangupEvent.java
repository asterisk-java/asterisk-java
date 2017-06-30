package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class HangupEvent extends ManagerEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    /**
     * The name of the channel.
     */
    private final Channel channel;

    private final Integer cause;
    private final String causeTxt;
    private final String uniqueId;

    public HangupEvent(final org.asteriskjava.manager.event.HangupEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.channel = pbx.registerHangupChannel(event.getChannel(), event.getUniqueId());
        this.cause = event.getCause();
        this.causeTxt = event.getCauseTxt();
        this.uniqueId = event.getUniqueId();

    }

    public Integer getCause()
    {
        return this.cause;
    }

    public String getCauseTxt()
    {
        return this.causeTxt;
    }

    @Override
    public Channel getChannel()
    {
        return this.channel;
    }

    public String getUniqueId()
    {
        return this.uniqueId;
    }

    public String toString()
    {
        return "HangupEvent: channel:" + this.channel + " cause:" + this.causeTxt; //$NON-NLS-1$//$NON-NLS-2$
    }
}
