package org.asteriskjava.fastagi.command;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class DialCommand extends AbstractAgiCommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3762248656229053753L;
    private String options;
    private String target;
    private int timeout;
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Creates a new AnswerCommand.
     */
    public DialCommand(String target, int timeout, String options)
    {
        super();
        this.target = target;
        this.timeout = timeout;
        this.options = options;
    }

    @Override
    public String buildCommand()
    {

        String separator = "|";
        if (getAsteriskVersion().isAtLeast(AsteriskVersion.ASTERISK_10))
        {
            separator = ",";
        }

        String command = "EXEC " + escapeAndQuote("dial") + " " + escapeAndQuote(target) + separator
                + escapeAndQuote("" + timeout);
        if (options != null && options.length() > 0)
        {
            command += separator + escapeAndQuote(options);
        }

        logger.info(command);

        return command;

    }
}
