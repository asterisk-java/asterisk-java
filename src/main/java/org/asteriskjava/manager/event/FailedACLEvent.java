package org.asteriskjava.manager.event;

public final class FailedACLEvent extends AbstractSecurityEvent {
    private String aclName;

    public FailedACLEvent(Object source) {
        super(source);
    }

    public String getAclName() {
        return this.aclName;
    }

    public void setAclName(String aclName) {
        this.aclName = aclName;
    }
}
