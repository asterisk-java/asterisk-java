package org.asteriskjava.pbx.internal.core;

import java.util.LinkedList;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.asterisk.wrap.events.MasqueradeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewChannelEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewStateEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/*
 * this class tracks the status of a Peer on asterisk. T
 * 
 * Peers are created by the PeerMonitor which also sends asterisk events to 
 * the peer.
 * 
 */

public class Peer implements CallEndedListener
{
    private static final Log logger = LogFactory.getLog(Peer.class);

    private final EndPoint peerEndPoint;

    /**
     * Used to track the list of channels associated with this peer. I think
     * there would normally only be a single channel unless we are in the middle
     * of a transfer or the phone has two calls up.
     */
    private final LinkedList<CallTracker> callList = new LinkedList<>();

    private PeerState _state = PeerState.NOTSET;
    private boolean dnd = false;

    public Peer(final EndPoint endPoint)
    {
        this.peerEndPoint = endPoint;
    }

    public boolean isSame(final Peer rhs)
    {
        return this.peerEndPoint.isSame(rhs.getEndPoint());
    }

    private CallTracker registerChannel(final Channel newChannel)
    {
        CallTracker associatedCall = null;
        boolean found = false;
        synchronized (this.callList)
        {
            for (final CallTracker call : this.callList)
            {
                if (call.findChannel(newChannel) != -1)
                {
                    found = true;
                    associatedCall = call;
                    break;
                }
            }
            if (!found)
            {
                if (logger.isDebugEnabled())
                    logger.debug("Peer adding Call: " + this.toString() + " " + newChannel.getExtendedChannelName()); //$NON-NLS-1$ //$NON-NLS-2$
                associatedCall = createCallTracker(newChannel);
                this.dumpCallList();
            }
        }
        return associatedCall;
    }

    private CallTracker createCallTracker(final Channel newChannel)
    {
        CallTracker newCall;
        synchronized (this.callList)
        {
            newCall = new CallTracker(this, newChannel);
            this.callList.add(newCall);
        }
        return newCall;
    }

    public boolean getDND()
    {
        return this.dnd;
    }

    // public String getFullChannelName()
    // {
    // String channelName = null;
    // synchronized (this.callList)
    // {
    // if (this.callList.size() > 0)
    // {
    // channelName = this.callList.get(0).getChannelName();
    // }
    // }
    //
    // return channelName;
    // }

    public PeerState getState()
    {
        return this._state;
    }

    /**
     * Determines if the given channel is connected to this peer.
     * 
     * @param channel
     * @return
     */
    private boolean isConnectedToSelf(final Channel channel)
    {
        return this.getEndPoint().isSame(channel.getEndPoint());
    }

    /**
     * We handle the masqurade event is it contains channel state information.
     * During a masquerade we will have two channels for the one peer, the
     * original and the clone. As the clone is taking over from the original
     * peer we now need to use that as true indicator of state.
     * 
     * @param b
     * @throws Exception
     */
    public void handleEvent(final MasqueradeEvent b)
    {
        if (this.isConnectedToSelf(b.getClone()))
        {
            // At this point we will actually have two CallTrackers
            // which we now need to merge into a single CallTracker.
            CallTracker original = findCall(b.getOriginal());
            CallTracker clone = findCall(b.getClone());
            if (original != null && clone != null)
            {
                clone.mergeCalls(original);
                clone.setState(b.getCloneState());
                this.evaluateState();
            }
            else
                logger.warn("When processing masquradeEvent we could not find the expected calls. event=" //$NON-NLS-1$
                        + b.toString() + " original=" + original + " clone=" + clone); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * When a new channel comes up associated it with the peer. Be warned a peer
     * can have multiple calls associated with it.
     * 
     * @param b
     * @throws Exception
     */
    public void handleEvent(final NewChannelEvent b)
    {
        // Very occasionally we get a null channel which we can't do anything
        // with so just throw the event away.
        // If we get a Console/dsp channel name it means that someone has
        // dialled from the asterisk
        // console. We just ignore these.
        if ((b.getChannel() == null) || (b.getChannel().isConsole()))
        {
            return;
        }

        if (this.isConnectedToSelf(b.getChannel()))
        {
            CallTracker call = this.registerChannel(b.getChannel());
            if (call != null)
            {
                call.setState(b.getChannelState());
                this.evaluateState();
            }
        }
    }

    public void handleEvent(final NewStateEvent b)
    {
        if (this.isConnectedToSelf(b.getChannel()))
        {
            CallTracker call = this.registerChannel(b.getChannel());
            if (call != null)
            {
                call.setState(b.getChannelState());
                this.evaluateState();
            }
        }
    }

    public void handleEvent(final StatusEvent b)
    {
        final Channel channel = b.getChannel();
        if (this.isConnectedToSelf(channel))
        {
            ((ChannelProxy) channel).getRealChannel().markChannel();

            CallTracker call = findCall(channel);
            if (call != null)
            {
                call.setState(b.getState());
                this.evaluateState();
            }
        }
    }

    private CallTracker findCall(Channel channel)
    {
        CallTracker result = null;
        synchronized (this.callList)
        {
            for (CallTracker call : this.callList)
            {
                if (call.findChannel(channel) != -1)
                {
                    result = call;
                    break;
                }
            }

        }
        return result;
    }

    public void setDND(final boolean on)
    {
        this.dnd = on;
    }

    public void startSweep()
    {
        Peer.logger.debug("Starting sweep for " + this.peerEndPoint.getFullyQualifiedName());//$NON-NLS-1$
        synchronized (this.callList)
        {
            for (final CallTracker call : this.callList)
            {
                call.startSweep();
            }
        }
    }

    public void endSweep()
    {
        Peer.logger.debug("Ending sweep for " + this.peerEndPoint.getFullyQualifiedName());//$NON-NLS-1$
        synchronized (this.callList)
        {
            for (final CallTracker call : this.callList)
            {
                call.endSweep();
            }
        }
        this.evaluateState();
    }

    /**
     * Called each time the state of a call changes to determine the Peers
     * overall state.
     */
    private void evaluateState()
    {
        synchronized (this.callList)
        {
            // Get the highest prioirty state from the set of calls.
            PeerState newState = PeerState.NOTSET;
            for (CallTracker call : this.callList)
            {
                if (call.getState().getPriority() > newState.getPriority())
                    newState = call.getState();
            }

            this._state = newState;
        }
    }

    public EndPoint getEndPoint()
    {
        return this.peerEndPoint;
    }

    @Override
    public String toString()
    {
        return this.getEndPoint().toString();
    }

    private void dumpCallList()
    {
        if (logger.isDebugEnabled())
        {
            Peer.logger.debug("Peer: dump CallList:" + this); //$NON-NLS-1$
            for (final CallTracker call : this.callList)
            {
                call.dumpChannelList();
            }
        }
    }

    @Override
    public void callEnded(CallTracker call)
    {
        boolean found = false;
        synchronized (this.callList)
        {
            for (final CallTracker aCall : this.callList)
            {
                if (call == aCall)
                {
                    found = true;
                    if (logger.isDebugEnabled())
                        logger.debug("peer Removing call " + call); //$NON-NLS-1$
                    this.callList.remove(call);
                    this.evaluateState();
                    this.dumpCallList();
                    break;
                }
            }
            if (!found)
            {
                logger.error("Error call not found removing call from peer: call=" + call + " peer=" + this); //$NON-NLS-1$ //$NON-NLS-2$
                this.dumpCallList();
            }
        }

    }

}
