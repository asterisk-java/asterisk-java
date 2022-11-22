package org.asteriskjava.manager.event;

/**
 * A MixMonitorStopEvent indicates that monitoring was stopped on a channel.<p>
 *
 * @see org.asteriskjava.manager.event.MixMonitorStartEvent
 * @since 3.13.0
 */
public class MixMonitorStopEvent extends AbstractMixMonitorEvent {
    private static final long serialVersionUID = 1L;

    public MixMonitorStopEvent(Object source) {
        super(source);
    }
}
