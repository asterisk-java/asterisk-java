package org.asteriskjava.manager.event;

/**
 * This event is sent when the conference detects that a user has either begin or stopped talking.
 *
 * @since 1.0.0
 */
public class ConfbridgeTalkingEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    private String conference;
    private String channel;
    private String uniqueId;
    private Boolean talkingStatus;

    public ConfbridgeTalkingEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the id of the conference.
     *
     * @param conference the id of the conference.
     */
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference.
     *
     * @return the id of the conference.
     */
    public String getConference()
    {
        return conference;
    }

    /**
     * Sets the name of the channel on which a participant started or stopped talking.
     *
     * @param channel the name of the channel on which a participant started or stopped talking.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the name of the channel on which a participant started or stopped talking.
     *
     * @return the name of the channel on which a participant started or stopped talking.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Returns the unique id of the channel on which a participant started or stopped talking.
     *
     * @return the unique id of the channel on which a participant started or stopped talking.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel on which a participant started or stopped talking.
     *
     * @param uniqueId the unique id of the channel on which a participant started or stopped talking.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
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
     * @return <code>true</code> if the participant started talking, <code>false</code> if the participant stopped talking.
     */
    public Boolean getTalkingStatus()
    {
        return talkingStatus;
    }
}
