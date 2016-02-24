package org.asteriskjava.fastagi.command;

public class BridgeCommand extends AbstractAgiCommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3762248656229053753L;
    private String options;
    private String channel;

    /**
     * Creates a new AnswerCommand.
     */
    public BridgeCommand(String channel, String options)
    {
        super();
        this.channel = channel;
        this.options = options;
    }

    @Override
    public String buildCommand()
    {
        String command = "EXEC " + escapeAndQuote("bridge") + " " + escapeAndQuote(channel);
        if (options != null && options.length() > 0)
        {
            command += " " + escapeAndQuote(options);
        }

        System.out.println(command);

        return command;

    }
}
