package org.asteriskjava.fastagi.command;

public class QueueCommand extends AbstractAgiCommand
{

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3762248656229053753L;

    private String queue;

    /**
     * Creates a new AnswerCommand.
     */
    public QueueCommand(String queue)
    {
        super();
        this.queue = queue;

    }

    @Override
    public String buildCommand()
    {
        String command = "EXEC " + escapeAndQuote("queue") + " " + escapeAndQuote(queue);
        // if (options != null && options.length() > 0)
        // {
        // command += " " + escapeAndQuote(options);
        // }

        return command;

    }
}
