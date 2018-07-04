package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.Map;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.NewChannelListener;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.DialToAgiActivity;
import org.asteriskjava.pbx.asterisk.wrap.actions.SetVarAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.managerAPI.DialToAgi;
import org.asteriskjava.pbx.internal.managerAPI.OriginateResult;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class DialToAgiActivityImpl extends ActivityHelper<DialToAgiActivity> implements DialToAgiActivity, NewChannelListener
{
    private static final Log logger = LogFactory.getLog(DialToAgiActivityImpl.class);

    private final boolean hideToCallerId;

    private boolean cancelledByOperator;

    private final EndPoint _originating;

    /**
     * Once the 'from' channel has been brought up this is set.
     */
    private Channel originatingChannel;

    private final CallerID toCallerID;

    private Map<String, String> channelVarsToSet;

    private AgiChannelActivityAction action;

    private DialToAgi originator;

    private Integer timeout;

    public DialToAgiActivityImpl(final EndPoint originating, final CallerID toCallerID, Integer timeout,
            final boolean hideToCallerID, final ActivityCallback<DialToAgiActivity> listener,
            Map<String, String> channelVarsToSet, AgiChannelActivityAction action)
    {
        super("Dial", listener);
        this.timeout = timeout;
        this.action = action;
        this._originating = originating;
        this.toCallerID = toCallerID;
        this.hideToCallerId = hideToCallerID;
        this.cancelledByOperator = false;
        this.channelVarsToSet = channelVarsToSet;

    }

    public void dial()
    {
        this.startActivity(false);
    }

    @Override
    public boolean doActivity() throws PBXException
    {
        boolean success = false;

        try (DialToAgi nr = new DialToAgi(this.toCallerID.toString()))
        {
            DialToAgiActivityImpl.logger.debug("**************************************************************************");
            DialToAgiActivityImpl.logger.info("***********                begin dial out to agi               ***********");
            DialToAgiActivityImpl.logger.debug("**************************************************************************");

            originator = nr;

            final OriginateResult[] resultChannels = nr.dial(this, this._originating, this.action, this.toCallerID,
                    this.timeout, this.hideToCallerId, channelVarsToSet);

            if (resultChannels[0] == null || !resultChannels[0].isSuccess())
            {
                // the dial failed

                // so notify the operator.
                // Unless the operated cancelled the call
                if (!this.cancelledByOperator)
                {
                    this.setLastException(new PBXException(("OperatorEndedCall")));
                    logger.warn("dialout to  failed.");
                }
            }
            else
            {
                // Dial succeeded.

                // - get new channel name from result
                this.originatingChannel = resultChannels[0].getChannel();

                DialToAgiActivityImpl.logger.debug("dialout succeeded");

                success = true;

            }
        }
        catch (InterruptedException e)
        {
            logger.error(e);
        }
        finally
        {
            if (success != true)
            {
                this.hangup();
            }
        }
        return success;
    }

    private void hangup()
    {
        try
        {
            final PBX pbx = PBXFactory.getActivePBX();

            if (this.originatingChannel != null)
            {
                logger.info("Hanging up");
                pbx.hangup(this.originatingChannel);
            }
        }
        catch (final Exception e)
        {
            DialToAgiActivityImpl.logger.error(e, e);
        }
    }

    /**
     * Called if the user cancels the call after starting it. The caller must
     * also hangup the trc channel.
     */
    @Override
    public void markCancelled()
    {
        this.cancelledByOperator = true;
    }

    @Override
    public Channel getOriginatingChannel()
    {
        return this.originatingChannel;
    }

    @Override
    public EndPoint getOriginatingEndPoint()
    {
        return this._originating;
    }

    @Override
    public void channelUpdate(final Channel channel)
    {

        if (channel.sameEndPoint(this._originating))
        {
            this.originatingChannel = channel;
        }

        super.progess(this, "Channel for " + channel.getEndPoint().getSIPSimpleName() + " is now up.");

    }

    /*
     * Attempt to set a variable on the channel to see if it's up.
     * @param channel the channel which is to be tested.
     */
    @Override
    public boolean validateChannel(final Channel channel)
    {

        boolean ret = false;
        final SetVarAction var = new SetVarAction(channel, "testState", "1");

        ManagerResponse response = null;
        try
        {
            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
            response = pbx.sendAction(var, 500);
        }
        catch (final Exception e)
        {
            DialToAgiActivityImpl.logger.debug(e, e);
            DialToAgiActivityImpl.logger.error("getVariable: " + e);
        }
        if ((response != null) && (response.getAttribute("Response").compareToIgnoreCase("success") == 0))
        {
            ret = true;
        }

        return ret;

    }

    @Override
    public boolean cancelledByOperator()
    {
        return this.cancelledByOperator;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();
        // no required.
        return required;
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

    @Override
    public void onManagerEvent(ManagerEvent event)
    {
        // NOOP
    }

    public void abort()
    {
        if (originator != null)
        {
            originator.abort();
        }
        else
        {
            logger.error("Call to abort, but it doesn't look like the Dial had started yet");
        }
    }

}
