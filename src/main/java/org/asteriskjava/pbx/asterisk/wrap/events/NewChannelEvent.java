package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class NewChannelEvent extends AbstractChannelStateEvent
{
    private static final long serialVersionUID = 1L;

    private final String accountCode;
    private final String context;
    private final String exten;

    public NewChannelEvent(final org.asteriskjava.manager.event.NewChannelEvent event) throws InvalidChannelName
    {
        super(event);
        this.accountCode = event.getAccountCode();
        this.context = event.getContext();
        this.exten = event.getExten();

        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        pbx.internalRegisterChannel(event.getChannel(), event.getUniqueId());

    }

    public String getAccountCode()
    {
        return this.accountCode;
    }

    public String getContext()
    {
        return this.context;
    }

    public String getExten()
    {
        return this.exten;
    }

    public String toString()
    {
        return "NewChannelEvent: channel: " + this.getChannel() + " context=" + this.context + " exten=" + this.exten //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + " state=" + this.getChannelStateDesc(); //$NON-NLS-1$
    }

}
