package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallDirection;
import org.asteriskjava.pbx.CallImpl;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.RedirectToActivity;
import org.asteriskjava.pbx.agi.AgiChannelActivityHold;
import org.asteriskjava.pbx.asterisk.wrap.actions.RedirectAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.ChannelProxy;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * The SplitActivity is used by the AsteriksPBX to split a call and place the
 * component channels into the specialist Activity fastagi. The SplitActivity
 * can only split a call with two channels. The (obvious?) limitation is that we
 * can't split something in a conference call as it has more than two channels.
 * 
 * @author bsutton
 */
public class RedirectToActivityImpl extends ActivityHelper<RedirectToActivity> implements RedirectToActivity
{
    private static final Log logger = LogFactory.getLog(RedirectToActivityImpl.class);

    private Channel channel1;

    private Call _lhsCall;

    /**
     * Splits a call by moving each of its two channels into the Activity agi.
     * The channels will sit in the agi (with no audio) until something is done
     * with them. As such you should leave them split for too long.
     * 
     * @param callToSplit The call to split
     * @param listener
     */
    public RedirectToActivityImpl(final Channel channel1, final ActivityCallback<RedirectToActivity> listener)
    {
        super("SplitActivity", listener);

        this.channel1 = channel1;

        this.startActivity(true);
    }

    @Override
    public boolean doActivity() throws PBXException
    {

        RedirectToActivityImpl.logger.debug("***************************************************************************");
        RedirectToActivityImpl.logger.info("***********            begin redirect to activity           ****************");
        RedirectToActivityImpl.logger.info("***********            " + this.channel1 + "                 ****************");
        RedirectToActivityImpl.logger.debug("***************************************************************************");

        // Splits the originating and secondary channels by moving each of them
        // into the associated
        // target.
        boolean success = false;

        if (this.channel1 != null)
        {
            success = splitTwo();

            // Now update the call to reflect the split
            if (success)
            {
                this._lhsCall = new CallImpl(channel1, CallDirection.OUTBOUND);
            }
        }
        return success;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        // No events required.
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

    /**
     * After a call has been split we get two new calls. One will hold the
     * original remote party and the other will hold the original local party.
     * 
     * @return the call which holds the original remote party.
     */
    @Override
    public Call getCall()
    {
        return this._lhsCall;
    }

    /**
     * After a call has been split we get two new calls. One will hold the
     * original remote party and the other will hold the original local party.
     * 
     * @return the call which holds the original local party.
     */

    /**
     * Splits two channels moving them to defined endpoints.
     * 
     * @param lhs
     * @param lhsTarget
     * @param lhsTargetContext
     * @param rhs
     * @param rhsTarget
     * @param rhsTargetContext
     * @return
     * @throws PBXException
     */
    private boolean splitTwo() throws PBXException
    {
        final AsteriskSettings profile = PBXFactory.getActiveProfile();
        AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        List<Channel> channels = new LinkedList<>();
        channels.add(channel1);
        if (!pbx.waitForChannelsToQuiescent(channels, 3000))
        {
            logger.error(callSite, callSite);
            throw new PBXException("Channel: " + channel1 + " cannot be split as they are still in transition.");
        }

        /*
         * redirects the specified channels to the specified endpoints. Returns
         * true or false reflecting success.
         */

        AgiChannelActivityHold agi1 = new AgiChannelActivityHold();

        pbx.setVariable(channel1, "proxyId", "" + ((ChannelProxy) channel1).getIdentity());

        channel1.setCurrentActivityAction(agi1);

        final String agiExten = profile.getAgiExtension();
        final String agiContext = profile.getManagementContext();
        logger.debug("redirect channel lhs:" + channel1 + " to " + agiExten + " in context " + agiContext);

        final EndPoint extensionAgi = pbx.getExtensionAgi();
        final RedirectAction redirect = new RedirectAction(channel1, agiContext, extensionAgi, 1);
        // logger.error(redirect);

        boolean ret = false;
        {
            try
            {

                // final ManagerResponse response =
                pbx.sendAction(redirect, 1000);
                double ctr = 0;
                while (!agi1.hasCallReachedAgi() && ctr < 10)
                {
                    Thread.sleep(100);
                    ctr += 100.0 / 1000.0;
                    if (!agi1.hasCallReachedAgi())
                    {
                        logger.warn("Waiting on (agi1) " + channel1);
                    }

                }
                ret = agi1.hasCallReachedAgi();

            }
            catch (final Exception e)
            {
                logger.error(e, e);
            }
        }
        return ret;
    }

}
