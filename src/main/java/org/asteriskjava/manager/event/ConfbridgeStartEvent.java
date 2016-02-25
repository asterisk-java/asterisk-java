package org.asteriskjava.manager.event;

/**
 * This event is sent when the first user requests a conference and it is
 * instantiated.
 *
 * @since 1.0.0
 */
public class ConfbridgeStartEvent extends AbstractConfbridgeEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    private String conference;

    public ConfbridgeStartEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the id of the conference started.
     *
     * @param conference the id of the conference started.
     */
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference started.
     *
     * @return the id of the conference started.
     */
    public String getConference()
    {
        return conference;
    }
}
