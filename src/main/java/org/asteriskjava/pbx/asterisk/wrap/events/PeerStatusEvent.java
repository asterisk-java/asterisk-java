package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class PeerStatusEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    private final EndPoint peer;

    public PeerStatusEvent(final org.asteriskjava.manager.event.PeerStatusEvent event)
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.peer = pbx.buildEndPoint(event.getPeer());// registerChannel(event.getPeer(),
                                                       // Channel.UNKNOWN_UNIQUE_ID);
    }

    public EndPoint getPeer()
    {
        return this.peer;
    }

}
