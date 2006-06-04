package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

/**
 * An entry in the extension history of an {@link AsteriskChannel}.
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public class ExtensionHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date date;
    private final Extension extension;

    /**
     * Creates a new instance.
     * 
     * @param date the date the extension has been visited.
     * @param extension the extension that has been visited.
     */
    public ExtensionHistoryEntry(Date date, Extension extension)
    {
        this.date = date;
        this.extension = extension;
    }

    /**
     * Returns the date the extension has been visited.
     * @return the date the extension has been visited.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Returns the extension that has been visited.
     * @return the extension that has been visited.
     */
    public Extension getExtension()
    {
        return extension;
    }

    public String toString()
    {
        final StringBuffer sb;

        sb = new StringBuffer("ExtensionHistoryEntry[");
        sb.append("date=" + date + ",");
        sb.append("extension=" + extension + "]");
        return sb.toString();
    }
}
