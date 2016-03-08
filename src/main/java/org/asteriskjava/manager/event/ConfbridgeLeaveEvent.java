package org.asteriskjava.manager.event;

/**
 * This event is sent when a user leaves a conference.
 *
 * @since 1.0.0
 */
public class ConfbridgeLeaveEvent extends AbstractConfbridgeEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;
    private String conference;

    public ConfbridgeLeaveEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the id of the conference the participant left.
     *
     * @param conference the id of the conference the participant left.
     */
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference the participant left.
     *
     * @return the id of the conference the participant left.
     */
    public String getConference()
    {
        return conference;
    }
}
