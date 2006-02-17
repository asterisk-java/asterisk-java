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
package org.asteriskjava.live;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.live.impl.AsteriskChannelImpl;
import org.asteriskjava.live.impl.ChannelManager;
import org.asteriskjava.live.impl.ManagerConnectionPool;
import org.asteriskjava.live.impl.QueueManager;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventHandler;
import org.asteriskjava.manager.Originate;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
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
import org.asteriskjava.manager.event.OriginateEvent;
import org.asteriskjava.manager.event.OriginateFailureEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Default implementation of the AsteriskManager interface.
 * 
 * @see org.asteriskjava.live.AsteriskManager
 * @author srt
 * @version $Id: DefaultAsteriskManager.java,v 1.23 2005/10/29 12:09:05 srt Exp $
 */
public class DefaultAsteriskManager
        implements
            AsteriskManager,
            ManagerEventHandler
{
    private static final Pattern SHOW_VERSION_FILES_PATTERN = Pattern
            .compile("^([\\S]+)\\s+Revision: ([0-9\\.]+)");

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The underlying manager connection used to receive events from Asterisk.
     */
    private ManagerConnection eventConnection;

    private final ChannelManager channelManager;

    private final QueueManager queueManager;
    
    private final ManagerConnectionPool connectionPool;

    /**
     * The version of the Asterisk server we are connected to.<br>
     * Contains <code>null</code> until lazily initialized.
     */
    private String version;

    /**
     * Holds the version of Asterisk's source files.<br>
     * That corresponds to the output of the CLI command
     * <code>show version files</code>.<br>
     * Contains <code>null</code> until lazily initialized.
     */
    private Map versions;

    /**
     * Flag to skip initializing queues as that results in a timeout on Asterisk
     * 1.0.x.
     */
    private boolean skipQueues;

    /**
     * Creates a new instance.
     */
    public DefaultAsteriskManager()
    {
        connectionPool = new ManagerConnectionPool(1);
        channelManager = new ChannelManager(connectionPool);
        queueManager = new QueueManager(channelManager);
    }

    /**
     * Creates a new instance.
     * 
     * @param eventConnection the ManagerConnection to use for receiving events from Asterisk.
     */
    public DefaultAsteriskManager(ManagerConnection eventConnection)
    {
        this();
        this.eventConnection = eventConnection;
        this.connectionPool.put(eventConnection);
    }

    /**
     * Determines if queue status is retrieved at startup. If you don't need
     * queue information and still run Asterisk 1.0.x you can set this to
     * <code>true</code> to circumvent the startup delay caused by the missing
     * QueueStatusComplete event.<br>
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
        this.connectionPool.put(eventConnection);
    }

    public void initialize() throws TimeoutException, IOException,
            AuthenticationFailedException
    {
        eventConnection.login();

        initializeChannels();
        initializeQueues();

        eventConnection.addEventHandler(this);
    }

    private void initializeChannels() throws EventTimeoutException, IOException
    {
        ResponseEvents re;

        re = eventConnection.sendEventGeneratingAction(new StatusAction());
        for (ManagerEvent event : re.getEvents())
        {
            if (event instanceof StatusEvent)
            {
                channelManager.handleStatusEvent((StatusEvent) event);
            }
        }
    }

    private void initializeQueues() throws IOException
    {
        ResponseEvents re;

        if (skipQueues)
        {
            return;
        }

        try
        {
            re = eventConnection.sendEventGeneratingAction(new QueueStatusAction());
        }
        catch (EventTimeoutException e)
        {
            // this happens with Asterisk 1.0.x as it doesn't send a
            // QueueStatusCompleteEvent
            re = e.getPartialResult();
        }

        for (ManagerEvent event : re.getEvents())
        {
            if (event instanceof QueueParamsEvent)
            {
                queueManager.handleQueueParamsEvent((QueueParamsEvent) event);
            }
            else if (event instanceof QueueMemberEvent)
            {
                queueManager.handleQueueMemberEvent((QueueMemberEvent) event);
            }
            else if (event instanceof QueueEntryEvent)
            {
                queueManager.handleQueueEntryEvent((QueueEntryEvent) event);
            }
        }
    }

    /* Implementation of the AsteriskManager interface */

    public Call originateCall(Originate originate) throws TimeoutException,
            IOException
    {
        OriginateAction originateAction;
        ResponseEvents responseEvents;
        Long timeout;

        if (originate.getTimeout() == null)
        {
            timeout = new Long(30000);
        }
        else
        {
            timeout = originate.getTimeout();
        }

        originateAction = new OriginateAction();
        originateAction.setAccount(originate.getAccount());
        originateAction.setApplication(originate.getApplication());
        originateAction.setCallerId(originate.getCallerId());
        originateAction.setChannel(originate.getChannel());
        originateAction.setContext(originate.getContext());
        originateAction.setData(originate.getData());
        originateAction.setExten(originate.getExten());
        originateAction.setPriority(originate.getPriority());
        originateAction.setTimeout(timeout);
        originateAction.setVariables(originate.getVariables());

        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);

        // 2000 ms extra for the OriginateFailureEvent should be fine
        responseEvents = eventConnection.sendEventGeneratingAction(originateAction,
                timeout.longValue() + 2000);

        return originateEvent2Call((OriginateEvent) responseEvents.getEvents()
                .toArray()[0]);
    }

    public Map<String, AsteriskChannel> getChannels()
    {
        return channelManager.getChannels();
    }

    public Map<String, AsteriskQueue> getQueues()
    {
        return queueManager.getQueues();
    }

    public String getVersion()
    {
        if (version == null)
        {
            ManagerResponse response;
            try
            {
                response = eventConnection.sendAction(new CommandAction(
                        "show version"));
                if (response instanceof CommandResponse)
                {
                    List result;

                    result = ((CommandResponse) response).getResult();
                    if (result.size() > 0)
                    {
                        version = (String) result.get(0);
                    }
                }
            }
            catch (Exception e)
            {
                logger.warn("Unable to send 'show version' command.", e);
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
                response = eventConnection.sendAction(new CommandAction(
                        "show version files"));
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
                fileVersion = (String) versions.get(file);
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

    /* Implementation of the ManagerEventHandler interface */

    /**
     * Handles all events received from the asterisk server.<br>
     * Events are queued until channels and queues are initialized and then
     * delegated to the dispatchEvent method.
     */
    public void handleEvent(ManagerEvent event)
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
        else if (event instanceof JoinEvent)
        {
            queueManager.handleJoinEvent((JoinEvent) event);
        }
        else if (event instanceof LeaveEvent)
        {
            queueManager.handleLeaveEvent((LeaveEvent) event);
        }
    }

    /**
     * Resets the internal state when the connection to the asterisk server is
     * lost.
     */
    protected void handleDisconnectEvent(DisconnectEvent disconnectEvent)
    {
        // reset version information as it might have changed while Asterisk
        // restarted
        version = null;
        versions = null;

        channelManager.clear();
        queueManager.clear();
    }

    /**
     * Requests the current state from the asterisk server after the connection
     * to the asterisk server is restored.
     */
    protected void handleConnectEvent(ConnectEvent connectEvent)
    {
        try
        {
            initializeChannels();
        }
        catch (Exception e)
        {
            logger.error("Unable to initialize channels after reconnect.", e);
        }

        try
        {
            initializeQueues();
        }
        catch (IOException e)
        {
            logger.error("Unable to initialize queues after reconnect.", e);
        }
    }

    /**
     * Returns a channel by its name.
     * 
     * @param name name of the channel to return
     * @return the channel with the given name
     */
    public AsteriskChannel getChannelByName(String name)
    {
        return channelManager.getChannelImplByName(name);
    }

    /**
     * Returns a channel by its unique id.
     * 
     * @param id the unique id of the channel to return
     * @return the channel with the given unique id
     */
    public AsteriskChannel getChannelById(String id)
    {
        return channelManager.getChannelImplById(id);
    }

    protected Call originateEvent2Call(OriginateEvent event)
    {
        Call call;
        AsteriskChannelImpl channel;

        channel = channelManager.getChannelImplById(event.getUniqueId());
        call = new Call();
        call.setUniqueId(event.getUniqueId());
        call.setChannel(channel);
        call.setStartTime(event.getDateReceived());
        if (event instanceof OriginateFailureEvent)
        {
            call.setEndTime(event.getDateReceived());
        }
        call.setReason(event.getReason());

        return call;
    }
}
