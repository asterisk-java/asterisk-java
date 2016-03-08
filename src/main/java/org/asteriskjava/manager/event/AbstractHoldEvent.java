package org.asteriskjava.manager.event;

/**
 * A HoldEvent is triggered when a channel is put on hold (or no longer on
 * hold).
 * <p>
 * It is implemented in <code>channels/chan_sip.c</code>.
 * <p>
 * Available since Asterisk 1.2 for SIP channels, as of Asterisk 1.6 this event
 * is also supported for IAX2 channels.
 * <p>
 * To receive HoldEvents for SIP channels you must set
 * <code>callevents = yes</code> in <code>sip.conf</code>.
 *
 * @author enro
 * @version $Id$
 * @since 1.0.0.10
 */
public class AbstractHoldEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the channel.
     */
    private String channel;

    private String accountCode;

    /**
     * The unique id of the channel.
     */
    private String uniqueId;

    private Boolean status;
    
    private String linkedId;
    private String language;
    

    /**
     * Creates a new HoldEvent.
     *
     * @param source
     */
    public AbstractHoldEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel.
     *
     * @return channel the name of the channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     *
     * @param channel the name of the channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the unique id of the channel.
     *
     * @return the unique id of the channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel.
     *
     * @param uniqueId the unique id of the channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /**
     * Returns whether this is a hold or unhold event.
     *
     * @return <code>true</code> if this a hold event, <code>false</code> if
     *         it's an unhold event.
     * @since 1.0.0
     */
    public Boolean getStatus()
    {
        return status;
    }

    /**
     * Returns whether this is a hold or unhold event.
     *
     * @param status <code>true</code> if this a hold event, <code>false</code>
     *            if it's an unhold event.
     * @since 1.0.0
     */
    public void setStatus(Boolean status)
    {
        this.status = status;
    }

    /**
     * Returns whether this is a hold event.
     *
     * @return <code>true</code> if this a hold event, <code>false</code> if
     *         it's an unhold event.
     * @since 1.0.0
     */
    public boolean isHold()
    {
        return status != null && status;
    }

    /**
     * Returns whether this is an unhold event.
     *
     * @return <code>true</code> if this an unhold event, <code>false</code> if
     *         it's a hold event.
     * @since 1.0.0
     */
    public boolean isUnhold()
    {
        return status != null && !status;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }
}
