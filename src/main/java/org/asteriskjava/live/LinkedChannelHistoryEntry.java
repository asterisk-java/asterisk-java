package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

public class LinkedChannelHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date dateLinked;
    private Date dateUnlinked;
    private final AsteriskChannel channel;

    public LinkedChannelHistoryEntry(Date dateLinked, AsteriskChannel channel)
    {
        this.dateLinked = dateLinked;
        this.channel = channel;
    }

    public Date getDateLinked()
    {
        return dateLinked;
    }

    public Date getDateUnlinked()
    {
        return dateUnlinked;
    }

    public void setDateUnlinked(Date dateUnlinked)
    {
        this.dateUnlinked = dateUnlinked;
    }

    public AsteriskChannel getChannel()
    {
        return channel;
    }

    public String toString()
    {
        final StringBuffer sb;

        sb = new StringBuffer("LinkedChannelHistoryEntry[");
        sb.append("dateLinked=" + dateLinked + ",");
        sb.append("dateUnlinked=" + dateUnlinked + ",");
        sb.append("channel=" + channel + "]");
        return sb.toString();
    }
}
