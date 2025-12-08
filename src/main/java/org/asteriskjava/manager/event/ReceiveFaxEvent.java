package org.asteriskjava.manager.event;

/**
 * A ReceiveFaxEvent is an event of Digium's Fax For Asterisk add-on.
 */
public class ReceiveFaxEvent extends ManagerEvent {
    private static final long serialVersionUID = 0L;
    private String channel;
    private String callerId;
    private String remoteStationId;
    private String localStationId;
    private Integer pagesTransferred;
    private String resolution;
    private Integer transferRate;
    private String fileName;

    private String language;
    private String accountCode;
    private String linkedId;
    private String uniqueId;

    public ReceiveFaxEvent(Object source) {
        super(source);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getRemoteStationId() {
        return remoteStationId;
    }

    public void setRemoteStationId(String remoteStationId) {
        this.remoteStationId = remoteStationId;
    }

    public String getLocalStationId() {
        return localStationId;
    }

    public void setLocalStationId(String localStationId) {
        this.localStationId = localStationId;
    }

    public Integer getPagesTransferred() {
        return pagesTransferred;
    }

    public void setPagesTransferred(Integer pagesTransferred) {
        this.pagesTransferred = pagesTransferred;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getTransferRate() {
        return transferRate;
    }

    public void setTransferRate(Integer transferRate) {
        this.transferRate = transferRate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
