package org.asteriskjava.fastagi.command;

import org.asteriskjava.AsteriskVersion;

public class ConfbridgeCommand extends AbstractAgiCommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3762248656229053753L;
    private String room;
    private String profile;

    /**
     * Creates a new AnswerCommand.
     */
    public ConfbridgeCommand(String room, String profile)
    {
        super();
        this.room = room;
        this.profile = profile;
    }

    @Override
    public String buildCommand()
    {
        String separator = "|";
        if (getAsteriskVersion().isAtLeast(AsteriskVersion.ASTERISK_10))
        {
            separator = ",";
        }

        String command = "EXEC " + escapeAndQuote("confbridge") + " " + escapeAndQuote(room);
        if (profile != null && profile.length() > 0)
        {
            command += separator + escapeAndQuote(profile);
        }

        return command;

    }
}
