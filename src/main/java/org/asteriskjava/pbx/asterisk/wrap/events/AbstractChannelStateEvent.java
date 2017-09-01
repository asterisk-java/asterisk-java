package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public abstract class AbstractChannelStateEvent extends ChannelEventHelper
{

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(AbstractChannelStateEvent.class);

    private final ChannelState channelState;
    private final String channelStateDesc;

    AbstractChannelStateEvent(final org.asteriskjava.manager.event.AbstractChannelStateEvent event) throws InvalidChannelName
    {
        super(event);
        this.channelState = ChannelState.valueOfDesc(event.getChannelStateDesc());
        this.channelStateDesc = event.getChannelStateDesc();
    }

    public ChannelState getChannelState()
    {
        return this.channelState;
    }

    public String getChannelStateDesc()
    {
        return this.channelStateDesc;
    }

}
