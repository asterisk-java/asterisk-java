package org.asteriskjava.config;

/**
 * A required parameter to a directive is missing.<p>
 * The #include and #exec directives require a parameter (include file or command to execute).
 */
public class MissingDirectiveParameterException extends ConfigParseException
{
    public MissingDirectiveParameterException(String filename, int lineno, String format, Object... params)
    {
        super(filename, lineno, format, params);
    }
}