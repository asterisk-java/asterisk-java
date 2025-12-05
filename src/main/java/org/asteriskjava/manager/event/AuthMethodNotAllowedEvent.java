package org.asteriskjava.manager.event;

public final class AuthMethodNotAllowedEvent extends AbstractSecurityEvent {
    private String authMethod;

    public AuthMethodNotAllowedEvent(Object source) {
        super(source);
    }

    public String getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(String value) {
        this.authMethod = value;
    }
}
