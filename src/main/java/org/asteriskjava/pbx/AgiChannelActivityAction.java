package org.asteriskjava.pbx;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;

public interface AgiChannelActivityAction
{

	void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException;

	boolean isDisconnect();

	void cancel();

}
