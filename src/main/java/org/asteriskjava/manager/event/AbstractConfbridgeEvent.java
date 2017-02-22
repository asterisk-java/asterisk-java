package org.asteriskjava.manager.event;

public abstract class AbstractConfbridgeEvent extends AbstractChannelEvent
{
    private static final long serialVersionUID = 3893862793876858636L;
    private String bridgeType;
    private String bridgeNumChannels;
    private String bridgeUniqueId;
    private String bridgeTechnology;
    private String bridgeName;
    private String bridgeCreator;
    private String conference;

    String admin;

    public AbstractConfbridgeEvent(Object source)
    {
        super(source);
    }

    /**
     * @return the admin
     */
    public String getAdmin()
    {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(String admin)
    {
        this.admin = admin;
    }

    /**
     * @return the bridgeType
     */
    public String getBridgeType()
    {
        return bridgeType;
    }

    /**
     * @param bridgeType the bridgeType to set
     */
    public void setBridgeType(String bridgeType)
    {
        this.bridgeType = bridgeType;
    }

    /**
     * @return the bridgeNumChannels
     */
    public String getBridgeNumChannels()
    {
        return bridgeNumChannels;
    }

    /**
     * @param bridgeNumChannels the bridgeNumChannels to set
     */
    public void setBridgeNumChannels(String bridgeNumChannels)
    {
        this.bridgeNumChannels = bridgeNumChannels;
    }

    /**
     * @return the bridgeUniqueId
     */
    public String getBridgeUniqueId()
    {
        return bridgeUniqueId;
    }

    /**
     * @param bridgeUniqueId the bridgeUniqueId to set
     */
    public void setBridgeUniqueId(String bridgeUniqueId)
    {
        this.bridgeUniqueId = bridgeUniqueId;
    }

    /**
     * @return the bridgeTechnology
     */
    public String getBridgeTechnology()
    {
        return bridgeTechnology;
    }

    /**
     * @param bridgeTechnology the bridgeTechnology to set
     */
    public void setBridgeTechnology(String bridgeTechnology)
    {
        this.bridgeTechnology = bridgeTechnology;
    }

    /**
     * @return the bridgeName
     */
    public String getBridgeName()
    {
        return bridgeName;
    }

    /**
     * @param bridgeName the bridgeName to set
     */
    public void setBridgeName(String bridgeName)
    {
        this.bridgeName = bridgeName;
    }

    /**
     * @return the bridgeCreator
     */
    public String getBridgeCreator()
    {
        return bridgeCreator;
    }

    /**
     * @param bridgeCreator the bridgeCreator to set
     */
    public void setBridgeCreator(String bridgeCreator)
    {
        this.bridgeCreator = bridgeCreator;
    }

    /**
     * Sets the id of the conference the participant left.
     *
     * @param conference the id of the conference the participant left.
     */
    public final void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference the participant left.
     *
     * @return the id of the conference the participant left.
     */
    public final String getConference()
    {
        return conference;
    }
    
}
