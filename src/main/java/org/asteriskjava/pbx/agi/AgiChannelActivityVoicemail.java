package org.asteriskjava.pbx.agi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;

public class AgiChannelActivityVoicemail implements AgiChannelActivityAction
{

	public AgiChannelActivityVoicemail(String mailbox)
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDisconnect()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void cancel()
	{
		// TODO Auto-generated method stub

	}
	// Logger logger = LogManager.getLogger();
}
