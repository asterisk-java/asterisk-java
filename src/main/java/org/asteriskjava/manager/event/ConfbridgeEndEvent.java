package org.asteriskjava.manager.event;

/**
 * This event is sent when the last user leaves a conference and it is torn
 * down.
 *
 * @since 1.0.0
 */
public class ConfbridgeEndEvent extends AbstractConfbridgeEvent
{

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -8973512592594074108L;

    public ConfbridgeEndEvent(Object source)
    {
        super(source);
    }

}
