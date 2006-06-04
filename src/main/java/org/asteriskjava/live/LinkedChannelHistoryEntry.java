package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

/**
 * An entry in the linked channels history of an {@link AsteriskChannel}.
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public class LinkedChannelHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date dateLinked;
    private Date dateUnlinked;
    private final AsteriskChannel channel;

    /**
     * Creates a new instance.
     * 
     * @param dateLinked the date the channel was linked.
     * @param channel the channel that has been linked.
     */
    public LinkedChannelHistoryEntry(Date dateLinked, AsteriskChannel channel)
    {
        this.dateLinked = dateLinked;
        this.channel = channel;
    }

    /**
     * Returns the date the channel was linked.
     * 
     * @return the date the channel was linked.
     */
    public Date getDateLinked()
    {
        return dateLinked;
    }

    /**
     * Returns the date the channel was unlinked.
     * 
     * @return the date the channel was unlinked.
     */
    public Date getDateUnlinked()
    {
        return dateUnlinked;
    }

    /**
     * Sets the date the channel was unlinked.
     * 
     * @param dateUnlinked the date the channel was unlinked.
     */
    public void setDateUnlinked(Date dateUnlinked)
    {
        this.dateUnlinked = dateUnlinked;
    }

    /**
     * Returns the channel that has been linked.
     * 
     * @return the channel that has been linked.
     */
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
