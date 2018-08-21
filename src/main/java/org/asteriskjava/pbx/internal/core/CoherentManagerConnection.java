package org.asteriskjava.pbx.internal.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.naming.OperationNotSupportedException;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.EventGeneratingAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.GetVarAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.ListCommandsAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.ManagerAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.SetVarAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ConnectEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.DisconnectEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvents;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.managerAPI.Connector;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * This is a wrapper class for the asterisk manager. <br>
 * <br>
 * It handles the hot swap of an asterisk server and distributes events to event
 * listeners.<br>
 * <br>
 * The CoherentManagerConnection should be used whenever manager events need to
 * be received either for short or extended periods of time. <br>
 * <br>
 * The CoherentManagerConnection uses the ManagerEventQueue to queue events to
 * each listener. <br>
 * <br>
 * The ManagerEventQueue dispatches the events to the set of listeners using a
 * separate thread (shared by all of the listeners). <br>
 * <br>
 * This means that we are always responsive to asterisk when receiving events,
 * if we are not responsive then asterisk will drop the connection. <br>
 * <br>
 * It should be noted that as all events are dispatch from a single thread and
 * as such a single tardy listener can block all other listeners. <br>
 * <br>
 * If your listener is likely to be slow in handling events then you should wrap
 * the listener in its own ManagerEventQueue. <br>
 * <br>
 * It is critical that you use the ManagerEventQueue as it forms an intrinsic
 * part of this classes ability to ensure that channel/event processing is
 * coherent (i.e events are processed in the correct order). <br>
 * <br>
 * For connections that generate large number of events you should use
 * 
 * @see org.asteriskjava.pbx.internal.core.CoherentManagerEventQueue Note:
 *      events for any action are distributed to all listeners!
 */
class CoherentManagerConnection implements FilteredManagerListener<ManagerEvent>
{

    static private Log logger = LogFactory.getLog(CoherentManagerConnection.class);

    static Map<String, Integer> eventStatistics = new HashMap<>();

    /**
     * Used to instantiate the manager connection including the initial login.
     */
    static Connector connector = null;

    /**
     * The actual manager connection. AJ actually maintains two socket
     * connections one for reading events and the other writing events.
     */
    static ManagerConnection managerConnection = null;

    /**
     * We operate two separate event queues each of which dispatches events via
     * its own thread. All ListenerPriority.REALTIME events are dispatch via the
     * realtime queue. REALTIME events cannot rely on the LiveChannelManager as
     * it will may have been updated (renames, masquerades) when the realtime
     * listener processes its event.
     */
    private CoherentManagerEventQueue eventQueue;

    private CoherentManagerEventQueue realtimeEventQueue;

    // private List<FilteredManagerListener<ManagerEvent>> realtimeListeners =
    // new CopyOnWriteArrayList<>();

    // True if the AMI function 'Bridge' is available.
    private boolean canBridge;

    // True if the AMI function 'MuteAudio' is available.
    private boolean canMuteAudio;

    private static CoherentManagerConnection self = null;

    // latch used for reconnections.
    // We create an initial latch incase we get connect before we are really
    // ready to wait for one.
    private CountDownLatch _reconnectLatch = new CountDownLatch(0);

    private boolean expectRenameEvents = true;

    public static synchronized void init()
            throws IllegalStateException, IOException, AuthenticationFailedException, TimeoutException, InterruptedException
    {
        if (self != null)
            logger.warn("The CoherentManagerConnection has already been initialised"); //$NON-NLS-1$
        else
        {
            self = new CoherentManagerConnection();
            boolean done = false;
            while (!done)
            {
                try
                {

                    self.checkConnection();
                    self.checkFeatures();
                    done = true;
                }
                catch (Exception e)
                {
                    logger.error(e, e);
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(500);
                    }
                    catch (InterruptedException e1)
                    {
                        throw e1;
                    }
                }
            }
        }

        self.checkConnection();
    }

    public static void reset()
    {
        self = null;
    }

    public static synchronized CoherentManagerConnection getInstance()
    {
        if (self == null)
            throw new IllegalStateException("The CoherentManagerConnection has not been initialised"); //$NON-NLS-1$

        self.checkConnection();

        return self;
    }

    private CoherentManagerConnection()
            throws IllegalStateException, IOException, AuthenticationFailedException, TimeoutException
    {
        super();
        connector = new Connector();
        this.configureConnection();
    }

    public AsteriskVersion getVersion() throws IllegalStateException
    {
        return CoherentManagerConnection.managerConnection.getVersion();
    }

    public void setVariable(final Channel channel, final String variableName, final String value) throws PBXException
    {
        try
        {
            /*
             * Sets the specified variable on the specified channel to the
             * specified value.
             */
            final SetVarAction setVariable = new SetVarAction(channel, variableName, value);
            ManagerResponse response;

            PBX pbx = PBXFactory.getActivePBX();
            if (!pbx.waitForChannelToQuiescent(channel, 3000))
                throw new PBXException("Channel: " + channel + " cannot be retrieved as it is still in transition.");

            response = sendAction(setVariable, 500);
            if ((response != null) && (response.getResponse().compareToIgnoreCase("success") == 0)) //$NON-NLS-1$
            {
                // $NON-NLS-1$

                CoherentManagerConnection.logger.debug("set variable " + variableName + " to " + value //$NON-NLS-1$ //$NON-NLS-2$
                        + " on " + channel); //$NON-NLS-1$
            }
            else
            {
                throw new PBXException("failed to set variable '" + variableName + "' on channel " + channel + " to '" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + value + "'" + (response != null ? " Error:" + response.getMessage() : "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
        catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e)
        {
            logger.error(e, e);
            throw new PBXException(e);

        }

    }

    /**
     * Retrieves and returns the value of a variable associated with a channel.
     * If the variable is empty or null then an empty string is returned.
     * 
     * @param channel
     * @param variableName
     * @return
     */
    public String getVariable(final Channel channel, final String variableName)
    {
        String value = "";
        final GetVarAction var = new GetVarAction(channel, variableName);

        try
        {
            PBX pbx = PBXFactory.getActivePBX();
            if (!pbx.waitForChannelToQuiescent(channel, 3000))
                throw new PBXException("Channel: " + channel + " cannot be retrieved as it is still in transition.");

            ManagerResponse convertedResponse = sendAction(var, 500);
            if (convertedResponse != null)
            {
                value = convertedResponse.getAttribute("value"); //$NON-NLS-1$
                if (value == null)
                    value = "";
                CoherentManagerConnection.logger.debug("getVarAction returned name:" + variableName + " value:" + value); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        catch (final Exception e)
        {
            CoherentManagerConnection.logger.debug(e, e);
            CoherentManagerConnection.logger.error("getVariable: " + e); //$NON-NLS-1$
        }
        return value;
    }

    /**
     * Allows the caller to send an action to asterisk without waiting for the
     * response. You should only use this if you don't care whether the action
     * actually succeeds.
     * 
     * @param sa
     */
    public static void sendActionNoWait(final ManagerAction action)
    {
        final Thread background = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    CoherentManagerConnection.sendAction(action, 5000);
                }
                catch (final Exception e)
                {
                    CoherentManagerConnection.logger.error(e, e);
                }
            }
        };
        background.setName("sendActionNoWait"); //$NON-NLS-1$
        background.setDaemon(true);
        background.start();
    }

    public static ResponseEvents sendEventGeneratingAction(EventGeneratingAction action)
            throws EventTimeoutException, IllegalArgumentException, IllegalStateException, IOException
    {
        org.asteriskjava.manager.ResponseEvents events = CoherentManagerConnection.managerConnection
                .sendEventGeneratingAction(action.getAJEventGeneratingAction());

        ResponseEvents convertedEvents = new ResponseEvents();
        for (org.asteriskjava.manager.event.ResponseEvent event : events.getEvents())
        {
            convertedEvents.add(CoherentEventFactory.build(event));
        }
        return convertedEvents;

    }

    public static ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, int timeout)
            throws EventTimeoutException, IllegalArgumentException, IllegalStateException, IOException
    {
        org.asteriskjava.manager.ResponseEvents events = CoherentManagerConnection.managerConnection
                .sendEventGeneratingAction(action.getAJEventGeneratingAction(), timeout);

        ResponseEvents convertedEvents = new ResponseEvents();
        for (org.asteriskjava.manager.event.ResponseEvent event : events.getEvents())
        {
            convertedEvents.add(CoherentEventFactory.build(event));
        }
        return convertedEvents;

    }

    /**
     * Sends an Asterisk action and waits for a ManagerRespose.
     * 
     * @param action
     * @param timeout timeout in milliseconds
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     * @throws TimeoutException
     * @throws OperationNotSupportedException
     */
    public static ManagerResponse sendAction(final ManagerAction action, final int timeout)
            throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException
    {
        if (logger.isDebugEnabled())
            CoherentManagerConnection.logger.debug("Sending Action: " + action.toString()); //$NON-NLS-1$

        CoherentManagerConnection.getInstance();
        if ((CoherentManagerConnection.managerConnection != null)
                && (CoherentManagerConnection.managerConnection.getState() == ManagerConnectionState.CONNECTED))
        {
            final org.asteriskjava.manager.action.ManagerAction ajAction = action.getAJAction();

            org.asteriskjava.manager.response.ManagerResponse response = CoherentManagerConnection.managerConnection
                    .sendAction(ajAction, timeout);
            ManagerResponse convertedResponse = null;

            // UserEventActions always return a null
            if (response != null)
                convertedResponse = CoherentEventFactory.build(response);

            if ((convertedResponse != null) && (convertedResponse.getResponse().compareToIgnoreCase("Error") == 0))//$NON-NLS-1$
            {
                CoherentManagerConnection.logger.warn("Action '" + ajAction + "' failed, Response: " //$NON-NLS-1$ //$NON-NLS-2$
                        + convertedResponse.getResponse() + " Message: " + convertedResponse.getMessage()); //$NON-NLS-1$
            }
            return convertedResponse;
        }

        throw new IllegalStateException("not connected."); //$NON-NLS-1$
    }

    private void checkConnection()
    {
        int trys = 3;

        this._reconnectLatch = new CountDownLatch(1);
        while ((trys > 0) && ((CoherentManagerConnection.managerConnection == null)
                || (CoherentManagerConnection.managerConnection.getState() != ManagerConnectionState.CONNECTED)))
        {
            if (trys == 3)
            {
                CoherentManagerConnection.logger.warn("Awaiting Manager connection"); //$NON-NLS-1$
            }
            try
            {

                this._reconnectLatch.await(300, TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException e)
            {
                CoherentManagerConnection.logger.error(e, e);
            }
            trys--;
        }
    }

    public ManagerConnectionState getState()
    {
        return CoherentManagerConnection.managerConnection.getState();
    }

    private void configureConnection()
            throws IOException, AuthenticationFailedException, TimeoutException, IllegalStateException
    {
        final AsteriskSettings profile = PBXFactory.getActiveProfile();
        CoherentManagerConnection.managerConnection = CoherentManagerConnection.connector.connect(profile);

        // After a reconnect we will have duplicate eventQueues and
        // realtimeEventQueues but
        // the original queues will be drained quite quickly (on the small
        // chance
        // that it hasn't already)
        // and should have no duplicate events. Once drained the queue will be
        // garbage collected.
        CoherentManagerEventQueue newRealtime = new CoherentManagerEventQueue("Realtime", //$NON-NLS-1$
                CoherentManagerConnection.managerConnection);
        if (this.realtimeEventQueue != null)
        {
            this.realtimeEventQueue.stop();
            newRealtime.transferListeners(this.realtimeEventQueue);
        }
        this.realtimeEventQueue = newRealtime;

        CoherentManagerEventQueue newStandard = new CoherentManagerEventQueue("Standard", //$NON-NLS-1$
                CoherentManagerConnection.managerConnection);
        if (this.eventQueue != null)
        {
            this.eventQueue.stop();
            newStandard.transferListeners(this.eventQueue);
        }
        this.eventQueue = newStandard;
    }

    private void checkFeatures() throws IOException, TimeoutException
    {
        final AsteriskSettings profile = PBXFactory.getActiveProfile();

        // Determine if the Bridge and Mute events are available.
        final ListCommandsAction lca = new ListCommandsAction();
        ManagerResponse convertedResponse = sendAction(lca, 500);
        boolean bridgeFound = false;
        boolean muteAudioFound = false;
        for (final String command : convertedResponse.getAttributes().keySet())
        {
            if (command.toLowerCase().contains("bridge")) //$NON-NLS-1$
            {
                bridgeFound = true;
            }
            if (command.toLowerCase().contains("muteaudio")) //$NON-NLS-1$
            {
                muteAudioFound = true;
            }
        }
        if (CoherentManagerConnection.managerConnection.getVersion().isAtLeast(AsteriskVersion.ASTERISK_13))
        {
            // we are really checking for the use of PJ SIP
            expectRenameEvents = false;
        }
        this.canMuteAudio = muteAudioFound;
        if (profile.getDisableBridge())
        {
            this.canBridge = false;
        }
        else
        {
            this.canBridge = bridgeFound;
        }
    }

    public void shutDown()
    {
        CoherentManagerConnection.managerConnection.removeEventListener(this.eventQueue);
        this.eventQueue.stop();
        try
        {
            CoherentManagerConnection.managerConnection.logoff();
        }
        catch (final Exception e)
        {
            CoherentManagerConnection.logger.debug("Manager logging off"); //$NON-NLS-1$
            CoherentManagerConnection.logger.debug(e, e);
        }
    }

    public boolean isBridgeSupported()
    {
        return this.canBridge;
    }

    public boolean isMuteAudioSupported()
    {
        return this.canMuteAudio;
    }

    public void removeListener(FilteredManagerListener<ManagerEvent> listener)
    {
        if (listener.getPriority() == ListenerPriority.REALTIME)
            this.realtimeEventQueue.removeListener(listener);
        else
            this.eventQueue.removeListener(listener);

    }

    public void addListener(FilteredManagerListener<ManagerEvent> listener)
    {
        if (listener.getPriority() == ListenerPriority.REALTIME)
            this.realtimeEventQueue.addListener(listener);
        else
            this.eventQueue.addListener(listener);

    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();
        required.add(ConnectEvent.class);
        required.add(DisconnectEvent.class);
        return required;
    }

    @Override
    public String getName()
    {
        return "CoherentManagerConnection"; //$NON-NLS-1$
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.REALTIME;
    }

    @Override
    public void onManagerEvent(ManagerEvent event)
    {
        // Special handler for the connect event in case we are
        // wait()ing for
        // the connection to complete. This wakes the code up in the shortest
        // time possible
        // by notifying it of the connection.
        if (event instanceof ConnectEvent)
        {
            logger.warn("****************** Asterisk manager connection acquired **************************"); //$NON-NLS-1$
            this._reconnectLatch.countDown();
        }
        else if (event instanceof DisconnectEvent)
        {
            logger.warn("****************** Asterisk manager connection lost **************************"); //$NON-NLS-1$
            // new Thread(new Runnable()
            // {
            // public void run()
            // {
            // reconnect();
            // }
            // });
        }

    }

    public boolean expectRenameEvents()
    {
        return expectRenameEvents;
    }

}
