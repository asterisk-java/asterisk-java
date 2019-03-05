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
    private String linkedID;
    private String language;

    public ConfbridgeLeaveEvent(Object source)
    {
        super(source);
    }

    public String getLinkedID() {
        return linkedID;
    }

    public void setLinkedID(String linkedID) {
        this.linkedID = linkedID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
