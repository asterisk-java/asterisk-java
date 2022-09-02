package org.asteriskjava.pbx.agi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;

import java.util.concurrent.CountDownLatch;

public class AgiChannelActivityBlindTransfer implements AgiChannelActivityAction {

    CountDownLatch latch = new CountDownLatch(1);
    private String target;
    private String sipHeader;
    int timeout = 30;
    private String callerId;
    private String dialOptions;
    private BlindTransferResultListener listener;

    public AgiChannelActivityBlindTransfer(String fullyQualifiedName, String sipHeader, String callerId, String dialOptions,
                                           BlindTransferResultListener listener) {
        this.target = fullyQualifiedName;
        this.sipHeader = sipHeader;
        this.callerId = callerId;
        this.dialOptions = dialOptions;
        if (sipHeader == null) {
            this.sipHeader = "";
        }
        this.listener = listener;
    }

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException {

        channel.setVariable("__SIPADDHEADER", sipHeader);
        channel.setCallerId(callerId);
        ichannel.setCurrentActivityAction(new AgiChannelActivityHold());
        channel.dial(target, timeout, dialOptions);
        String status = channel.getVariable("DIALSTATUS");
        boolean success = "ANSWER".equalsIgnoreCase(status);
        if (listener != null) {
            listener.result(status, success);
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
