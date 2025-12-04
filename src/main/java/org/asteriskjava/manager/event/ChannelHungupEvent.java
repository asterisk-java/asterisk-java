package org.asteriskjava.manager.event;

public class ChannelHungupEvent extends ResponseEvent {
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -3439496042163782400L;

    private String channel;

    /**
     * Constructs a ChannelHungupEvent
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ChannelHungupEvent(Object source) {
        super(source);
    }

    /**
     * Get the name of the channel that was hung up.
     *
     * @return the name of the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Set the name of the channel that was hung up.
     *
     * @param channel the name of the channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
}
