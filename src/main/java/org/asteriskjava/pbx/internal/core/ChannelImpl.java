package org.asteriskjava.pbx.internal.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ChannelFactory;
import org.asteriskjava.pbx.ChannelHangupListener;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * TODO set the channel unique id when registering against an existing channel
 * which doesn't have its unique id set. <br>
 * <br>
 * Create a single asterisk event source. All users of MyBaseEventCalls will
 * become listeners to this class rather than talking to asterisk directly. Each
 * listener will have events queued to it. It can process these events in a
 * separate thread. <br>
 * <br>
 * Key feature is that one a 'rename' event arrives we must pause all of the
 * queues wait for them to empty and for the queue clients to quiescent, then
 * apply the rename before resuming pushing data in to the queues. <br>
 * <br>
 * Additionally we need to redo the asterisk events classes with our own classes
 * that pass around an iChannel rather than a raw channel name. By doing this
 * the rename affectively becomes global updating every instance of the channel
 * (because they actually only have an instance handle).
 * 
 * @author bsutton
 */
public class ChannelImpl implements Channel
{
    public static final String ZOMBIE = "<ZOMBIE>"; //$NON-NLS-1$
    public static final String MASQ = "<MASQ>"; //$NON-NLS-1$

    public static final String UNKNOWN_UNIQUE_ID = "-1"; //$NON-NLS-1$

    private static final Log logger = LogFactory.getLog(ChannelImpl.class);
    private static int logCounter = 100;

    /**
     * The channel name including the tech and the channel sequence number but
     * not the masquerade prefix and not the zombie suffix. Forced to upper case
     * for comparisons.
     */
    private String _channelName;

    /**
     * A unique id created by Asterisk to uniquely identify this channel. Often
     * a channel can be 're-created' on the fly. In this case the channels name
     * will be the same but the unique id will change.
     */
    private String _uniqueID;
    /**
     * This is an abbreviated form of the channel name obtained by removing
     * everything after the '-' in the channel name.
     */
    private EndPoint _endPoint;

    /**
     * The caller id for this channel. If this is an inbound channel then it the
     * callerid received from the remote end. If this is an outbound channel
     * then this is the caller id we are to present to the remote end.
     */
    private CallerID _callerID;

    /**
     * A globally unique id assigned to each channel as it is created
     */
    private long _channelId;

    private boolean _muted = false;

    private boolean _parked = false;

    private volatile boolean _isZombie = false;

    /**
     * A channel is live if it has not been hungup.
     */
    private boolean _isLive = true;

    /**
     * Indicates that the channel is being masquerading by another channel.
     * Masqueraded channels have an extra suffix <MASQ> after the sequence
     * number e.g. SIP/100-000009823<MASQ>
     */
    private volatile boolean _isMasqueraded = false;

    /**
     * Indicates that the channel is undergoing an action such as being parked.
     * Action channels have an extra prefix before the tech e.g.
     * Parked/SIP/100-000009823
     */
    private volatile boolean _isInAction = false;

    /**
     * Indicates that the channel has been originated directly from the Asterisk
     * console. We can generally ignore these cases.
     */
    private boolean _isConsole = false;

    /**
     * The prefix added to a channel during some type of action: e.g.
     * Parked/SIP/100-0000032323 In this case the prefix is Parked.
     */
    private String _actionPrefix = null;

    public static final String[] _actions = new String[]{"PARKED/", "ASYNCGOTO/", "BRIDGE/"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    /**
     * There should be only one hangup listener which is the ChannelProxy that
     * wraps this channel.
     */
    private ChannelHangupListener hangupListener;

    /**
     * When validating channels the start time is set in the _sweepStartTime. as
     * a channel is validated the _mark field is set to true. if new channels
     * are created as the sweep is in progress the _sweepStartTime will not be
     * set so they won't be cleaned up.
     */
    private Long _sweepStartTime = null;
    private boolean _marked = false;

    /**
     * Creates a channel from a channelName <br>
     * <br>
     * This constructor is package private for a reason, and is called from
     * AsteriskPBX... If you need to add a channel to the PBX do it like
     * this<br>
     * <br>
     * AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();<br>
     * final Channel channel = pbx.registerChannel(channelName, uniqueId); <br>
     * <br>
     * Channel names are one of the following formats:
     * [Action/]Tech/EndPointName-<sequence-number>[<MASQ>][ <ZOMBIE>]
     * DAHDI/i<span>/<number>[:<subaddress>]-<sequence-number> <br>
     * <br>
     * Where: [Action/] is an action that is being performed on a channel. e.g.
     * Parked Tech is the Tech used to reach the end point. <br>
     * <br>
     * The set of supported techs are defined by the enum Tech. <br>
     * <br>
     * EndPointName the name of the endpoint. <br>
     * <br>
     * '<MASQ>' the optional terminating '<MASQ>' which indicates the channel is
     * now being masqueraded. A masqueraded channel is one that has been cloned.
     * The original channel is marked as being masqueraded and will hangup
     * shortly. It plays no further part in the Call. <br>
     * <br>
     * '<ZOMBIE>' the optional terminating '<ZOMBIE>' which indicates the
     * channel is now a zombie. A zombie channel is one that has been hungup and
     * is awaiting final cleanup. I believe the zombie channel is used by the
     * hangup extension 'h' in the dialplan. <br>
     * <br>
     * The channel name is stripped of the Action, MASQ and ZOMBIE elements.
     * 
     * @param asteriskStateName
     * @throws InvalidChannelName
     */
    ChannelImpl(final String channelName, final String uniqueID) throws InvalidChannelName
    {
        if (uniqueID == null)
            throw new IllegalArgumentException("The UniqueID may not be null."); //$NON-NLS-1$

        if (channelName == null)
            throw new IllegalArgumentException("The channelName may not be null."); //$NON-NLS-1$

        if (uniqueID.compareToIgnoreCase("-1") == 0)
        {
            logger.debug("uniqueID is -1");
        }

        this._uniqueID = uniqueID;

        this.setChannelName(channelName);

    }

    /**
     * designed for use by the ChannelProxy when a channel is being cloned as a
     * result of Asterisk undertaking an Masquerade. This is not intended to be
     * a complete clone just the key elements that we generally track on our
     * side rather than getting directly from asterisk.
     */
    public void masquerade(Channel channel)
    {
        // If the channel doesn't have a caller id
        // preserve the existing one (as given this is a clone they should be
        // identical).
        // Asterisk doesn't always pass the caller ID on the channel hence this
        // protects us from Asterisk accidentally clearing the caller id.
        if (channel.hasCallerID())
        {
            this._callerID = channel.getCallerID();
        }
        else if (this._callerID != null && ((ChannelImpl) channel)._callerID != null)
        {
            // Force the caller id back into the channel so it has one as well.
            PBX pbx = PBXFactory.getActivePBX();
            if (this._callerID != null)
            {
                ((ChannelImpl) channel)._callerID = pbx.buildCallerID(this._callerID.getNumber(), this._callerID.getName());
            }
        }

        this._muted = channel.isMute();

        this._parked = channel.isParked();

        // just in case the original channel is part way through a sweep by the
        // PeerMonitor
        // marking the sweep as true will stop the new clone being hungup as it
        // may not have been around when the sweep started.
        this._marked = true;
        this._sweepStartTime = null;
    }

    private void setChannelName(final String channelName) throws InvalidChannelName
    {
        logger.debug("Renamed channel from " + this._channelName + " to " + channelName);
        this._channelName = this.cleanChannelName(channelName);
        this.validateChannelName(this._channelName);

        this._channelId = ChannelFactory.getNextChannelId();
        this._endPoint = new EndPointImpl(this.extractPeerName(this._channelName));

        // After stripping everything from the channel name after the hypen
        // we should have the endpoint name which shouldn't match the
        // channel name
        // unless its a channel caused by a console dial (Console/dsp) which
        // we don't really care about.
        // If the peer name still matches then we have a malformed channel
        // name.
        if ((this._channelName.compareTo(this.getEndPoint().getFullyQualifiedName()) == 0) && !this._isConsole)
        {
            if (ChannelImpl.logCounter > 0)
            {
                ChannelImpl.logger.warn("Invalid channel name " + this._channelName); //$NON-NLS-1$
                ChannelImpl.logCounter--;
            }
            else if (ChannelImpl.logCounter == 0)
            {
                ChannelImpl.logger.warn("Further Invalid channel name warnings suppressed"); //$NON-NLS-1$
                ChannelImpl.logCounter--;
            }
        }
    }

    /**
     * validates the channel name. Validate is to be called after the channel
     * has been cleaned.
     * 
     * @param channelName
     * @throws InvalidChannelName
     */
    private void validateChannelName(final String channelName) throws InvalidChannelName
    {
        if (!this._isConsole)
        {
            if (!TechType.hasValidTech(channelName))
            {
                throw new InvalidChannelName("Invalid channelName: " + channelName + ". Unknown tech."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Check we have the expected hypen
            final int hypen = channelName.indexOf("-"); //$NON-NLS-1$
            if (hypen == -1)
            {
                throw new InvalidChannelName("Invalid channelName: " + channelName + ". Missing hypen."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Check we have the expected slash
            final int slash = channelName.indexOf("/"); //$NON-NLS-1$
            if (slash == -1)
            {
                throw new InvalidChannelName("Invalid channelName: " + channelName + ". Missing slash."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Check that the hypen is after the slash.
            if (hypen < slash)
            {
                throw new InvalidChannelName("Invalid channelName: " + channelName + ". Hypen must be after the slash."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Check that there is at least one characters between the hypen and
            // the
            // slash
            if ((hypen - slash) < 2)
            {
                throw new InvalidChannelName("Invalid channelName: " + channelName //$NON-NLS-1$
                        + ". Must be one character between the hypen and the slash."); //$NON-NLS-1$
            }

            // Check that the channel sequence number is at least 1 character
            // long.
            if ((channelName.length() - hypen) < 2)
            {
                throw new InvalidChannelName("Invalid channelName: " + channelName //$NON-NLS-1$
                        + ". The channel sequence number must be at least 1 character."); //$NON-NLS-1$
            }
        }

    }

    /**
     * Cleans up the channel name by applying the following: 1) trim any
     * whitespace 2) convert to upper case for easy string comparisions 3) strip
     * of the masquerade prefix if it exists and mark the channel as
     * masquerading 4) strip the zombie suffix and mark it as being a zombie.
     * 
     * @param name
     * @return
     */
    private String cleanChannelName(final String name)
    {
        String cleanedName = name.trim().toUpperCase();

        // If if the channel is the console
        this._isConsole = false;
        if (name.compareToIgnoreCase("Console/dsp") == 0) //$NON-NLS-1$
        {
            this._isConsole = true;
        }

        // Check if the channel is in an action
        boolean wasInAction = this._isInAction;
        this._isInAction = false;
        for (final String prefix : ChannelImpl._actions)
        {
            if (cleanedName.startsWith(prefix))
            {
                this._isInAction = true;
                this._actionPrefix = cleanedName.substring(0, prefix.length() - 1);
                cleanedName = cleanedName.substring(prefix.length());
                break;
            }
        }
        if (wasInAction != this._isInAction)
        {
            logger.debug("Channel " + this + " : inaction status changed from " + wasInAction + " to " + this._isInAction);
        }

        // Channels can be marked as in a zombie state
        // so we need to strip of the zombie suffix and just mark the channel
        // as a
        // zombie.
        this._isZombie = false;
        if (cleanedName.contains(ChannelImpl.ZOMBIE))
        {
            this._isZombie = true;
            cleanedName = cleanedName.substring(0, cleanedName.indexOf(ChannelImpl.ZOMBIE));
        }

        // Channels can be marked as in a MASQ state
        // This happens during transfers (and other times) when the channel is
        // replaced
        // by a new channel in the call. The old channel is renamed with the
        // word
        // <MASQ> added as a suffix.
        this._isMasqueraded = false;
        if (cleanedName.contains(ChannelImpl.MASQ))
        {
            this._isMasqueraded = true;
            cleanedName = cleanedName.substring(0, cleanedName.indexOf(ChannelImpl.MASQ));
        }

        return cleanedName;
    }

    @Override
    public void rename(final String newName, String uniqueId) throws InvalidChannelName
    {

        String oldChannelName = getChannelName();
        logger.info("Changing " + oldChannelName + " to " + newName + " on " + oldChannelName + " " + _uniqueID);
        this.setChannelName(newName);

        if (_uniqueID.equalsIgnoreCase("-1"))
        {
            logger.info("Changing " + _uniqueID + " to " + uniqueId + " on " + oldChannelName + " " + _uniqueID);
            _uniqueID = uniqueId;
        }

        // the new replacement channel will go through a rename (without
        // MASQ) and is ready to use.

        // the old channel will go through a rename (MASQ), then anther
        // rename (ZOMBIE) and finally a hangup

        _isInAction = false;

    }

    @Override
    public String getChannelName()
    {
        return this._channelName;
    }

    @Override
    public EndPoint getEndPoint()
    {
        return this._endPoint;
    }

    /*
     * Extracts the peer name from a full channel name. The channel name is
     * created by asterisk and consists of the peer name a hypen and a unique
     * sequence number. e.g. SIP/100-000000123 * For SIP channels the Peer name
     * is of the form: SIP/100 For dahdi channels the channel name is of the
     * form: DAHDI/i<span>/<number>[:<subaddress>]-0000000123 With the peer name
     * of the form: DAHDI/i<span>/<number>[:<subaddress>]
     */
    private final String extractPeerName(final String channelName)
    {
        // Find the start of the sequence number
        int channelNameEndPoint = channelName.lastIndexOf("-"); //$NON-NLS-1$
        if (channelNameEndPoint == -1)
        {
            channelNameEndPoint = channelName.length();
        }
        // return the peer name which is everything before the sequence number
        // (and its hypen).
        return channelName.substring(0, channelNameEndPoint);
    }

    boolean wasMarkedDuringSweep()
    {
        boolean ret = false;
        if ((this._sweepStartTime == null) || (this._marked == true))
        {
            this._sweepStartTime = null;
            ret = true;
        }
        return ret;
    }

    public void setCallerId(final CallerID callerId)
    {
        this._callerID = callerId;
    }

    /**
     * Used to start the Mark and Sweep process by setting the marked status to
     * false. At the end of the sweep the marked flag should have been set to
     * true. If not then this channel has been hungup.
     */
    void startSweep()
    {
        this._sweepStartTime = new Date().getTime();
        this._marked = false;
    }

    /**
     * The channel was found to be active on asterisk so it is still alive.
     */
    void markChannel()
    {
        this._marked = true;

    }

    /**
     * *************************************************************************
     * ************************** The remaining methods are bridging methods to
     * the new abstract PBX classes.
     * *************************************************************************
     * **************************
     */
    @Override
    public boolean isSame(final Channel _rhs)
    {
        boolean equals = false;

        if (_rhs == null)
        {
            logger.warn("isSame called with null");
            return false;
        }
        ChannelImpl rhs;
        if (_rhs instanceof ChannelImpl)
        {
            rhs = (ChannelImpl) _rhs;
        }
        else
        {
            rhs = ((ChannelProxy) _rhs).getRealChannel();
        }
        // If we have unique id's for both channels then we can compare the id's
        // directly.
        if ((this._uniqueID.compareTo(ChannelImpl.UNKNOWN_UNIQUE_ID) != 0)
                && (rhs._uniqueID.compareTo(ChannelImpl.UNKNOWN_UNIQUE_ID) != 0))
        {
            if (this._uniqueID.compareToIgnoreCase(rhs._uniqueID) == 0)
            {
                equals = true;
            }
        }
        else
        {
            boolean ok = (this._channelName != null) && (rhs._channelName != null);
            final boolean namesMatch = (ok && (this._channelName.compareToIgnoreCase(rhs._channelName) == 0));

            if (namesMatch)
            {
                // check if the actions match
                if (this._isInAction != rhs._isInAction)
                {
                    ok = false;
                }
                else if (this._isInAction)
                {
                    ok = this._actionPrefix.compareTo(rhs._actionPrefix) == 0;
                }
            }

            if (ok && namesMatch)
            {
                // check if the zombie match
                if (this._isZombie != rhs._isZombie)
                {
                    ok = false;
                }
            }

            if (ok && namesMatch)
            {
                // check if the masquerade match
                if (this._isMasqueraded != rhs._isMasqueraded)
                {
                    ok = false;
                }
            }

            equals = ok & namesMatch;
        }
        return equals;
    }

    @Override
    public boolean isSame(final String extendedChannelName, final String uniqueID)
    {
        boolean equals = false;
        if ((this._uniqueID.compareTo(ChannelImpl.UNKNOWN_UNIQUE_ID) != 0)
                && (uniqueID.compareTo(ChannelImpl.UNKNOWN_UNIQUE_ID) != 0))
        {
            if (this._uniqueID.compareTo(uniqueID) == 0)
            {
                equals = true;
            }
        }
        else
        {
            equals = this.sameExtenededChannelName(extendedChannelName);
        }

        return equals;
    }

    @Override
    public boolean sameExtenededChannelName(final String extendedChannelName)
    {
        boolean ok = (this.getExtendedChannelName() != null) && (extendedChannelName != null);

        return ok && (this.getExtendedChannelName().compareToIgnoreCase(extendedChannelName) == 0);
    }

    /**
     * Try to match a channel based solely on its unique ID
     * 
     * @param uniqueID
     * @return
     */
    public boolean sameUniqueID(String uniqueID)
    {
        boolean equals = false;
        if ((this._uniqueID.compareTo(ChannelImpl.UNKNOWN_UNIQUE_ID) != 0)
                && (uniqueID.compareTo(ChannelImpl.UNKNOWN_UNIQUE_ID) != 0))
        {
            if (this._uniqueID.compareTo(uniqueID) == 0)
            {
                equals = true;
            }
        }

        return equals;
    }

    @Override
    public boolean sameEndPoint(final Channel rhs)
    {
        return this.sameEndPoint(rhs.getEndPoint());
    }

    @Override
    public boolean sameEndPoint(final EndPoint rhs)
    {
        boolean ok = (this._endPoint != null) && (rhs != null);
        return ok && this._endPoint.isSame(rhs);
    }

    @Override
    public long getChannelId()
    {
        return this._channelId;
    }

    @Override
    public boolean isLive()
    {
        return this._isLive;
    }

    @Override
    public void addHangupListener(final ChannelHangupListener listener)
    {
        if (this.hangupListener != null)
            throw new IllegalStateException("This channel may only have ONE listener which should be its ChannelProxy"); //$NON-NLS-1$

        this.hangupListener = listener;
    }

    @Override
    public void removeListener(final ChannelHangupListener listener)
    {

        if (this.hangupListener != listener)
            throw new IllegalStateException("An invalid attempt was made to remove a non-existant listener."); //$NON-NLS-1$

        this.hangupListener = null;

    }

    /**
     * Called by Peer when we have been hungup. This can happen when Peer
     * receives a HangupEvent or during a periodic sweep done by PeerMonitor to
     * find the status of all channels. Notify any listeners that this channel
     * has been hung up.
     */
    @Override
    public void notifyHangupListeners(Integer cause, String causeText)
    {
        this._isLive = false;
        if (this.hangupListener != null)
        {
            this.hangupListener.channelHangup(this, cause, causeText);
        }
        else
        {
            logger.warn("Hangup listener is null");
        }
    }

    @Override
    public boolean isConnectedTo(final EndPoint endPoint)
    {
        return this._endPoint.isSame(endPoint);
    }

    @Override
    public boolean isMute()
    {
        return this._muted;
    }

    @Override
    public void setMute(final boolean mutedState)
    {
        this._muted = mutedState;
    }

    @Override
    public void setParked(final boolean parked)
    {
        this._parked = parked;

    }

    @Override
    public boolean isParked()
    {
        return this._parked;
    }

    @Override
    public String toString()
    {
        return this.getExtendedChannelName() + ":" + this._uniqueID; //$NON-NLS-1$
    }

    /**
     * Returns the full channel name including the masquerade prefix and the
     * zombie suffix.
     * 
     * @return
     */
    @Override
    public String getExtendedChannelName()
    {
        final StringBuilder name = new StringBuilder();

        if (this._isInAction)
        {
            name.append(this._actionPrefix);
            name.append("/"); //$NON-NLS-1$
        }
        name.append(this._channelName);

        if (this._isMasqueraded)
        {
            name.append(ChannelImpl.MASQ);
        }

        if (this._isZombie)
        {
            name.append(ChannelImpl.ZOMBIE);
        }

        return name.toString();

    }

    @Override
    public boolean isLocal()
    {
        return this._endPoint.isLocal();
    }

    @Override
    public boolean isZombie()
    {
        return this._isZombie;
    }

    @Override
    public boolean isConsole()
    {
        return this._isConsole;
    }

    TechType getTech()
    {
        return this._endPoint.getTech();
    }

    /**
     * Returns the actionPrefix for the channel or an empty string if the
     * channel is not doing an action.
     * 
     * @return
     */
    String getActionPrefix()
    {
        return this._isInAction ? this._actionPrefix : ""; //$NON-NLS-1$
    }

    boolean isInAction()
    {
        return this._isInAction;
    }

    boolean isMasqueraded()
    {
        return this._isMasqueraded;
    }

    @Override
    public boolean hasCallerID()
    {
        return this._callerID != null && !this._callerID.isUnknown();
    }

    @Override
    public CallerID getCallerID()
    {
        if (this._callerID == null)
        {
            final CoherentManagerConnection smc = CoherentManagerConnection.getInstance();
            final String number = smc.getVariable(this, "CALLERID(number)"); //$NON-NLS-1$
            final String name = smc.getVariable(this, "CALLERID(name)"); //$NON-NLS-1$
            final PBX pbx = PBXFactory.getActivePBX();
            this._callerID = pbx.buildCallerID(number, name);
        }

        return this._callerID;

    }

    @Override
    public String getUniqueId()
    {
        return this._uniqueID;
    }

    /*
     * Returns true if this channel can detect hangups A SIP channel can always
     * detect a hangup if its not channel then we need to check the profile to
     * see if hangup detection is available. Hangup detection is important as we
     * don't want to connect two channels both of which can't do hangup
     * detection. If this happened then we would end up with a call which would
     * never hangup.
     */
    @Override
    public boolean canDetectHangup()
    {
        boolean canDetectHangup = true;

        AsteriskSettings profile = PBXFactory.getActiveProfile();
        final boolean detect = profile.getCanDetectHangup();
        if (!this.getEndPoint().isSIP() && !detect)
        {
            canDetectHangup = false;
        }
        return canDetectHangup;
    }

    /**
     * Returns true if the channel is quiescent. A quescent channel is one that
     * is current not going through a transition (name change) such as MASQ,
     * ZOMBIE, ASYNC, PARK
     * 
     * @return
     */
    @Override
    public boolean isQuiescent()
    {
        return !(_isInAction || _isMasqueraded || _isZombie);
    }

    @Override
    public AgiChannelActivityAction getCurrentActivityAction()
    {
        throw new RuntimeException("This method is only implemented in ChannelProxy");
    }

    @Override
    public void setCurrentActivityAction(AgiChannelActivityAction action)
    {
        throw new RuntimeException("This method is only implemented in ChannelProxy");
    }

    @Override
    public void setIsInAgi(boolean b)
    {
        throw new RuntimeException("This method is only implemented in ChannelProxy");
    }

    @Override
    public boolean isInAgi()
    {
        throw new RuntimeException("This method is only implemented in ChannelProxy");
    }

    @Override
    public boolean waitForChannelToReachAgi(long timeout, TimeUnit timeunit) throws InterruptedException
    {
        throw new RuntimeException("This method is only implemented in ChannelProxy");
    }

}
