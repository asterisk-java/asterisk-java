package org.asteriskjava.manager.event;

/**
 * This event is sent when a user joins a conference - either one already in
 * progress or as the first user to join a newly instantiated bridge.
 *
 * @since 1.0.0
 */
public class ConfbridgeJoinEvent extends AbstractConfbridgeEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    String muted;

    public ConfbridgeJoinEvent(Object source)
    {
        super(source);
    }

    public String getMuted()
    {
        return muted;
    }

    public void setMuted(String muted)
    {
        this.muted = muted;
    }

}
