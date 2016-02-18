package org.asteriskjava.fastagi.command;

/**
 * Calls a subroutine from dialplan.
 * <br>
 * Example return code: 200 result=0 Gosub complete
 * <br>
 * This command is available since Asterisk 1.6.
 *
 * @author fadishei
 * @since 1.0.0
 */
public class GosubCommand extends AbstractAgiCommand
{
    private static final long serialVersionUID = 1L;

    /**
     * the context of the called subroutine.
     */
    private String context;

    /**
     * the extension in the called context.
     */
    private String extension;

    /**
     * the priority of the called extension.
     */
    private String priority;

    /**
     * an optional list of arguments to be passed to the subroutine.
     * They will accessible in the form of ${ARG1}, ${ARG2}, etc in the subroutine body.
     */
    private String[] arguments;

    /**
     * Creates a new GosubCommand.
     *
     * @param context   context of the called subroutine.
     * @param extension the extension in the called context.
     * @param priority  of the called extension.
     */
    public GosubCommand(String context, String extension, String priority)
    {
        super();
        this.context = context;
        this.extension = extension;
        this.priority = priority;
    }

    /**
     * Creates a new GosubCommand.
     *
     * @param context   context of the called subroutine.
     * @param extension the extension in the called context.
     * @param priority  the priority of the called extension.
     * @param arguments the arguments to be passed to the called subroutine.
     */
    public GosubCommand(String context, String extension, String priority, String... arguments)
    {
        super();
        this.context = context;
        this.extension = extension;
        this.priority = priority;
        this.arguments = arguments;
    }

    /**
     * Returns the context of the subroutine to call.
     *
     * @return the context of the subroutine to call.
     */
    public String getContext()
    {
        return context;
    }

    /**
     * Sets the context of the subroutine to call.
     *
     * @param context the context of the subroutine to call.
     */
    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * Returns the extension within the called context.
     *
     * @return the extension within the called context.
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * Sets the extension within the called context.
     *
     * @param extension the extension within the called context.
     */
    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    /**
     * Returns the priority of the called extension.
     *
     * @return the priority of the called extension.
     */
    public String getPriority()
    {
        return priority;
    }

    /**
     * Sets the priority of the called extension.
     *
     * @param priority the priority of the called extension.
     */
    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    /**
     * Returns the arguments to be passed to the subroutine.
     *
     * @return the arguments to be passed to the subroutine.
     */
    public String[] getArguments()
    {
        return arguments;
    }

    /**
     * Sets the arguments to be passed to the subroutine.
     *
     * @param arguments the arguments to be passed to the subroutine.
     */
    public void setArguments(String[] arguments)
    {
        this.arguments = arguments;
    }

    @Override
    public String buildCommand()
    {
        final StringBuilder sb = new StringBuilder("GOSUB ");
        sb.append(escapeAndQuote(context)).append(" ");
        sb.append(escapeAndQuote(extension)).append(" ");
        sb.append(escapeAndQuote(priority));

        if (arguments != null)
        {
            sb.append(" ").append(escapeAndQuote(arguments));
        }

        return sb.toString();
    }
}
