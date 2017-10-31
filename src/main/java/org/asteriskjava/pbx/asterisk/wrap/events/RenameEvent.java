package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelProxy;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class RenameEvent extends ManagerEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    private static final Log logger = LogFactory.getLog(RenameEvent.class);

    /**
     * The existing channel which is about to be renamed.
     */
    private final Channel _channel;

    /**
     * New name of the channel after renaming occurred.
     */
    private final String _newName;

    private String uniqueId;

    public RenameEvent(final org.asteriskjava.manager.event.RenameEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this._channel = pbx.internalRegisterChannel(event.getChannel(), event.getUniqueId());
        this._newName = event.getNewname();

        String uniqueId = ((ChannelProxy) this._channel).getRealChannel().getUniqueId();
        logger.debug("Renaming :" + uniqueId + " " + event.getUniqueId());

        this.uniqueId = event.getUniqueId();

        assert uniqueId.equalsIgnoreCase("-1")
                || uniqueId.compareToIgnoreCase(event.getUniqueId()) == 0 : "Rename registered against incorrect channel"; //$NON-NLS-1$

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

    public final String getUniqueId()
    {
        return uniqueId;
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
