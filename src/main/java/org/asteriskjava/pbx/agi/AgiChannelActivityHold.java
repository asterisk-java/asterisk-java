package org.asteriskjava.pbx.agi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class AgiChannelActivityHold implements AgiChannelActivityAction
{
    private final Log logger = LogFactory.getLog(this.getClass());

    CountDownLatch latch = new CountDownLatch(1);
    volatile boolean callReachedAgi = false;
    long timer = System.currentTimeMillis();

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException
    {
        try
        {
            callReachedAgi = true;
            channel.answer();
            channel.playMusicOnHold();
            logger.info(ichannel + " is still on hold");
            if (latch.await(10, TimeUnit.SECONDS))
            {
                try
                {
                    channel.stopMusicOnHold();
                }
                catch (Exception e)
                {
                    logger.warn(e);
                }
            }
            else
            {
                long secondsOnHold = Math.abs(System.currentTimeMillis() - timer) / 1000;
                if (channel.getName().startsWith("Local") && secondsOnHold > 3600)
                {
                    // cleanup Local channels older than 1 hour
                    logger.error("Hanging up local channel that has been on hold for 1 hour " + channel.getName());
                    channel.hangup();
                }
            }
        }
        catch (AgiHangupException e)
        {
            logger.warn(e);
        }

    }

    @Override
    public boolean isDisconnect()
    {
        return false;
    }

    @Override
    public void cancel(Channel channel)
    {
        latch.countDown();

    }

    public boolean hasCallReachedAgi()
    {
        return callReachedAgi;
    }
}
