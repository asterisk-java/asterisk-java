package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.manager.event.MeetMeEvent;
import org.asteriskjava.manager.event.MeetMeJoinEvent;
import org.asteriskjava.manager.event.MeetMeLeaveEvent;
import org.asteriskjava.manager.event.MeetMeStopTalkingEvent;
import org.asteriskjava.manager.event.MeetMeTalkingEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Manages MeetMe events on behalf of an AsteriskManager.
 * 
 * @author srt
 */
class MeetMeManager
{
    private final Log logger = LogFactory.getLog(getClass());
    
    private final ManagerConnectionPool connectionPool;
    private final ChannelManager channelManager;
    
    /**
     * Maps room number to MeetMe room.
     */
    private final Map<String, MeetMeRoomImpl> rooms;

    MeetMeManager(ManagerConnectionPool connectionPool, ChannelManager channelManager)
    {
        this.connectionPool = connectionPool;
        this.channelManager = channelManager;
        this.rooms = new HashMap<String, MeetMeRoomImpl>();
    }

    void initialize()
    {
        
    }

    void disconnected()
    {
        synchronized (rooms)
        {
            rooms.clear();
        }
    }
    
    Collection<MeetMeRoom> getMeetMeRooms()
    {
        Collection<MeetMeRoom> copy;

        synchronized (rooms)
        {
            copy = new ArrayList<MeetMeRoom>(rooms.size() + 2);
            for (MeetMeRoomImpl room : rooms.values())
            {
                copy.add(room);
            }
        }
        return copy;
    }

    void handleMeetMeEvent(MeetMeEvent event)
    {
        String uniqueId;
        String roomNumber;
        AsteriskChannelImpl channel;
        MeetMeRoomImpl room;
        MeetMeUserImpl user;

        uniqueId = event.getUniqueId();
        if (uniqueId == null)
        {
            logger.warn("UniqueId is null. Ignoring " + event.getClass().getName());
            return;
        }

        roomNumber = event.getMeetMe();
        if (roomNumber == null)
        {
            logger.warn("RoomNumber (meetMe property) is null. Ignoring " + event.getClass().getName());
            return;
        }

        channel = channelManager.getChannelImplById(uniqueId);
        if (channel == null)
        {
            logger.warn("No channel with unique id " + uniqueId + ". Ignoring " + event.getClass().getName());
            return;
        }

        room = getOrCreateRoomImpl(roomNumber);
        user = channel.getMeetMeUserImpl();

        if (event instanceof MeetMeJoinEvent)
        {
            if (user != null)
            {
                logger.error("Got MeetMeJoin event for channel " + channel.getName() + " that is already user of a room");
                user.setDateLeft(event.getDateReceived());
                if (user.getMeetMeRoomImpl() != null)
                {
                    user.getMeetMeRoomImpl().removeUser(user);
                }
                channel.setMeetMeUserImpl(null);
            }
            
            logger.info("Adding channel " + channel.getName() + " as user " + event.getUserNum() + " to room " + roomNumber);
            user = new MeetMeUserImpl(connectionPool, room, event.getUserNum(), channel, event.getDateReceived());
            room.addUser(user);
            channel.setMeetMeUserImpl(user);
            return;
        }

        // all events below require the user to already exist
        if (user == null)
        {
            logger.warn("No MeetMe user associated with channel " + channel.getName() + ". Ignoring " + event.getClass().getName());
            return;
        }

        if (event instanceof MeetMeLeaveEvent)
        {
            logger.info("Removing channel " + channel.getName() + " from room " + roomNumber);
            if (room != user.getMeetMeRoomImpl())
            {
                if (user.getMeetMeRoomImpl() != null)
                {
                    logger.error("Channel " + channel.getName() + " should be removed from room " + roomNumber + " but is user of room " + user.getMeetMeRoomImpl().getRoomNumber());
                    user.getMeetMeRoomImpl().removeUser(user);
                }
                else
                {
                    logger.error("Channel " + channel.getName() + " should be removed from room " + roomNumber + " but is user of no room");
                }
            }
            user.setDateLeft(event.getDateReceived());
            room.removeUser(user);
            channel.setMeetMeUserImpl(null);
        }
        else if (event instanceof MeetMeTalkingEvent)
        {
            user.setTalking(true);
        }
        else if (event instanceof MeetMeStopTalkingEvent)
        {
            user.setTalking(false);
        }
    }

    /**
     * Returns the room with the given number or creates a new one if none is there yet.
     * 
     * @param roomNumber number of the room to get or create.
     * @return the room with the given number.
     */
    private MeetMeRoomImpl getOrCreateRoomImpl(String roomNumber)
    {
        MeetMeRoomImpl room;
        boolean created = false;
        
        synchronized (rooms)
        {
            room = rooms.get(roomNumber); 
            if (room == null)
            {
                room = new MeetMeRoomImpl(connectionPool, roomNumber);
                rooms.put(roomNumber, room);
                created = true;
            }
        }

        if (created)
        {
            logger.debug("Created MeetMeRoom " + roomNumber);
        }

        return room;
    }
}
