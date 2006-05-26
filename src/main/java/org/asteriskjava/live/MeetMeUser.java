package org.asteriskjava.live;

import java.util.Date;

/**
 * Represents a user of a MeetMe room.<br>
 * PropertyChangeEvents are fired for the following properties:
 * <ul>
 * <li>talking
 * <li>muted
 * <li>state
 * </ul>
 * 
 * @see org.asteriskjava.live.MeetMeRoom
 * @author srt
 * @since 0.3
 */
public interface MeetMeUser extends LiveObject
{
    final String PROPERTY_TALKING = "talking";
    final String PROPERTY_MUTED = "muted";
    final String PROPERTY_STATE = "state";
    
    /**
     * Returns whether this user is currently talking or not.<br>
     * Asterisk supports talker detection since version 1.2.
     * 
     * @return <code>true</code> if this user is currently talking and
     *         talker detection is supported, <code>false</code> otherwise. 
     */
    boolean isTalking();

    /**
     * Returns whether this user is muted or not.<br>
     * Supported since Asterisk version 1.4.
     * 
     * @return <code>true</code> if this user is muted and
     *         mute detection is supported, <code>false</code> otherwise. 
     */
    boolean isMuted();
    
    /**
     * Returns the date this user joined the MeetMe room.<br>
     * This property is immutable.
     * 
     * @return the date this user joined the MeetMe room.
     */
    Date getDateJoined();

    /**
     * Returns the date this user left the MeetMe room.<br>
     * This property is <code>null</code> as long as the user is
     * in state {@link MeetMeUserState#JOINED} and set to date the
     * user left when entering {@link MeetMeUserState#LEFT}.
     * 
     * @return the date this user left the MeetMe room or 
     *         <code>null</code> if the user did not yet leave.
     */
    Date getDateLeft();
    
    /**
     * Returns the lifecycle status of this MeetMeUser.<br>
     * Initially the user is in state {@link MeetMeUserState#JOINED}.
     * 
     * @return the lifecycle status of this MeetMeUser.
     */
    MeetMeUserState getState();

    /**
     * Returns the MeetMe room this user joined.<br>
     * This property is immutable.
     * 
     * @return the MeetMe room this user joined.
     */
    MeetMeRoom getRoom();
    
    /**
     * Returns the channel associated with this user.<br>
     * This property is immutable.
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
