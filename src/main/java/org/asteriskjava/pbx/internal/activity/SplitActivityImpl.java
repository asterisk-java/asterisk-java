package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallImpl;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.SplitActivity;
import org.asteriskjava.pbx.agi.AgiChannelActivityBridge;
import org.asteriskjava.pbx.agi.AgiChannelActivityHold;
import org.asteriskjava.pbx.asterisk.wrap.actions.RedirectAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.RenameEvent;
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
public class SplitActivityImpl extends ActivityHelper<SplitActivity> implements SplitActivity
{
    private static final Log logger = LogFactory.getLog(SplitActivityImpl.class);

    private Call _callToSplit;

    private Channel channel1;

    private Channel channel2;

    private Call _lhsCall;

    private Call _rhsCall;

    /**
     * Splits a call by moving each of its two channels into the Activity agi.
     * The channels will sit in the agi (with no audio) until something is done
     * with them. As such you should leave them split for too long.
     * 
     * @param callToSplit The call to split
     * @param listener
     */
    public SplitActivityImpl(final Call callToSplit, final ActivityCallback<SplitActivity> listener)
    {
        super("SplitActivity", listener);

        renameEventReceived.set(new CountDownLatch(1));
        this._callToSplit = callToSplit;

        List<Channel> tmp = callToSplit.getChannels();
        if (tmp.size() > 1)
        {
            channel1 = tmp.get(0);
            channel2 = tmp.get(1);
        }
        this.startActivity(true);

    }

    @Override
    public boolean doActivity() throws PBXException
    {

        SplitActivityImpl.logger.debug("*******************************************************************************");
        SplitActivityImpl.logger.info("***********                    begin split               ****************");
        SplitActivityImpl.logger.info("***********            " + this.channel1 + "                 ****************");
        SplitActivityImpl.logger.debug("***********            " + this.channel2 + "                 ****************");
        SplitActivityImpl.logger.debug("*******************************************************************************");

        // Splits the originating and secondary channels by moving each of them
        // into the associated
        // target.
        boolean success = false;

        if (this.channel2 != null)
        {
            success = splitTwo();

            // Now update the call to reflect the split
            if (success)
            {
                this._lhsCall = ((CallImpl) this._callToSplit).split(channel1);
                this._rhsCall = ((CallImpl) this._callToSplit).split(channel2);
            }
        }
        return success;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(RenameEvent.class);
        return required;
    }

    final AtomicReference<CountDownLatch> renameEventReceived = new AtomicReference<>();

    @Override
    synchronized public void onManagerEvent(final ManagerEvent event)
    {
        if (event instanceof RenameEvent)
        {
            RenameEvent rename = (RenameEvent) event;
            if (rename.getChannel().isSame(channel1) || rename.getChannel().isSame(channel2))
            {
                renameEventReceived.get().countDown();
            }
        }
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
    public Call getLHSCall()
    {
        return this._lhsCall;
    }

    /**
     * After a call has been split we get two new calls. One will hold the
     * original remote party and the other will hold the original local party.
     * 
     * @return the call which holds the original local party.
     */
    @Override
    public Call getRHSCall()
    {
        return this._rhsCall;
    }

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

        if (channel1 == channel2)
        {
            throw new PBXException("channel1 is the same as channel2. if I let this happen, asterisk will core dump :)");
        }

        List<Channel> channels = new LinkedList<>();
        channels.add(channel1);
        channels.add(channel2);
        if (!pbx.waitForChannelsToQuiescent(channels, 3000))
        {
            logger.error(callSite, callSite);
            throw new PBXException(
                    "Channel: " + channel1 + " or " + channel2 + " cannot be split as they are still in transition.");
        }

        /*
         * redirects the specified channels to the specified endpoints. Returns
         * true or false reflecting success.
         */

        AgiChannelActivityHold agi1 = new AgiChannelActivityHold();
        AgiChannelActivityHold agi2 = new AgiChannelActivityHold();

        pbx.setVariable(channel1, "proxyId", "" + ((ChannelProxy) channel1).getIdentity());
        pbx.setVariable(channel2, "proxyId", "" + ((ChannelProxy) channel2).getIdentity());

        // AgiChannelAcitivyBridge has a latch which other activities may be
        // sleeping on, so it's important
        // to change the other channels activity first to avoid it waking and
        // taking an undesired action
        // when the AgiChannelActivityBridge channel has it's acitvity
        // cancelled.
        if (channel2.getCurrentActivityAction() instanceof AgiChannelActivityBridge)
        {
            channel1.setCurrentActivityAction(agi1);
            channel2.setCurrentActivityAction(agi2);
        }
        else
        {
            channel2.setCurrentActivityAction(agi2);
            channel1.setCurrentActivityAction(agi1);
        }

        final String agiExten = profile.getAgiExtension();
        final String agiContext = profile.getManagementContext();
        logger.debug("splitTwo channel lhs:" + channel1 + " to " + agiExten + " in context " + agiContext + " from "
                + this._callToSplit);

        final EndPoint extensionAgi = pbx.getExtensionAgi();
        final RedirectAction redirect = new RedirectAction(channel1, agiContext, extensionAgi, 1);
        redirect.setExtraChannel(channel2);
        redirect.setExtraContext(agiContext);
        redirect.setExtraExten(extensionAgi);
        redirect.setExtraPriority(1);
        // logger.error(redirect);

        boolean ret = false;
        {
            try
            {

                renameEventReceived.set(new CountDownLatch(1));
                // final ManagerResponse response =
                pbx.sendAction(redirect, 1000);
                if (pbx.expectRenameEvents())
                {

                    // wait for channels to unbridge
                    if (!renameEventReceived.get().await(2000, TimeUnit.MILLISECONDS))
                    {
                        logger.error("There was no rename event");
                    }
                    else
                    {
                        logger.warn("Channels are renaming, now waiting for Quiescent");
                    }
                }

                channels.clear();

                channels.add(channel1);
                channels.add(channel2);
                if (!pbx.waitForChannelsToQuiescent(channels, 3000))
                {
                    logger.error(callSite, callSite);
                    throw new PBXException("Channel: " + channel1 + " or " + channel2
                            + " cannot be split as they are still in transition.");
                }

                double ctr = 0;
                while ((!agi1.hasCallReachedAgi() || !agi2.hasCallReachedAgi()) && ctr < 10)
                {
                    Thread.sleep(100);
                    ctr += 100.0 / 1000.0;
                    if (!agi1.hasCallReachedAgi())
                    {
                        logger.info("Waiting on (agi1) " + channel1);
                    }
                    if (!agi2.hasCallReachedAgi())
                    {
                        logger.info("Waiting on (agi2) " + channel2);
                    }
                }
                ret = agi1.hasCallReachedAgi() && agi2.hasCallReachedAgi();

            }
            catch (final Exception e)
            {
                logger.error(e, e);
            }
        }
        return ret;
    }

}
