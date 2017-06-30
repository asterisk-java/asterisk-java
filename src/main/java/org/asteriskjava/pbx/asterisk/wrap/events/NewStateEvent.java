package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.internal.core.ChannelProxy;

public class NewStateEvent extends AbstractChannelStateEvent
{
    private static final long serialVersionUID = 1L;
    private ChannelProxy destChannel;

    public NewStateEvent(final org.asteriskjava.manager.event.NewStateEvent event) throws InvalidChannelName
    {
        super(event);
    }

    public Channel getDestination()
    {
        return destChannel;
    }

    public String toString()
    {
        return "NewStateEvent: channel:" + this.getChannel() + " channelState:" + this.getChannelState(); //$NON-NLS-1$//$NON-NLS-2$
    }
}
