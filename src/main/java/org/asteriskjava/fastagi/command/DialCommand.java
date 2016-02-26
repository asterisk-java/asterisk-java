package org.asteriskjava.fastagi.command;

public class DialCommand extends AbstractAgiCommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3762248656229053753L;
    private String options;
    private String target;
    private int timeout;

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
        String command = "EXEC " + escapeAndQuote("dial") + " " + escapeAndQuote(target) + " "
                + escapeAndQuote("" + timeout);
        if (options != null && options.length() > 0)
        {
            command += " " + escapeAndQuote(options);
        }

        System.out.println(command);

        return command;

    }
}
