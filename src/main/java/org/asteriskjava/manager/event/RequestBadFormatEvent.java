package org.asteriskjava.manager.event;

/**
 * A RequestBadFormatEvent is triggered when bad format of request.
 */

public class RequestBadFormatEvent extends ManagerEvent {

    /**
     * Serializable version identifier.
     */
    private final static long serialVersionUID = -4626589852577838795L;

    private String severity;

    private Integer eventversion;

    private String sessiontv;

    private String eventtv;

    private String sessionid;

    private String localaddress;

    private String accountid;

    private String requesttype;

    private String service;

    private String remoteaddress;

    private String module;

    private String requestparams;

    public RequestBadFormatEvent(Object source) {
        super(source);
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Integer getEventversion() {
        return eventversion;
    }

    public void setEventversion(Integer eventversion) {
        this.eventversion = eventversion;
    }

    public String getSessiontv() {
        return sessiontv;
    }

    public void setSessiontv(String sessiontv) {
        this.sessiontv = sessiontv;
    }

    public String getEventtv() {
        return eventtv;
    }

    public void setEventtv(String eventtv) {
        this.eventtv = eventtv;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getLocaladdress() {
        return localaddress;
    }

    public void setLocaladdress(String localaddress) {
        this.localaddress = localaddress;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRemoteaddress() {
        return remoteaddress;
    }

    public void setRemoteaddress(String remoteaddress) {
        this.remoteaddress = remoteaddress;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getRequestparams() {
        return requestparams;
    }

    public void setRequestparams(String requestparams) {
        this.requestparams = requestparams;
    }
}
