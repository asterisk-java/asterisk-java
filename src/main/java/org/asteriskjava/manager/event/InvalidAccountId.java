package org.asteriskjava.manager.event;

import org.asteriskjava.ami.event.api.ManagerEvent;

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 2021-03-03
 */

public class InvalidAccountId extends ManagerEvent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String severity;
    private Integer eventVersion;
    private String eventtv;
    private String sessionId;
    private String localAddress;
    private String accountId;
    private String service;
    private String remoteAddress;
    private String event;

    public InvalidAccountId(Object source) {
        super(source);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Integer getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(Integer eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getEventtv() {
        return eventtv;
    }

    public void setEventtv(String eventtv) {
        this.eventtv = eventtv;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
