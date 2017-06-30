package org.asteriskjava.pbx.asterisk.wrap.events;

public class StatusCompleteEvent extends ResponseEvent
{
    private static final long serialVersionUID = 1L;

    public StatusCompleteEvent(final org.asteriskjava.manager.event.StatusCompleteEvent event)
    {
        super(event);
    }
}
