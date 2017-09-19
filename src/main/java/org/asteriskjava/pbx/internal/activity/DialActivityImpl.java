package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.Map;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallDirection;
import org.asteriskjava.pbx.CallImpl;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.NewChannelListener;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.asterisk.wrap.actions.SetVarAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.managerAPI.Dial;
import org.asteriskjava.pbx.internal.managerAPI.OriginateResult;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class DialActivityImpl extends ActivityHelper<DialActivity> implements DialActivity, NewChannelListener
{
    private static final Log logger = LogFactory.getLog(DialActivityImpl.class);

    private final boolean hideToCallerId;

    private boolean cancelledByOperator;

    private final EndPoint _originating;

    /**
     * Once the 'from' channel has been brought up this is set.
     */
    private Channel originatingChannel;

    private final EndPoint _accepting;

    private final CallerID toCallerID;

    /**
     * Once the 'to' channel has been brought up this is set.
     */
    private Channel acceptingChannel;

    private CallImpl newCall;

    private Map<String, String> channelVarsToSet;

    public DialActivityImpl(final EndPoint originating, final EndPoint accepting, final CallerID toCallerID,
            final boolean hideToCallerID, final ActivityCallback<DialActivity> listener,
            Map<String, String> channelVarsToSet)
    {
        super("Dial", listener);
        this._originating = originating;
        this._accepting = accepting;
        this.toCallerID = toCallerID;
        this.hideToCallerId = hideToCallerID;
        this.cancelledByOperator = false;
        this.channelVarsToSet = channelVarsToSet;

        this.startActivity(false);
    }

    @Override
    public boolean doActivity() throws PBXException
    {
        boolean success = false;

        try (Dial nr = new Dial(this.toCallerID.toString()))
        {
            DialActivityImpl.logger.debug("*******************************************************************************");
            DialActivityImpl.logger.info("***********                    begin dial out                  ****************");
            DialActivityImpl.logger.info("***********               " + this._accepting + "             ****************");
            DialActivityImpl.logger.debug("*******************************************************************************");

            final AsteriskSettings profile = PBXFactory.getActiveProfile();

            final OriginateResult[] resultChannels = nr.dial(this, this._originating, this._accepting,
                    profile.getManagementContext(), this.toCallerID, this.hideToCallerId, channelVarsToSet);

            if ((resultChannels[0] == null) || (resultChannels[1] == null))
            {
                // the dial failed

                // so notify the operator.
                // Unless the operated cancelled the call
                if (!this.cancelledByOperator)
                {
                    this.setLastException(new PBXException(("OperatorEndedCall")));
                    DialActivityImpl.logger.error("dialout to " + this._accepting.getFullyQualifiedName() + " failed.");
                }
            }
            else
            {
                // Dial succeeded.

                // - get new channel name from result
                this.originatingChannel = resultChannels[0].getChannel();
                this.acceptingChannel = resultChannels[1].getChannel();

                newCall = new CallImpl(originatingChannel, acceptingChannel, CallDirection.OUTBOUND);

                DialActivityImpl.logger.debug("dialout succeeded: dest channel is :" + this.acceptingChannel);

                if (this.validateChannel(this.acceptingChannel) == false)
                {
                    this.setLastException(new PBXException(("dialed extension hungup unexpectedly")));
                    DialActivityImpl.logger.error("dialed extension hungup unexpectedly");
                }
                else
                {
                    success = true;
                }
            }
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
            if (this.acceptingChannel != null)
            {
                logger.warn("Hanging up");
                pbx.hangup(this.acceptingChannel);
            }
            if (this.originatingChannel != null)
            {
                logger.warn("Hanging up");
                pbx.hangup(this.originatingChannel);
            }
        }
        catch (final Exception e)
        {
            DialActivityImpl.logger.error(e, e);
        }
    }

    /**
     * Called to cancels the dial before it fully comes up. The caller must also
     * hangup the associated channels.
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
    public Channel getAcceptingChannel()
    {
        return this.acceptingChannel;
    }

    @Override
    public EndPoint getOriginatingEndPoint()
    {
        return this._originating;
    }

    @Override
    public EndPoint getAcceptingEndPoint()
    {
        return this._accepting;
    }

    @Override
    public void channelUpdate(final Channel channel)
    {
        if (channel.sameEndPoint(this._accepting))
        {
            this.acceptingChannel = channel;
        }

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
            DialActivityImpl.logger.debug(e, e);
            DialActivityImpl.logger.error("getVariable: " + e);
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

    @Override
    public Call getNewCall()
    {
        return newCall;
    }

}
