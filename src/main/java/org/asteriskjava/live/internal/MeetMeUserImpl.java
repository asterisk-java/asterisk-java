package org.asteriskjava.live.internal;

import java.util.Date;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.manager.action.CommandAction;

class MeetMeUserImpl implements MeetMeUser
{
    private static final String COMMAND_PREFIX = "meetme";
    private static final String MUTE_COMMAND = "mute";
    private static final String UNMUTE_COMMAND = "unmute";
    private static final String KICK_COMMAND = "kick";

    private final ManagerConnectionPool connectionPool;
    private final MeetMeRoomImpl room;
    private final Integer userNumber;
    private final AsteriskChannel channel;
    private final Date dateJoined;

    private Date dateLeft;
    private boolean talking;

    MeetMeUserImpl(ManagerConnectionPool connectionPool, MeetMeRoomImpl room, Integer userNumber,
            AsteriskChannel channel, Date dateJoined)
    {
        this.connectionPool = connectionPool;
        this.room = room;
        this.userNumber = userNumber;
        this.channel = channel;
        this.dateJoined = dateJoined;
    }

    public AsteriskChannel getChannel()
    {
        return channel;
    }

    public Date getDateJoined()
    {
        return dateJoined;
    }

    public Date getDateLeft()
    {
        return dateLeft;
    }

    void setDateLeft(Date dateLeft)
    {
        this.dateLeft = dateLeft;
    }

    public boolean isTalking()
    {
        return talking;
    }

    void setTalking(boolean talking)
    {
        this.talking = talking;
    }

    // action methods

    public void kick() throws ManagerCommunicationException
    {
        sendMeetMeUserCommand(KICK_COMMAND);
    }

    public void mute() throws ManagerCommunicationException
    {
        sendMeetMeUserCommand(MUTE_COMMAND);
    }

    public void unmute() throws ManagerCommunicationException
    {
        sendMeetMeUserCommand(UNMUTE_COMMAND);
    }

    private void sendMeetMeUserCommand(String command) throws ManagerCommunicationException
    {
        StringBuffer sb = new StringBuffer();
        sb.append(COMMAND_PREFIX);
        sb.append(" ");
        sb.append(command);
        sb.append(" ");
        sb.append(room.getRoomNumber());
        sb.append(" ");
        sb.append(userNumber);

        connectionPool.sendAction(new CommandAction(sb.toString()));
    }
    
    public String toString()
    {
        StringBuffer sb;
        int systemHashcode;

        sb = new StringBuffer("MeetMeUser[");

        synchronized (this)
        {
            sb.append("dateJoined='" + getDateJoined() + "',");
            sb.append("dateLeft='" + getDateLeft() + "',");
            sb.append("talking=" + isTalking() + ",");
            sb.append("room=" + room + ",");
            systemHashcode = System.identityHashCode(this);
        }
        sb.append("systemHashcode=" + systemHashcode);
        sb.append("]");

        return sb.toString();
    }
}
