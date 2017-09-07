package org.asteriskjava.pbx.internal.asterisk;

import java.util.Date;
import java.util.LinkedList;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/*
 * This class tracks the status, channel names and number of participants in
 * a meetme room. The hangup function will hangup all known participants in
 * this meetme room.
 */
public class MeetmeRoom
{
    /**
     * The asterisk room number. This will be value offset from the Meetme Base.
     * e.g. if the base is 3750 and this is the third allocated room, then the
     * roomNumber will equal 3753.
     */
    private final int roomNumber;

    private static final Log logger = LogFactory.getLog(MeetmeRoom.class);

    LinkedList<Channel> channels = new LinkedList<>();

    private int channelCount = 0;

    private boolean active = false;

    private boolean forceClose = false;

    private Date lastUpdated = null;

    private RoomOwner owner = null;

    public MeetmeRoom(final int number)
    {
        this.roomNumber = number;
    }

    /*
     * returns true if the channel was added to the list of channels in this
     * meetme. if the channel is already in the meetme, returns false
     */
    synchronized public boolean addChannel(final Channel channel)
    {
        boolean newChannel = false;
        if (!this.channels.contains(channel))
        {
            this.channels.add(channel);
            this.channelCount++;
            newChannel = true;
        }
        else
            MeetmeRoom.logger.error("rejecting " + channel + " already in meetme."); //$NON-NLS-1$ //$NON-NLS-2$
        return newChannel;
    }

    synchronized public int getChannelCount()
    {
        return this.channelCount;
    }

    synchronized public Channel[] getChannels()
    {
        final Channel list[] = new Channel[this.channels.size()];

        int cnt = 0;
        for (final Channel channel : this.channels)
        {
            list[cnt++] = channel;
        }
        return list;
    }

    public boolean getForceClose()
    {
        return this.forceClose;
    }

    public Date getLastUpdated()
    {
        return this.lastUpdated;
    }

    public String getRoomNumber()
    {
        return ("" + this.roomNumber); //$NON-NLS-1$
    }

    /**
     * returns true if the meetme room is active, false if it is available
     * 
     * @return
     */
    public boolean isActive()
    {
        return this.active;
    }

    synchronized public void removeChannel(final Channel channel)
    {
        final boolean channelCountInSync = this.channelCount == this.channels.size();
        final boolean removed = this.channels.remove(channel);

        if (!removed)
        {
            MeetmeRoom.logger.warn(
                    "An attempt to remove an non-existing channel " + channel + " from Meetme Room " + this.getRoomNumber()); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (channelCountInSync && removed)
        {
            this.channelCount--;
        }

        // If the channel count is not insync then we decrement the channel
        // count even if the remove was for a non-existent channel.
        // We do this as if the channel count is out of sync it means that we
        // have polled asterisk (usually during startup)
        // and our local count was out of sync with asterisk. Asterisk is the
        // definitive source. If we then get a remove
        // channel then its probably a channel that asterisk knows about but
        // which we don't know about.
        // In that case our channelCount will also have come from asterisk so
        // decrementing it keeps us in sync with asterisk
        // and eventually we will get back in sync (hopefully).
        if (!channelCountInSync && removed)
        {
            this.channelCount--;
        }

        if ((this.channels.size() < 2) && (this.channels.size() > 0))
        {
            if (!this.channels.get(0).isLocal())
            {
                logger.warn("One channel left in the meet me room " + this.channels.get(0) + " room " + this.roomNumber); //$NON-NLS-1$
            }
        }

    }

    public void setActive()
    {
        this.active = true;
    }

    public void setForceClose(final boolean canClose)
    {
        this.forceClose = canClose;
    }

    public void setInactive()
    {
        this.active = false;
        this.channels.clear();
        this.forceClose = false;
        this.lastUpdated = null;

    }

    public void setLastUpdated()
    {
        this.lastUpdated = new Date();
    }

    /**
     * This method should only be called if asterisk is reporting a channel
     * count for this meetme room which does not match our local channel count.
     * This could happen if it was restarted whilst a call was active.
     * 
     * @param channelCount
     */
    public void resetChannelCount(final int resetChannelCount)
    {
        this.channelCount = resetChannelCount;

    }

    public RoomOwner getOwner()
    {
        return owner;
    }

    public void setOwner(RoomOwner newOwner)
    {
        owner = newOwner;
        owner.setRoom(this);
        setActive();
    }

    public void removeOwner(RoomOwner toRemove)
    {
        if (owner == toRemove)
        {
            owner = null;
        }
        else
        {
            logger.error("Tring to remove the owner, but it's not the current owner");
        }
    }

}
