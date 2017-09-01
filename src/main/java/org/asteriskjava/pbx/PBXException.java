package org.asteriskjava.pbx;

public class PBXException extends Exception
{
    private static final long serialVersionUID = 1L;

    public PBXException(String message, final Exception e)
    {
        super(message, e);
    }

    public PBXException(final String message)
    {
        super(message);
    }

    public PBXException(Throwable e)
    {
        super(e);
    }
}
