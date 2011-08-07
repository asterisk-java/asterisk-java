package org.asteriskjava.manager.event;

/**
 * This event is sent when a user joins a conference - either one already in progress or as the first user to
 * join a newly instantiated bridge.
 *
 * @since 1.0.0
 */
public class ConfbridgeJoinEvent extends AbstractChannelEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;
    private String conference;

    public ConfbridgeJoinEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the id of the conference the participant joined.
     *
     * @param conference id of the conference the participant joined.
     */
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference the participant joined.
     *
     * @return id of the conference the participant joined.
     */
    public String getConference()
    {
        return conference;
    }
}
