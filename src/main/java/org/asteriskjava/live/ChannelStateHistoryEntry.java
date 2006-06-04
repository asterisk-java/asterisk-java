package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

public class ChannelStateHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date date;
    private final ChannelState state;

    public ChannelStateHistoryEntry(Date date, ChannelState state)
    {
        this.date = date;
        this.state = state;
    }

    public Date getDate()
    {
        return date;
    }

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
