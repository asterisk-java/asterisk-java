package org.asteriskjava.pbx.internal.agi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.pbx.Channel;

public interface AgiChannelActivityAction
{

	void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException;

	boolean isDisconnect();

	void cancel(Channel channel);

}
