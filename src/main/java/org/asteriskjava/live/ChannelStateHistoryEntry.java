package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

/**
 * An entry in the channel state history of an {@link AsteriskChannel}.
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public class ChannelStateHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date date;
    private final ChannelState state;

    /**
     * Creates a new instance.
     * 
     * @param date the date the channel entered the state.
     * @param state the state the channel entered.
     */
    public ChannelStateHistoryEntry(Date date, ChannelState state)
    {
        this.date = date;
        this.state = state;
    }

    /**
     * Returns the date the channel entered the state.
     * 
     * @return the date the channel entered the state.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * The state the channel entered.
     * 
     * @return the state the channel entered.
     */
    public ChannelState getState()
    {
        return state;
    }

    public String toString()
    {
        final StringBuffer sb;

        sb = new StringBuffer("ChannelStateHistoryEntry[");
        sb.append("date=" + date + ",");
        sb.append("state=" + state + "]");
        return sb.toString();
    }
}
