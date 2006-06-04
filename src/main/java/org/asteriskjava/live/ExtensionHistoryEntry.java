package org.asteriskjava.live;

import java.io.Serializable;
import java.util.Date;

public class ExtensionHistoryEntry implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5437551192335452460L;
    private final Date date;
    private final Extension extension;

    public ExtensionHistoryEntry(Date date, Extension extension)
    {
        this.date = date;
        this.extension = extension;
    }

    public Date getDate()
    {
        return date;
    }

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
