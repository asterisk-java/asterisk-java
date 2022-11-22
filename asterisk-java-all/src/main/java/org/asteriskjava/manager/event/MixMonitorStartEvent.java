package org.asteriskjava.manager.event;

/**
 * A MixMonitorStartEvent indicates that monitoring was started on a channel.<p>
 *
 * @see org.asteriskjava.manager.event.MixMonitorStopEvent
 * @since 3.13.0
 */
public class MixMonitorStartEvent extends AbstractMixMonitorEvent {
    private static final long serialVersionUID = 1L;

    public MixMonitorStartEvent(Object source) {
        super(source);
    }
}
