package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public abstract class AbstractBridgeEvent extends ManagerEvent
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String bridgeUniqueId;
    private String bridgeType;
    private Integer bridgeNumChannels;
    private String bridgeCreator;
    private String bridgeName;
    private String bridgeTechnology;
    private String accountCode;

    AbstractBridgeEvent(Object source)
    {
        super(source);
    }

    public String getBridgeUniqueId()
    {
        return bridgeUniqueId;
    }

    public void setBridgeUniqueId(String bridgeUniqueId)
    {
        this.bridgeUniqueId = bridgeUniqueId;
    }

    public String getBridgeType()
    {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType)
    {
        this.bridgeType = bridgeType;
    }

    public Integer getBridgeNumChannels()
    {
        return bridgeNumChannels;
    }

    public void setBridgeNumChannels(Integer bridgeNumChannels)
    {
        this.bridgeNumChannels = bridgeNumChannels;
    }

    public String getBridgeCreator()
    {
        return bridgeCreator;
    }

    public void setBridgeCreator(String bridgeCreator)
    {
        this.bridgeCreator = bridgeCreator;
    }

    public String getBridgeName()
    {
        return bridgeName;
    }

    public void setBridgeName(String bridgeName)
    {
        this.bridgeName = bridgeName;
    }

    public String getBridgeTechnology()
    {
        return bridgeTechnology;
    }

    public void setBridgeTechnology(String bridgeTechnology)
    {
        this.bridgeTechnology = bridgeTechnology;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }
}
