package org.asteriskjava.manager.event;

/**
 * This event is sent when the last user leaves a conference and it is torn
 * down.
 *
 * @since 1.0.0
 */
public class ConfbridgeEndEvent extends AbstractConfbridgeEvent
{

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -8973512592594074108L;
    private String conference;

    public ConfbridgeEndEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the id of the conference ended.
     *
     * @param conference the id of the conference ended.
     */
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference ended.
     *
     * @return the id of the conference ended.
     */
    public String getConference()
    {
        return conference;
    }

}
