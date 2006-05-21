package org.asteriskjava.live;

import java.util.Date;

/**
 * Represents a user of a MeetMe room.
 * 
 * @see org.asteriskjava.live.MeetMeRoom
 * @author srt
 * @since 0.3
 */
public interface MeetMeRoomUser
{
    /**
     * Returns whether this user is currently talking or not.<br>
     * Asterisk supports talker detection since version 1.2.
     * 
     * @return <code>true</code> if this user is currently talking and
     *         talker detection is supported, <code>false</code> otherwise. 
     */
    boolean isTalking();

    /**
     * Returns the date this user joined the MeetMe room.
     * 
     * @return the date this user joined the MeetMe room.
     */
    Date getDateJoined();

    /**
     * Returns the date this user left the MeetMe room.
     * 
     * @return the date this user left the MeetMe room.
     */
    Date getDateLeft();

    /**
     * Returns the channel associated with this user.
     * 
     * @return the channel associated with this user.
     */
    AsteriskChannel getChannel();

    /**
     * Stops sending voice from this user to the MeetMe room.
     * 
     * @throws ManagerCommunicationException if there is a problem talking to the Asterisk server.
     */
    void mute() throws ManagerCommunicationException;

    /**
     * (Re)starts sending voice from this user to the MeetMe room.
     * 
     * @throws ManagerCommunicationException if there is a problem talking to the Asterisk server.
     */
    void unmute() throws ManagerCommunicationException;

    /**
     * Removes this user from the MeetMe room.
     * 
     * @throws ManagerCommunicationException if there is a problem talking to the Asterisk server.
     */
    void kick() throws ManagerCommunicationException;
}
