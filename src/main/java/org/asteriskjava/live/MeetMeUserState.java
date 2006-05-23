package org.asteriskjava.live;

/**
 * The lifecycle status of a {@link org.asteriskjava.live.Me}.
 * 
 * @author srt
 * @version $Id: ChannelState.java 349 2006-05-22 17:47:00Z srt $
 */
public enum MeetMeUserState
{
    /**
     * The user joined the MeetMe room.
     */
    JOINED,

    /**
     * The user left the MeetMe room.
     */
    LEFT
}
