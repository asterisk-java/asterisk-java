package org.asteriskjava.fastagi.command;

public class MeetmeCommand extends AbstractAgiCommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3762248656229053753L;
    private String room;
    private String options;

    /**
     * Creates a new AnswerCommand.
     */
    public MeetmeCommand(String room, String options)
    {
        super();
        this.room = room;
        this.options = options;
    }

    @Override
    public String buildCommand()
    {
        String command = "EXEC " + escapeAndQuote("meetme") + " " + escapeAndQuote(room);
        if (options != null && options.length() > 0)
        {
            command += "|" + escapeAndQuote(options);
        }

        return command;

    }
}
