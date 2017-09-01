package org.asteriskjava.pbx;

/**
 * Represents an asterisk dial plan extension such as: [njr-operator]
 * njr-inbound,1,xxxxx In the above example njr-inbound is the dialplan
 * extension. This class primarily exists to stop common mistakes that can occur
 * if we pass this around as a simple string.
 * 
 * @author bsutton
 */
public class DialPlanExtension implements EndPoint
{

    // the dial plan extension.
    String _extension;
    final boolean _empty;

    public DialPlanExtension(String extension)
    {
        if (extension == null || extension.trim().length() == 0)
        {
            this._empty = true;
            this._extension = "";
        }
        else
        {
            this._extension = extension.trim();
            this._empty = false;
        }
    }

    @Override
    public int compareTo(EndPoint rhs)
    {
        return this._extension.compareTo(rhs.getSimpleName());
    }

    @Override
    public boolean isSame(EndPoint rhs)
    {
        return this.compareTo(rhs) == 0;
    }

    @Override
    public boolean isLocal()
    {
        return false;
    }

    @Override
    public boolean isSIP()
    {
        return false;
    }

    /**
     * The same as getSimpleName as in this case the Tech is really just an
     * identifier rather than a legitimate tech that the pbx understands.
     */
    @Override
    public String getFullyQualifiedName()
    {
        return this._extension;
    }

    @Override
    public String getSimpleName()
    {
        return this._extension;
    }

    @Override
    public String getSIPSimpleName()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUnknown()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public TechType getTech()
    {
        return TechType.DIALPLAN;
    }

    public String toString()
    {
        return this._extension;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }
}
