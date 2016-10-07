package org.asteriskjava.pbx.internal.core;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.asterisk.InvalidChannelName;
import org.asteriskjava.pbx.internal.asterisk.wrap.events.ChannelEvent;
import org.asteriskjava.pbx.internal.asterisk.wrap.events.ManagerEvent;

public abstract class ChannelEventHelper extends ManagerEvent implements ChannelEvent
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ChannelEventHelper.class);

	/**
	 * The name of the channel.
	 */
	private final ChannelProxy channel;

	protected ChannelEventHelper(final org.asteriskjava.manager.event.AbstractChannelEvent event)
			throws InvalidChannelName
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

		this.channel = pbx.registerChannel(channel, uniqueId);
	}

	@Override
	public ChannelProxy getChannel()
	{
		return this.channel;
	}

	public static ChannelProxy registerChannel(final String channelName, final String uniqueId,
			final String callerIdNum, final String callerIdName) throws InvalidChannelName
	{
		final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

		final ChannelProxy channel = pbx.registerChannel(channelName, uniqueId);
		channel.getRealChannel().setCallerId(pbx.buildCallerID(callerIdNum, callerIdName));
		return channel;
	}
}
