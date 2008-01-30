package org.asteriskjava.manager.event;

/**
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
public class AsyncAgiEvent extends AbstractAgiEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    private String env;

    /**
     * Creates a new AsyncAgiEvent.
     *
     * @param source
     */
    public AsyncAgiEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the AGI environment similar to the AGI request for FastAGI.<p>
     * This property is only available for the "Start" sub event.
     *
     * @return the AGI environment.
     */
    public String getEnv()
    {
        return env;
    }

    /**
     * Sets the AGI environment.
     *
     * @param env the AGI environment.
     */
    public void setEnv(String env)
    {
        this.env = env;
    }
}
