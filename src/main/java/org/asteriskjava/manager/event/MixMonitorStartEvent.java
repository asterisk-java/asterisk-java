package org.asteriskjava.manager.event;

/**
 * A MixMonitorStartEvent indicates that monitoring was started on a channel.<p>
 *
 * @since 3.13.0
 * @see org.asteriskjava.manager.event.MixMonitorStopEvent
 */
public class MixMonitorStartEvent extends AbstractMixMonitorEvent
{
	private static final long serialVersionUID = 1L;

	public MixMonitorStartEvent(Object source)
	{
		super(source);
	}
}
