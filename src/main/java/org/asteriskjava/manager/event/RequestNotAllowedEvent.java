package org.asteriskjava.manager.event;

public final class RequestNotAllowedEvent extends AbstractSecurityEvent {
    private String requestParams;
    private String requestType;

    public RequestNotAllowedEvent(Object source) {
        super(source);
    }

    public Object getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String value) {
        this.requestParams = value;
    }

    public Object getRequestType() {
        return requestType;
    }

    public void setRequestType(String value) {
        this.requestType = value;
    }
}
