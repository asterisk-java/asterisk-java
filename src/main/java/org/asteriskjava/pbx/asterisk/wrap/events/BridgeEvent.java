package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class BridgeEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(BridgeEvent.class);

    enum BridgeState
    {
        Link, Unlink
    }

    private final BridgeState bridgeState;
    private final Channel channel1;
    private final Channel channel2;

    public BridgeEvent(final org.asteriskjava.manager.event.BridgeEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.bridgeState = BridgeState.valueOf(event.getBridgeState());
        this.channel1 = pbx.internalRegisterChannel(event.getChannel1(), event.getUniqueId1());
        this.channel2 = pbx.internalRegisterChannel(event.getChannel2(), event.getUniqueId2());
    }

    public BridgeState getBridgeState()
    {
        return this.bridgeState;
    }

    public Channel getChannel1()
    {
        return this.channel1;
    }

    public Channel getChannel2()
    {
        return this.channel2;
    }

    /**
     * Returns whether the two channels have been linked.
     * 
     * @return <code>true</code> the two channels have been linked,
     *         <code>false</code> if they have been unlinked.
     * @since 1.0.0
     */
    public boolean isLink()
    {
        return this.bridgeState == BridgeState.Link;
    }

    /**
     * Returns whether the two channels have been unlinked.
     * 
     * @return <code>true</code> the two channels have been unlinked,
     *         <code>false</code> if they have been linked.
     * @since 1.0.0
     */
    public boolean isUnlink()
    {
        return this.bridgeState == BridgeState.Unlink;
    }

    public String toString()
    {
        return "BridgeEvent: channel1:" + this.channel1 + " channel2:" + this.channel2 + " bridgeState:" + this.bridgeState; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }
}
