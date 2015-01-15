package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class AsyncAgiStartEvent extends ManagerEvent
{
    private String channel;

    public AsyncAgiStartEvent(Object source) { super(source); }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
