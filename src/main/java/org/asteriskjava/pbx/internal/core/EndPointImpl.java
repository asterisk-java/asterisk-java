package org.asteriskjava.pbx.internal.core;

import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.pbx.Trunk;

public class EndPointImpl implements EndPoint
{
    private static final String DELIMITER = "/"; //$NON-NLS-1$

    final private TechType _tech;

    final private String _endPointName;

    final private String _fullyQualifiedName;

    // If true then this is an empty end point e.g. blank name and unknown tech.
    final private boolean _empty;

    private Trunk _trunk;

    /**
     * Creates a EndPoint from a fully qualified peer name of the form: SIP/NNN
     * LOCAL/NNN TODO: the tech encoding should probably be specific to a
     * particular pbx Implementation.
     * 
     * @param fullyQualifiedPeerName
     */
    public EndPointImpl(final String fullyQualifiedEndPoint)
    {
        if (fullyQualifiedEndPoint == null)
        {
            throw new IllegalArgumentException("The fullyQualifiedEndPoint may not be null"); //$NON-NLS-1$
        }

        final String cleaned = this.clean(fullyQualifiedEndPoint);
        this._tech = TechType.getTech(cleaned);
        this._endPointName = cleaned.substring(this._tech.name().length() + 1);

        if (this._tech == TechType.DIALPLAN)
        {
            this._fullyQualifiedName = this._endPointName;
        }
        else
        {
            this._fullyQualifiedName = this._tech.name() + EndPointImpl.DELIMITER + this._endPointName;
        }
        this._empty = false;
    }

    /**
     * Creates a empty end point.
     */
    public EndPointImpl()
    {
        this._empty = true;
        this._tech = TechType.UNKNOWN;
        this._endPointName = "";
        this._fullyQualifiedName = "";
    }

    /**
     * Creates an EndPoint from from a simple endpoint name and a tech.
     * 
     * @param _tech
     * @param peerName
     */
    // EndPoint(String endPointName, String tech)
    // {
    // if (endPointName == null)
    // throw new IllegalArgumentException("The endPoint Name may not be null");
    //
    // if (tech == null)
    // throw new IllegalArgumentException("The endPoint Tehc may not be null");
    //
    // String cleaned = clean(endPointName);
    // // Tech must be upper case to match the enum but
    // // the simple name must be lower case to matcht the context extension
    // // names.
    // this.tech = Tech.valueOf(tech.toUpperCase());
    // this.endPointName = cleaned;
    // }

    public EndPointImpl(final TechType defaultTech, final String endPointName)
    {
        if (endPointName == null)
        {
            throw new IllegalArgumentException("The endPointName may not be null"); //$NON-NLS-1$
        }

        final String cleaned = this.clean(endPointName);

        if (cleaned.length() == 0)
        {
            throw new IllegalArgumentException("The endPointName may not be empty"); //$NON-NLS-1$
        }

        // If tech is unknown then attempt to extract the tech from the
        // endPointName.
        if (TechType.hasTech(cleaned))
        {
            this._tech = TechType.getTech(cleaned);
            this._endPointName = cleaned.substring(this._tech.name().length() + 1);

        }
        else
        {
            this._tech = defaultTech;
            this._endPointName = cleaned;
        }

        if (this._tech == TechType.DIALPLAN)
        {
            this._fullyQualifiedName = this._endPointName;
        }
        else
        {
            this._fullyQualifiedName = this._tech.name() + EndPointImpl.DELIMITER + this._endPointName;
        }
        this._empty = false;
    }

    public EndPointImpl(TechType defaultTech, Trunk trunk, String endPointName)
    {
        if (endPointName == null)
        {
            throw new IllegalArgumentException("The endPointName may not be null"); //$NON-NLS-1$
        }

        final String cleaned = this.clean(endPointName);

        if (cleaned.length() == 0)
        {
            throw new IllegalArgumentException("The endPointName may not be empty"); //$NON-NLS-1$
        }

        // If tech is unknown then attempt to extract the tech from the
        // endPointName.
        if (TechType.hasTech(cleaned))
        {
            throw new IllegalArgumentException("endPointName may not contain a Tech");

        }

        this._tech = defaultTech;
        this._endPointName = cleaned;
        this._trunk = trunk;

        if (this._tech == TechType.DIALPLAN || this._tech == TechType.CONSOLE || this._tech == TechType.LOCAL)
        {
            throw new IllegalArgumentException("defaultTech may not be DIALPLAN, LOCAL or CONSOLE");
        }

        this._fullyQualifiedName = this._tech.name() + EndPointImpl.DELIMITER + trunk.getTrunkAsString()
                + EndPointImpl.DELIMITER + this._endPointName;

        this._empty = false;

    }

    /**
     * Returns the fully qualified endPoint.
     */
    @Override
    public String getFullyQualifiedName()
    {
        return this._fullyQualifiedName;
    }

    public Trunk getTrunk()
    {
        return _trunk;
    }

    @Override
    public boolean isSame(final EndPoint rhs)
    {
        return this.compareTo(rhs) == 0;
    }

    @Override
    public int compareTo(EndPoint rhs)
    {
        return this.getFullyQualifiedName().compareTo(rhs.getFullyQualifiedName());
    }

    @Override
    public boolean isLocal()
    {
        return this._tech == TechType.LOCAL;
    }

    /**
     * For the purposes of asterisk IAX and SIP are both considered SIP.
     */
    @Override
    public boolean isSIP()
    {
        return this._tech == TechType.SIP || this._tech == TechType.IAX || this._tech == TechType.IAX2;
    }

    public boolean isDAHDI()
    {
        return this._tech == TechType.DAHDI;
    }

    @Override
    public String getSimpleName()
    {
        return this._endPointName;
    }

    @Override
    public boolean isUnknown()
    {
        return this._tech == TechType.UNKNOWN;
    }

    /**
     * Cleans up an endpoint name by removing all whitespace (including internal
     * whitespace) as well as making the string lower case.
     * 
     * @param _endPointName
     * @return
     */
    private String clean(final String _endPointName)
    {
        String localTo = _endPointName.trim();

        // strip all white space from within the name as some source
        // pass in a formatted phone number (e.g. 03 8320 8100)
        int tmp = localTo.indexOf(" "); //$NON-NLS-1$
        while (tmp != -1)
        {
            localTo = localTo.substring(0, tmp) + localTo.substring(tmp + 1, localTo.length());
            tmp = localTo.indexOf(" "); //$NON-NLS-1$
        }
        return localTo.toLowerCase();
    }

    @Override
    public TechType getTech()
    {
        return this._tech;
    }

    @Override
    public String toString()
    {
        return this.getFullyQualifiedName();
    }

    @Override
    public String getSIPSimpleName()
    {
        String name;

        if (isSIP())
            name = getSimpleName();
        else
            name = getFullyQualifiedName();
        return name;
    }

    public boolean isEmpty()
    {
        return this._empty;
    }

}
