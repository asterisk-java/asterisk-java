package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

public class DialedChannelHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date date;
    private final AsteriskChannel channel;

    public DialedChannelHistoryEntry(Date date, AsteriskChannel channel)
    {
        this.date = date;
        this.channel = channel;
    }

    public Date getDate()
    {
        return date;
    }

    public AsteriskChannel getChannel()
    {
        return channel;
    }

    public String toString()
    {
        final StringBuffer sb;

        sb = new StringBuffer("DialedChannelHistoryEntry[");
        sb.append("date=" + date + ",");
        sb.append("channel=" + channel + "]");
        return sb.toString();
    }
}
