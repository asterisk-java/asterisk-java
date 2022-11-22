package org.asteriskjava.pbx.internal.managerAPI;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.agi.AgiChannelActivityMeetme;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class RedirectToMeetMe {
    private static final Log logger = LogFactory.getLog(RedirectToMeetMe.class);

    public RedirectToMeetMe() {
        super();
    }

    public boolean redirectToMeetme(final Channel channel, final String room, String bridgeProfile, String userProfile)
            throws PBXException {
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        /*
         * this procedure rediects the specified channel to the specified meetme
         * room. This is achieved through the dial plan. q option - don't
         * announce new members to meetme d option - dynamically create meetme x
         * option - close the conference when last marked user exits A options -
         * set marked user
         */

        RedirectToMeetMe.logger.info("redirect to Meetme channel " + channel + " room " + room);

        if (!pbx.moveChannelToAgi(channel)) {
            throw new PBXException("Channel: " + channel + " couldn't be moved to agi");
        }
        channel.setCurrentActivityAction(new AgiChannelActivityMeetme(room, bridgeProfile, userProfile));

        return true;
    }

}
