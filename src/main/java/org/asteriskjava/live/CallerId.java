package org.asteriskjava.live;

import java.io.Serializable;

import org.asteriskjava.util.AstUtil;

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

    /**
     * Parses a caller id string in the form
     * <code>"Some Name" &lt;1234&gt;</code> to a CallerId object.
     * 
     * @param s the caller id string to parse.
     * @return the corresponding CallerId object which is never <code>null</code>.
     * @see AstUtil#parseCallerId(String)
     */
    public static CallerId valueOf(String s)
    {
        final String[] parsedCallerId;
        
        parsedCallerId = AstUtil.parseCallerId(s);
        return new CallerId(parsedCallerId[0], parsedCallerId[1]);
    }

    /**
     * Returns a string representation of this CallerId in the form
     * <code>"Some Name" &lt;1234&gt;</code>.
     */
    @Override
    public String toString()
    {
        final StringBuffer sb;
        
        sb = new StringBuffer();
        if (name != null)
        {
            sb.append("\"");
            sb.append(name);
            sb.append("\"");
            if (number != null)
            {
                sb.append(" ");
            }
        }
        
        if (number != null)
        {
            sb.append("<");
            sb.append(number);
            sb.append(">");
        }
        return  sb.toString();
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((number == null) ? 0 : number.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CallerId other = (CallerId) obj;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (number == null)
        {
            if (other.number != null)
                return false;
        }
        else if (!number.equals(other.number))
            return false;
        return true;
    }
}
