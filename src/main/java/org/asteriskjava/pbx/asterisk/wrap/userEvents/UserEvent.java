package org.asteriskjava.pbx.asterisk.wrap.userEvents;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class UserEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    private Channel channel;

    public UserEvent(org.asteriskjava.manager.event.UserEvent source, Channel channel)
    {
        super(source);
        this.channel = channel;
    }

    public UserEvent(org.asteriskjava.manager.event.UserEvent event) throws InvalidChannelName
    {
        super(event);

        AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        this.channel = pbx.internalRegisterChannel(event.getChannel(), event.getUniqueId());
    }

    public Channel getChannel()
    {
        return this.channel;
    }
}
