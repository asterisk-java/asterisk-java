package org.asteriskjava.pbx.asterisk.wrap.events;

public class DisconnectEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    public DisconnectEvent(final org.asteriskjava.manager.event.DisconnectEvent event)
    {
        super(event);
    }
}
