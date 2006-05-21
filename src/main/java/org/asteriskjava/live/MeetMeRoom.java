package org.asteriskjava.live;

import java.util.Collection;

/**
 * Represents an Asterisk MeetMe room.<br>
 * MeetMe rooms bridge multiple channels.
 * 
 * @author srt
 * @since 0.3
 */
public interface MeetMeRoom
{
    /**
     * Returns the number of this MeetMe room.
     * 
     * @return the number of this MeetMe room.
     */
    String getRoomNumber();

    /**
     * Returns a collection of all active users in this MeetMe room.
     * 
     * @return a collection of all active users in this MeetMe room.
     */
    Collection<MeetMeUser> getUsers();

    void lock() throws ManagerCommunicationException;

    void unlock() throws ManagerCommunicationException;
}
