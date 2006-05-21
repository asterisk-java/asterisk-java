package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeRoomUser;
import org.asteriskjava.manager.action.CommandAction;

class MeetMeRoomImpl implements MeetMeRoom
{
    private static final String COMMAND_PREFIX = "meetme";
    private static final String LOCK_COMMAND = "lock";
    private static final String UNLOCK_COMMAND = "unlock";

    final ManagerConnectionPool connectionPool;
    final String roomNumber;
    final Collection<MeetMeRoomUserImpl> users;

    MeetMeRoomImpl(ManagerConnectionPool connectionPool, String roomNumber)
    {
        this.connectionPool = connectionPool;
        this.roomNumber = roomNumber;
        this.users = new ArrayList<MeetMeRoomUserImpl>(20);
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }

    public Collection<MeetMeRoomUser> getUsers()
    {
        Collection<MeetMeRoomUser> copy;

        synchronized (users)
        {
            copy = new ArrayList<MeetMeRoomUser>(users.size() + 2);
            for (MeetMeRoomUserImpl user : users)
            {
                copy.add(user);
            }
        }
        return copy;
    }
    
    void addUser(MeetMeRoomUserImpl user)
    {
        synchronized (users)
        {
            users.add(user);
        }
    }

    void removeUser(MeetMeRoomUserImpl user)
    {
        synchronized (users)
        {
            users.remove(user);
        }
    }

    // action methods

    public void lock() throws ManagerCommunicationException
    {
        sendMeetMeCommand(LOCK_COMMAND);
    }

    public void unlock() throws ManagerCommunicationException
    {
        sendMeetMeCommand(UNLOCK_COMMAND);
    }

    private void sendMeetMeCommand(String command) throws ManagerCommunicationException
    {
        StringBuffer sb = new StringBuffer();
        sb.append(COMMAND_PREFIX);
        sb.append(" ");
        sb.append(command);
        sb.append(" ");
        sb.append(roomNumber);

        connectionPool.sendAction(new CommandAction(sb.toString()));
    }

    public String toString()
    {
        StringBuffer sb;
        int systemHashcode;

        sb = new StringBuffer("MeetMeRoom[");

        synchronized (this)
        {
            sb.append("roomNumber='" + getRoomNumber() + "',");
            systemHashcode = System.identityHashCode(this);
        }
        sb.append("systemHashcode=" + systemHashcode);
        sb.append("]");

        return sb.toString();
    }
}
