package org.asteriskjava.manager.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChallengeResponseFailedEvent extends ManagerEvent {
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = -4630209124005390153L;

    private String severity;
    private String eventversion;
    private String service;
    private String remoteaddress;
    private String localaddress;
    private String accountId;
    private String module;
    private String sessiontv;
    private String sessionId;
    private String challange;
    private String response;
    private String expectedResponse;

    /**
     * @param source
     */
    public ChallengeResponseFailedEvent(Object source) {
        super(source);
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getEventversion() {
        return eventversion;
    }

    public void setEventversion(String eventversion) {
        this.eventversion = eventversion;
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRemoteAddress() {
        return remoteaddress;
    }

    public void setRemoteAddress(String remoteaddress) {
        this.remoteaddress = remoteaddress;
    }

    public String getLocalAddress() {
        return localaddress;
    }

    public void setLocalAddress(String localaddress) {
        this.localaddress = localaddress;
    }

    public void setEventtv(String eventv) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        Long t = 0L;
        try {
            Date date = format.parse(eventv);
            t = date.getTime();
        } catch (ParseException ex) {
        } finally {
            this.setTimestamp(t.doubleValue());
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getSessiontv() {
        return sessiontv;
    }

    public void setSessiontv(String sessiontv) {
        this.sessiontv = sessiontv;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getChallange() {
        return challange;
    }

    public void setChallange(String challange) {
        this.challange = challange;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        this.expectedResponse = expectedResponse;
    }
}
