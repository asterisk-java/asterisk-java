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

/**
 * Waits in silence for 10 seconds, then hangs up. This is useful where channels
 * are waiting for some external action to redirect/bridge them.
 * 
 * @author rsutton
 */
public class AgiChannelActivityTransientHoldSilence implements AgiChannelActivityAction
{
    private final Log logger = LogFactory.getLog(this.getClass());

    CountDownLatch latch = new CountDownLatch(1);
    volatile boolean callReachedAgi = false;
    boolean firstPass = true;

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException
    {
        try
        {
            callReachedAgi = true;
            channel.answer();
            if (!firstPass)
            {
                logger.error(ichannel + " is still on hold after first pass, Hanging up");
                channel.hangup();
            }
            else
            {
                latch.await(10, TimeUnit.SECONDS);
            }
        }
        catch (AgiHangupException e)
        {
            logger.warn(e);
        }
        finally
        {
            firstPass = false;
        }

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

    public boolean hasCallReachedAgi()
    {
        return callReachedAgi;
    }
}
