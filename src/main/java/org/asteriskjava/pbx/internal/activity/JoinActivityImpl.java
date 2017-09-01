package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.Call.OperandChannel;
import org.asteriskjava.pbx.CallDirection;
import org.asteriskjava.pbx.CallImpl;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.JoinActivity;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * The JoinActivity is used by the AsteriksPBX implementation to Join two
 * channels into a single call. The join activity expects two channels which are
 * each a leg of a different call it will then join those two channels into a
 * single call.iChannel hangupChannel, The channels that remain in the
 * originating calls will be hungup implicitly.
 * 
 * @author bsutton
 */
public class JoinActivityImpl extends ActivityHelper<JoinActivity> implements JoinActivity
{

    private static final Log logger = LogFactory.getLog(JoinActivityImpl.class);

    private final Call _lhsCall;
    private final OperandChannel _originatingOperand;

    private final Call _rhsCall;
    private final OperandChannel _acceptingOperand;
    private final CallDirection _direction;

    private Call _joined;

    private Exception callSite;

    /**
     * Joins a specific channel from this call with a specific channel from
     * another call which results in a new Call object being created. Channels
     * that do not participate in the join are left in their original Call.
     * 
     * @param lhsCall one of the calls we are joining
     * @param originatingOperand the channel from lhsCall call that will
     *            participate in the join as the originating Channel
     * @param rhsCall the other call we are joining.
     * @param acceptingOperand the channel from the rhsCall that will
     *            participate in the join as the accepting channel.
     * @return
     * @throws PBXException
     */
    public JoinActivityImpl(final Call lhsCall, OperandChannel originatingOperand, final Call rhsCall,
            OperandChannel acceptingOperand, CallDirection direction, final ActivityCallback<JoinActivity> listener)
    {
        super("JoinActivity", listener);

        callSite = new Exception("Call site");

        this._lhsCall = lhsCall;
        this._originatingOperand = originatingOperand;
        this._rhsCall = rhsCall;
        this._acceptingOperand = acceptingOperand;
        this._direction = direction;

        if (this._lhsCall == null)
        {
            throw new IllegalArgumentException("lhsCall may not be null");
        }

        if (this._originatingOperand == null)
        {
            throw new IllegalArgumentException("lhsOperand may not be null");
        }

        if (this._rhsCall == null)
        {
            throw new IllegalArgumentException("rhsCall may not be null");
        }

        if (this._acceptingOperand == null)
        {
            throw new IllegalArgumentException("rhsOperand may not be null");
        }

        this.startActivity(true);
    }

    @Override
    public boolean doActivity() throws PBXException
    {
        boolean success = false;
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        JoinActivityImpl.logger.debug("*******************************************************************************");
        JoinActivityImpl.logger.info("***********                    begin join               ****************");
        JoinActivityImpl.logger.info("***********            " + this._lhsCall + "                 ****************");
        JoinActivityImpl.logger.debug("***********            " + this._rhsCall + "                 ****************");
        JoinActivityImpl.logger.debug("*******************************************************************************");

        try
        {
            final Channel rhsChannel = this._rhsCall.getOperandChannel(this._acceptingOperand);
            final Channel lhsChannel = this._lhsCall.getOperandChannel(this._originatingOperand);

            if (!pbx.moveChannelToAgi(rhsChannel))
            {
                throw new PBXException("Channel: " + rhsChannel + " couldn't be moved to agi");
            }
            if (!pbx.moveChannelToAgi(lhsChannel))
            {
                throw new PBXException("Channel: " + lhsChannel + " couldn't be moved to agi");
            }

            List<Channel> channels = new LinkedList<>();
            channels.add(lhsChannel);
            channels.add(rhsChannel);
            if (!pbx.waitForChannelsToQuiescent(channels, 3000))
            {
                logger.error(callSite, callSite);
                throw new PBXException("Channel: " + rhsChannel + " cannot be joined as it is still in transition.");
            }
            // Now do the actual join on the pbx.
            pbx.bridge(lhsChannel, rhsChannel);
            this._joined = ((CallImpl) this._lhsCall).join(this._originatingOperand, this._rhsCall, this._acceptingOperand,
                    this._direction);
            success = true;

        }
        catch (RuntimeException e)
        {
            logger.error(e, e);
            logger.error(callSite, callSite);
            throw new PBXException(e);

        }
        return success;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        return required;
    }

    @Override
    synchronized public void onManagerEvent(final ManagerEvent event)
    {
        // NOOP
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

    @Override
    public Call getJoinedCall()
    {
        return this._joined;
    }

}
