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
