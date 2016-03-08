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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.config.ConfigFile;
import org.asteriskjava.live.AsteriskAgent;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.live.Voicemailbox;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.ManagerEventListenerProxy;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.DbGetAction;
import org.asteriskjava.manager.action.DbPutAction;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.GetConfigAction;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.MailboxCountAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.ModuleCheckAction;
import org.asteriskjava.manager.action.ModuleLoadAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.asteriskjava.manager.action.SipPeersAction;
import org.asteriskjava.manager.event.AbstractMeetMeEvent;
import org.asteriskjava.manager.event.AgentCallbackLoginEvent;
import org.asteriskjava.manager.event.AgentCallbackLogoffEvent;
import org.asteriskjava.manager.event.AgentCalledEvent;
import org.asteriskjava.manager.event.AgentCompleteEvent;
import org.asteriskjava.manager.event.AgentConnectEvent;
import org.asteriskjava.manager.event.AgentLoginEvent;
import org.asteriskjava.manager.event.AgentLogoffEvent;
import org.asteriskjava.manager.event.AgentsEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DbGetResponseEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.DtmfEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MonitorStartEvent;
import org.asteriskjava.manager.event.MonitorStopEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;
import org.asteriskjava.manager.event.ParkedCallGiveUpEvent;
import org.asteriskjava.manager.event.ParkedCallTimeOutEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberPenaltyEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.UnparkedCallEvent;
import org.asteriskjava.manager.event.VarSetEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.GetConfigResponse;
import org.asteriskjava.manager.response.MailboxCountResponse;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.manager.response.ModuleCheckResponse;
import org.asteriskjava.util.AstUtil;
import org.asteriskjava.util.DateUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Default implementation of the {@link AsteriskServer} interface.
 *
 * @author srt
 * @version $Id$
 */
public class AsteriskServerImpl implements AsteriskServer, ManagerEventListener
{
    private static final String ACTION_ID_PREFIX_ORIGINATE = "AJ_ORIGINATE_";
    private static final String SHOW_VERSION_COMMAND = "show version";
    private static final String SHOW_VERSION_1_6_COMMAND = "core show version";
    private static final String SHOW_VERSION_FILES_COMMAND = "show version files";
    private static final String SHOW_VERSION_FILES_1_6_COMMAND = "core show file version";
    private static final Pattern SHOW_VERSION_FILES_PATTERN = Pattern.compile("^([\\S]+)\\s+Revision: ([0-9\\.]+)");
    private static final String SHOW_VOICEMAIL_USERS_COMMAND = "show voicemail users";
    private static final String SHOW_VOICEMAIL_USERS_1_6_COMMAND = "voicemail show users";
    private static final Pattern SHOW_VOICEMAIL_USERS_PATTERN = Pattern.compile("^(\\S+)\\s+(\\S+)\\s+(.{25})");

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The underlying manager connection used to receive events from Asterisk.
     */
    private ManagerConnection eventConnection;
    private ManagerEventListener eventListener = null;
    private ManagerEventListenerProxy managerEventListenerProxy;

    private boolean initialized = false;
    private boolean initializing = false;

    final Set<AsteriskServerListener> listeners;

    final ChannelManager channelManager;
    final MeetMeManager meetMeManager;
    final QueueManager queueManager;
    final AgentManager agentManager;

    /**
     * The exact version string of the Asterisk server we are connected to.
     * <p/>
     * Contains <code>null</code> until lazily initialized.
     */
    private String version;

    /**
     * Holds the version of Asterisk's source files.
     * <p/>
     * That corresponds to the output of the CLI command
     * <code>show version files</code>.
     * <p/>
     * Contains <code>null</code> until lazily initialized.
     */
    private Map<String, String> versions;

    /**
     * Maps the traceId to the corresponding callback data.
     */
    private final Map<String, OriginateCallbackData> originateCallbacks;

    private final AtomicLong idCounter;

    /* config options */

    /**
     * Flag to skip initializing queues as that results in a timeout on Asterisk
     * 1.0.x.
     */
    private boolean skipQueues;

    /**
     * Set to <code>true</code> to not handle ManagerEvents in the reader tread
     * but process them asynchronously. This is a good idea :)
     */
    private boolean asyncEventHandling = true;

    /**
     * The chainListener allows a listener to receive manager events after they
     * have been processed by the AsteriskServer. If the AsteriskServer is
     * handling messages using the asyncEventHandling then these messages will
     * also be async. You would use the chainListener if you are processing raw
     * events and using the AJ live ChannelManager. If you don't use the chain
     * listener then you can't be certain that a channel name passed in a raw
     * event will match the channel name held by the Channel Manager. By
     * chaining events you can be certain that events such as channel Rename
     * events have been processed by the live ChannelManager before you receive
     * an event and as such the names will always match.
     */
    private List<ManagerEventListener> chainListeners = new ArrayList<>();

    /**
     * Creates a new instance.
     */
    public AsteriskServerImpl()
    {
        idCounter = new AtomicLong();
        listeners = new LinkedHashSet<>();
        originateCallbacks = new HashMap<>();
        channelManager = new ChannelManager(this);
        agentManager = new AgentManager(this);
        meetMeManager = new MeetMeManager(this, channelManager);
        queueManager = new QueueManager(this, channelManager);
    }

    /**
     * Creates a new instance.
     *
     * @param eventConnection the ManagerConnection to use for receiving events
     *            from Asterisk.
     */
    public AsteriskServerImpl(ManagerConnection eventConnection)
    {
        this();
        setManagerConnection(eventConnection); // todo: !!! Possible bug !!!:
                                               // call to overridable method
                                               // over object construction
    }

    /**
     * Determines if queue status is retrieved at startup. If you don't need
     * queue information and still run Asterisk 1.0.x you can set this to
     * <code>true</code> to circumvent the startup delay caused by the missing
     * QueueStatusComplete event. <br>
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
        if (this.eventConnection != null)
        {
            throw new IllegalStateException("ManagerConnection already set.");
        }

        this.eventConnection = eventConnection;
    }

    public ManagerConnection getManagerConnection()
    {
        return eventConnection;
    }

    public void initialize() throws ManagerCommunicationException
    {
        initializeIfNeeded();
    }

    private synchronized void initializeIfNeeded() throws ManagerCommunicationException
    {
        if (initialized || initializing)
        {
            return;
        }
        if (asyncEventHandling && managerEventListenerProxy == null)
        {
            managerEventListenerProxy = new ManagerEventListenerProxy(this);
            eventConnection.addEventListener(managerEventListenerProxy);
        }
        else if (!asyncEventHandling && eventListener == null)
        {
            eventListener = this;
            eventConnection.addEventListener(eventListener);
        }

        initializing = true;

        if (eventConnection.getState() == ManagerConnectionState.INITIAL
                || eventConnection.getState() == ManagerConnectionState.DISCONNECTED)
        {
            try
            {
                eventConnection.login();
            }
            catch (Exception e)
            {
                throw new ManagerCommunicationException("Unable to login: " + e.getMessage(), e);
            }
        }

        channelManager.initialize();
        agentManager.initialize();
        meetMeManager.initialize();
        if (!skipQueues)
        {
            queueManager.initialize();
        }

        if (asyncEventHandling && managerEventListenerProxy == null)
        {
            managerEventListenerProxy = new ManagerEventListenerProxy(this);
            eventConnection.addEventListener(managerEventListenerProxy);
        }
        else if (!asyncEventHandling && eventListener == null)
        {
            eventListener = this;
            eventConnection.addEventListener(eventListener);
        }
        logger.info("Initializing done");
        initializing = false;
        initialized = true;
    }

    /* Implementation of the AsteriskServer interface */

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout)
            throws ManagerCommunicationException, NoSuchChannelException
    {
        return originateToExtension(channel, context, exten, priority, timeout, null, null);
    }

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout,
            CallerId callerId, Map<String, String> variables) throws ManagerCommunicationException, NoSuchChannelException
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

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout)
            throws ManagerCommunicationException, NoSuchChannelException
    {
        return originateToApplication(channel, application, data, timeout, null, null);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout,
            CallerId callerId, Map<String, String> variables) throws ManagerCommunicationException, NoSuchChannelException
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

    public AsteriskChannel originate(OriginateAction originateAction)
            throws ManagerCommunicationException, NoSuchChannelException
    {
        final ResponseEvents responseEvents;
        final Iterator<ResponseEvent> responseEventIterator;
        String uniqueId;
        AsteriskChannel channel = null;

        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);

        initializeIfNeeded();

        // 2000 ms extra for the OriginateFailureEvent should be fine
        responseEvents = sendEventGeneratingAction(originateAction, originateAction.getTimeout() + 2000);

        responseEventIterator = responseEvents.getEvents().iterator();
        if (responseEventIterator.hasNext())
        {
            ResponseEvent responseEvent;

            responseEvent = responseEventIterator.next();
            if (responseEvent instanceof OriginateResponseEvent)
            {
                uniqueId = ((OriginateResponseEvent) responseEvent).getUniqueId();
                logger.debug(responseEvent.getClass().getName() + " received with uniqueId " + uniqueId);
                channel = getChannelById(uniqueId);
            }
        }

        if (channel == null)
        {
            throw new NoSuchChannelException("Channel '" + originateAction.getChannel() + "' is not available");
        }

        return channel;
    }

    public void originateToExtensionAsync(String channel, String context, String exten, int priority, long timeout,
            OriginateCallback cb) throws ManagerCommunicationException
    {
        originateToExtensionAsync(channel, context, exten, priority, timeout, null, null, cb);
    }

    public void originateToExtensionAsync(String channel, String context, String exten, int priority, long timeout,
            CallerId callerId, Map<String, String> variables, OriginateCallback cb) throws ManagerCommunicationException
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

    public void originateToApplicationAsync(String channel, String application, String data, long timeout,
            OriginateCallback cb) throws ManagerCommunicationException
    {
        originateToApplicationAsync(channel, application, data, timeout, null, null, cb);
    }

    public void originateToApplicationAsync(String channel, String application, String data, long timeout, CallerId callerId,
            Map<String, String> variables, OriginateCallback cb) throws ManagerCommunicationException
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

    public void originateAsync(OriginateAction originateAction, OriginateCallback cb) throws ManagerCommunicationException
    {
        final Map<String, String> variables;
        final String traceId;

        traceId = ACTION_ID_PREFIX_ORIGINATE + idCounter.getAndIncrement();
        if (originateAction.getVariables() == null)
        {
            variables = new HashMap<>();
        }
        else
        {
            variables = new HashMap<>(originateAction.getVariables());
        }

        // prefix variable name by "__" to enable variable inheritence across
        // channels
        variables.put("__" + Constants.VARIABLE_TRACE_ID, traceId);
        originateAction.setVariables(variables);

        // async must be set to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);
        originateAction.setActionId(traceId);

        if (cb != null)
        {
            final OriginateCallbackData callbackData;

            callbackData = new OriginateCallbackData(originateAction, DateUtil.getDate(), cb);
            // register callback
            synchronized (originateCallbacks)
            {
                originateCallbacks.put(traceId, callbackData);
            }
        }

        initializeIfNeeded();
        sendActionOnEventConnection(originateAction);
    }

    public Collection<AsteriskChannel> getChannels() throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return channelManager.getChannels();
    }

    public AsteriskChannel getChannelByName(String name) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return channelManager.getChannelImplByName(name);
    }

    public AsteriskChannel getChannelById(String id) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return channelManager.getChannelImplById(id);
    }

    public Collection<MeetMeRoom> getMeetMeRooms() throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return meetMeManager.getMeetMeRooms();
    }

    public MeetMeRoom getMeetMeRoom(String name) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return meetMeManager.getOrCreateRoomImpl(name);
    }

    public Collection<AsteriskQueue> getQueues() throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return queueManager.getQueues();
    }

    @Override
    public AsteriskQueue getQueueByName(String queueName)
    {
        initializeIfNeeded();
        return queueManager.getQueueByName(queueName);
    }

    @Override
    public List<AsteriskQueue> getQueuesUpdatedAfter(Date date)
    {
        initializeIfNeeded();
        return queueManager.getQueuesUpdatedAfter(date);
    }

    public synchronized String getVersion() throws ManagerCommunicationException
    {
        final ManagerResponse response;
        final String command;

        initializeIfNeeded();
        if (version == null)
        {
            if (eventConnection.getVersion().isAtLeast(AsteriskVersion.ASTERISK_1_6))
            {
                command = SHOW_VERSION_1_6_COMMAND;
            }
            else
            {
                command = SHOW_VERSION_COMMAND;
            }

            response = sendAction(new CommandAction(command));
            if (response instanceof CommandResponse)
            {
                final List<String> result;

                result = ((CommandResponse) response).getResult();
                if (!result.isEmpty())
                {
                    version = result.get(0);
                }
            }
            else
            {
                logger.error("Response to CommandAction(\"" + command + "\") was not a CommandResponse but " + response);
            }
        }

        return version;
    }

    public int[] getVersion(String file) throws ManagerCommunicationException
    {
        String fileVersion = null;
        String[] parts;
        int[] intParts;

        initializeIfNeeded();
        if (versions == null)
        {
            Map<String, String> map;
            ManagerResponse response;

            map = new HashMap<>();
            try
            {
                final String command;

                if (eventConnection.getVersion().isAtLeast(AsteriskVersion.ASTERISK_1_6))
                {
                    command = SHOW_VERSION_FILES_1_6_COMMAND;
                }
                else
                {
                    command = SHOW_VERSION_FILES_COMMAND;
                }
                response = sendAction(new CommandAction(command));
                if (response instanceof CommandResponse)
                {
                    List<String> result;

                    result = ((CommandResponse) response).getResult();
                    for (int i = 2; i < result.size(); i++)
                    {
                        String line;
                        Matcher matcher;

                        line = result.get(i);
                        matcher = SHOW_VERSION_FILES_PATTERN.matcher(line);
                        if (matcher.find())
                        {
                            String key = matcher.group(1);
                            String value = matcher.group(2);

                            map.put(key, value);
                        }
                    }

                    fileVersion = map.get(file);
                    versions = map;
                }
                else
                {
                    logger.error("Response to CommandAction(\"" + command + "\") was not a CommandResponse but " + response);
                }
            }
            catch (Exception e)
            {
                logger.warn("Unable to send '" + SHOW_VERSION_FILES_COMMAND + "' command.", e);
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

        initializeIfNeeded();
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

        initializeIfNeeded();
        response = sendAction(new SetVarAction(variable, value));
        if (response instanceof ManagerError)
        {
            logger.error("Unable to set global variable '" + variable + "' to '" + value + "':" + response.getMessage());
        }
    }

    public Collection<Voicemailbox> getVoicemailboxes() throws ManagerCommunicationException
    {
        final Collection<Voicemailbox> voicemailboxes;
        ManagerResponse response;
        final List<String> result;

        initializeIfNeeded();
        voicemailboxes = new ArrayList<>();
        if (eventConnection.getVersion().isAtLeast(AsteriskVersion.ASTERISK_1_6))
        {
            response = sendAction(new CommandAction(SHOW_VOICEMAIL_USERS_1_6_COMMAND));
        }
        else
        {
            response = sendAction(new CommandAction(SHOW_VOICEMAIL_USERS_COMMAND));
        }
        if (!(response instanceof CommandResponse))
        {
            logger.error("Response to CommandAction(\"" + SHOW_VOICEMAIL_USERS_COMMAND + "\") was not a CommandResponse but "
                    + response);
            return voicemailboxes;
        }

        result = ((CommandResponse) response).getResult();
        if (result == null || result.isEmpty())
        {
            return voicemailboxes;
        }

        // remove headline
        result.remove(0);

        for (String line : result)
        {
            final Matcher matcher;
            final Voicemailbox voicemailbox;
            final String context;
            final String mailbox;
            final String user;

            matcher = SHOW_VOICEMAIL_USERS_PATTERN.matcher(line);
            if (!matcher.find())
            {
                continue;
            }

            context = matcher.group(1);
            mailbox = matcher.group(2);
            user = matcher.group(3).trim();

            voicemailbox = new Voicemailbox(mailbox, context, user);
            voicemailboxes.add(voicemailbox);
        }

        // get message count for each mailbox
        for (Voicemailbox voicemailbox : voicemailboxes)
        {
            final String fullname;

            fullname = voicemailbox.getMailbox() + "@" + voicemailbox.getContext();
            response = sendAction(new MailboxCountAction(fullname));
            if (response instanceof MailboxCountResponse)
            {
                MailboxCountResponse mailboxCountResponse;

                mailboxCountResponse = (MailboxCountResponse) response;
                voicemailbox.setNewMessages(mailboxCountResponse.getNewMessages());
                voicemailbox.setOldMessages(mailboxCountResponse.getOldMessages());
            }
            else
            {
                logger.error("Response to MailboxCountAction was not a MailboxCountResponse but " + response);
            }
        }

        return voicemailboxes;
    }

    public List<String> executeCliCommand(String command) throws ManagerCommunicationException
    {
        final ManagerResponse response;

        initializeIfNeeded();
        response = sendAction(new CommandAction(command));
        if (!(response instanceof CommandResponse))
        {
            throw new ManagerCommunicationException(
                    "Response to CommandAction(\"" + command + "\") was not a CommandResponse but " + response, null);
        }

        return ((CommandResponse) response).getResult();
    }

    public boolean isModuleLoaded(String module) throws ManagerCommunicationException
    {
        return sendAction(new ModuleCheckAction(module)) instanceof ModuleCheckResponse;
    }

    public void loadModule(String module) throws ManagerCommunicationException
    {
        sendModuleLoadAction(module, ModuleLoadAction.LOAD_TYPE_LOAD);
    }

    public void unloadModule(String module) throws ManagerCommunicationException
    {
        sendModuleLoadAction(module, ModuleLoadAction.LOAD_TYPE_UNLOAD);
    }

    public void reloadModule(String module) throws ManagerCommunicationException
    {
        sendModuleLoadAction(module, ModuleLoadAction.LOAD_TYPE_RELOAD);
    }

    public void reloadAllModules() throws ManagerCommunicationException
    {
        sendModuleLoadAction(null, ModuleLoadAction.LOAD_TYPE_RELOAD);
    }

    protected void sendModuleLoadAction(String module, String loadType) throws ManagerCommunicationException
    {
        final ManagerResponse response;

        response = sendAction(new ModuleLoadAction(module, loadType));
        if (response instanceof ManagerError)
        {
            final ManagerError error = (ManagerError) response;
            throw new ManagerCommunicationException(error.getMessage(), null);
        }
    }

    public ConfigFile getConfig(String filename) throws ManagerCommunicationException
    {
        final ManagerResponse response;
        final GetConfigResponse getConfigResponse;

        initializeIfNeeded();
        response = sendAction(new GetConfigAction(filename));
        if (!(response instanceof GetConfigResponse))
        {
            throw new ManagerCommunicationException(
                    "Response to GetConfigAction(\"" + filename + "\") was not a CommandResponse but " + response, null);
        }

        getConfigResponse = (GetConfigResponse) response;

        final Map<Integer, String> categoryMap = getConfigResponse.getCategories();
        final Map<String, List<String>> categories = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> categoryEntry : categoryMap.entrySet())
        {
            final List<String> lines;
            final Map<Integer, String> lineMap = getConfigResponse.getLines(categoryEntry.getKey());

            if (lineMap == null)
            {
                lines = new ArrayList<>();
            }
            else
            {
                lines = new ArrayList<>(lineMap.values());
            }

            categories.put(categoryEntry.getValue(), lines);
        }

        return new ConfigFileImpl(filename, categories);
    }

    @Override
    public void addAsteriskServerListener(AsteriskServerListener listener) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        synchronized (listeners)
        {
            if (!listeners.contains(listener))
            {
                listeners.add(listener);
            }
        }
    }

    @Override
    public void removeAsteriskServerListener(AsteriskServerListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }

    @Override
    public boolean isAsteriskServerListening(AsteriskServerListener listener)
    {
        return listeners.contains(listener);
    }

    public void addChainListener(ManagerEventListener chainListener)
    {
        synchronized (this.chainListeners)
        {
            if (!this.chainListeners.contains(chainListener))
                this.chainListeners.add(chainListener);
        }

    }

    public void removeChainListener(ManagerEventListener chainListener)
    {
        this.chainListeners.remove(chainListener);
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
        // return connectionPool.sendAction(action);
        try
        {
            return eventConnection.sendAction(action);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    ResponseEvents sendEventGeneratingAction(EventGeneratingAction action) throws ManagerCommunicationException
    {
        // return connectionPool.sendEventGeneratingAction(action);
        try
        {
            return eventConnection.sendEventGeneratingAction(action);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    ResponseEvents

    sendEventGeneratingAction(EventGeneratingAction action, long timeout) throws ManagerCommunicationException
    {
        // return connectionPool.sendEventGeneratingAction(action, timeout);
        try
        {
            return eventConnection.sendEventGeneratingAction(action, timeout);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    OriginateCallbackData getOriginateCallbackDataByTraceId(String traceId)
    {
        synchronized (originateCallbacks)
        {
            return originateCallbacks.get(traceId);
        }
    }

    /* Implementation of the ManagerEventListener interface */

    /**
     * Handles all events received from the Asterisk server. <br>
     * Events are queued until channels and queues are initialized and then
     * delegated to the dispatchEvent method.
     */
    public void onManagerEvent(ManagerEvent event)
    {
        // Handle Channel related events
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
        else if (event instanceof BridgeEvent)
        {
            channelManager.handleBridgeEvent((BridgeEvent) event);
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
        else if (event instanceof VarSetEvent)
        {
            channelManager.handleVarSetEvent((VarSetEvent) event);
        }
        else if (event instanceof DtmfEvent)
        {
            channelManager.handleDtmfEvent((DtmfEvent) event);
        }
        else if (event instanceof MonitorStartEvent)
        {
            channelManager.handleMonitorStartEvent((MonitorStartEvent) event);
        }
        else if (event instanceof MonitorStopEvent)
        {
            channelManager.handleMonitorStopEvent((MonitorStopEvent) event);
        }
        // End of channel related events
        // Handle parking related event
        else if (event instanceof ParkedCallEvent)
        {
            channelManager.handleParkedCallEvent((ParkedCallEvent) event);
        }
        else if (event instanceof ParkedCallGiveUpEvent)
        {
            channelManager.handleParkedCallGiveUpEvent((ParkedCallGiveUpEvent) event);
        }
        else if (event instanceof ParkedCallTimeOutEvent)
        {
            channelManager.handleParkedCallTimeOutEvent((ParkedCallTimeOutEvent) event);
        }
        else if (event instanceof UnparkedCallEvent)
        {
            channelManager.handleUnparkedCallEvent((UnparkedCallEvent) event);
        }
        // End of parking related events
        // Handle queue related event
        else if (event instanceof JoinEvent)
        {
            queueManager.handleJoinEvent((JoinEvent) event);
        }
        else if (event instanceof LeaveEvent)
        {
            queueManager.handleLeaveEvent((LeaveEvent) event);
        }
        else if (event instanceof QueueMemberStatusEvent)
        {
            queueManager.handleQueueMemberStatusEvent((QueueMemberStatusEvent) event);
        }
        else if (event instanceof QueueMemberPenaltyEvent)
        {
            queueManager.handleQueueMemberPenaltyEvent((QueueMemberPenaltyEvent) event);
        }
        else if (event instanceof QueueMemberAddedEvent)
        {
            queueManager.handleQueueMemberAddedEvent((QueueMemberAddedEvent) event);
        }
        else if (event instanceof QueueMemberRemovedEvent)
        {
            queueManager.handleQueueMemberRemovedEvent((QueueMemberRemovedEvent) event);
        }
        else if (event instanceof QueueMemberPausedEvent)
        {
            queueManager.handleQueueMemberPausedEvent((QueueMemberPausedEvent) event);
        }
        // >>>>>> AJ 94
        // Handle meetMeEvents
        else if (event instanceof AbstractMeetMeEvent)
        {
            meetMeManager.handleMeetMeEvent((AbstractMeetMeEvent) event);
        }
        else if (event instanceof OriginateResponseEvent)
        {
            handleOriginateEvent((OriginateResponseEvent) event);
        }
        // Handle agents-related events
        else if (event instanceof AgentsEvent)
        {
            agentManager.handleAgentsEvent((AgentsEvent) event);
        }
        else if (event instanceof AgentCalledEvent)
        {
            agentManager.handleAgentCalledEvent((AgentCalledEvent) event);
        }
        else if (event instanceof AgentConnectEvent)
        {
            agentManager.handleAgentConnectEvent((AgentConnectEvent) event);
        }
        else if (event instanceof AgentCompleteEvent)
        {
            agentManager.handleAgentCompleteEvent((AgentCompleteEvent) event);
        }
        else if (event instanceof AgentCallbackLoginEvent)
        {
            agentManager.handleAgentCallbackLoginEvent((AgentCallbackLoginEvent) event);
        }
        else if (event instanceof AgentCallbackLogoffEvent)
        {
            agentManager.handleAgentCallbackLogoffEvent((AgentCallbackLogoffEvent) event);
        }
        else if (event instanceof AgentLoginEvent)
        {
            agentManager.handleAgentLoginEvent((AgentLoginEvent) event);
        }
        else if (event instanceof AgentLogoffEvent)
        {
            agentManager.handleAgentLogoffEvent((AgentLogoffEvent) event);
        }
        // End of agent-related events

        // dispatch the events to the chainListener if they exist.
        fireChainListeners(event);
    }

    /**
     * dispatch the event to the chainListener if they exist.
     * 
     * @param event
     */
    private void fireChainListeners(ManagerEvent event)
    {
        synchronized (this.chainListeners)
        {
            for (ManagerEventListener listener : this.chainListeners)
                listener.onManagerEvent(event);
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

        // same for channels, agents and queues rooms, they are reinitialized
        // when reconnected
        channelManager.disconnected();
        agentManager.disconnected();
        meetMeManager.disconnected();
        queueManager.disconnected();
        initialized = false;
        initializing = false;
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

            channelManager.initialize();
            agentManager.initialize();
            meetMeManager.initialize();
            if (!skipQueues)
            {
                queueManager.initialize();
            }

            logger.info("Initializing done");
            initialized = true;
            initializing = false;
        }
        catch (Exception e)
        {
            logger.error("Unable to reinitialize state after reconnection", e);
        }
    }

    private void handleOriginateEvent(OriginateResponseEvent originateEvent)
    {
        final String traceId;
        final OriginateCallbackData callbackData;
        final OriginateCallback cb;
        final AsteriskChannelImpl channel;
        final AsteriskChannelImpl otherChannel; // the other side if local
                                                // channel

        traceId = originateEvent.getActionId();
        if (traceId == null)
        {
            return;
        }

        synchronized (originateCallbacks)
        {
            callbackData = originateCallbacks.get(traceId);
            if (callbackData == null)
            {
                return;
            }
            originateCallbacks.remove(traceId);
        }

        cb = callbackData.getCallback();
        if (!AstUtil.isNull(originateEvent.getUniqueId()))
        {
            channel = channelManager.getChannelImplById(originateEvent.getUniqueId());
        }
        else
        {
            channel = callbackData.getChannel();
        }

        try
        {
            if (channel == null)
            {
                final LiveException cause;

                cause = new NoSuchChannelException(
                        "Channel '" + callbackData.getOriginateAction().getChannel() + "' is not available");
                cb.onFailure(cause);
                return;
            }

            if (channel.wasInState(ChannelState.UP))
            {
                cb.onSuccess(channel);
                return;
            }

            if (channel.wasBusy())
            {
                cb.onBusy(channel);
                return;
            }

            otherChannel = channelManager.getOtherSideOfLocalChannel(channel);
            // special treatment of local channels:
            // the interesting things happen to the other side so we have a look
            // at that
            if (otherChannel != null)
            {
                final AsteriskChannel dialedChannel;

                dialedChannel = otherChannel.getDialedChannel();

                // on busy the other channel is in state busy when we receive
                // the originate event
                if (otherChannel.wasBusy())
                {
                    cb.onBusy(channel);
                    return;
                }

                // alternative:
                // on busy the dialed channel is hung up when we receive the
                // originate event having a look at the hangup cause reveals the
                // information we are interested in
                // this alternative has the drawback that there might by
                // multiple channels that have been dialed by the local channel
                // but we only look at the last one.
                if (dialedChannel != null && dialedChannel.wasBusy())
                {
                    cb.onBusy(channel);
                    return;
                }
            }

            // if nothing else matched we asume no answer
            cb.onNoAnswer(channel);
        }
        catch (Throwable t)
        {
            logger.warn("Exception dispatching originate progress", t);
        }
    }

    @Override
    public void shutdown()
    {
        if (eventConnection != null && (eventConnection.getState() == ManagerConnectionState.CONNECTED
                || eventConnection.getState() == ManagerConnectionState.RECONNECTING))
        {
            try
            {
                eventConnection.logoff();
            }
            catch (Exception ignore)
            {
            }
        }

        if (managerEventListenerProxy != null)
        {
            if (eventConnection != null)
            {
                eventConnection.removeEventListener(managerEventListenerProxy);
            }
            managerEventListenerProxy.shutdown();
        }

        if (eventConnection != null && eventListener != null)
        {
            eventConnection.removeEventListener(eventListener);
        }

        managerEventListenerProxy = null;
        eventListener = null;

        if (initialized)
        {// incredible, but it happened
            handleDisconnectEvent(null);
        } // i

    }// shutdown

    public List<PeerEntryEvent> getPeerEntries() throws ManagerCommunicationException
    {
        ResponseEvents responseEvents = sendEventGeneratingAction(new SipPeersAction(), 2000);
        List<PeerEntryEvent> peerEntries = new ArrayList<>(30);
        for (ResponseEvent re : responseEvents.getEvents())
        {
            if (re instanceof PeerEntryEvent)
            {
                peerEntries.add((PeerEntryEvent) re);
            }
        }
        return peerEntries;
    }

    public DbGetResponseEvent dbGet(String family, String key) throws ManagerCommunicationException
    {
        ResponseEvents responseEvents = sendEventGeneratingAction(new DbGetAction(family, key), 2000);
        DbGetResponseEvent dbgre = null;
        for (ResponseEvent re : responseEvents.getEvents())
        {
            dbgre = (DbGetResponseEvent) re;
        }
        return dbgre;
    }

    public void dbDel(String family, String key) throws ManagerCommunicationException
    {
        // The following only works with BRIStuffed asrterisk: sendAction(new
        // DbDelAction(family,key));
        // Use cli command instead ...
        sendAction(new CommandAction("database del " + family + " " + key));
    }

    public void dbPut(String family, String key, String value) throws ManagerCommunicationException
    {
        sendAction(new DbPutAction(family, key, value));
    }

    public AsteriskChannel getChannelByNameAndActive(String name) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return channelManager.getChannelImplByNameAndActive(name);
    }

    public Collection<AsteriskAgent> getAgents() throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return agentManager.getAgents();
    }

    void fireNewAgent(AsteriskAgentImpl agent)
    {
        synchronized (listeners)
        {
            for (AsteriskServerListener listener : listeners)
            {
                try
                {
                    listener.onNewAgent(agent);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onNewAgent()", e);
                }
            }
        }
    }

    void fireNewQueueEntry(AsteriskQueueEntry entry)
    {
        synchronized (listeners)
        {
            for (AsteriskServerListener listener : listeners)
            {
                try
                {
                    listener.onNewQueueEntry(entry);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onNewQueueEntry()", e);
                }
            }
        }
    }

    /* OCTAVIO LUNA */
    @Override
    public void forceQueuesMonitor(boolean force)
    {
        queueManager.forceQueuesMonitor(force);
    }

    @Override
    public boolean isQueuesMonitorForced()
    {
        return queueManager.isQueuesMonitorForced();
    }
}
