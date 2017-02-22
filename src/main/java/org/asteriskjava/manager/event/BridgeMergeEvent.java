package org.asteriskjava.manager.event;

public class BridgeMergeEvent extends ManagerEvent
{

    private static final long serialVersionUID = 1L;

    public BridgeMergeEvent(Object source)
    {
        super(source);
    }
    
    private Integer fromBridgeNumChannels; 
    private Integer toBridgeNumChannels; 
    private String fromBridgeName; 
    private String fromBridgeUniqueId;
    private String fromBridgeCreator; 
    private String toBridgeName; 
    private String fromBridgeTechnology; 
    private String toBridgeUniqueId; 
    private String toBridgeTechnology; 
    private String fromBridgeType; 
    private String toBridgeType; 
    private String toBridgeCreator;

    public Integer getFromBridgeNumChannels()
    {
        return fromBridgeNumChannels;
    }
    public void setFromBridgeNumChannels(Integer fromBridgeNumChannels)
    {
        this.fromBridgeNumChannels = fromBridgeNumChannels;
    }
    public Integer getToBridgeNumChannels()
    {
        return toBridgeNumChannels;
    }
    public void setToBridgeNumChannels(Integer toBridgeNumChannels)
    {
        this.toBridgeNumChannels = toBridgeNumChannels;
    }
    public String getFromBridgeName()
    {
        return fromBridgeName;
    }
    public void setFromBridgeName(String fromBridgeName)
    {
        this.fromBridgeName = fromBridgeName;
    }
    public String getFromBridgeUniqueId()
    {
        return fromBridgeUniqueId;
    }
    public void setFromBridgeUniqueId(String fromBridgeUniqueId)
    {
        this.fromBridgeUniqueId = fromBridgeUniqueId;
    }
    public String getFromBridgeCreator()
    {
        return fromBridgeCreator;
    }
    public void setFromBridgeCreator(String fromBridgeCreator)
    {
        this.fromBridgeCreator = fromBridgeCreator;
    }
    public String getToBridgeName()
    {
        return toBridgeName;
    }
    public void setToBridgeName(String toBridgeName)
    {
        this.toBridgeName = toBridgeName;
    }
    public String getFromBridgeTechnology()
    {
        return fromBridgeTechnology;
    }
    public void setFromBridgeTechnology(String fromBridgeTechnology)
    {
        this.fromBridgeTechnology = fromBridgeTechnology;
    }
    public String getToBridgeUniqueId()
    {
        return toBridgeUniqueId;
    }
    public void setToBridgeUniqueId(String toBridgeUniqueId)
    {
        this.toBridgeUniqueId = toBridgeUniqueId;
    }
    public String getToBridgeTechnology()
    {
        return toBridgeTechnology;
    }
    public void setToBridgeTechnology(String toBridgeTechnology)
    {
        this.toBridgeTechnology = toBridgeTechnology;
    }
    public String getFromBridgeType()
    {
        return fromBridgeType;
    }
    public void setFromBridgeType(String fromBridgeType)
    {
        this.fromBridgeType = fromBridgeType;
    }
    public String getToBridgeType()
    {
        return toBridgeType;
    }
    public void setToBridgeType(String toBridgeType)
    {
        this.toBridgeType = toBridgeType;
    }
    public String getToBridgeCreator()
    {
        return toBridgeCreator;
    }
    public void setToBridgeCreator(String toBridgeCreator)
    {
        this.toBridgeCreator = toBridgeCreator;
    }
    
    
}
