package org.asteriskjava.config;

/**
 * The equal sign on a variable assignment line is missing.<p>
 * A variable line must include an equal sign.
 */
public class MissingEqualSignException extends ConfigParseException
{
    public MissingEqualSignException(String filename, int lineno, String format, Object... params)
    {
        super(filename, lineno, format, params);
    }
}