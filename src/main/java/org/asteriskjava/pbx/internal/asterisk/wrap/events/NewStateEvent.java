package org.asteriskjava.pbx.internal.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.internal.asterisk.InvalidChannelName;
import org.asteriskjava.pbx.internal.core.ChannelProxy;

public class NewStateEvent extends AbstractChannelStateEvent
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(NewStateEvent.class);
	private ChannelProxy destChannel;

	public NewStateEvent(final org.asteriskjava.manager.event.NewStateEvent event) throws InvalidChannelName
	{
		super(event);
	}

	public ChannelProxy getDestination()
	{
		return destChannel;
	}

	public String toString()
	{
		return "NewStateEvent: channel:" + this.getChannel() + " channelState:" + this.getChannelState(); //$NON-NLS-1$//$NON-NLS-2$
	}
}
