package org.asteriskjava.live;

/**
 * You can register an AsteriskServerListener with an
 * {@link org.asteriskjava.live.AsteriskServer} to be notified about
 * new channels and MeetMe users.
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public interface AsteriskServerListener
{
    /**
     * Called whenever a new channel appears on the Asterisk server.
     * 
     * @param channel the new channel.
     */
    void onNewAsteriskChannel(AsteriskChannel channel);

    /**
     * Called whenever a user joins a {@link MeetMeRoom}.
     * 
     * @param user the user that joined.
     */
    void onNewMeetMeUser(MeetMeUser user);
}
