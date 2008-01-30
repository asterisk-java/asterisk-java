package org.asteriskjava.manager.event;

/**
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
public class AgiExecEvent extends AbstractAgiEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    private String command;
    private Integer resultCode;

    /**
     * Creates a new AgiExecEvent.
     *
     * @param source
     */
    public AgiExecEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the AGI command.
     *
     * @return the AGI command.
     */
    public String getCommand()
    {
        return command;
    }

    /**
     * Sets the AGI command.
     *
     * @param command the AGI command.
     */
    public void setCommand(String command)
    {
        this.command = command;
    }

    public Integer getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(Integer resultCode)
    {
        this.resultCode = resultCode;
    }
}