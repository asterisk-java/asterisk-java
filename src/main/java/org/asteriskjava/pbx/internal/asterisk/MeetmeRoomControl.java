package org.asteriskjava.pbx.internal.asterisk;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.CommandAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.ConfbridgeListAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ConfbridgeListEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.MeetMeJoinEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.MeetMeLeaveEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvents;
import org.asteriskjava.pbx.asterisk.wrap.response.CommandResponse;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.CoherentManagerEventListener;
import org.asteriskjava.pbx.internal.managerAPI.EventListenerBaseClass;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class MeetmeRoomControl extends EventListenerBaseClass implements CoherentManagerEventListener
{
    /*
     * listens for a channel entering or leaving meetme rooms. when there is
     * only 1 channel left in a room it sets it as inactive .It will not set to
     * inactive unless okToKill is set to true. It is set to false by
     * default.Also manages the available room list.
     */

    private static final Log logger = LogFactory.getLog(MeetmeRoomControl.class);

    private Integer meetmeBaseAddress;

    private MeetmeRoom rooms[];

    private int roomCount;

    private boolean meetmeInstalled = false;

    private final static AtomicReference<MeetmeRoomControl> self = new AtomicReference<>();

    synchronized public static void init(PBX pbx, final int roomCount) throws NoMeetmeException
    {
        if (MeetmeRoomControl.self.get() != null)
        {
            logger.warn("The MeetmeRoomControl has already been initialised."); //$NON-NLS-1$
        }
        else
        {
            MeetmeRoomControl.self.set(new MeetmeRoomControl(pbx, roomCount));
        }
    }

    public static MeetmeRoomControl getInstance()
    {
        if (MeetmeRoomControl.self.get() == null)
        {
            throw new IllegalStateException(
                    "The MeetmeRoomControl has not been initialised. Please call MeetmeRoomControl.init()."); //$NON-NLS-1$
        }

        return MeetmeRoomControl.self.get();

    }

    private MeetmeRoomControl(PBX pbx, final int roomCount) throws NoMeetmeException
    {
        super("MeetmeRoomControl", pbx); //$NON-NLS-1$
        this.roomCount = roomCount;
        final AsteriskSettings settings = PBXFactory.getActiveProfile();
        this.meetmeBaseAddress = settings.getMeetmeBaseAddress();
        this.rooms = new MeetmeRoom[roomCount];
        this.configure((AsteriskPBX) pbx);

        this.startListener();
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(MeetMeJoinEvent.class);
        required.add(MeetMeLeaveEvent.class);

        return required;
    }

    /*
     * returns the next available meetme room, or null if no rooms are
     * available.
     */
    public synchronized MeetmeRoom findAvailableRoom(RoomOwner newOwner)
    {
        int count = 0;
        for (final MeetmeRoom room : this.rooms)
        {
            if (MeetmeRoomControl.logger.isDebugEnabled())
            {
                MeetmeRoomControl.logger.debug("room " + room.getRoomNumber() + " count " + count);
            }
            if (room.getOwner() == null || !room.getOwner().isRoomStillRequired())
            {
                /*
                 * new code to attempt to recover uncleared meetme rooms safely
                 */
                try
                {
                    final Date lastUpdated = room.getLastUpdated();
                    final long now = new Date().getTime();
                    if (lastUpdated != null)
                    {
                        final long elapsedTime = now - lastUpdated.getTime();
                        MeetmeRoomControl.logger
                                .debug("room: " + room.getRoomNumber() + " count: " + count + " elapsed: " + elapsedTime);
                        if ((elapsedTime > 7200000) && (room.getChannelCount() == 1))
                        {
                            MeetmeRoomControl.logger.debug("clearing room"); //$NON-NLS-1$
                            room.setInactive();
                        }
                    }
                }
                catch (final Exception e)
                {
                    /*
                     * attempt to make this new change safe
                     */
                    MeetmeRoomControl.logger.error(e, e);
                }

                if (room.getChannelCount() == 0)
                {
                    room.setInactive();
                    room.setOwner(newOwner);
                    MeetmeRoomControl.logger.warn("Returning available room " + room.getRoomNumber());
                    return room;
                }

            }
            else
            {
                logger.warn("Meetme " + room.getRoomNumber() + " is still in use by " + room.getOwner());
            }
            count++;
        }
        MeetmeRoomControl.logger.error("no more available rooms");
        return null;

    }

    /**
     * Returns the MeetmeRoom for the given room number. The room number will be
     * an integer value offset from the meetme base address.
     * 
     * @param roomNumber the meetme room number
     * @return
     */
    synchronized private MeetmeRoom findMeetmeRoom(final String roomNumber)
    {
        MeetmeRoom foundRoom = null;
        for (final MeetmeRoom room : this.rooms)
        {
            if (room.getRoomNumber().compareToIgnoreCase(roomNumber) == 0)
            {
                foundRoom = room;
                break;
            }
        }
        return foundRoom;
    }

    synchronized MeetmeRoom getRoom(final int room)
    {
        return this.rooms[room];
    }

    @Override
    public void onManagerEvent(final ManagerEvent event)
    {
        MeetmeRoom room;
        if (event instanceof MeetMeJoinEvent)
        {
            final MeetMeJoinEvent evt = (MeetMeJoinEvent) event;
            room = this.findMeetmeRoom(evt.getMeetMe());
            final Channel channel = evt.getChannel();
            if (room != null)
            {
                if (room.addChannel(channel))
                {
                    MeetmeRoomControl.logger.debug(channel + " has joined the conference " //$NON-NLS-1$
                            + room.getRoomNumber() + " channelCount " + (room.getChannelCount())); //$NON-NLS-1$
                    room.setLastUpdated();
                }
            }
        }
        if (event instanceof MeetMeLeaveEvent)
        {
            final MeetMeLeaveEvent evt = (MeetMeLeaveEvent) event;
            room = this.findMeetmeRoom(evt.getMeetMe());
            final Channel channel = evt.getChannel();
            if (room != null)
            {
                // ignore local dummy channels// &&
                // !channel.toUpperCase().startsWith("LOCAL/")) {

                if (MeetmeRoomControl.logger.isDebugEnabled())
                {
                    MeetmeRoomControl.logger.debug(channel + " has left the conference " //$NON-NLS-1$
                            + room.getRoomNumber() + " channel count " + (room.getChannelCount())); //$NON-NLS-1$
                }
                room.removeChannel(channel);
                room.setLastUpdated();
                if ((room.getChannelCount() < 2) && (room.getForceClose() == true))
                {
                    this.hangupChannels(room);
                    room.setInactive();
                }

                if (room.getChannelCount() < 1)
                {
                    room.setInactive();
                }
            }
        }
    }

    public void hangupChannels(final MeetmeRoom room)
    {

        final Channel Channels[] = room.getChannels();
        if (room.isActive() == true)
        {
            PBX pbx = PBXFactory.getActivePBX();

            for (final Channel channel : Channels)
            {
                room.removeChannel(channel);

                try
                {
                    logger.warn("Hanging up");
                    pbx.hangup(channel);
                }
                catch (IllegalArgumentException | IllegalStateException | PBXException e)
                {
                    logger.error(e, e);

                }
            }
        }
    }

    private void configure(AsteriskPBX pbx) throws NoMeetmeException
    {
        final int base = this.meetmeBaseAddress;
        for (int r = 0; r < this.roomCount; r++)
        {
            this.rooms[r] = new MeetmeRoom(r + base);
        }

        try
        {
            String command;
            if (pbx.getVersion().isAtLeast(AsteriskVersion.ASTERISK_13))
            {
                command = "ConfBridge list"; //$NON-NLS-1$
                ConfbridgeListAction action = new ConfbridgeListAction();
                final ResponseEvents response = pbx.sendEventGeneratingAction(action, 3000);
                Map<String, Integer> roomChannelCount = new HashMap<>();
                for (ResponseEvent event : response.getEvents())
                {

                    ConfbridgeListEvent e = (ConfbridgeListEvent) event;
                    Integer current = roomChannelCount.get(e.getConference());
                    if (current == null)
                    {
                        roomChannelCount.put(e.getConference(), 1);
                    }
                    else
                    {
                        roomChannelCount.put(e.getConference(), current + 1);
                    }
                }
                for (Entry<String, Integer> entry : roomChannelCount.entrySet())
                {
                    setRoomCount(entry.getKey(), entry.getValue(), Integer.parseInt(entry.getKey()));

                }
                this.meetmeInstalled = true;

            }
            else
            {
                if (pbx.getVersion().isAtLeast(AsteriskVersion.ASTERISK_1_6))
                {
                    command = "meetme list"; //$NON-NLS-1$
                }
                else
                {
                    command = "meetme"; //$NON-NLS-1$
                }
                final CommandAction commandAction = new CommandAction(command);
                final ManagerResponse response = pbx.sendAction(commandAction, 3000);
                if (!(response instanceof CommandResponse))
                {
                    throw new ManagerCommunicationException(response.getMessage(), null);
                }

                final CommandResponse commandResponse = (CommandResponse) response;
                MeetmeRoomControl.logger.debug("parsing active meetme rooms"); //$NON-NLS-1$
                for (final String line : commandResponse.getResult())
                {
                    this.parseMeetme(line);
                    this.meetmeInstalled = true;
                    MeetmeRoomControl.logger.debug(line);
                }
            }
        }
        catch (final NoMeetmeException e)
        {
            throw e;
        }
        catch (final Exception e)
        {
            MeetmeRoomControl.logger.error(e, e);
            throw new NoMeetmeException(e.getLocalizedMessage());
        }
    }

    private synchronized void parseMeetme(final String line) throws NoMeetmeException
    {

        if (line != null)
        {
            if (line.toLowerCase().startsWith("no such command 'meetme'") == true) //$NON-NLS-1$
            {
                throw new NoMeetmeException("Asterisk is not configured correctly! Please enable the MeetMe app"); //$NON-NLS-1$
            }

            if ((line.toLowerCase().startsWith("no active meetme conferences.") == false) //$NON-NLS-1$
                    && (line.toLowerCase().startsWith("conf num") == false) //$NON-NLS-1$
                    && (line.toLowerCase().startsWith("* total number") == false) //$NON-NLS-1$
                    && (line.toLowerCase().startsWith("no such conference") == false) //$NON-NLS-1$
                    && (line.toLowerCase().startsWith("no such command 'meetme") == false) //$NON-NLS-1$
            )
            {
                // Update the stats on each meetme
                final String roomNumber = line.substring(0, 10).trim();
                final String tmp = line.substring(11, 25).trim();
                final int channelCount = Integer.parseInt(tmp);

                final int roomNo = Integer.valueOf(roomNumber);
                setRoomCount(roomNumber, channelCount, roomNo);
            }
        }
    }

    private void setRoomCount(final String roomNumber, final int channelCount, final int roomNo)
    {
        Integer base = this.meetmeBaseAddress;
        // First check if its one of our rooms.
        if ((roomNo >= base) && (roomNo < (base + this.roomCount)))
        {
            final MeetmeRoom room = this.findMeetmeRoom(roomNumber);
            if (room != null)
            {
                if (room.getChannelCount() != channelCount)
                {
                    /*
                     * After a restart there may have been meetme rooms left up
                     * and running with live calls. We need to identify any
                     * active rooms so we don't accidentally re-use an active
                     * room which would result in a crossed channel.
                     */
                    MeetmeRoomControl.logger.warn("Room number: " + room.getRoomNumber() //$NON-NLS-1$
                            + " has a server side channel count = " + channelCount //$NON-NLS-1$
                            + " when the channel count for that room is: " + room.getChannelCount() //$NON-NLS-1$
                            + " the server side channel count will be reset."); //$NON-NLS-1$
                }
                room.resetChannelCount(channelCount);
                room.setActive();
            }
            // else "Found roomNumber:" + roomNumber + " but it was not
            // in the list of rooms managed by MeetmeRoomControl.");
            // //$NON-NLS-1$ //$NON-NLS-2$

        }
    }

    public void stop()
    {
        this.close();

    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

    public boolean isMeetmeInstalled()
    {
        return this.meetmeInstalled;
    }

}
