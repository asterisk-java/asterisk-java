package org.asteriskjava.pbx.internal.asterisk;

import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.PBXFactory;

public class CallerIDImpl implements CallerID
{
    private final String number;
    private final String name;
    private boolean hideCallerID = false;

    /**
     * Creates a caller ID.
     * 
     * @param number - if null then we store an empty string.
     * @param name - if null then we store an empty string.
     */
    public CallerIDImpl(final String number, final String name)
    {
        this.number = (number == null ? "" : number.trim()); //$NON-NLS-1$
        this.name = (name == null ? "" : name.trim()); //$NON-NLS-1$
    }

    @Override
    public String getNumber()
    {
        return this.number;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    /**
     * This is a little helper class which will buid the name component of a
     * clid from the first and lastnames. If both firstname and lastname are
     * null then the name component will be an empty string.
     * 
     * @param firstname the person's firstname, may be null.
     * @param lastname the person's lastname, may be null
     * @param number the phone number.
     * @return
     */
    public static CallerID buildFromComponents(final String firstname, final String lastname, final String number)
    {
        String name = ""; //$NON-NLS-1$
        if (firstname != null)
        {
            name += firstname.trim();
        }

        if (lastname != null)
        {
            if (name.length() > 0)
            {
                name += " "; //$NON-NLS-1$
            }
            name += lastname.trim();
        }

        return PBXFactory.getActivePBX().buildCallerID(number, name);
    }

    /**
     * Formats and returns the caller ID in an asterisk specific format suitable
     * for passing to the likes of the originate action. The format is: name
     * <number>
     * 
     * @return a formatted clid.
     */
    public String formatted()
    {
        String callerID = ""; //$NON-NLS-1$
        if (this.name != null)
        {
            callerID += this.name;
        }
        if (this.number != null)
        {
            if (callerID.length() > 1)
            {
                callerID += " "; //$NON-NLS-1$
            }
            callerID += "<" + this.number + ">"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return callerID;
    }

    @Override
    public void setHideCallerID(final boolean hide)
    {
        this.hideCallerID = hide;

    }

    @Override
    public boolean isHideCallerID()
    {
        return this.hideCallerID;
    }

    @Override
    public String toString()
    {
        return this.formatted();
    }

    @Override
    public boolean isUnknown()
    {
        return (this.number.length() == 0) || (this.number.compareToIgnoreCase("unknown") == 0); //$NON-NLS-1$
    }
}
