package org.asteriskjava.pbx.agi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import java.util.concurrent.CountDownLatch;

public class AgiChannelActivityBridge implements AgiChannelActivityAction {

    private Channel target;
    private final Log logger = LogFactory.getLog(this.getClass());

    CountDownLatch latch = new CountDownLatch(1);

    public AgiChannelActivityBridge(Channel target) {
        this.target = target;
    }

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException {
        try {
            // TODO: this should probably pass the 'F' option to allow the
            // bridgee
            // to go back to dialplan.
            // I'm not currently prepared to do it as it is called from a number
            // of places, and
            // I can't currently see it causing problems.
            channel.bridge(target.getChannelName(), "");
            channel.hangup();
        } catch (AgiHangupException e) {
            logger.warn(e);
        } finally {
            latch.countDown();
        }
    }

    @Override
    public boolean isDisconnect(ActivityAgi activityAgi) {
        return false;
    }

    @Override
    public void cancel() {
        latch.countDown();

    }
    // Logger logger = LogManager.getLogger();

    public void sleepWhileBridged() throws InterruptedException {
        latch.await();

    }
}
