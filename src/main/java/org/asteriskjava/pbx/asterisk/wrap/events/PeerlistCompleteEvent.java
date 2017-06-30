package org.asteriskjava.pbx.asterisk.wrap.events;

public class PeerlistCompleteEvent extends ResponseEvent
{
    private static final long serialVersionUID = 1L;

    public PeerlistCompleteEvent(final org.asteriskjava.manager.event.PeerlistCompleteEvent event)
    {
        super(event);
    }
}
