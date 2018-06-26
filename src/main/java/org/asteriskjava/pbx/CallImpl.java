package org.asteriskjava.pbx;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.asteriskjava.pbx.activities.BlindTransferActivity;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Holds a call which may consist of 0, 1, 2 or 3 channels. When dialing out a
 * call The 1st channel is always the call 'originator'. Channel 2 is
 * 'accepting' party Channel 3 is a 'third party' which has joined the call via
 * a conference mode. The concept of a Call is independent of a particular PBX.
 * All channels of a Call must belong to the same PBX as we have no way of
 * bridging calls across multiple PBX's anyway. Calls can be established by: 1)
 * When a call arrives at the NJR Management inbound context 2) Dialing 3)
 * Joining two existing calls The 'originating' channel is the first channel
 * introduced to the call and is stored in the zeroth element of the channel
 * list.
 * 
 * @author bsutton
 */

public class CallImpl implements ChannelHangupListener, Call
{
    private static final Log logger = LogFactory.getLog(CallImpl.class);

    /**
     * We give every call a unique call id to help track the calls when checking
     * logs
     */
    private static final AtomicInteger global_call_identifier_index = new AtomicInteger();

    /**
     * A id that uniquely identifies this call.
     */
    private final int _uniquecallID;

    /*
     * transfer types
     */
    public enum TransferType
    {
        NONE, ASSISTED, BLIND
    }

    /**
     * The 1st channel is always the call 'originator'.
     */
    private Channel _originatingParty;

    /**
     * Channel 2 is 'accepting' party
     */

    private Channel _acceptingParty;

    /**
     * Channel 3 is a 'third party' which has joined the call via a conference
     * mode which would normally be created during a transfer attempt.
     */
    private Channel _transferTargetParty;

    /**
     * The direct of the call. If it is OUTBOUND then the originating party is
     * the local call . If it is INBOUND then the originationg party is the
     * caller.
     */
    final private CallDirection _direction;

    private CallerID _originatingPartyCallerID = null;

    private CallerID _acceptingPartyCallerID = null;

    /**
     * When we attempt to transfer a call to a third party we save the transfer
     * target. This allows us to automatically re-try the transfer if the
     * initial attempt failed.
     */
    private EndPoint _transferTarget = null;

    private TransferType transferType = TransferType.NONE;

    private CallerID _transferTargetCallerID = null;

    private Date _callStarted;

    private Date _holdStarted = null;

    private EndPoint _parkingLot = null;

    private Date _timeAtDialin = null;

    private boolean _parked = false;

    /**
     * The primary key for the Contact record.
     * 
     * @return
     */
    private Integer _contactId;

    private ArrayList<CallHangupListener> _hangupListeners = new ArrayList<>();

    /**
     * Used to indicate the ownership of the call. SELF - this instance of NJR
     * has answered the call and is now considered the owner. REMOTE - some
     * other instance of NJR has answered the call. UNOWNED - the call has
     * entered the inbound extension but has not been answered by any instance
     * of NJR. Note: a calls ownership can change over time. All calls start as
     * UNOWNED. If we answer a call it becomes SELF. If we put a call on hold
     * and another NJR instances answers it then it can then become REMOTE.
     * 
     * @author bsutton
     */
    public enum OWNER
    {
        SELF
    }

    private OWNER _owner;

    protected BlindTransferActivity _transferActivity;

    public CallImpl(Channel originatingChannel, CallDirection direction) throws PBXException
    {
        logger.debug("Created call=" + originatingChannel + " direction=" + direction); //$NON-NLS-1$ //$NON-NLS-2$
        this._owner = OWNER.SELF;
        this._uniquecallID = CallImpl.global_call_identifier_index.incrementAndGet();
        this._direction = direction;
        this._callStarted = new Date();
        this.setOriginatingParty(originatingChannel);

    }

    public CallImpl(Channel originatingChannel, Channel acceptingChannel, CallDirection direction) throws PBXException
    {
        logger.debug("Created call=" + originatingChannel + " direction=" + direction); //$NON-NLS-1$ //$NON-NLS-2$
        this._owner = OWNER.SELF;
        this._uniquecallID = CallImpl.global_call_identifier_index.incrementAndGet();
        this._direction = direction;
        this._callStarted = new Date();
        this.setOriginatingParty(originatingChannel);
        this.setAcceptingParty(acceptingChannel);

    }

    public CallImpl(Channel agent, Channel callee) throws PBXException
    {
        this._owner = OWNER.SELF;
        this._uniquecallID = CallImpl.global_call_identifier_index.incrementAndGet();
        this._direction = CallDirection.OUTBOUND;
        this._callStarted = new Date();
        this.setOriginatingParty(agent);
        this.setAcceptingParty(callee);

    }

    /**
     * Joins a specific channel from this call with a specific channel from
     * another call which results in a new Call object being created. Channels
     * that do not participate in the join are left in their original Call.
     * 
     * @param originatingOperand the channel from this call that will
     *            participate in the join as the originating Channel
     * @param rhs the call we are joining to.
     * @param acceptingOperand the channel from the rhs call that will
     *            participate in the join as the accepting channel.
     * @return
     * @throws PBXException
     */
    public CallImpl join(OperandChannel originatingOperand, Call rhs, OperandChannel acceptingOperand,
            CallDirection direction) throws PBXException
    {
        CallImpl joinTo = (CallImpl) rhs;

        Channel originatingParty = null;
        Channel acceptingParty = null;

        // Pull the originating party from the this call.
        originatingParty = this.getOperandChannel(originatingOperand);
        if (originatingParty != null && originatingParty.isLive())
            originatingParty.removeListener(this);

        // Pull the accepting party from the other 'call' as the accepting
        // party.
        acceptingParty = rhs.getOperandChannel(acceptingOperand);
        if (acceptingParty != null && acceptingParty.isLive())
            acceptingParty.removeListener((CallImpl) rhs);

        if (originatingParty == null || !originatingParty.isLive() || acceptingParty == null || !acceptingParty.isLive())
            throw new PBXException("Call.HangupDuringJoin"); //$NON-NLS-1$

        CallImpl joined = new CallImpl(originatingParty, direction);
        joined.setAcceptingParty(acceptingParty);
        joined._owner = OWNER.SELF;

        joined._callStarted = minDate(this._callStarted, joinTo._callStarted);

        joined._holdStarted = minDate(this._holdStarted, joinTo._holdStarted);

        joined._timeAtDialin = minDate(this._timeAtDialin, joinTo._timeAtDialin);

        logger.debug("Joined two calls lhs=" + this + ", rhs=" + joinTo); //$NON-NLS-1$//$NON-NLS-2$

        return joined;
    }

    private Date minDate(Date d1, Date d2)
    {

        if (d1 == null)
        {
            return d2;
        }
        if (d2 == null)
        {
            return d1;
        }
        if (d1.before(d2))
        {
            return d1;
        }
        return d2;
    }

    //
    // /**
    // * Joins a specific channel from this call with a specific channel from
    // another call which results in a new Call object
    // * being created.
    // *
    // * Channels that do not participate in the join are left in their original
    // Call.
    // *
    // * @param lhsOperand the channel from this call that will participate in
    // the join
    // * @param rhs the call we are joining to.
    // * @param rhsOperand the channel from the rhs call that will participate
    // in the join.
    // * @return
    // * @throws PBXException
    // */
    // public Call join(OperandChannel lhsOperand, iCall rhs, OperandChannel
    // rhsOperand) throws PBXException
    // {
    // Call joinTo = (Call) rhs;
    //
    // // // Check there is no transfer party.
    // // if (this._transferTargetParty != null || joinTo._transferTargetParty
    // != null)
    //// throw new IllegalArgumentException("Neither call in a 'Join' may have a
    // third party"); //$NON-NLS-1$
    // //
    // // // Check that we only have one active party
    // // if (this._originatingParty != null && this._acceptingParty != null)
    // // throw new IllegalArgumentException(
    //// "The call " + this + " has more than one active channel which is not
    // allowed."); //$NON-NLS-1$ //$NON-NLS-2$
    // //
    // // if (joinTo._originatingParty != null && joinTo._acceptingParty !=
    // null)
    // // throw new IllegalArgumentException(
    //// "The call " + joinTo + " has more than one active channel which is not
    // allowed."); //$NON-NLS-1$ //$NON-NLS-2$
    //
    // iChannel originatingParty = null;
    // iChannel acceptingParty = null;
    //
    // // First select the remote party from this call.
    // // For no particular reason we use 'this' as the originating party.
    // originatingParty = this.getRemoteParty();
    // if (originatingParty != null && originatingParty.isLive())
    // originatingParty.removeListener(this);
    //
    // // Now select the remote party from the other 'call' as the accepting
    // // party.
    // acceptingParty = rhs.getRemoteParty();
    // if (acceptingParty != null && acceptingParty.isLive())
    // acceptingParty.removeListener((Call) rhs);
    //
    // if (originatingParty == null || acceptingParty == null)
    // throw new PBXException(Messages.getString("Call.HangupDuringJoin"));
    // //$NON-NLS-1$
    //
    // /**
    // * We just choose one of the directions as it doesn't really matter
    // */
    // Call joined = new Call(originatingParty, this._direction);
    // joined.setAcceptingParty(acceptingParty);
    // joined._owner = OWNER.SELF;
    //
    //
    // joined._callStarted = DateUtils.min(this._callStarted,
    // joinTo._callStarted);
    //
    // joined._holdStarted = DateUtils.min(this._holdStarted,
    // joinTo._holdStarted);
    //
    // joined._timeAtDialin = DateUtils.min(this._timeAtDialin,
    // joinTo._timeAtDialin);
    //
    // // The selection of _didEntity is arbitrary
    // if (this._didEntity != null)
    // joined._didEntity = this._didEntity;
    // else
    // joined._didEntity = joinTo._didEntity;
    //
    // logger.debug("Joined two calls lhs=" + this + ", rhs=" + joinTo);
    // //$NON-NLS-1$//$NON-NLS-2$
    //
    // return joined;
    // }

    /**
     * Splits a channel out of a call into a separate call. This method should
     * only be called from the SplitActivity.
     * 
     * @return the resulting call.
     * @throws PBXException
     */
    public Call split(OperandChannel channelToSplit) throws PBXException
    {
        Channel channel = this.getOperandChannel(channelToSplit);
        channel.removeListener(this);

        CallDirection direction = CallDirection.INBOUND;

        // If this is the operator channel we need to flip the default
        // direction.
        if (this.getLocalParty() != null && this.getLocalParty().isSame(channel))
        {
            direction = CallDirection.OUTBOUND;
        }

        CallImpl call = new CallImpl(channel, direction);
        call._owner = OWNER.SELF;

        // clear the channel have just split out.
        switch (channelToSplit)
        {
            case ACCEPTING_PARTY :
                this.setAcceptingParty(null);
                break;
            case LOCAL_PARTY :
                if (this._direction == CallDirection.INBOUND)
                {
                    this.setAcceptingParty(null);
                }
                else
                {
                    this.setOriginatingParty(null);
                }
                break;
            case ORIGINATING_PARTY :
                this.setOriginatingParty(null);
                break;
            case REMOTE_PARTY :
                if (this._direction == CallDirection.INBOUND)
                    this.setOriginatingParty(null);
                else
                    this.setAcceptingParty(null);
                break;
            case TRANSFER_TARGET_PARTY :
                this.setTransferTargetParty(null);
                break;
            default :
                break;
        }

        return call;
    }

    public Call split(Channel channelToSplit) throws PBXException
    {
        channelToSplit.removeListener(this);
        CallDirection direction = CallDirection.INBOUND;
        // If this is the operator channel we need to flip the default
        // direction.
        if (this.getLocalParty() != null && this.getLocalParty().isSame(channelToSplit))
        {
            direction = CallDirection.OUTBOUND;
        }

        CallImpl call = new CallImpl(channelToSplit, direction);
        call._owner = OWNER.SELF;
        if (_acceptingParty != null && _acceptingParty.isSame(channelToSplit))
            _acceptingParty = null;
        if (_originatingParty != null && _originatingParty.isSame(channelToSplit))
            _originatingParty = null;
        if (_transferTargetParty != null && _transferTargetParty.isSame(channelToSplit))
            _transferTargetParty = null;
        return call;

    }

    @Override
    public void channelHangup(Channel channel, Integer cause, String causeText)
    {
        try
        {
            if (isSameChannel(this._originatingParty, channel))
            {
                this.setOriginatingParty(null);
                logger.debug("Removed originatingParty=" + channel + " from Call=" + this._uniquecallID //$NON-NLS-1$ //$NON-NLS-2$
                        + " as channel was hung up"); //$NON-NLS-1$
            }

            else if (isSameChannel(this._acceptingParty, channel))
            {
                this.setAcceptingParty(null);
                logger.debug("Removed acceptingParty=" + channel + " from Call=" + this._uniquecallID //$NON-NLS-1$ //$NON-NLS-2$
                        + " as channel was hung up"); //$NON-NLS-1$

            }

            else if (isSameChannel(this._transferTargetParty, channel))
            {
                this.setTransferTargetParty(null);
                logger.debug("Removed transferTarget=" + channel + " from Call=" + this._uniquecallID //$NON-NLS-1$ //$NON-NLS-2$
                        + " as channel was hung up"); //$NON-NLS-1$
            }

            else
            {
                logger.warn("Received hangup for unknown channel=" + channel.getChannelId() + " on Call=" //$NON-NLS-1$ //$NON-NLS-2$
                        + this._uniquecallID + " as channel was hung up"); //$NON-NLS-1$
            }
            if (!this.isLive())
            {
                notifyCallHangupListeners();
            }
        }
        catch (PBXException e)
        {
            // not much we can do here so just log it
            logger.error(e, e);
        }
    }

    private boolean isSameChannel(Channel lhs, Channel rhs)
    {
        return lhs != null && rhs != null && lhs.isSame(rhs);
    }

    // private int findChannel(iChannel channel)
    // {
    // int index = -1;
    //
    // synchronized (this.channelList)
    // {
    // for (int i = 0; i < this.channelList.size(); i++)
    // {
    // iChannel aChannel = this.channelList.get(i);
    // if (aChannel.isSame(channel))
    // {
    // index = i;
    // break;
    // }
    // }
    // }
    // return index;
    // }

    private void notifyCallHangupListeners()
    {
        for (CallHangupListener listener : this._hangupListeners)
        {
            listener.callHungup(this);
        }

    }

    /**
     * Returns the originating channel for this call. e.g. the first channel
     * that came up. Whilst a call must always start with an originating channel
     * as a call is shutting down it will get to the point where it will have no
     * channels. In these cases the return will be null.
     * 
     * @return null or the originating channel.
     */

    @Override
    public Channel getOriginatingParty()
    {
        return this._originatingParty;
    }

    // /**
    // * Call this method to mark this call as owned by another instance of NJR
    // as
    // * that instance has answered the call. The only time a calls ownership
    // can
    // * transition is when the call is answered by an instances of NJR or the
    // * call is transfered to the vacant transfer extension. Roaming calls are
    // * still owned by the NJR instance that originally answered the call.
    // */
    // public void callAnsweredRemotely()
    // {
    // this._owner = OWNER.REMOTE;
    //
    // }

    // private void callIsTransferring() throws PBXException
    // {
    // if (this.holdStarted == null)
    // {
    // this.holdStarted = new Date();
    // }
    // this._state = Call.CallState.TRANSFER;
    // }

    /**
     * Call this method to indicate that this is an outbound call originated by
     * this NJR instance. These type of calls are not seen by a remote operator
     * unless they are put on hold.
     * 
     * @param remoteChannel - the remote channel this call connected to when
     *            dialing.
     * @throws PBXException
     */
    public void callDialedOut(Channel remoteChannel, CallerID fromClid, CallerID toClid) throws PBXException
    {

        // -- moved to ANSWER
    }

    @Override
    public Date getCallStartTime()
    {
        return this._callStarted;
    }

    public Integer getContactId()
    {
        return this._contactId;
    }

    /**
     * The current transfer target.
     * 
     * @return
     */
    public EndPoint getTransferTarget()
    {
        return this._transferTarget;
    }

    public CallerID getTransferTargetCallerID()
    {
        return this._transferTargetCallerID;
    }

    @Override
    public CallerID getAcceptingPartyCallerID()
    {
        return this._acceptingPartyCallerID;
    }

    @Override
    public CallerID getOriginatingPartyCallerID()
    {
        return this._originatingPartyCallerID;
    }

    @Override
    public CallerID getRemotePartyCallerID()
    {
        CallerID remoteCallerID = null;

        if (this.getDirection() == CallDirection.INBOUND)
        {
            remoteCallerID = this.getOriginatingPartyCallerID();
        }
        else
        {
            remoteCallerID = this.getAcceptingPartyCallerID();
        }
        return remoteCallerID;
    }

    public Date getHoldStartTime()
    {
        return this._holdStarted;
    }

    public EndPoint getParkingLot()
    {
        return this._parkingLot;
    }

    public Date getTimeAtDialIn()
    {
        return this._timeAtDialin;
    }

    public TransferType getTransferType()
    {
        return this.transferType;
    }

    public String getUniqueCallID()
    {
        /*
         * this is a unique call identifier.
         */
        return "" + this._uniquecallID; //$NON-NLS-1$
    }

    public boolean isCallParked()
    {
        return this._parked;
    }

    private void setOriginatingParty(Channel originatingParty) throws PBXException
    {
        this._originatingParty = originatingParty;

        if (originatingParty != null)
        {
            if (!this._originatingParty.canDetectHangup() && this._acceptingParty != null
                    && !this._acceptingParty.canDetectHangup())
                throw new PBXException("Call.BothWithNoHangupDetection"); //$NON-NLS-1$

            this._originatingParty.addHangupListener(this);
        }
    }

    private void setAcceptingParty(Channel acceptingParty) throws PBXException
    {
        this._acceptingParty = acceptingParty;

        if (acceptingParty != null)
        {
            if (!this._acceptingParty.canDetectHangup() && this._originatingParty != null
                    && !this._originatingParty.canDetectHangup())
                throw new PBXException("Call.BothWithNoHangupDetection"); //$NON-NLS-1$

            this._acceptingParty.addHangupListener(this);
        }
    }

    private void setTransferTargetParty(Channel transferTargetParty)
    {
        this._transferTargetParty = transferTargetParty;

        if (transferTargetParty != null)
        {
            this._transferTargetParty.addHangupListener(this);
        }
    }

    /**
     * The contact id is just used when refilling the list of contacts so we
     * know which one is selected at the moment. It probably should be stored
     * here!
     * 
     * @param _contactId
     */
    public void setContactId(final Integer _contactId)
    {
        this._contactId = _contactId;

    }

    // private void setTransferType(final TransferType tType)
    // {
    // this.transferType = tType;
    // }

    @Override
    public CallDirection getDirection()
    {
        return this._direction;
    }

    /**
     * Call this method to get a notification when this call hangs up.
     * 
     * @param listener
     * @return true if the hangup listener was added. False if the call has
     *         already been hungup.
     */
    public boolean addHangupListener(CallHangupListener listener)
    {
        boolean callStillUp = true;

        if ((this._originatingParty != null && this._originatingParty.isLive())
                || (this._acceptingParty != null && this._acceptingParty.isLive())
                || (this._transferTargetParty != null && this._transferTargetParty.isLive()))
            this._hangupListeners.add(listener);
        else
            callStillUp = false;
        return callStillUp;

    }

    /**
     * returns true if the call has any active channels.
     * 
     * @return
     */
    public boolean isLive()
    {
        boolean live = false;
        if ((this._originatingParty != null && this._originatingParty.isLive())
                || (this._acceptingParty != null && this._acceptingParty.isLive())
                || (this._transferTargetParty != null && this._transferTargetParty.isLive()))
            live = true;
        return live;
    }

    /**
     * @return true if this call is owned by another instance of NJR.
     */
    public OWNER getOwner()
    {
        return this._owner;
    }

    // /**
    // * Returns a shallow clone of the list of channels.
    // */
    // @SuppressWarnings("unchecked")
    // @Override
    // public Collection<iChannel> getChannels()
    // {
    // synchronized (this.channelList)
    // {
    // return (Collection<iChannel>) this.channelList.clone();
    // }
    // }

    @Override
    public String toString()
    {
        return "State: Originating Channel:" + getOriginatingParty() + " Direction: " //$NON-NLS-1$ //$NON-NLS-2$
                                                                                      // //$NON-NLS-3$
                + this.getDirection() + " accepting:" + this._acceptingParty; //$NON-NLS-1$
    }

    @Override
    public boolean contains(Channel parkChannel)
    {
        boolean found = false;

        if (isSameChannel(this._originatingParty, parkChannel))
        {
            found = true;
        }
        else if (isSameChannel(this._acceptingParty, parkChannel))
        {
            found = true;
        }

        else if (isSameChannel(this._transferTargetParty, parkChannel))
        {
            found = true;
        }

        return found;
    }

    @Override
    public Channel getAcceptingParty()
    {
        return this._acceptingParty;
    }

    @Override
    public Channel getTransferTargetParty()
    {
        return this._transferTargetParty;
    }

    @Override
    /**
     * This method use the CallDirection to determine which leg of the call is
     * the local call and which is the called/calling party. It then returns the
     * called/calling party.
     * 
     * @return
     */
    public Channel getRemoteParty()
    {
        Channel remoteChannel = null;

        if (this.getDirection() == CallDirection.INBOUND)
        {
            remoteChannel = this.getOriginatingParty();
        }
        else
        {
            remoteChannel = this.getAcceptingParty();
        }
        if (remoteChannel == null)
        {
            logger.error("remoteChannel is null for {}" + this.getDirection());
        }
        return remoteChannel;

    }

    /**
     * This method use the CallDirection to determine which leg of the call is
     * the local call and which is the called/calling party. It then returns the
     * local call .
     * 
     * @return
     */
    @Override
    public Channel getLocalParty()
    {

        if (this.getDirection() == CallDirection.OUTBOUND)
            return this.getOriginatingParty();

        return this.getAcceptingParty();

    }

    @Override
    public boolean canSplit()
    {
        return (this._originatingParty != null && this._acceptingParty != null && this._transferTargetParty == null);
    }

    @Override
    public Channel getOperandChannel(OperandChannel operand)
    {
        final Channel channel;

        switch (operand)
        {
            case ACCEPTING_PARTY :
                channel = this._acceptingParty;
                break;
            case ORIGINATING_PARTY :
                channel = this._originatingParty;
                break;
            case REMOTE_PARTY :
                channel = getRemoteParty();
                break;
            case TRANSFER_TARGET_PARTY :
                channel = this._transferTargetParty;
                break;
            case LOCAL_PARTY :
                channel = this.getLocalParty();
                break;
            default :
                // should never happen
                throw new IllegalArgumentException("An unsupported operand was passed:" + operand); //$NON-NLS-1$
        }
        if (channel == null)
        {
            logger.warn("failed to get channel for " + operand);
        }
        return channel;
    }

    public boolean isSame(Call rhs)
    {
        return this._uniquecallID == ((CallImpl) rhs)._uniquecallID;
    }

    /**
     * Returns all of the Channels associated with this call.
     */
    @Override
    public List<Channel> getChannels()
    {
        List<Channel> channels = new LinkedList<>();
        if (_acceptingParty != null)
            channels.add(_acceptingParty);
        if (_originatingParty != null)
            channels.add(_originatingParty);
        if (_transferTargetParty != null)
            channels.add(_transferTargetParty);
        return channels;
    }

}
