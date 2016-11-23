package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelProxy;

public class RenameEvent extends ManagerEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(RenameEvent.class);

    /**
     * The existing channel which is about to be renamed.
     */
    private final Channel _channel;

    /**
     * New name of the channel after renaming occurred.
     */
    private final String _newName;

    public RenameEvent(final org.asteriskjava.manager.event.RenameEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this._channel = pbx.internalRegisterChannel(event.getChannel(), event.getUniqueId());
        this._newName = event.getNewname();

        assert ((ChannelProxy) this._channel).getRealChannel().getUniqueId()
                .compareToIgnoreCase(event.getUniqueId()) == 0 : "Rename registered against incorrect channel"; //$NON-NLS-1$

    }

    @Override
    public final Channel getChannel()
    {
        return this._channel;
    }

    public final String getNewName()
    {
        return this._newName;
    }

    @Override
    public String toString()
    {
        org.asteriskjava.manager.event.RenameEvent rawEvent = (org.asteriskjava.manager.event.RenameEvent) getSource();
        return "RenameEvent: existing channel: " + this._channel + " newname=" + this._newName //$NON-NLS-1$ //$NON-NLS-2$
                + "rawExisting:" + rawEvent.getChannel() + " rawNewname:" + rawEvent.getNewname() //$NON-NLS-1$ //$NON-NLS-2$
                + "rawUniquID:" + rawEvent.getUniqueId(); //$NON-NLS-1$
    }

}
