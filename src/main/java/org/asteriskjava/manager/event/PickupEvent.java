package org.asteriskjava.manager.event;

public class PickupEvent extends ManagerEvent {

    private String channel;
    private String targetchannel;
    private String accountCode;
    private String targetLinkedID;
    private String targetCallerIDNum;

    private String targetLanguage;
    private String targetPriority;
    private String targetAccountCode;
    private String targetContext;
    private String targetChannelDtate;
    private String uniqueID;

    private String targetConnectedLineNum;
    private String targetConnectedLineName;
    private String targetExten;
    private String targetUniqueID;
    private String targetCallerIDName;
    private String language;
    private String targetChannelStateDesc;
    private String targetChannelCtate;
    private String linkedID;

    public PickupEvent(Object source) {
        super(source);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTargetchannel() {
        return targetchannel;
    }

    public void setTargetchannel(String targetchannel) {
        this.targetchannel = targetchannel;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getTargetCallerIDNum() {
        return targetCallerIDNum;
    }

    public void setTargetCallerIDNum(String targetCallerIDNum) {
        this.targetCallerIDNum = targetCallerIDNum;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getTargetPriority() {
        return targetPriority;
    }

    public void setTargetPriority(String targetPriority) {
        this.targetPriority = targetPriority;
    }

    public String getTargetAccountCode() {
        return targetAccountCode;
    }

    public void setTargetAccountCode(String targetAccountCode) {
        this.targetAccountCode = targetAccountCode;
    }

    public String getTargetContext() {
        return targetContext;
    }

    public void setTargetContext(String targetContext) {
        this.targetContext = targetContext;
    }

    public String getTargetChannelDtate() {
        return targetChannelDtate;
    }

    public void setTargetChannelDtate(String targetChannelDtate) {
        this.targetChannelDtate = targetChannelDtate;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getTargetConnectedLineNum() {
        return targetConnectedLineNum;
    }

    public void setTargetConnectedLineNum(String targetConnectedLineNum) {
        this.targetConnectedLineNum = targetConnectedLineNum;
    }

    public String getTargetExten() {
        return targetExten;
    }

    public void setTargetExten(String targetExten) {
        this.targetExten = targetExten;
    }

    public String getTargetUniqueID() {
        return targetUniqueID;
    }

    public void setTargetUniqueID(String targetUniqueID) {
        this.targetUniqueID = targetUniqueID;
    }

    public String getTargetCallerIDName() {
        return targetCallerIDName;
    }

    public void setTargetCallerIDName(String targetCallerIDName) {
        this.targetCallerIDName = targetCallerIDName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTargetChannelStateDesc() {
        return targetChannelStateDesc;
    }

    public void setTargetChannelStateDesc(String targetChannelStateDesc) {
        this.targetChannelStateDesc = targetChannelStateDesc;
    }

    public String getLinkedID() {
        return linkedID;
    }

    public void setLinkedID(String linkedID) {
        this.linkedID = linkedID;
    }

    public String getTargetChannelState() {
        return targetChannelState;
    }

    public void setTargetChannelState(String targetChannelState) {
        this.targetChannelState = targetChannelState;
    }
    private String targetChannelState;

    public String getTargetLinkedID() {
        return targetLinkedID;
    }

    public void setTargetLinkedID(String targetLinkedID) {
        this.targetLinkedID = targetLinkedID;
    }

    public String getTargetChannelCtate() {
        return targetChannelCtate;
    }

    public void setTargetChannelCtate(String targetChannelCtate) {
        this.targetChannelCtate = targetChannelCtate;
    }

    public String getTargetConnectedLineName() {
        return targetConnectedLineName;
    }

    public void setTargetConnectedLineName(String targetConnectedLineName) {
        this.targetConnectedLineName = targetConnectedLineName;
    }

}
