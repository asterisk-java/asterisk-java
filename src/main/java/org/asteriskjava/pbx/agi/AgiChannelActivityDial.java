package org.asteriskjava.pbx.agi;

import java.util.concurrent.CountDownLatch;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;

public class AgiChannelActivityDial implements AgiChannelActivityAction
{

	CountDownLatch latch = new CountDownLatch(1);
	private String target;
	private String sipHeader;

	public AgiChannelActivityDial(String target)
	{
		this.target = target;
	}

	public AgiChannelActivityDial(String fullyQualifiedName, String sipHeader)
	{
		target = fullyQualifiedName;
		this.sipHeader = sipHeader;
	}

	@Override
	public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException
	{
		if (sipHeader != null)
		{
			channel.setVariable("__SIPADDHEADER", sipHeader);
		}
		// setting the activity to hold, means that when the call falls out of
		// the dial it'll go to hold.

		// also if some other code sets and activity and then redirects, we
		// won't over write it on the way out (if we did it after the dial
		// command)
		ichannel.setCurrentActivityAction(new AgiChannelActivityHold());
		channel.dial(target, 30, "");

	}

	@Override
	public boolean isDisconnect()
	{
		return false;
	}

	@Override
	public void cancel()
	{
		latch.countDown();

	}
}
