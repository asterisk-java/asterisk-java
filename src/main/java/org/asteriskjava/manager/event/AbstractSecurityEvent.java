package org.asteriskjava.manager.event;

public abstract class AbstractSecurityEvent extends ManagerEvent {
    private String eventTv;
    private String severity;
    private String service;
    private Integer eventVersion;
    private String accountId;
    private String sessionId;
    private String localAddress;
    private String remoteAddress;
    private String module;
    private String sessionTv;

    protected AbstractSecurityEvent(Object source) {
        super(source);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEventTv() {
        return eventTv;
    }

    public void setEventTv(String eventTimestamp) {
        this.eventTv = eventTimestamp;
    }

    public Integer getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(Integer eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionTv() {
        return sessionTv;
    }

    public void setSessionTv(String sessionTimestamp) {
        this.sessionTv = sessionTimestamp;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
