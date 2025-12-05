package org.asteriskjava.manager.event;

public final class UnexpectedAddressEvent extends AbstractSecurityEvent {
    private String expectedAddress;

    public UnexpectedAddressEvent(Object source) {
        super(source);
    }

    public String getExpectedAddress() {
        return expectedAddress;
    }

    public void setExpectedAddress(String value) {
        this.expectedAddress = value;
    }
}
