/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.HangupCause;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.ManagerEventListenerProxy;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.asteriskjava.manager.event.AbstractMeetMeEvent;
import org.asteriskjava.manager.event.AbstractOriginateEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Default implementation of the {@link AsteriskServer} interface.
 * 
 * @author srt
 * @version $Id$
 */
public class AsteriskServerImpl
        implements
            AsteriskServer,
            ManagerEventListener
{
    private static final String ACTION_ID_PREFIX_ORIGINATE = "AJ_ORIGINATE_";
    private static final Pattern SHOW_VERSION_FILES_PATTERN = 
        Pattern.compile("^([\\S]+)\\s+Revision: ([0-9\\.]+)");

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The underlying manager connection used to receive events from Asterisk.
     */
    private ManagerConnection eventConnection;

    /**
     * A pool of manager connections to use for sending actions to Asterisk.
     */
    private final ManagerConnectionPool connectionPool;

    private final List<AsteriskServerListener> listeners;

    private final ChannelManager channelManager;
    private final MeetMeManager meetMeManager;
    private final QueueManager queueManager;

    /**
     * The version of the Asterisk server we are connected to.<p>
     * Contains <code>null</code> until lazily initialized.
     */
    private String version;

    /**
     * Holds the version of Asterisk's source files.<p>
     * That corresponds to the output of the CLI command
     * <code>show version files</code>.<p>
     * Contains <code>null</code> until lazily initialized.
     */
    private Map<String, String> versions;

    private final Map<String, OriginateCallback> originateCallbacks;

    private final AtomicLong idCounter;

    /* config options */

    /**
     * Flag to skip initializing queues as that results in a timeout on Asterisk
     * 1.0.x.
     */
    private boolean skipQueues;

    /**
     * Set to <code>true</code> to not handle ManagerEvents in the reader tread but
     * process them asynchronously.
     * This is a good idea :)
     */
    private boolean asyncEventHandling = true;

    /**
     * Creates a new instance.
     */
    public AsteriskServerImpl()
    {
        connectionPool = new ManagerConnectionPool(1);
        idCounter = new AtomicLong();
        listeners = new ArrayList<AsteriskServerListener>();
        originateCallbacks = new HashMap<String, OriginateCallback>();
        channelManager = new ChannelManager(this);
        meetMeManager = new MeetMeManager(this, channelManager);
        queueManager = new QueueManager(this, channelManager);
    }

    /**
     * Creates a new instance.
     * 
     * @param eventConnection the ManagerConnection to use for receiving events from Asterisk.
     */
    public AsteriskServerImpl(ManagerConnection eventConnection)
    {
        this();
        this.eventConnection = eventConnection;
        connectionPool.add(eventConnection);
    }

    /**
     * Determines if queue status is retrieved at startup. If you don't need
     * queue information and still run Asterisk 1.0.x you can set this to
     * <code>true</code> to circumvent the startup delay caused by the missing
     * QueueStatusComplete event.<p>
     * Default is <code>false</code>.
     * 
     * @param skipQueues <code>true</code> to skip queue initialization,
     *            <code>false</code> to not skip.
     * @since 0.2
     */
    public void setSkipQueues(boolean skipQueues)
    {
        this.skipQueues = skipQueues;
    }

    public void setManagerConnection(ManagerConnection eventConnection)
    {
        this.eventConnection = eventConnection;
        this.connectionPool.clear();
        this.connectionPool.add(eventConnection);
    }

    public void initialize() throws AuthenticationFailedException, ManagerCommunicationException
    {
        if (eventConnection.getState() == ManagerConnectionState.INITIAL ||
            eventConnection.getState() == ManagerConnectionState.DISCONNECTED)
        {
            try
            {
                eventConnection.login();
            }
            catch (Exception e)
            {
                throw new ManagerCommunicationException("Unable to login", e);
            }
        }

        channelManager.initialize();
        meetMeManager.initialize();
        if (!skipQueues)
        {
            queueManager.initialize();
        }

        if (asyncEventHandling)
        {
            eventConnection.addEventListener(new ManagerEventListenerProxy(this));
        }
        else
        {
            eventConnection.addEventListener(this);
        }
    }

    /* Implementation of the AsteriskServer interface */

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout) throws ManagerCommunicationException, NoSuchChannelException
    {
        return originateToExtension(channel, context, exten, priority, timeout, null, null);
    }

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout, CallerId callerId, Map<String, String> variables) throws ManagerCommunicationException, NoSuchChannelException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setContext(context);
        originateAction.setExten(exten);
        originateAction.setPriority(priority);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        return originate(originateAction);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout) throws ManagerCommunicationException, NoSuchChannelException
    {
        return originateToApplication(channel, application, data, timeout, null, null);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout, CallerId callerId, Map<String, String> variables) throws ManagerCommunicationException, NoSuchChannelException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setApplication(application);
        originateAction.setData(data);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        return originate(originateAction);
    }

    private AsteriskChannel originate(OriginateAction originateAction) throws ManagerCommunicationException, NoSuchChannelException
    {
        final ResponseEvents responseEvents;
        final Iterator<ResponseEvent> responseEventIterator;
        final Map<String, String> variables;
        AsteriskChannel channel = null;

        if (originateAction.getVariables() == null)
        {
            variables = new HashMap<String, String>();
        }
        else
        {
            variables = new HashMap<String, String>(originateAction.getVariables());
        }
        variables.put("__AJ_TRACE_ID", "trace me!");
        originateAction.setVariables(variables);
        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);

        // 2000 ms extra for the OriginateFailureEvent should be fine
        responseEvents = sendEventGeneratingAction(originateAction,
                originateAction.getTimeout() + 2000);

        responseEventIterator = responseEvents.getEvents().iterator();
        if (responseEventIterator.hasNext())
        {
            ResponseEvent responseEvent;

            responseEvent = responseEventIterator.next();
            if (responseEvent instanceof AbstractOriginateEvent)
            {
                channel = getChannelById(((AbstractOriginateEvent) responseEvent).getUniqueId()); 
            }
        }

        if (channel == null)
        {
            throw new NoSuchChannelException("Channel '" + originateAction.getChannel() + " is not available");
        }

        return channel;
    }

    public void originateToExtensionAsync(String channel, String context, String exten, int priority, long timeout, OriginateCallback cb) throws ManagerCommunicationException
    {
        originateToExtensionAsync(channel, context, exten, priority, timeout, null, null, cb);
    }

    public void originateToExtensionAsync(String channel, String context, String exten, int priority, long timeout, CallerId callerId, Map<String, String> variables, OriginateCallback cb) throws ManagerCommunicationException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setContext(context);
        originateAction.setExten(exten);
        originateAction.setPriority(priority);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        originateAsync(originateAction, cb);
    }

    public void originateToApplicationAsync(String channel, String application, String data, long timeout, OriginateCallback cb) throws ManagerCommunicationException
    {
        originateToApplicationAsync(channel, application, data, timeout, null, null, cb);
    }

    public void originateToApplicationAsync(String channel, String application, String data, long timeout, CallerId callerId, Map<String, String> variables, OriginateCallback cb) throws ManagerCommunicationException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setApplication(application);
        originateAction.setData(data);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        originateAsync(originateAction, cb);
    }

    private void originateAsync(OriginateAction originateAction, OriginateCallback cb) throws ManagerCommunicationException
    {
        final String actionId;

        actionId = ACTION_ID_PREFIX_ORIGINATE + idCounter.getAndIncrement();
        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);
        originateAction.setActionId(actionId);

        if (cb != null)
        {
            // register callback
            synchronized (originateCallbacks)
            {
                originateCallbacks.put(actionId, cb);
            }
        }

        sendActionOnEventConnection(originateAction);
    }

    public Collection<AsteriskChannel> getChannels()
    {
        return channelManager.getChannels();
    }

    public AsteriskChannel getChannelByName(String name)
    {
        return channelManager.getChannelImplByName(name);
    }

    public AsteriskChannel getChannelById(String id)
    {
        return channelManager.getChannelImplById(id);
    }

    public Collection<MeetMeRoom> getMeetMeRooms()
    {
        return meetMeManager.getMeetMeRooms();
    }

    public MeetMeRoom getMeetMeRoom(String name)
    {
        return meetMeManager.getOrCreateRoomImpl(name);
    }

    public Collection<AsteriskQueue> getQueues()
    {
        return queueManager.getQueues();
    }

    public synchronized String getVersion() throws ManagerCommunicationException
    {
        final ManagerResponse response;

        if (version == null)
        {
            response = sendAction(new CommandAction("show version"));
            if (response instanceof CommandResponse)
            {
                final List result;

                result = ((CommandResponse) response).getResult();
                if (result.size() > 0)
                {
                    version = (String) result.get(0);
                }
            }
        }

        return version;
    }

    public int[] getVersion(String file)
    {
        String fileVersion = null;
        String[] parts;
        int[] intParts;

        if (versions == null)
        {
            Map<String, String> map;
            ManagerResponse response;

            map = new HashMap<String, String>();
            try
            {
                response = sendAction(new CommandAction("show version files"));
                if (response instanceof CommandResponse)
                {
                    List<String> result;

                    result = ((CommandResponse) response).getResult();
                    for (int i = 2; i < result.size(); i++)
                    {
                        String line;
                        Matcher matcher;

                        line = (String) result.get(i);
                        matcher = SHOW_VERSION_FILES_PATTERN.matcher(line);
                        if (matcher.find())
                        {
                            String key = matcher.group(1);
                            String value = matcher.group(2);

                            map.put(key, value);
                        }
                    }

                    fileVersion = (String) map.get(file);
                    versions = map;
                }
            }
            catch (Exception e)
            {
                logger.warn("Unable to send 'show version files' command.", e);
            }
        }
        else
        {
            synchronized (versions)
            {
                fileVersion = versions.get(file);
            }
        }

        if (fileVersion == null)
        {
            return null;
        }

        parts = fileVersion.split("\\.");
        intParts = new int[parts.length];

        for (int i = 0; i < parts.length; i++)
        {
            try
            {
                intParts[i] = Integer.parseInt(parts[i]);
            }
            catch (NumberFormatException e)
            {
                intParts[i] = 0;
            }
        }

        return intParts;
    }

    public String getGlobalVariable(String variable) throws ManagerCommunicationException
    {
        ManagerResponse response;
        String value;

        response = sendAction(new GetVarAction(variable));
        if (response instanceof ManagerError)
        {
            return null;
        }
        value = response.getAttribute("Value");
        if (value == null)
        {
            value = response.getAttribute(variable); // for Asterisk 1.0.x
        }
        return value;
    }

    public void setGlobalVariable(String variable, String value) throws ManagerCommunicationException
    {
        ManagerResponse response;

        response = sendAction(new SetVarAction(variable, value));
        if (response instanceof ManagerError)
        {
            logger.error("Unable to set global variable '" + variable + "' to '" + value + "':" + response.getMessage());
        }
    }

    public void addAsteriskServerListener(AsteriskServerListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    public void removeAsteriskServerListener(AsteriskServerListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }

    void fireNewAsteriskChannel(AsteriskChannel channel)
    {
        synchronized (listeners)
        {
            for (AsteriskServerListener listener : listeners)
            {
                try
                {
                    listener.onNewAsteriskChannel(channel);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onNewAsteriskChannel()", e);
                }
            }
        }
    }

    void fireNewMeetMeUser(MeetMeUser user)
    {
        synchronized (listeners)
        {
            for (AsteriskServerListener listener : listeners)
            {
                try
                {
                    listener.onNewMeetMeUser(user);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onNewMeetMeUser()", e);
                }
            }
        }
    }

    ManagerResponse sendActionOnEventConnection(ManagerAction action) throws ManagerCommunicationException
    {
        try
        {
            return eventConnection.sendAction(action);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    ManagerResponse sendAction(ManagerAction action) throws ManagerCommunicationException
    {
        return connectionPool.sendAction(action);
    }

    ResponseEvents sendEventGeneratingAction(EventGeneratingAction action) throws ManagerCommunicationException
    {
        return connectionPool.sendEventGeneratingAction(action);
    }

    ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, long timeout) throws ManagerCommunicationException
    {
        return connectionPool.sendEventGeneratingAction(action, timeout);
    }

    /* Implementation of the ManagerEventListener interface */

    /**
     * Handles all events received from the Asterisk server.<p>
     * Events are queued until channels and queues are initialized and then
     * delegated to the dispatchEvent method.
     */
    public void onManagerEvent(ManagerEvent event)
    {
        if (event instanceof ConnectEvent)
        {
            handleConnectEvent((ConnectEvent) event);
        }
        else if (event instanceof DisconnectEvent)
        {
            handleDisconnectEvent((DisconnectEvent) event);
        }
        else if (event instanceof NewChannelEvent)
        {
            channelManager.handleNewChannelEvent((NewChannelEvent) event);
        }
        else if (event instanceof NewExtenEvent)
        {
            channelManager.handleNewExtenEvent((NewExtenEvent) event);
        }
        else if (event instanceof NewStateEvent)
        {
            channelManager.handleNewStateEvent((NewStateEvent) event);
        }
        else if (event instanceof NewCallerIdEvent)
        {
            channelManager.handleNewCallerIdEvent((NewCallerIdEvent) event);
        }
        else if (event instanceof DialEvent)
        {
            channelManager.handleDialEvent((DialEvent) event);
        }
        else if (event instanceof LinkEvent)
        {
            channelManager.handleLinkEvent((LinkEvent) event);
        }
        else if (event instanceof UnlinkEvent)
        {
            channelManager.handleUnlinkEvent((UnlinkEvent) event);
        }
        else if (event instanceof RenameEvent)
        {
            channelManager.handleRenameEvent((RenameEvent) event);
        }
        else if (event instanceof HangupEvent)
        {
            channelManager.handleHangupEvent((HangupEvent) event);
        }
        else if (event instanceof CdrEvent)
        {
            channelManager.handleCdrEvent((CdrEvent) event);
        }
        else if (event instanceof JoinEvent)
        {
            queueManager.handleJoinEvent((JoinEvent) event);
        }
        else if (event instanceof LeaveEvent)
        {
            queueManager.handleLeaveEvent((LeaveEvent) event);
        }
        else if (event instanceof AbstractMeetMeEvent)
        {
            meetMeManager.handleMeetMeEvent((AbstractMeetMeEvent) event);
        }
        else if (event instanceof AbstractOriginateEvent)
        {
            handleOriginateEvent((AbstractOriginateEvent) event);
        }
    }

    /*
     * Resets the internal state when the connection to the asterisk server is
     * lost.
     */
    private void handleDisconnectEvent(DisconnectEvent disconnectEvent)
    {
        // reset version information as it might have changed while Asterisk
        // restarted
        version = null;
        versions = null;

        // same for channels and queues, they are reinitialized when reconnected
        channelManager.disconnected();
        meetMeManager.disconnected();
        queueManager.disconnected();
    }

    /*
     * Requests the current state from the asterisk server after the connection
     * to the asterisk server is restored.
     */
    private void handleConnectEvent(ConnectEvent connectEvent)
    {
        try
        {
            initialize();
        }
        catch (Exception e)
        {
            logger.error("Unable to reinitialize state after reconnection", e);
        }
    }
    
    private void handleOriginateEvent(AbstractOriginateEvent originateEvent)
    {
        final OriginateCallback cb;
        final AsteriskChannelImpl channel;
        final AsteriskChannelImpl otherChannel; // the other side if local channel

        if (originateEvent.getActionId() == null)
        {
            return;
        }

        synchronized (originateCallbacks)
        {
            cb = originateCallbacks.get(originateEvent.getActionId());
            if (cb == null)
            {
                return;
            }
            originateCallbacks.remove(originateEvent.getActionId());
        }

        channel = channelManager.getChannelImplById(originateEvent.getUniqueId());
        if (channel == null)
        {
            cb.onFailure();
            return;
        }

        if (channel.wasInState(ChannelState.UP))
        {
            cb.onSuccess(channel);
            return;
        }
        
        if (channel.wasInState(ChannelState.BUSY)
                || channel.getHangupCause() == HangupCause.AST_CAUSE_BUSY
                || channel.getHangupCause() == HangupCause.AST_CAUSE_USER_BUSY)
        {
            cb.onBusy(channel);
            return;
        }

        otherChannel = channelManager.getOtherSideOfLocalChannel(channel);
        // special treatment of local channels:
        // the interesting things happen to the other side so we have a look at that
        if (otherChannel != null)
        {
            final AsteriskChannel dialedChannel;

            dialedChannel = otherChannel.getDialedChannel();

            // on busy the other channel is in state busy when we receive the originate event
            if (otherChannel.wasInState(ChannelState.BUSY)
                    || otherChannel.getHangupCause() == HangupCause.AST_CAUSE_BUSY
                    || otherChannel.getHangupCause() == HangupCause.AST_CAUSE_USER_BUSY)
            {
                cb.onBusy(channel);
                return;
            }

            // alternative:
            // on busy the dialed channel is hung up when we receive the originate event
            // having a look at the hangup cause reveals the information we are interested in
            // this alternative has the drawback that there might by multiple channels that have
            // been dialed by the local channel but we only look at the last one.
            if (dialedChannel != null &&
                    (dialedChannel.wasInState(ChannelState.BUSY)
                    || dialedChannel.getHangupCause() == HangupCause.AST_CAUSE_BUSY
                    || dialedChannel.getHangupCause() == HangupCause.AST_CAUSE_USER_BUSY))
            {
                cb.onBusy(channel);
                return;
            }

        }

        // if nothing else matched we asume no answer
        cb.onNoAnswer(channel);
    }
}
