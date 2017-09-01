package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelImpl;

public class DndStateEvent extends ManagerEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    /**
     * The name of the channel.
     */
    private final Channel channel;

    /**
     * The DND state of the channel.
     */
    private final Boolean state;

    public DndStateEvent(final org.asteriskjava.manager.event.DndStateEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        this.channel = pbx.internalRegisterChannel(event.getChannel(), ChannelImpl.UNKNOWN_UNIQUE_ID);
        this.state = event.getState();
    }

    @Override
    public Channel getChannel()
    {
        return this.channel;
    }

    public Boolean getState()
    {
        return this.state;
    }
}
