package org.asteriskjava.pbx.internal.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.AbstractChannelEvent;
import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.Call.OperandChannel;
import org.asteriskjava.pbx.CallDirection;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ChannelHangupListener;
import org.asteriskjava.pbx.CompletionAdaptor;
import org.asteriskjava.pbx.DTMFTone;
import org.asteriskjava.pbx.DialPlanExtension;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.pbx.Trunk;
import org.asteriskjava.pbx.activities.BlindTransferActivity;
import org.asteriskjava.pbx.activities.BridgeActivity;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.activities.DialToAgiActivity;
import org.asteriskjava.pbx.activities.HoldActivity;
import org.asteriskjava.pbx.activities.JoinActivity;
import org.asteriskjava.pbx.activities.ParkActivity;
import org.asteriskjava.pbx.activities.RedirectToActivity;
import org.asteriskjava.pbx.activities.SplitActivity;
import org.asteriskjava.pbx.agi.AgiChannelActivityHangup;
import org.asteriskjava.pbx.agi.AgiChannelActivityHold;
import org.asteriskjava.pbx.asterisk.wrap.actions.CommandAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.EventGeneratingAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.HangupAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.ManagerAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.PlayDtmfAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.RedirectAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvents;
import org.asteriskjava.pbx.asterisk.wrap.response.CommandResponse;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.activity.BlindTransferActivityImpl;
import org.asteriskjava.pbx.internal.activity.BridgeActivityImpl;
import org.asteriskjava.pbx.internal.activity.DialActivityImpl;
import org.asteriskjava.pbx.internal.activity.DialToAgiActivityImpl;
import org.asteriskjava.pbx.internal.activity.HoldActivityImpl;
import org.asteriskjava.pbx.internal.activity.JoinActivityImpl;
import org.asteriskjava.pbx.internal.activity.ParkActivityImpl;
import org.asteriskjava.pbx.internal.activity.RedirectToActivityImpl;
import org.asteriskjava.pbx.internal.activity.SplitActivityImpl;
import org.asteriskjava.pbx.internal.asterisk.CallerIDImpl;
import org.asteriskjava.pbx.internal.asterisk.MeetmeRoom;
import org.asteriskjava.pbx.internal.asterisk.MeetmeRoomControl;
import org.asteriskjava.pbx.internal.asterisk.RoomOwner;
import org.asteriskjava.pbx.internal.managerAPI.RedirectCall;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public enum AsteriskPBX implements PBX, ChannelHangupListener
{

    SELF;

    public static final String ACTIVITY_AGI = "activityAgi";
    private final Log logger = LogFactory.getLog(getClass());
    private boolean muteSupported;
    private boolean bridgeSupport;
    private boolean expectRenameEvents;

    private static final int MAX_MEETME_ROOMS = 500;

    private LiveChannelManager liveChannels;

    AsteriskPBX()
    {
        try
        {
            CoherentManagerConnection.init();

            this.muteSupported = CoherentManagerConnection.getInstance().isMuteAudioSupported();
            this.bridgeSupport = CoherentManagerConnection.getInstance().isBridgeSupported();
            expectRenameEvents = CoherentManagerConnection.getInstance().expectRenameEvents();
            liveChannels = new LiveChannelManager();
            try
            {
                MeetmeRoomControl.init(this, AsteriskPBX.MAX_MEETME_ROOMS);
            }
            catch (Throwable e)
            {
                logger.error(e, e);
            }

        }
        catch (IllegalStateException | IOException | AuthenticationFailedException | TimeoutException
                | InterruptedException e1)
        {
            logger.error(e1, e1);
        }

    }

    @Override
    public void performPostCreationTasks()
    {
        liveChannels.performPostCreationTasks();
    }

    /**
     * Call this method when shutting down the PBX interface to allow it to
     * cleanup.
     */
    @Override
    public void shutdown()
    {
        MeetmeRoomControl.getInstance().stop();
        CoherentManagerConnection.getInstance().shutDown();
    }

    @Override
    public boolean isBridgeSupported()
    {
        return this.bridgeSupport;
    }

    @Override
    public BlindTransferActivity blindTransfer(Call call, Call.OperandChannel channelToTransfer, EndPoint transferTarget,
            CallerID toCallerID, boolean autoAnswer, long timeout)
    {
        final CompletionAdaptor<BlindTransferActivity> completion = new CompletionAdaptor<>();

        final BlindTransferActivityImpl transfer = new BlindTransferActivityImpl(call, channelToTransfer, transferTarget,
                toCallerID, autoAnswer, timeout, completion);

        completion.waitForCompletion(timeout + 2, TimeUnit.SECONDS);

        return transfer;

    }

    @Override
    public void blindTransfer(Call call, Call.OperandChannel channelToTransfer, EndPoint transferTarget, CallerID toCallerID,
            boolean autoAnswer, long timeout, ActivityCallback<BlindTransferActivity> listener)
    {
        new BlindTransferActivityImpl(call, channelToTransfer, transferTarget, toCallerID, autoAnswer, timeout, listener);

    }

    public BlindTransferActivity blindTransfer(Channel agentChannel, EndPoint transferTarget, CallerID toCallerID,
            boolean autoAnswer, int timeout, ActivityCallback<BlindTransferActivity> iCallback) throws PBXException
    {
        return new BlindTransferActivityImpl(agentChannel, transferTarget, toCallerID, autoAnswer, timeout, iCallback);

    }

    /**
     * Utility method to bridge two channels
     * 
     * @param lhsChannel
     * @param rhsChannel
     * @param direction
     * @throws PBXException
     */
    public BridgeActivity bridge(final Channel lhsChannel, final Channel rhsChannel) throws PBXException
    {
        final CompletionAdaptor<BridgeActivity> completion = new CompletionAdaptor<>();

        final BridgeActivityImpl bridge = new BridgeActivityImpl(lhsChannel, rhsChannel, completion);

        completion.waitForCompletion(10, TimeUnit.SECONDS);

        return bridge;

    }

    @Override
    public void split(final Call callToSplit) throws PBXException
    {
        final CompletionAdaptor<SplitActivity> completion = new CompletionAdaptor<>();

        new SplitActivityImpl(callToSplit, completion);

        completion.waitForCompletion(10, TimeUnit.SECONDS);

    }

    @Override
    public SplitActivity split(final Call callToSplit, final ActivityCallback<SplitActivity> listener)
    {

        return new SplitActivityImpl(callToSplit, listener);
    }

    @Override
    public RedirectToActivity redirectToActivity(final Channel channel, final ActivityCallback<RedirectToActivity> listener)
    {

        return new RedirectToActivityImpl(channel, listener);
    }

    /**
     * Joins two calls not returning until the join completes. The join will
     * complete almost immediately as it is a simple bridging of two active
     * channels. Each call must only have one active channel
     */
    @Override
    public JoinActivity join(Call lhs, OperandChannel originatingOperand, Call rhs, OperandChannel acceptingOperand,
            CallDirection direction)
    {
        final CompletionAdaptor<JoinActivity> completion = new CompletionAdaptor<>();

        final JoinActivityImpl join = new JoinActivityImpl(lhs, originatingOperand, rhs, acceptingOperand, direction,
                completion);

        completion.waitForCompletion(10, TimeUnit.SECONDS);

        return join;

    }

    /**
     * Joins two calls not returning until the join completes. The join will
     * complete almost immediately as it is a simple bridging of two active
     * channels. Each call must only have one active channel
     */
    @Override
    public void join(Call lhs, OperandChannel originatingOperand, Call rhs, OperandChannel acceptingOperand,
            CallDirection direction, ActivityCallback<JoinActivity> listener)
    {
        new JoinActivityImpl(lhs, originatingOperand, rhs, acceptingOperand, direction, listener);

    }

    @Override
    public void conference(final Channel channelOne, final Channel channelTwo, final Channel channelThree)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void conference(final Channel channelOne, final Channel channelTwo, final Channel channelThree,
            final ActivityCallback<Activity> callback)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public DialActivity dial(final EndPoint from, final CallerID fromCallerID, final EndPoint to, final CallerID toCallerID)
    {
        final CompletionAdaptor<DialActivity> completion = new CompletionAdaptor<>();

        final DialActivityImpl dialer = new DialActivityImpl(from, to, toCallerID, false, completion, null);

        completion.waitForCompletion(3, TimeUnit.MINUTES);

        return dialer;
    }

    public DialLocalToAgiActivity dialLocalToAgi(final EndPoint from, final CallerID fromCallerID,
            ActivityCallback<DialLocalToAgiActivity> callback, Map<String, String> channelVarsToSet)
    {
        return new DialLocalToAgiActivity(from, fromCallerID, callback, channelVarsToSet);
    }

    public DialActivity dial(final EndPoint from, final CallerID fromCallerID, final EndPoint to, final CallerID toCallerID,
            final ActivityCallback<DialActivity> callback, Map<String, String> channelVarsToSet)
    {
        final DialActivityImpl dialer = new DialActivityImpl(from, to, toCallerID, false, callback, channelVarsToSet);
        return dialer;
    }

    @Override
    public void dial(final EndPoint from, final CallerID fromCallerID, final EndPoint to, final CallerID toCallerID,
            final ActivityCallback<DialActivity> callback)
    {
        new DialActivityImpl(from, to, toCallerID, false, callback, null);

    }

    /**
     * Convenience method to hangup the call without having to extract the
     * channel yourself.
     */
    public void hangup(Call call) throws PBXException
    {
        this.hangup(call.getOriginatingParty());
    }

    @Override
    public void hangup(final Channel channel) throws PBXException
    {
        if (channel.isLive())
        {
            logger.info("Sending hangup action for channel: " + channel); //$NON-NLS-1$

            PBX pbx = PBXFactory.getActivePBX();
            if (!pbx.waitForChannelToQuiescent(channel, 3000))
                throw new PBXException("Channel: " + channel + " cannot be retrieved as it is still in transition.");

            final HangupAction hangup = new HangupAction(channel);
            try
            {
                channel.setCurrentActivityAction(new AgiChannelActivityHangup());
                CoherentManagerConnection.sendAction(hangup, 1000);
            }
            catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e)
            {
                logger.error(e, e);
                throw new PBXException(e);
            }
        }
        else
            logger.debug("Suppressed hangup for " + channel + " as it was already hungup"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    @Override
    public void hangup(final Channel channel, final ActivityCallback<Activity> callback)
    {
        throw new UnsupportedOperationException("Not yet implemented."); //$NON-NLS-1$
    }

    @Override
    public HoldActivity hold(final Channel channel)
    {
        final CompletionAdaptor<HoldActivity> completion = new CompletionAdaptor<>();

        HoldActivity activity = null;
        try
        {
            activity = new HoldActivityImpl(channel, completion);
            completion.waitForCompletion(10, TimeUnit.SECONDS);

        }
        catch (final Exception e)
        {
            logger.error(e, e);

        }
        return activity;
    }

    @Override
    public boolean isMuteSupported()
    {
        return this.muteSupported;
    }

    @Override
    public ParkActivity park(final Call call, final Channel parkChannel)
    {
        final CompletionAdaptor<ParkActivity> completion = new CompletionAdaptor<>();

        final ParkActivity activity = new ParkActivityImpl(call, parkChannel, completion);
        parkChannel.setParked(true);

        completion.waitForCompletion(10, TimeUnit.SECONDS);
        return activity;
    }

    @Override
    public void park(final Call call, final Channel parkChannel, final ActivityCallback<ParkActivity> callback)
    {
        new ParkActivityImpl(call, parkChannel, callback);

    }

    @Override
    public void sendDTMF(final Channel channel, final DTMFTone tone) throws PBXException
    {
        try
        {
            if (!waitForChannelToQuiescent(channel, 3000))
                throw new PBXException("Channel: " + channel + " cannot play dtmf as it is still in transition.");

            CoherentManagerConnection.sendAction(new PlayDtmfAction(channel, tone), 1000);
        }
        catch (final Exception e)
        {
            logger.error(e, e);
            throw new PBXException(e);
        }
    }

    @Override
    public void sendDTMF(final Channel channel, final DTMFTone tone, final ActivityCallback<Activity> callback)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void transferToMusicOnHold(final Channel channel) throws PBXException
    {
        final RedirectCall transfer = new RedirectCall();
        transfer.redirect(channel, new AgiChannelActivityHold());
    }

    public String getManagementContext()
    {
        final AsteriskSettings settings = PBXFactory.getActiveProfile();
        return settings.getManagementContext();
    }

    @Override
    public Channel getChannelByEndPoint(final EndPoint endPoint)
    {
        return this.liveChannels.getChannelByEndPoint(endPoint);
    }

    @Override
    public void channelHangup(Channel channel, Integer cause, String causeText)
    {
        this.liveChannels.remove((ChannelProxy) channel);
    }

    public DialPlanExtension getExtensionPark()
    {
        final AsteriskSettings settings = PBXFactory.getActiveProfile();
        return this.buildDialPlanExtension(settings.getExtensionPark());
    }

    @Override
    public EndPoint getExtensionAgi()
    {
        final AsteriskSettings settings = PBXFactory.getActiveProfile();
        return this.buildDialPlanExtension(settings.getAgiExtension());
    }

    /**
     * Builds an end point from a fully qualified end point name. If the
     * endpoint name doesn't have a tech then it is considered invalid and null
     * is returned.
     */

    @Override
    public EndPoint buildEndPoint(final String fullyQualifiedEndPoint)
    {
        EndPoint endPoint = null;
        try
        {
            endPoint = new EndPointImpl(fullyQualifiedEndPoint);
        }
        catch (final IllegalArgumentException e)
        {
            logger.warn(e, e);
        }
        return endPoint;
    }

    /**
     * Builds an end point from an end point name. If the endpoint name doesn't
     * have a tech specified then the defaultTech is used.
     */
    @Override
    public EndPoint buildEndPoint(final TechType defaultTech, final String endPointName)
    {
        EndPoint endPoint = null;
        try
        {
            if (endPointName == null || endPointName.trim().length() == 0)
                endPoint = new EndPointImpl();
            else
                endPoint = new EndPointImpl(defaultTech, endPointName);
        }
        catch (final IllegalArgumentException e)
        {
            logger.error(e, e);
        }
        return endPoint;

    }

    @Override
    public EndPoint buildEndPoint(final TechType defaultTech, final Trunk trunk, final String endPointName)
    {
        return new EndPointImpl(defaultTech, trunk, endPointName);

    }

    /**
     * Builds an end point from an end point name. If the endpoint name doesn't
     * have a tech specified then the defaultTech is used.
     */
    public DialPlanExtension buildDialPlanExtension(final String extension)
    {
        DialPlanExtension dialPlan = null;
        try
        {
            dialPlan = new DialPlanExtension(extension);
        }
        catch (final IllegalArgumentException e)
        {
            logger.error(e, e);
        }
        return dialPlan;

    }

    @Override
    public CallerID buildCallerID(final String number, final String name)
    {
        return new CallerIDImpl(number, name);
    }

    /**
     * Convenience method to build a call id from an event.
     * 
     * @param event
     */
    public CallerID buildCallerID(final AbstractChannelEvent event)
    {
        final String number = event.getCallerIdNum();
        final String name = event.getCallerIdName();
        return this.buildCallerID(number, name);
    }

    public Channel registerChannel(final String channelName, final String uniqueIdParam) throws InvalidChannelName
    {
        String uniqueID = uniqueIdParam;
        if (uniqueIdParam == null || uniqueIdParam.length() == 0)
        {
            uniqueID = ChannelImpl.UNKNOWN_UNIQUE_ID;
        }

        if (channelName == null || channelName.trim().length() == 0)
        {
            throw new IllegalArgumentException("Channel name must not be empty");
        }

        Channel proxy = findChannel(cleanChannelName(channelName), null);
        if (proxy == null)
        {
            logger.info("Couldn't find the channel " + channelName + ", creating it");
            proxy = internalRegisterChannel(channelName, uniqueID);
        }
        else
        {
            if (uniqueID != null && !uniqueID.equals(proxy.getUniqueId()))
            {
                logger.info(
                        "Found the channel(" + proxy.getUniqueId() + "), but with a different uniqueId (" + uniqueID + ")");

            }
        }
        liveChannels.sanityCheck();

        return proxy;
    }

    /**
     * This method is not part of the public API. <br>
     * <br>
     * Use registerChannel instead calling this method with an incorrect or
     * stale uniqueId will cause inconsistent behaviour.
     * 
     * @param channelName
     * @param uniqueID
     * @return
     * @throws InvalidChannelName
     */
    public Channel internalRegisterChannel(final String channelName, final String uniqueID) throws InvalidChannelName
    {
        ChannelProxy proxy = null;
        synchronized (this.liveChannels)
        {

            String localUniqueID = (uniqueID == null ? ChannelImpl.UNKNOWN_UNIQUE_ID : uniqueID);
            proxy = this.findChannel(cleanChannelName(channelName), localUniqueID);
            if (proxy == null)
            {
                proxy = new ChannelProxy(new ChannelImpl(channelName, localUniqueID));
                logger.debug("Creating new Channel Proxy " + proxy);
                this.liveChannels.add(proxy);
                proxy.addHangupListener(this);
            }
        }
        return proxy;
    }

    /**
     * remove white space
     * 
     * @param name
     * @return
     */
    private String cleanChannelName(final String name)
    {
        String cleanedName = name.trim().toUpperCase();

        return cleanedName;
    }

    public Channel registerHangupChannel(String channel, String uniqueId) throws InvalidChannelName
    {
        Channel newChannel = null;
        synchronized (this.liveChannels)
        {
            newChannel = this.findChannel(channel, uniqueId);
            if (newChannel == null)
            {
                // WE don't add this channel to the liveChannels as it is in the
                // process
                // of being hungup so we don't need to track it.
                // If we tried to track it that would likely cause a problem
                // as the livechannel manager would never be able to discard it
                // as
                // it relies on the hangup event which is being processed right
                // now.
                newChannel = new ChannelProxy(new ChannelImpl(channel, uniqueId));
            }
        }
        return newChannel;
    }

    public ChannelProxy findChannel(final String channelName, final String uniqueID)
    {
        return this.liveChannels.findChannel(channelName, uniqueID);
    }

    public MeetmeRoom acquireMeetmeRoom(RoomOwner owner)
    {
        return MeetmeRoomControl.getInstance().findAvailableRoom(owner);
    }

    public void addListener(FilteredManagerListener<ManagerEvent> listener)
    {
        CoherentManagerConnection connection = CoherentManagerConnection.getInstance();
        connection.addListener(listener);
    }

    public void removeListener(FilteredManagerListener<ManagerEvent> listener)
    {
        CoherentManagerConnection connection = CoherentManagerConnection.getInstance();
        connection.removeListener(listener);
    }

    /**
     * sends an action with a default timeout of 30 seconds.
     * 
     * @param theAction
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     * @throws TimeoutException
     */
    public ManagerResponse sendAction(ManagerAction theAction)
            throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException
    {
        return CoherentManagerConnection.sendAction(theAction, 30000);
    }

    public ManagerResponse sendAction(ManagerAction theAction, int timeout)
            throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException
    {
        return CoherentManagerConnection.sendAction(theAction, timeout);
    }

    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action)
            throws EventTimeoutException, IllegalArgumentException, IllegalStateException, IOException
    {
        ResponseEvents events = CoherentManagerConnection.sendEventGeneratingAction(action);
        return events;

    }

    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, int timeout)
            throws EventTimeoutException, IllegalArgumentException, IllegalStateException, IOException
    {
        return CoherentManagerConnection.sendEventGeneratingAction(action, timeout);

    }

    public void setVariable(Channel channel, String name, String value) throws PBXException
    {
        CoherentManagerConnection.getInstance().setVariable(channel, name, value);

    }

    public void sendActionNoWait(final ManagerAction action)
    {
        CoherentManagerConnection.sendActionNoWait(action);

    }

    public String getVariable(Channel channel, String name)
    {
        return CoherentManagerConnection.getInstance().getVariable(channel, name);
    }

    public AsteriskVersion getVersion()
    {
        return CoherentManagerConnection.getInstance().getVersion();
    }

    public boolean isConnected()
    {
        return ((CoherentManagerConnection.managerConnection != null)
                && (CoherentManagerConnection.managerConnection.getState() == ManagerConnectionState.CONNECTED));
    }

    public boolean isMeetmeInstalled()
    {
        return MeetmeRoomControl.getInstance().isMeetmeInstalled();
    }

    @Override
    public boolean isChannel(String channelName)
    {
        boolean isChannel = false;
        try
        {
            internalRegisterChannel(channelName, ChannelImpl.UNKNOWN_UNIQUE_ID);
            isChannel = true;
        }
        catch (InvalidChannelName e)
        {
            // if we get here then its not avalid channel name.
        }
        return isChannel;
    }

    static public String getSIPADDHeader(final boolean inherit, final boolean targetIsSIP)
    {
        String sipHeader = "SIPADDHEADER"; //$NON-NLS-1$
        if (!targetIsSIP || inherit)
        {
            sipHeader = "__" + sipHeader; //$NON-NLS-1$
        }
        return sipHeader;
    }

    /**
     * Waits for a channel to become quiescent. A Quiescent channel is one that
     * is not in the middle of a name change (e.g. masquerade)
     * 
     * @param channel
     * @param timeout the time to wait (in milliseconds) for the channel to
     *            become quiescent.
     */
    @Override
    public boolean waitForChannelsToQuiescent(List<Channel> channels, long timeout)
    {
        long elapsed = 0;
        while (elapsed < timeout && !channelsAreQuiesent(channels))
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                logger.error(e, e);
            }
            logger.info("Waiting for channesl to Quiescent");
            elapsed += 200;
        }

        if (elapsed > timeout / 2)
        {
            logger.warn("Took " + elapsed + "ms for channels to Quiescent");
        }
        if (elapsed >= timeout)
        {
            logger.error("Channels didn't Quiescent");
            for (Channel channel : channels)
            {
                logger.error(channel);
            }

        }
        return timeout > elapsed;
    }

    private boolean channelsAreQuiesent(List<Channel> channels)
    {
        boolean ret = true;
        for (Channel channel : channels)
        {
            ret &= channel.isQuiescent();
        }
        return ret;
    }

    public boolean moveChannelToAgi(Channel channel) throws PBXException
    {
        if (!waitForChannelToQuiescent(channel, 3000))
            throw new PBXException("Channel: " + channel + " cannot be transfered as it is still in transition.");

        boolean isInAgi = channel.isInAgi();
        if (!isInAgi)
        {
            final AsteriskSettings profile = PBXFactory.getActiveProfile();

            channel.setCurrentActivityAction(new AgiChannelActivityHold());
            final RedirectAction redirect = new RedirectAction(channel, profile.getManagementContext(), getExtensionAgi(),
                    1);

            logger.error("Issuing redirect on channel " + channel + " to move it to the agi");

            try
            {
                final ManagerResponse response = sendAction(redirect, 1000);
                if ((response != null) && (response.getResponse().compareToIgnoreCase("success") == 0))//$NON-NLS-1$
                {
                    int limit = 50;
                    while (!channel.isInAgi() && limit-- > 0)
                    {
                        Thread.sleep(100);
                    }
                    isInAgi = channel.isInAgi();
                    if (!isInAgi)
                    {
                        logger.error("Failed to move channel");
                    }
                }

            }
            catch (final Exception e)
            {
                logger.error(e, e);
            }

        }
        return isInAgi;

    }

    public void moveChannelTo(Channel channel, String context, String exten, int prio)
    {

        DialPlanExtension ext = this.buildDialPlanExtension(exten);

        channel.setCurrentActivityAction(new AgiChannelActivityHold());
        final RedirectAction redirect = new RedirectAction(channel, context, ext, prio);

        try
        {
            sendAction(redirect, 1000);

        }
        catch (final Exception e)
        {
            logger.error(e, e);
        }

    }

    @Override
    public boolean waitForChannelToQuiescent(Channel channel, int timeout)
    {
        List<Channel> channels = new LinkedList<>();
        channels.add(channel);
        return waitForChannelsToQuiescent(channels, timeout);
    }

    public ChannelProxy getProxyById(String id)
    {
        return liveChannels.findProxyById(id);
    }

    public DialToAgiActivityImpl dialToAgi(EndPoint endPoint, CallerID callerID, AgiChannelActivityAction action,
            ActivityCallback<DialToAgiActivity> iCallback)
    {

        final CompletionAdaptor<DialToAgiActivity> completion = new CompletionAdaptor<>();

        final DialToAgiActivityImpl dialer = new DialToAgiActivityImpl(endPoint, callerID, null, false, completion, null,
                action);

        dialer.startActivity(false);

        completion.waitForCompletion(3, TimeUnit.MINUTES);

        final ActivityStatusEnum status;

        if (dialer.isSuccess())
        {
            status = ActivityStatusEnum.SUCCESS;
        }
        else
        {
            status = ActivityStatusEnum.FAILURE;
        }

        iCallback.progress(dialer, status, status.getDefaultMessage());

        return dialer;
    }

    public DialToAgiWithAbortCallback dialToAgiWithAbort(EndPoint endPoint, CallerID callerID, int timeout,
            AgiChannelActivityAction action, ActivityCallback<DialToAgiActivity> iCallback)
    {

        final CompletionAdaptor<DialToAgiActivity> completion = new CompletionAdaptor<>();

        final DialToAgiActivityImpl dialer = new DialToAgiActivityImpl(endPoint, callerID, timeout, false, completion, null,
                action);

        return new DialToAgiWithAbortCallback(dialer, completion, iCallback);
    }

    /**
     * Creates the set of extensions required to test NJR during the
     * installation. The context must already exist in the dialplan.
     * 
     * @param profile
     * @param dialContext
     * @return success or failure
     * @throws IllegalStateException
     * @throws IOException
     * @throws AuthenticationFailedException
     * @throws TimeoutException
     */
    public boolean createAgiEntryPoint() throws IOException, AuthenticationFailedException, TimeoutException
    {

        try
        {

            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
            AsteriskSettings profile = PBXFactory.getActiveProfile();
            if (!checkDialplanExists(profile))

            {

                String host = profile.getAgiHost();
                String agi = profile.getAgiExtension();
                pbx.addAsteriskExtension(agi, 1,
                        "AGI(agi://" + host + "/" + ACTIVITY_AGI + "), into " + profile.getManagementContext());
                pbx.addAsteriskExtension(agi, 2, "wait(0.5), into " + profile.getManagementContext());
                pbx.addAsteriskExtension(agi, 3, "goto(" + agi + ",1), into " + profile.getManagementContext());

                return checkDialplanExists(profile);
            }
            return true;
        }
        catch (Exception e)
        {
            logger.error(e);

            return false;
        }

    }

    public boolean checkDialplanExists(AsteriskSettings profile)
            throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException
    {
        String command;

        if (getVersion().isAtLeast(AsteriskVersion.ASTERISK_1_6))
        {
            // TODO: Use ShowDialplanAction instead of CommandAction?
            command = "dialplan show " + profile.getManagementContext();
        }
        else
        {
            command = "show dialplan " + profile.getManagementContext();
        }

        CommandAction action = new CommandAction(command);
        CommandResponse response = (CommandResponse) sendAction(action, 30000);

        boolean exists = false;
        for (String line : response.getResult())
        {
            if (line.contains(ACTIVITY_AGI))
            {
                exists = true;
                break;
            }
        }
        return exists;

    }

    public String addAsteriskExtension(String extNumber, int priority, String command) throws Exception

    {
        String ext = "dialplan add extension " + extNumber + "," + priority + "," + command;

        CommandAction action = new CommandAction(ext);
        CommandResponse response = (CommandResponse) sendAction(action, 30000);

        List<String> line = response.getResult();
        String answer = line.get(0);
        String tmp = "Extension '" + extNumber + "," + priority + ",";
        if (answer.substring(0, tmp.length()).compareToIgnoreCase(tmp) == 0)
            return "OK";

        throw new Exception("InitiateAction.AddExtentionFailed" + ext);
    }

    @Override
    public Trunk buildTrunk(final String trunk)
    {
        return new Trunk()
        {

            @Override
            public String getTrunkAsString()
            {
                return trunk;
            }
        };
    }

    public List<ChannelProxy> getChannelList()
    {
        return liveChannels.getChannelList();
    }

    public boolean expectRenameEvents()
    {
        return expectRenameEvents;
    }

}
