package org.asteriskjava.pbx.agi;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.RedirectAction;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class AgiChannelActivityMeetme implements AgiChannelActivityAction {
    private final Log logger = LogFactory.getLog(this.getClass());

    CountDownLatch latch = new CountDownLatch(1);
    private String room;
    private volatile boolean hangup = true;

    private Channel ichannel;

    private String bridgeProfile;

    private String userProfile;

    public AgiChannelActivityMeetme(String room, String bridgeProfile, String userProfile) {
        this.room = room;
        this.bridgeProfile = bridgeProfile;
        this.userProfile = userProfile;
    }

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException {
        if (ichannel == null) {
            throw new NullPointerException("ichannel cannot be null");
        }
        this.ichannel = ichannel;
        channel.confbridge(room, bridgeProfile + "," + userProfile);

        if (hangup) {
            try {
                channel.hangup();
            } catch (Exception e) {
                logger.warn(e);
            }
        }
        logger.info(ichannel + " left the meetme");

    }

    @Override
    public boolean isDisconnect(ActivityAgi activityAgi) {
        return false;
    }

    @Override
    public void cancel() {
        hangup = false;
        final AsteriskSettings profile = PBXFactory.getActiveProfile();

        AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        final RedirectAction redirect = new RedirectAction(ichannel, profile.getManagementContext(), pbx.getExtensionAgi(),
                1);
        try {
            pbx.sendAction(redirect, 1000);
        } catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e) {
            logger.error(e, e);
        }

    }
}
