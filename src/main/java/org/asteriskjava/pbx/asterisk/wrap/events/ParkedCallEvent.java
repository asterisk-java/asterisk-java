package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelImpl;

public class ParkedCallEvent extends ManagerEvent
{

    private static final long serialVersionUID = 1L;

    private final Channel fromChannel;
    private final Integer timeout;
    private String exten;

    public ParkedCallEvent(final org.asteriskjava.manager.event.ParkedCallEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        if (event.getParkerDialString() != null)
            this.fromChannel = pbx.internalRegisterChannel(event.getParkerDialString(), ChannelImpl.UNKNOWN_UNIQUE_ID);
        else
            this.fromChannel = null;
        this.timeout = event.getTimeout();
        this.exten = event.getExten();
    }

    /**
     * The channel that parked the call. In some circumstances this can be null.
     * 
     * @return
     */
    public Channel getFromChannel()
    {
        return this.fromChannel;
    }

    public Integer getTimeout()
    {
        return this.timeout;
    }

    public String getExten()
    {
        return this.exten;
    }

}
