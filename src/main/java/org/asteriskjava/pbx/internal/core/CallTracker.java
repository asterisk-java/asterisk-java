package org.asteriskjava.pbx.internal.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ChannelHangupListener;
import org.asteriskjava.pbx.asterisk.wrap.events.ChannelState;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * This is a fairly simple class which is used by a Peer to track what calls are
 * live for the given Peer. It should be noted that the Peer must keep track of
 * all calls regardless of whether they are associated with NJR. The reason for
 * this is that a Peer needs to track the status of the associated endpoint so
 * that it can show if a handset is on a call. In this way the operator can tell
 * if a person is busy even before they call them. Essentially all this calls
 * does is track the set of channels associated with a call.
 * 
 * @author bsutton
 */
public class CallTracker implements ChannelHangupListener
{
    private static final Log logger = LogFactory.getLog(CallTracker.class);

    /**
     * The list of channels associated with this call.
     */
    ArrayList<Channel> _associatedChannels = new ArrayList<>();

    /**
     * The state of this call.
     */
    PeerState _state;

    private CallEndedListener listener;

    CallTracker(Peer peer, Channel initialChannel)
    {
        this._associatedChannels.add(initialChannel);
        initialChannel.addHangupListener(this);
        this.listener = peer;

    }

    /**
     * Used to merge two calls into a single call. This is required as during a
     * masquerade a new channel is created an initially we will create a
     * CallTracker for it. When the masquerade event finally turns up we will
     * realise it belongs to an existing CallTracker and as such we need to
     * merge the two CallTrackers.
     * 
     * @param rhs
     */
    void mergeCalls(CallTracker rhs)
    {
        synchronized (this._associatedChannels)
        {
            this._associatedChannels.addAll(rhs._associatedChannels);
            // not certain this is necessary but lets just tidy up a bit.
            rhs._associatedChannels.clear();
        }
    }

    public PeerState getState()
    {
        return this._state;
    }

    /**
     * @param s
     */
    public void setState(final ChannelState channelState)
    {
        this._state = PeerState.valueByChannelState(channelState);
    }

    public int findChannel(Channel newChannel)
    {
        int index = -1;
        synchronized (this._associatedChannels)
        {

            for (int i = 0; i < this._associatedChannels.size(); i++)
            {
                Channel channel = this._associatedChannels.get(i);
                if (channel.isSame(newChannel))
                {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    /**
     * Remove a channel from the call.
     * 
     * @param channel
     */
    public void remove(Channel channel)
    {
        synchronized (this._associatedChannels)
        {
            int index = findChannel(channel);
            if (index != -1)
            {
                if (logger.isDebugEnabled())
                    logger.debug(
                            "CallTracker removing channel: " + this.toString() + " " + channel.getExtendedChannelName()); //$NON-NLS-1$ //$NON-NLS-2$

                this._associatedChannels.remove(index);
            }
        }

    }

    public void startSweep()
    {
        synchronized (this._associatedChannels)
        {
            for (final Channel channel : this._associatedChannels)
            {
                ((ChannelProxy) channel).getRealChannel().startSweep();
            }
        }
    }

    public void endSweep()
    {
        final List<Channel> toremove = new LinkedList<>();
        synchronized (this._associatedChannels)
        {
            for (final Channel channel : this._associatedChannels)
            {
                if (!((ChannelProxy) channel).getRealChannel().wasMarkedDuringSweep())
                {
                    toremove.add(channel);
                    logger.warn("removing channel " + this.hashCode() + " " + channel.getChannelName());//$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            this._associatedChannels.removeAll(toremove);
        }

        // Now tell each of the channels it has been hungup
        for (final Channel channel : toremove)
        {
            ((ChannelProxy) channel).getRealChannel().notifyHangupListeners(-1,
                    "Lingering channel probably due to missed hangup event");
        }

    }

    void dumpChannelList()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("CallTracker: dump channellist:" + this); //$NON-NLS-1$
            synchronized (this._associatedChannels)
            {
                for (final Channel channel : this._associatedChannels)
                {
                    logger.debug("--> " + channel.toString()); //$NON-NLS-1$
                }
            }
        }
    }

    public boolean hasEnded()
    {
        synchronized (this._associatedChannels)
        {
            return this._associatedChannels.size() == 0;
        }
    }

    @Override
    public void channelHangup(Channel channel, Integer cause, String causeText)
    {
        remove(channel);
        synchronized (this._associatedChannels)
        {
            if (this._associatedChannels.size() == 0)
            {
                this._state = PeerState.NOTSET;
                notifyCallEndedListener();
            }
        }
    }

    private void notifyCallEndedListener()
    {
        this.listener.callEnded(this);
    }

    public String toString()
    {
        return this.listener + " : " + this._state; //$NON-NLS-1$
    }

}
