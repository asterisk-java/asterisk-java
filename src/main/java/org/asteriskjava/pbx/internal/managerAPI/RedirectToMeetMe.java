package org.asteriskjava.pbx.internal.managerAPI;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.agi.AgiChannelActivityMeetme;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class RedirectToMeetMe
{
    static Logger logger = Logger.getLogger(RedirectToMeetMe.class);

    public RedirectToMeetMe()
    {
        super();
    }

    public boolean redirectToMeetme(final Channel channel, final String room, final boolean markedUser) throws PBXException
    {
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        /*
         * this procedure rediects the specified channel to the specified meetme
         * room. This is achieved through the dial plan. q option - don't
         * announce new members to meetme d option - dynamically create meetme x
         * option - close the conference when last marked user exits A options -
         * set marked user
         */

        RedirectToMeetMe.logger.info("redirect to Meetme channel " + channel + " room " + room //$NON-NLS-1$ //$NON-NLS-2$
                + " markedUser " + markedUser); //$NON-NLS-1$
        String options = new String();
        if (markedUser == true)
        {
            options = "qdxAF"; //$NON-NLS-1$
        }
        else
        {
            options = "qdxF"; //$NON-NLS-1$
        }

        if (!pbx.moveChannelToAgi(channel))
        {
            throw new PBXException("Channel: " + channel + " couldn't be moved to agi");
        }
        channel.setCurrentActivityAction(new AgiChannelActivityMeetme(room, options));

        return true;
    }

}
