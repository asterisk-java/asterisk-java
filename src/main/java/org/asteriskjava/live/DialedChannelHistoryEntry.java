package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

/**
 * An entry in the dialed channels history of an {@link AsteriskChannel}.
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public class DialedChannelHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date date;
    private final AsteriskChannel channel;

    /**
     * Creates a new instance.
     * 
     * @param date the date the channel was dialed.
     * @param channel the channel that has been dialed.
     */
    public DialedChannelHistoryEntry(Date date, AsteriskChannel channel)
    {
        this.date = date;
        this.channel = channel;
    }

    /**
     * Returns the date the channel was dialed.
     * 
     * @return the date the channel was dialed.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Returns the channel that has been dialed.
     * 
     * @return the channel that has been dialed.
     */
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
