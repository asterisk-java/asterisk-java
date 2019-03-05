package org.asteriskjava.manager.event;

/**
 * This event is sent when the first user requests a conference and it is
 * instantiated.
 *
 * @since 1.0.0
 */
public class ConfbridgeStartEvent extends AbstractConfbridgeEvent {

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private String conference;
    private String muted;

    public ConfbridgeStartEvent(Object source) {
        super(source);
    }

    public String getMuted() {
        return muted;
    }

    public void setMuted(String muted) {
        this.muted = muted;
    }

}
