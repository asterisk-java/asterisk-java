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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.ManagerEventListenerProxy;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MeetMeEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Default implementation of the AsteriskManager interface.
 * 
 * @see org.asteriskjava.live.AsteriskServer
 * @author srt
 * @version $Id$
 */
public class AsteriskServerImpl
        implements
            AsteriskServer,
            ManagerEventListener
{
    private static final Pattern SHOW_VERSION_FILES_PATTERN = Pattern
            .compile("^([\\S]+)\\s+Revision: ([0-9\\.]+)");

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The underlying manager connection used to receive events from Asterisk.
     */
    private ManagerConnection eventConnection;

    /**
     * A pool of manager connections to use for sending actions to Asterisk.
     */
    private final ManagerConnectionPool connectionPool;

    private final ChannelManager channelManager;
    private final MeetMeManager meetMeManager;
    private final QueueManager queueManager;

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
    private Map<String, String> versions;

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
        this.connectionPool.add(eventConnection);
    }

    public void initialize() throws AuthenticationFailedException, ManagerCommunicationException
    {
        if (eventConnection.getState() == ManagerConnectionState.DISCONNECTED)
        {
            try
            {
                eventConnection.login();
            }
            catch (IllegalStateException e)
            {
                throw new ManagerCommunicationException("Unable to login", e);
            }
            catch (IOException e)
            {
                throw new ManagerCommunicationException("Unable to login", e);
            }
            catch (TimeoutException e)
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

    /* Implementation of the AsteriskManager interface */

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout) throws ManagerCommunicationException
    {
        return originateToExtension(channel, context, exten, priority, timeout, null);
    }

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout, Map<String, String> variables) throws ManagerCommunicationException
    {
        OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setContext(context);
        originateAction.setExten(exten);
        originateAction.setPriority(priority);
        originateAction.setTimeout(timeout);
        originateAction.setVariables(variables);

        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);
        
        return originate(originateAction);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout) throws ManagerCommunicationException
    {
        return originateToApplication(channel, application, data, timeout, null);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout, Map<String, String> variables) throws ManagerCommunicationException
    {
        OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setApplication(application);
        originateAction.setData(data);
        originateAction.setTimeout(timeout);
        originateAction.setVariables(variables);
        
        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);
        
        return originate(originateAction);
    }

    private AsteriskChannel originate(OriginateAction originateAction) throws ManagerCommunicationException
    {
        ResponseEvents responseEvents;
        Iterator<ResponseEvent> responseEventIterator;
        
        // 2000 ms extra for the OriginateFailureEvent should be fine
        responseEvents = connectionPool.sendEventGeneratingAction(originateAction,
                originateAction.getTimeout() + 2000);
            
        responseEventIterator = responseEvents.getEvents().iterator();
        if (responseEventIterator.hasNext())
        {
            ResponseEvent responseEvent;
            
            responseEvent = responseEventIterator.next();
            if (responseEvent instanceof OriginateEvent)
            {
                return getChannelById(((OriginateEvent) responseEvent).getUniqueId()); 
            }
        }

        return null;
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

    public Collection<AsteriskQueue> getQueues()
    {
        return queueManager.getQueues();
    }

    public String getVersion() throws ManagerCommunicationException
    {
        if (version == null)
        {
            ManagerResponse response;
            response = connectionPool.sendAction(new CommandAction("show version"));
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
     * Handles all events received from the Asterisk server.<br>
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
        else if (event instanceof MeetMeEvent)
        {
            meetMeManager.handleMeetMeEvent((MeetMeEvent) event);
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
}
