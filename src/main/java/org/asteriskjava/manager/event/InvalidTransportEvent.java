package org.asteriskjava.manager.event;

public final class InvalidTransportEvent extends AbstractSecurityEvent {
    private String attemptedTransport;

    public InvalidTransportEvent(Object source) {
        super(source);
    }

    public String getAttemptedTransport() {
        return attemptedTransport;
    }

    public void setAttemptedTransport(String value) {
        this.attemptedTransport = value;
    }
}
