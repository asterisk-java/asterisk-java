package org.asteriskjava.pbx.internal.managerAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.NewChannelListener;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.OriginateAction;
import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.DialBeginEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.DialEndEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.HangupEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.LinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewChannelEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.OriginateResponseEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.UnlinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public abstract class OriginateBaseClass extends EventListenerBaseClass
{

    /*
     * this class generates and issues ActionEvents to asterisk through the
     * manager. This is the asterisk coal face.
     */
    protected static final Log logger = LogFactory.getLog(OriginateBaseClass.class);

    private volatile String originateID;

    volatile private boolean originateSuccess;

    private final Channel monitorChannel1;

    private boolean hungup = false;

    private Channel newChannel;

    private final Channel monitorChannel2;

    private final OriginateResult result;

    Exception ex = new Exception("Created here");

    /**
     * The following two variables together are used to determine if the
     * originated channel has come up. This is to overcome the problem that the
     * OriginateResponseEvent and the NewChannelEvent can occur in any order
     * (although the NewChannelEvent will occur first in most circumstances).
     * Note: we get many NewChannelEvents but we are only interested in a very
     * specific one.
     */
    // Used to track if the originate event has been seen.
    private boolean originateSeen = false;

    // Used to track if the new (final) channel has been seen.
    private boolean channelSeen = false;

    private final NewChannelListener listener;

    private final CountDownLatch originateLatch = new CountDownLatch(1);

    private final AsteriskPBX pbx;

    private String channelId;

    protected OriginateBaseClass(final NewChannelListener listener, final Channel monitor, final Channel monitor2)
    {
        super("NewOrginateClass", PBXFactory.getActivePBX());
        pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        this.listener = listener;
        this.monitorChannel1 = monitor;
        this.monitorChannel2 = monitor2;
        this.result = new OriginateResult();

    }

    private static final AtomicLong originateSeed = new AtomicLong();

    /**
     * @param local
     * @param target
     * @param myVars
     * @param callerId the caller id to set when initiating this call.
     * @param hideCallerId
     * @param context
     * @return
     */
    OriginateResult originate(final EndPoint local, final EndPoint target, final HashMap<String, String> myVars,
            final CallerID callerID, final Integer timeout, final boolean hideCallerId, final String context)
    {
        OriginateBaseClass.logger.debug("originate called");
        this.originateSeen = false;
        this.channelSeen = false;

        if (this.hungup == true)
        {
            // the monitored channel already hungup so just return false and
            // shutdown
            return null;
        }

        OriginateBaseClass.logger.debug("originate connection endPoint \n" + local + " to endPoint " + target //$NON-NLS-2$
                + " vars " + myVars);
        ManagerResponse response = null;

        final AsteriskSettings settings = PBXFactory.getActiveProfile();

        final OriginateAction originate = new OriginateAction();
        this.originateID = originate.getActionId();

        channelId = "" + (System.currentTimeMillis() / 1000) + ".AJ" + originateSeed.incrementAndGet();
        originate.setChannelId(channelId);

        Integer localTimeout = timeout;

        if (timeout == null)
        {
            localTimeout = 30000;
            try
            {
                localTimeout = settings.getDialTimeout() * 1000;
            }
            catch (final Exception e)
            {
                OriginateBaseClass.logger.error("Invalid dial timeout value");
            }
        }

        // Whilst the originate document says that it takes a channel it
        // actually takes an
        // end point. I haven't check but I'm skeptical that you can actually
        // originate to
        // a channel as the doco talks about 'dialing the channel'. I suspect
        // this
        // may be part of asterisk's sloppy terminology.
        if (local.isLocal())
        {
            originate.setEndPoint(local);
            originate.setOption("/n");
        }
        else
        {
            originate.setEndPoint(local);
        }

        originate.setContext(context);
        originate.setExten(target);
        originate.setPriority(1);

        // Set the caller id.
        if (hideCallerId)
        {
            // hide callerID
            originate.setCallingPres(32);
        }
        else
        {
            originate.setCallerId(callerID);
        }

        originate.setVariables(myVars);
        originate.setAsync(true);
        originate.setTimeout(localTimeout);

        try
        {
            // Just add us as an asterisk event listener.
            this.startListener();

            response = pbx.sendAction(originate, localTimeout);
            OriginateBaseClass.logger.debug("Originate.sendAction completed");
            if (response.getResponse().compareToIgnoreCase("Success") != 0)
            {
                OriginateBaseClass.logger
                        .error("Error Originating call" + originate.toString() + " : " + response.getMessage());//$NON-NLS-2$
                throw new ManagerCommunicationException(response.getMessage(), null);
            }

            // wait the set timeout +1 second to allow for
            // asterisk to start the originate
            originateLatch.await(localTimeout + 1000, TimeUnit.MILLISECONDS);
        }
        catch (final InterruptedException e)
        {
            OriginateBaseClass.logger.debug(e, e);
        }
        catch (final Exception e)
        {
            OriginateBaseClass.logger.error(e, e);
        }
        finally
        {
            this.close();
        }

        if (this.originateSuccess == true)
        {
            this.result.setSuccess(true);
            this.result.setChannelData(this.newChannel);
            OriginateBaseClass.logger.debug("new channel ok: " + this.newChannel);
        }
        else
        {
            OriginateBaseClass.logger.warn("originate failed to connect endPoint: " + local + " to ext " + target); //$NON-NLS-2$

            if (this.newChannel != null)
            {
                try
                {
                    logger.info("Hanging up");
                    pbx.hangup(this.newChannel);
                }
                catch (IllegalArgumentException | IllegalStateException | PBXException e)
                {
                    logger.error(e, e);

                }
            }
        }
        return this.result;
    }

    void abort(final String reason)
    {
        OriginateBaseClass.logger.debug("Aborting originate ");
        this.originateSuccess = false;
        this.result.setAbortReason(reason);
        this.hungup = true;
        if (this.newChannel != null)
        {
            OriginateBaseClass.logger.warn("Aborted, Hangup up on the way out");
            this.result.setChannelHungup(true);

            PBX pbx = PBXFactory.getActivePBX();
            try
            {
                pbx.hangup(this.newChannel);
            }
            catch (IllegalArgumentException | IllegalStateException | PBXException e)
            {
                logger.error(e, e);

            }
        }
        originateLatch.countDown();

    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(OriginateResponseEvent.class);
        required.add(BridgeEvent.class);
        // bridge event is a subclass of linkevent, so we need link & unlink in
        // our list of events to support Asterisk 1.4
        required.add(LinkEvent.class);
        required.add(UnlinkEvent.class);
        required.add(HangupEvent.class);
        required.add(NewChannelEvent.class);
        required.add(DialEndEvent.class);
        required.add(DialBeginEvent.class);

        return required;
    }

    /**
     * It is important that this method is synchronised as there is some
     * interaction between the events and we need to ensure we process one at a
     * time.
     */
    @Override
    synchronized public void onManagerEvent(final ManagerEvent event)
    {

        if (event instanceof HangupEvent)
        {
            final HangupEvent hangupEvt = (HangupEvent) event;
            final Channel hangupChannel = hangupEvt.getChannel();

            if (channelId.equals(hangupEvt.getUniqueId()))
            {
                this.originateSuccess = false;
                OriginateBaseClass.logger.warn("Dest channel " + this.newChannel + " hungup");
                originateLatch.countDown();
            }
            if ((this.monitorChannel1 != null) && (hangupChannel.isSame(this.monitorChannel1)))
            {
                this.originateSuccess = false;
                this.hungup = true;
                if (this.newChannel != null)
                {
                    OriginateBaseClass.logger.debug("hanging up " + this.newChannel);
                    this.result.setChannelHungup(true);

                    PBX pbx = PBXFactory.getActivePBX();
                    try
                    {
                        logger.warn("Hanging up");
                        pbx.hangup(this.newChannel);
                    }
                    catch (IllegalArgumentException | IllegalStateException | PBXException e)
                    {
                        logger.error(e, e);

                    }
                }
                OriginateBaseClass.logger.debug("notify channel 1 hungup");
                originateLatch.countDown();
            }
            if ((this.monitorChannel2 != null) && (hangupChannel.isSame(this.monitorChannel2)))
            {
                this.originateSuccess = false;
                this.hungup = true;
                if (this.newChannel != null)
                {
                    OriginateBaseClass.logger.debug("Hanging up channel " + this.newChannel);
                    this.result.setChannelHungup(true);

                    PBX pbx = PBXFactory.getActivePBX();
                    try
                    {
                        pbx.hangup(this.newChannel);
                    }
                    catch (IllegalArgumentException | IllegalStateException | PBXException e)
                    {
                        logger.error(e, e);

                    }
                }
                OriginateBaseClass.logger.debug("Notify channel 2 (" + this.monitorChannel2 + ") hungup");//$NON-NLS-2$
                originateLatch.countDown();
            }

        }
        if (event instanceof OriginateResponseEvent)
        {
            OriginateBaseClass.logger.debug("response : " + this.newChannel);

            final OriginateResponseEvent response = (OriginateResponseEvent) event;
            OriginateBaseClass.logger.debug("OriginateResponseEvent: channel="
                    + (response.isChannel() ? response.getChannel() : response.getEndPoint()) + " originateID:"
                    + this.originateID);
            OriginateBaseClass.logger.debug("{" + response.getReason() + ":" + response.getResponse() + "}"); //$NON-NLS-2$ //$NON-NLS-3$
            if (this.originateID != null)
            {
                if (this.originateID.compareToIgnoreCase(response.getActionId()) == 0)
                {
                    this.originateSuccess = response.isSuccess();
                    OriginateBaseClass.logger.debug("OriginateResponse: matched actionId, success=" + this.originateSuccess
                            + " channelSeen=" + this.channelSeen);

                    this.originateSeen = true;

                    if (originateSuccess)
                    {
                        if (newChannel == null)
                        {
                            this.newChannel = response.getChannel();
                        }
                        channelSeen = newChannel != null;

                        // if we have also seen the channel then we can notify
                        // the
                        // originate() method
                        // that the call is up. Otherwise we will rely on the
                        // NewChannelEvent doing the
                        // notify.
                        if (this.channelSeen == true)
                        {
                            OriginateBaseClass.logger.info("notify originate response event " + this.originateSuccess);
                            originateLatch.countDown();
                        }
                        else
                        {
                            logger.error("Originate Response didn't contain the channel");
                        }
                    }
                }
            }
            else
            {
                OriginateBaseClass.logger.warn("actionid is null");
            }
        }

        if (event instanceof NewChannelEvent)
        {
            final NewChannelEvent newState = (NewChannelEvent) event;
            if (channelId.equalsIgnoreCase(newState.getChannel().getUniqueId()))
            {
                final Channel channel = newState.getChannel();

                OriginateBaseClass.logger.debug("new channel event :" + channel + " context = " + newState.getContext() //$NON-NLS-2$
                        + " state =" + newState.getChannelStateDesc() + " state =" + newState.getChannelState()); //$NON-NLS-2$

                handleId(channel);
            }
        }

        if (event instanceof BridgeEvent)
        {
            if (((BridgeEvent) event).isLink())
            {
                final BridgeEvent bridgeEvent = (BridgeEvent) event;
                Channel channel = bridgeEvent.getChannel1();
                if (bridgeEvent.getChannel1().isLocal())
                {
                    channel = bridgeEvent.getChannel2();
                }
                if (channelId.equalsIgnoreCase(bridgeEvent.getChannel1().getUniqueId())
                        || channelId.equalsIgnoreCase(bridgeEvent.getChannel2().getUniqueId()))
                {

                    OriginateBaseClass.logger
                            .debug("new bridge event :" + channel + " channel1 = " + bridgeEvent.getChannel1() //$NON-NLS-2$
                                    + " channel2 =" + bridgeEvent.getChannel2());

                    handleId(channel);
                }
            }

        }

        if (event instanceof DialEndEvent)
        {
            if (channelId.equalsIgnoreCase(((DialEndEvent) event).getDestChannel().getUniqueId()))
            {
                String forward = ((DialEndEvent) event).getForward();
                if (forward != null && forward.length() > 0)
                {
                    originateLatch.countDown();
                }
            }
        }
    }

    private void handleId(Channel channel)
    {

        if ((this.newChannel == null) && !channel.isLocal())
        {
            this.newChannel = channel;
            this.channelSeen = true;

            OriginateBaseClass.logger.info("new channel name " + channel);
            if (this.listener != null)
            {
                /*
                 * Update the phone channel to allow the call to be cancelled
                 * before it's answered.
                 */
                this.listener.channelUpdate(channel);
            }

            if (this.originateSeen == true)
            {
                OriginateBaseClass.logger.debug("notifying success 362");
                originateLatch.countDown();
            }
        }

    }

}
