package org.asteriskjava.pbx;

/**
 * Used to notify channel listeners of state changes to a channel.
 * 
 * @author bsutton
 */
public interface ChannelHangupListener
{
    // Called by the channel when it is being hungup
    void channelHangup(Channel channel, Integer cause, String causeText);

}
