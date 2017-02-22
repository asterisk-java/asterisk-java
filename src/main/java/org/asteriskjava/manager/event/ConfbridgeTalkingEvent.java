package org.asteriskjava.manager.event;

/**
 * This event is sent when the conference detects that a user has either begin
 * or stopped talking.
 *
 * @since 1.0.0
 */
public class ConfbridgeTalkingEvent extends AbstractConfbridgeEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    private Boolean talkingStatus;
    
    public ConfbridgeTalkingEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the talking status on or off.
     *
     * @param talkingStatus the talking status
     */
    public void setTalkingStatus(Boolean talkingStatus)
    {
        this.talkingStatus = talkingStatus;
    }

    /**
     * Returns the talking status.
     *
     * @return <code>true</code> if the participant started talking,
     *         <code>false</code> if the participant stopped talking.
     */
    public Boolean getTalkingStatus()
    {
        return talkingStatus;
    }
    
}
