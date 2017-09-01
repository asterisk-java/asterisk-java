package org.asteriskjava.pbx.asterisk.wrap.events;

public class ConnectEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    /**
     * The version of the manager protocol.
     */
    private final String protocolIdentifier;

    public ConnectEvent(final org.asteriskjava.manager.event.ConnectEvent event)
    {
        super(event);
        this.protocolIdentifier = event.getProtocolIdentifier();
    }

    public String getProtocolIdentifier()
    {
        return this.protocolIdentifier;
    }

}
