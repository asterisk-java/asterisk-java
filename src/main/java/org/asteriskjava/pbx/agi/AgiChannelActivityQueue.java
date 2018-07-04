package org.asteriskjava.pbx.agi;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.RedirectAction;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class AgiChannelActivityQueue implements AgiChannelActivityAction
{

    private final Log logger = LogFactory.getLog(this.getClass());

    CountDownLatch latch = new CountDownLatch(1);

    final private String queue;
    private volatile boolean hangup = true;
    // volatile private iChannel ichannel;

    private Channel ichannel;

    public AgiChannelActivityQueue(String queue)
    {
        this.queue = queue;
    }

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException
    {
        this.ichannel = ichannel;

        channel.queue(queue);
        if (hangup)
        {
            try
            {
                channel.hangup();
            }
            catch (AgiHangupException e)
            {
                logger.warn("Channel " + channel.getName() + " hungup");
            }
            catch (Exception e)
            {
                logger.warn(e);
            }
        }
        logger.info(ichannel + " left the queue");

    }

    @Override
    public boolean isDisconnect()
    {
        return false;
    }

    @Override
    public void cancel()
    {

        hangup = false;
        final AsteriskSettings profile = PBXFactory.getActiveProfile();

        AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        final RedirectAction redirect = new RedirectAction(ichannel, profile.getManagementContext(), pbx.getExtensionAgi(),
                1);
        try
        {

            pbx.sendAction(redirect, 1000);
        }
        catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e)
        {
            logger.error(e, e);
        }

    }
}
