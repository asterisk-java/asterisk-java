package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelImpl;

/**
 * A Masquerade is used when a new channel is created to replace an existing
 * channel. The typical cause of this is an action such as a Redirect, Park etc.
 * During the Redirect a new channel will be created which has the same name as
 * the existing channel but with a Suffix attached such as ASYNCGOTO. The suffix
 * selected will depend on the actual action that cause the masquerade. As we
 * proxy all channels a key goal for us is for the existing proxy to end up
 * containing the new channel. The existing channel will eventually be renamed
 * with a <MASQ> suffix and then go on to become a zombie before it is hungup.
 * The masquerade event is associated a NewEvent, three renames and one hangup
 * event. The terminology used by asterisk is quite confusing. The 'original
 * channel' is the channel created as a result of the action (redirect, park
 * etc). The 'clone' channel is the pre-existing channel that was acted upon.
 * New Event: Original - the Original channel is currently holding the active
 * call due to the transfer/park.. event. - the Original name will be something
 * like ASYNCGOTO/SIP/101-0000002D:1351178863.121 Masquerade Event: Clone,
 * Original (e.g. SIP/101-0000002D:1351178863.121,
 * ASYNCGOTO/SIP/101-0000002D:1351178863.121) - swap the guts of the two
 * channels - for us this means that we swap the channels between the two parent
 * proxies. Some channel information is also copied from the clone to the
 * original channel (yes this terminology appears to be arse about. Remember the
 * 'clone' is the pre-existing channel). Rename: Clone to Clone<MASQ> (e.g.
 * rename the pre-existing channel SIP/101-0000002D:1351178863.121 to
 * SIP/101-0000002D:1351178863.121<MASQ> - start shutting down the clone
 * (pre-existing channel). Rename: Original to Clone's initial name Name (e.g
 * rename ASYNCGOTO/SIP/101-0000002D:1351178863.121,
 * SIP/101-0000002D:1351178863.121) Rename:Clone<MASQ> to Clone<Zombie> (e.g.
 * rename SIP/101-0000002D:1351178863.121<MASQ> to
 * SIP/101-0000002D:1351178863.121<ZOMBIE> Hangup: Clone<Zombie> (e.g. hangup
 * SIP/101-0000002D:1351178863.121<ZOMBIE> Example sequence: RedirectAction:
 * chanel=(proxy=2) SIP/101-0000002F:1351181832.124 context=njr-operator
 * exten=njr-musiconhold priority=1 extraContext=null extraChannel=null
 * extraExten=null Dump of LiveChannels, cause:Add: (proxy=3)
 * ASYNCGOTO/SIP/101-0000002F:1351181837.125 ChannelProxy: (proxy=1)
 * SIP/110-0000002E:1351181832.123 ChannelProxy: (proxy=2)
 * SIP/101-0000002F:1351181832.124 ChannelProxy: (proxy=3)
 * ASYNCGOTO/SIP/101-0000002F:1351181837.125 dispatch=NewChannelEvent: channel:
 * (proxy=3) ASYNCGOTO/SIP/101-0000002F:1351181837.125 context=from-internal
 * exten=null state=Up dispatch=MasqueradeEvent: originalChannel:(proxy=3)
 * ASYNCGOTO/SIP/101-0000002F:1351181837.125 cloned from:(proxy=2)
 * SIP/101-0000002F:1351181832.124 Dump of LiveChannels, cause:Masquerade:
 * (proxy=2) ASYNCGOTO/SIP/101-0000002F:1351181837.125 ChannelProxy: (proxy=1)
 * SIP/110-0000002E:1351181832.123 ChannelProxy: (proxy=2)
 * ASYNCGOTO/SIP/101-0000002F:1351181837.125 ChannelProxy: (proxy=3)
 * SIP/101-0000002F:1351181832.124 RenameEvent: existing channel: (proxy=3)
 * SIP/101-0000002F:1351181832.124
 * newname=SIP/101-0000002f<MASQ>rawExisting:SIP/101-0000002f
 * rawNewname:SIP/101-0000002f<MASQ>rawUniquID:1351181832.124 Dump of
 * LiveChannels, cause:RenameEvent: (proxy=3)
 * SIP/101-0000002F<MASQ>:1351181832.124 ChannelProxy: (proxy=1)
 * SIP/110-0000002E:1351181832.123 ChannelProxy: (proxy=2)
 * ASYNCGOTO/SIP/101-0000002F:1351181837.125 ChannelProxy: (proxy=3)
 * SIP/101-0000002F<MASQ>:1351181832.124 dispatch=RenameEvent: existing channel:
 * (proxy=2) ASYNCGOTO/SIP/101-0000002F:1351181837.125
 * newname=SIP/101-0000002frawExisting:AsyncGoto/SIP/101-0000002f
 * rawNewname:SIP/101-0000002frawUniquID:1351181837.125 Dump of LiveChannels,
 * cause:RenameEvent: (proxy=2) SIP/101-0000002F:1351181837.125 ChannelProxy:
 * (proxy=1) SIP/110-0000002E:1351181832.123 ChannelProxy: (proxy=2)
 * SIP/101-0000002F:1351181837.125 ChannelProxy: (proxy=3)
 * SIP/101-0000002F<MASQ>:1351181832.124 dispatch=RenameEvent: existing channel:
 * (proxy=3) SIP/101-0000002F<MASQ>:1351181832.124
 * newname=AsyncGoto/SIP/101-0000002f<ZOMBIE>rawExisting:SIP/101-0000002f<MASQ>
 * rawNewname:AsyncGoto/SIP/101-0000002f<ZOMBIE>rawUniquID:1351181832.124 Dump
 * of LiveChannels, cause:RenameEvent: (proxy=3)
 * ASYNCGOTO/SIP/101-0000002F<ZOMBIE>:1351181832.124 ChannelProxy: (proxy=1)
 * SIP/110-0000002E:1351181832.123 ChannelProxy: (proxy=2)
 * SIP/101-0000002F:1351181837.125 ChannelProxy: (proxy=3)
 * ASYNCGOTO/SIP/101-0000002F<ZOMBIE>:1351181832.124 dispatch=BridgeEvent:
 * channel1:(proxy=1) SIP/110-0000002E:1351181832.123 channel2:(proxy=3)
 * ASYNCGOTO/SIP/101-0000002F<ZOMBIE>:1351181832.124 bridgeState:Unlink
 * dispatch=HangupEvent: channel:(proxy=3)
 * ASYNCGOTO/SIP/101-0000002F<ZOMBIE>:1351181832.124 cause:Normal Clearing Dump
 * of LiveChannels, cause:Removing: (proxy=3)
 * ASYNCGOTO/SIP/101-0000002F<ZOMBIE>:1351181832.124 ChannelProxy: (proxy=1)
 * SIP/110-0000002E:1351181832.123 ChannelProxy: (proxy=2)
 * SIP/101-0000002F:1351181837.125 dispatch=HangupEvent: channel:(proxy=1)
 * SIP/110-0000002E:1351181832.123 cause:Normal Clearing Dump of LiveChannels,
 * cause:Removing: (proxy=1) SIP/110-0000002E:1351181832.123 ChannelProxy:
 * (proxy=2) SIP/101-0000002F:1351181837.125 The end result is that the original
 * (new) channel ends up with the clones (pre-existing) channel name. The
 * pre-existing proxy ends up holding the original channel with the pre-existing
 * channel's initial name. The masquerade event then is about ripping the guts
 * out of the clone (pre-existing channel) and swapping with the original (new)
 * channel. At the end of the swap the original (new) channel is left active (as
 * it has the clones guts) and the clone (pre-existing) is inactive so we can
 * hangup the clone as it is no longer needed.
 * 
 * @author bsutton
 */
public class MasqueradeEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    private final Channel _original;

    private final Channel _clone;

    private ChannelState cloneState;

    private ChannelState originalState;

    public MasqueradeEvent(final org.asteriskjava.manager.event.MasqueradeEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this._original = pbx.internalRegisterChannel(event.getOriginal(), ChannelImpl.UNKNOWN_UNIQUE_ID);
        this._clone = pbx.internalRegisterChannel(event.getClone(), ChannelImpl.UNKNOWN_UNIQUE_ID);
        this.originalState = ChannelState.valueOf(event.getOriginalStateDesc());
        this.cloneState = ChannelState.valueOf(event.getCloneStateDesc());
    }

    /**
     * Returns the original channel
     * 
     * @return
     */
    public Channel getOriginal()
    {
        return this._original;
    }

    public Channel getClone()
    {
        return this._clone;
    }

    public ChannelState getCloneState()
    {
        return this.cloneState;
    }

    public ChannelState getOriginalState()
    {
        return this.originalState;
    }

    public String toString()
    {
        // Even though it talks about the original channel
        return "MasqueradeEvent: originalChannel(new):" + this._original + " cloned from (pre-existing):" + this._clone; //$NON-NLS-1$//$NON-NLS-2$
    }
}
