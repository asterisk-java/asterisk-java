package org.asteriskjava.manager.event;

/**
 * Abstract base class for mix monitoring events.<p>
 *
 * @version $Id$
 * @since 3.13.0
 */
public abstract class AbstractMixMonitorEvent extends ManagerEvent {
    private static final long serialVersionUID = 1L;
    private String accountCode;
    private String channel;
    private String language;
    private String linkedId;
    private String uniqueId;

    protected AbstractMixMonitorEvent(Object source) {
        super(source);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }
}
