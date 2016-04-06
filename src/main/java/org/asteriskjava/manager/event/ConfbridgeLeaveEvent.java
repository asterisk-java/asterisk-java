package org.asteriskjava.manager.event;

/**
 * This event is sent when a user leaves a conference.
 *
 * @since 1.0.0
 */
public class ConfbridgeLeaveEvent extends AbstractConfbridgeEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    public ConfbridgeLeaveEvent(Object source)
    {
        super(source);
    }

}
