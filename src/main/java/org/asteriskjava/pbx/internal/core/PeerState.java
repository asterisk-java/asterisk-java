package org.asteriskjava.pbx.internal.core;

import org.asteriskjava.pbx.asterisk.wrap.events.ChannelState;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public enum PeerState
{
    DOWN(1, "Down", "OnHook", false), //$NON-NLS-1$ //$NON-NLS-2$
    UP(2, "Up", "OnPhone", true), //$NON-NLS-1$ //$NON-NLS-2$
    UNKNOWN(0, "Unknown", "Unknown", false), //$NON-NLS-1$ //$NON-NLS-2$
    NOTSET(0, "Not Set", "", false), //$NON-NLS-1$ //$NON-NLS-2$
    UNMONITORED(0, "Unmonitored", "Unmonitored", true), //$NON-NLS-1$ //$NON-NLS-2$
    DND(3, "DND", "DND", true), //$NON-NLS-1$ //$NON-NLS-2$
    RINGING(4, "Ringing", "Ringing", true), //$NON-NLS-1$ //$NON-NLS-2$
    OFF_LINE(1, "Off line", "Offline", true), //$NON-NLS-1$ //$NON-NLS-2$
    UNREGISTERED(0, "Unregistered", "Unregistered", true), //$NON-NLS-1$ //$NON-NLS-2$
    REGISTERED(0, "Registered", "Registered", false), //$NON-NLS-1$ //$NON-NLS-2$
    BUSY(2, "Busy", "Busy", true), //$NON-NLS-1$//$NON-NLS-2$
    RING(4, "Ring", "Dialing", true), //$NON-NLS-1$ //$NON-NLS-2$
    EXTERNAL(0, "External", "External", false);//$NON-NLS-1$ //$NON-NLS-2$

    private static final Log logger = LogFactory.getLog(PeerState.class);

    final String _asteriskStateName;

    final String label;

    /**
     * Controls whether the state should be displayed to the end user or just
     * hidden.
     */
    private boolean _visible;

    /**
     * The priority ranks the PeerStates relative to each other. When a Peer has
     * multiple active calls we need to display the call state from the call
     * with the highest priority. e.g. If one call is UP and another is RINGING
     * then we need to display the RINGING state as this is more interesting.
     */
    private int _priority;

    public static PeerState valueByName(final String value)
    {
        PeerState status = NOTSET;

        for (PeerState aState : PeerState.values())
        {

            if (aState.getAsteriskStateName().compareToIgnoreCase(value) == 0)
            {
                status = aState;
                break;
            }
        }
        return status;
    }

    private String getAsteriskStateName()
    {
        return this._asteriskStateName;
    }

    public static PeerState valueByChannelState(final ChannelState state)
    {
        PeerState status = NOTSET;

        for (PeerState aState : PeerState.values())
        {

            if (aState.getAsteriskStateName().compareToIgnoreCase(state.name()) == 0)
            {
                status = aState;
                break;
            }
        }

        if (status == NOTSET)
            logger.warn("Unknown channelState: " + state + " recieved", new Throwable("Unknown channelState")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return status;
    }

    PeerState(int priority, final String asteriskStateName, final String label, boolean visible)
    {
        this._asteriskStateName = asteriskStateName;
        this.label = label;
        this._visible = visible;
        this._priority = priority;
    }

    public String getLabel()
    {
        return this.label;
    }

    @Override
    public String toString()
    {
        return this.label;
    }

    public boolean isVisible()
    {
        return this._visible;
    }

    public int getPriority()
    {
        return this._priority;
    }
}
