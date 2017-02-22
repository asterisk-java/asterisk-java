package org.asteriskjava.pbx;

public interface NewChannelListener
{
	/**
	 * Allows a listener to be informed as new channels come up during an
	 * originate.
	 * 
	 * The OriginateBase class will call this for each channel that comes up (as
	 * a result of the originate).
	 * 
	 * Intermediate channels (local etc) that the underlying pbx may create on
	 * the way through are ignored with only the final channels being notified.
	 * 
	 * This listener should be used by anyone that needs to have the channel at
	 * the earliest possible moment.
	 * 
	 * Listeners should not run long lived process from this call as it will
	 * stall the dialer.
	 * 
	 * @param channel
	 */
	void channelUpdate(Channel channel);
}
