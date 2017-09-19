package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.Call.OperandChannel;
import org.asteriskjava.pbx.CallDirection;
import org.asteriskjava.pbx.CallImpl;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.BlindTransferActivity;
import org.asteriskjava.pbx.agi.AgiChannelActivityBlindTransfer;
import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.DialEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.HangupEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.LinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.UnlinkEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * The BlindTransferActivity is used by the AsteriksPBX to transfer a live
 * channel to an endpoint that may need to be dialed.
 * 
 * @author bsutton
 */
public class BlindTransferActivityImpl extends ActivityHelper<BlindTransferActivity> implements BlindTransferActivity
{
    private static final Log logger = LogFactory.getLog(BlindTransferActivityImpl.class);

    private Call _call;

    private Call.OperandChannel _channelToTransfer;

    private EndPoint _transferTarget;

    private CallerID _toCallerID;

    private boolean _autoAnswer;

    private CountDownLatch _latch;

    private CompletionCause _completionCause;

    private long _timeout;

    /**
     * The new channel that was originated for the transferTarget endpoint.
     */
    private Channel _transferTargetChannel;

    private CallImpl _newCall;

    private Channel dialedChannel;

    final Channel actualChannelToTransfer;

    /**
     * Blind transfers a live channel to a given endpoint which may need to be
     * dialed. When we dial the endpoint we display the 'toCallerID'.
     * 
     * @param channelToTransfer The channel which is being transfered
     * @param transferTarget The target to transfer (connect) the channel to.
     * @param toCallerID The caller id to display on the target endpoint.
     * @param timeout The time to wait (in seconds) for the activity to
     *            complete.
     * @param listener
     */
    public BlindTransferActivityImpl(Call call, final Call.OperandChannel channelToTransfer, final EndPoint transferTarget,
            final CallerID toCallerID, boolean autoAnswer, long timeout,
            final ActivityCallback<BlindTransferActivity> listener)
    {
        super("BlindTransferActivity", listener);

        this._call = call;
        this._channelToTransfer = channelToTransfer;
        this._transferTarget = transferTarget;
        this._toCallerID = toCallerID;
        this._autoAnswer = autoAnswer;
        this._timeout = timeout;

        actualChannelToTransfer = _call.getOperandChannel(this._channelToTransfer);

        this.startActivity(true);
    }

    public BlindTransferActivityImpl(Channel agentChannel, EndPoint transferTarget, CallerID toCallerID, boolean autoAnswer,
            int timeout, ActivityCallback<BlindTransferActivity> iCallback) throws PBXException
    {
        super("BlindTransferActivity", iCallback);

        this._transferTarget = transferTarget;
        this._toCallerID = toCallerID;
        this._autoAnswer = autoAnswer;
        this._timeout = timeout;
        actualChannelToTransfer = agentChannel;
        _channelToTransfer = OperandChannel.ORIGINATING_PARTY;
        this._call = new CallImpl(agentChannel, CallDirection.OUTBOUND);
        this.startActivity(true);

    }

    @Override
    public boolean doActivity() throws PBXException
    {

        boolean success = false;
        AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        /*
         * initiate a blind transfer of the call , to the specified destination.
         * when a link event occurs this call will be removed from the active
         * call list.
         */
        try
        {

            logger.debug("*******************************************************************************");
            logger.info("***********                    begin blind transfer            ****************");
            logger.info("***********          " + this._channelToTransfer + "                           ****************");
            logger.debug("***********            " + this._transferTarget + "                            ****************");
            logger.debug("***********            " + this._toCallerID + "                            ****************");
            logger.debug("*******************************************************************************");

            if (!pbx.moveChannelToAgi(actualChannelToTransfer))
            {
                throw new PBXException("Channel: " + this._channelToTransfer + " couldn't be moved to agi");
            }

            if (!pbx.waitForChannelToQuiescent(actualChannelToTransfer, 3000))
                throw new PBXException(
                        "Channel: " + this._channelToTransfer + " cannot be transfered as it is still in transition.");

            // Set or clear the auto answer header.
            // Clearing is important as it may have been set during the
            // initial answer sequence. If we don't clear it then then transfer
            // target will be auto-answered, which is fun but bad.
            String sipHeader = null;
            if (this._autoAnswer)
            {
                sipHeader = PBXFactory.getActiveProfile().getAutoAnswer();
            }
            actualChannelToTransfer.setCurrentActivityAction(new AgiChannelActivityBlindTransfer(
                    this._transferTarget.getFullyQualifiedName(), sipHeader, _toCallerID.getNumber()));

            // TODO: At one point we were adding the /n option to the end of the
            // channel to get around
            // secondary transfer options. Need to review if this is still
            // required.
            // TODO control the caller id.
            this._latch = new CountDownLatch(1);

            success = this._latch.await(this._timeout, TimeUnit.SECONDS);
            if (!success)
            {
                this._completionCause = CompletionCause.TIMEOUT;
            }
            else if (_completionCause == CompletionCause.CANCELLED)
            {
                logger.warn("Cancelled, hanging up dialed channel");
                pbx.hangup(dialedChannel);
            }

            else
            {
                Call call = ((CallImpl) _call).split(actualChannelToTransfer);
                Call rhsCall = new CallImpl(_transferTargetChannel, CallDirection.OUTBOUND);
                try
                {
                    _newCall = ((CallImpl) call).join(OperandChannel.ORIGINATING_PARTY, rhsCall,
                            OperandChannel.ORIGINATING_PARTY, CallDirection.OUTBOUND);
                }
                catch (Exception e)
                {
                    logger.error("New call doesn't seem to exist!?");
                }
            }
        }
        catch (final Exception e)
        {
            logger.error("error occurred in blindtransfer", e);
            this.setLastException(new PBXException(e));
        }

        return this._completionCause == CompletionCause.BRIDGED;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(BridgeEvent.class);
        required.add(LinkEvent.class);
        required.add(UnlinkEvent.class);
        required.add(HangupEvent.class);
        required.add(DialEvent.class);
        return required;
    }

    @Override
    synchronized public void onManagerEvent(final ManagerEvent event)
    {
        if (event instanceof BridgeEvent)
        {
            BridgeEvent bridge = (BridgeEvent) event;
            if (bridge.isLink())
            {
                if (bridge.getChannel1().isSame(_call.getOperandChannel(this._channelToTransfer)))
                {
                    this._completionCause = CompletionCause.BRIDGED;
                    this._transferTargetChannel = bridge.getChannel2();
                    this._latch.countDown();
                }
                else if (bridge.getChannel2().isSame(_call.getOperandChannel(this._channelToTransfer)))
                {
                    this._completionCause = CompletionCause.BRIDGED;
                    this._transferTargetChannel = bridge.getChannel1();
                    this._latch.countDown();
                }
            }
        }
        else if (event instanceof HangupEvent)
        {
            HangupEvent hangup = (HangupEvent) event;
            if (hangup.getChannel().isSame(_call.getOperandChannel(this._channelToTransfer)))
            {
                this._completionCause = CompletionCause.HANGUP;
                this._latch.countDown();
            }
            if (hangup.getChannel().isSame(dialedChannel))
            {
                this._completionCause = CompletionCause.HANGUP;
                this._latch.countDown();
            }
        }
        else if (event instanceof DialEvent)
        {
            if (((DialEvent) event).getChannel().isSame(_call.getOperandChannel(_channelToTransfer)))
            {
                DialEvent de = (DialEvent) event;
                dialedChannel = de.getDestination();
            }
        }

    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

    @Override
    public CompletionCause getCompletionCause()
    {
        return this._completionCause;
    }

    /**
     * cancels the BlindTransfer.
     */
    @Override
    public void cancel()
    {
        this._completionCause = CompletionCause.CANCELLED;
        this._latch.countDown();
    }

    @Override
    public Channel getChannelToTransfer()
    {
        return _call.getOperandChannel(this._channelToTransfer);
    }

    @Override
    public CallerID getTransferTargetCallerID()
    {
        return this._toCallerID;
    }

    @Override
    public EndPoint getTransferTarget()
    {
        return this._transferTarget;
    }

    @Override
    public Channel getTransferTargetChannel()
    {
        return this._transferTargetChannel;
    }

    @Override
    public Call getNewCall() throws PBXException
    {
        return _newCall;
    }
}
