package org.asteriskjava.manager.event;

public final class RequestNotSupportedEvent extends AbstractSecurityEvent {
    private String requestType;

    public RequestNotSupportedEvent(Object source) {
        super(source);
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String value) {
        this.requestType = value;
    }
}
