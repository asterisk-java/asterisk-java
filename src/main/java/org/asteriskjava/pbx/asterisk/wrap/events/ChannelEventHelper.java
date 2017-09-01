package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public abstract class ChannelEventHelper extends ManagerEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    /**
     * The name of the channel.
     */
    private final Channel channel;

    protected ChannelEventHelper(final org.asteriskjava.manager.event.AbstractChannelEvent event) throws InvalidChannelName
    {
        this(event.getChannel(), event.getUniqueId(), event.getCallerIdNum(), event.getCallerIdName());
    }

    protected ChannelEventHelper(final String channel, final String uniqueId, final String callerIdNum,
            final String callerIdName) throws InvalidChannelName
    {
        super(channel); // we must have a source but since don't have one pass
                        // anything.
        this.channel = ChannelEventHelper.registerChannel(channel, uniqueId, callerIdNum, callerIdName);
    }

    public ChannelEventHelper(final String channel, final String uniqueId) throws InvalidChannelName
    {
        super(channel);// we must have a source but since don't have one pass
                       // anything.
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.channel = pbx.internalRegisterChannel(channel, uniqueId);
    }

    @Override
    public Channel getChannel()
    {
        return this.channel;
    }

    public static Channel registerChannel(final String channelName, final String uniqueId, final String callerIdNum,
            final String callerIdName) throws InvalidChannelName
    {
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        final Channel channel = pbx.internalRegisterChannel(channelName, uniqueId);
        channel.setCallerId(pbx.buildCallerID(callerIdNum, callerIdName));
        return channel;
    }
}
