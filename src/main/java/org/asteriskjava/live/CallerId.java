package org.asteriskjava.live;

import java.io.Serializable;

/**
 * Represents a Caller*ID containing name and number.
 * <p>
 * Objects of this type are immutable.
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public class CallerId implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 6498024163374551005L;
    private final String name;
    private final String number;

    /**
     * Creates a new CallerId.
     * 
     * @param name the Caller*ID name.
     * @param number the Caller*ID number.
     */
    public CallerId(String name, String number)
    {
        this.number = number;
        this.name = name;
    }

    /**
     * Returns the Caller*ID name.
     * 
     * @return the Caller*ID name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the the Caller*ID number.
     * 
     * @return the Caller*ID number.
     */
    public String getNumber()
    {
        return number;
    }

    @Override
    public String toString()
    {
        return "CallerId[name='" + name + "',number=" + number + "']";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null && !(obj instanceof CallerId))
            return false;

        CallerId o = (CallerId) obj;

        if (o.name == null)
            return name == null;

        if (o.number == null)
            return number == null;

        return o.name.equals(name) && o.number.equals(number);
    }
}
