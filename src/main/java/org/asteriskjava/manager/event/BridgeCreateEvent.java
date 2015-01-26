package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BridgeCreateEvent extends ManagerEvent {
    private String bridgeUniqueId;
    private String privilege;
    private String bridgeType;
    private Integer bridgeNumChannels;
    private String bridgeCreator;
    private String bridgeName;

    public BridgeCreateEvent (Object source) { super(source); }

    public String getBridgeUniqueId() {
        return bridgeUniqueId;
    }

    public void setBridgeUniqueId(String bridgeUniqueId) {
        this.bridgeUniqueId = bridgeUniqueId;
    }

    @Override
    public String getPrivilege() {
        return privilege;
    }

    @Override
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
    }

    public Integer getBridgeNumChannels() {
        return bridgeNumChannels;
    }

    public void setBridgeNumChannels(Integer bridgeNumChannels) {
        this.bridgeNumChannels = bridgeNumChannels;
    }

    public String getBridgeCreator() {
        return bridgeCreator;
    }

    public void setBridgeCreator(String bridgeCreator) {
        this.bridgeCreator = bridgeCreator;
    }

    public String getBridgeName() {
        return bridgeName;
    }

    public void setBridgeName(String bridgeName) {
        this.bridgeName = bridgeName;
    }
}
