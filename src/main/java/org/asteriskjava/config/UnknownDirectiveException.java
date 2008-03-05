package org.asteriskjava.config;

/**
 * An unknown directive has been encountered.<p>
 * Asterisk only supports #include and #exec directives.
 */
public class UnknownDirectiveException extends ConfigParseException
{
    public UnknownDirectiveException(String filename, int lineno, String format, Object... params)
    {
        super(filename, lineno, format, params);
    }
}
