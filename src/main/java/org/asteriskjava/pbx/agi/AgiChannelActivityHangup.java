package org.asteriskjava.pbx.agi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import java.util.concurrent.CountDownLatch;

public class AgiChannelActivityHangup implements AgiChannelActivityAction {
    private final Log logger = LogFactory.getLog(this.getClass());

    CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException {
        try {

            channel.hangup();
            logger.info("Hungup");
        } catch (AgiHangupException e) {
            // don't care
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
}
