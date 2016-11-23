package org.asteriskjava.pbx.agi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class AgiChannelActivityHoldForBridge implements AgiChannelActivityAction
{
    private final Log logger = LogFactory.getLog(this.getClass());
    private AgiChannelActivityBridge bridgeActivity;

    public AgiChannelActivityHoldForBridge(AgiChannelActivityBridge bridgeActivity)
    {
        this.bridgeActivity = bridgeActivity;
    }

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException
    {
        try
        {
            channel.playMusicOnHold();
            bridgeActivity.sleepWhileBridged();

            channel.hangup();
        }
        catch (AgiHangupException e)
        {
            logger.warn(e.getMessage() + " " + channel.getName());
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
        // TODO Auto-generated method stub

    }
    // Logger logger = LogManager.getLogger();
}
