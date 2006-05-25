package org.asteriskjava.live;

/**
 * The lifecycle status of a {@link org.asteriskjava.live.MeetMeUser}.
 * 
 * @author srt
 * @version $Id$
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
