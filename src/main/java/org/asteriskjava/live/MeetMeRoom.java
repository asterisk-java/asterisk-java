package org.asteriskjava.live;

import java.util.Collection;

/**
 * Represents an Asterisk MeetMe room.<p>
 * MeetMe rooms bridge multiple channels.
 * 
 * @author srt
 * @since 0.3
 */
public interface MeetMeRoom
{
    /**
     * Returns the number of this MeetMe room.<p>
     * This property is immutable.
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

    /**
     * Locks this room so no addtional users can join.
     * 
     * @throws ManagerCommunicationException if the room can't be locked.
     */
    void lock() throws ManagerCommunicationException;

    /**
     * Unlocks this room so additional users can join again.
     * 
     * @throws ManagerCommunicationException if the room can't be locked.
     */
    void unlock() throws ManagerCommunicationException;
}
