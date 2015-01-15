package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class NewConnectedLineEvent extends ManagerEvent {
    private String channel;
    private String uniqueId;

    public NewConnectedLineEvent(Object source) { super(source); }

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
}
