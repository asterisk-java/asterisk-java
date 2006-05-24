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
package org.asteriskjava.manager.internal;

import static org.asteriskjava.manager.ManagerConnectionState.CONNECTED;
import static org.asteriskjava.manager.ManagerConnectionState.CONNECTING;
import static org.asteriskjava.manager.ManagerConnectionState.DISCONNECTED;
import static org.asteriskjava.manager.ManagerConnectionState.DISCONNECTING;
import static org.asteriskjava.manager.ManagerConnectionState.INITIAL;
import static org.asteriskjava.manager.ManagerConnectionState.RECONNECTING;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.manager.AsteriskServer;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerEventHandler;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.ManagerResponseHandler;
import org.asteriskjava.manager.ManagerResponseListener;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ChallengeAction;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.LoginAction;
import org.asteriskjava.manager.action.LogoffAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.ProtocolIdentifierReceivedEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.response.ChallengeResponse;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.DateUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.SocketConnectionFacade;
import org.asteriskjava.util.internal.SocketConnectionFacadeImpl;


/**
 * Default implemention of the ManagerConnection interface.<br>
 * Generelly avoid direct use of this class. Use the ManagerConnectionFactory to
 * obtain a ManagerConnection instead.<br>
 * When using a dependency injection framework like Spring direct usage for
 * wiring up beans that require a ManagerConnection property is fine though.<br>
 * Note that the DefaultManagerConnection will create one new Thread
 * for reading data from Asterisk on the first call to on of the login()
 * methods.
 * 
 * @see org.asteriskjava.manager.ManagerConnectionFactory
 * @author srt
 * @version $Id$
 */
public class ManagerConnectionImpl implements ManagerConnection, Dispatcher
{
    private static final int RECONNECTION_INTERVAL_2 = 5000;

    private static final int RECONNECTION_INTERVAL_1 = 50;

    /**
     * Instance logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Used to construct the internalActionId.
     */
    private AtomicLong actionIdCount = new AtomicLong(0);

    /* Config attributes */
    /**
     * The Asterisk server to connect to.
     */
    private AsteriskServer asteriskServer;

    /**
     * The username to use for login as defined in Asterisk's
     * <code>manager.conf</code>.
     */
    protected String username;

    /**
     * The password to use for login as defined in Asterisk's
     * <code>manager.conf</code>.
     */
    protected String password;

    /**
     * The default timeout to wait for a ManagerResponse after sending a
     * ManagerAction.
     */
    private long defaultResponseTimeout = 2000;

    /**
     * The default timeout to wait for the last ResponseEvent after sending an
     * EventGeneratingAction.
     */
    private long defaultEventTimeout = 5000;

    /**
     * The timeout to use when connecting the the Asterisk server.
     */
    private int socketTimeout = 0;

    /**
     * Should we continue to reconnect after an authentication failure?
     */
    private boolean keepAliveAfterAuthenticationFailure = true;

    /**
     * The socket to use for TCP/IP communication with Asterisk.
     */
    private SocketConnectionFacade socket;

    /**
     * The thread that runs the reader.
     */
    private Thread readerThread;
    private AtomicLong readerThreadNum = new AtomicLong(0);

    /**
     * The thread that performs reconnect.
     */
    private Thread reconnectThread;
    private AtomicLong reconnectThreadNum = new AtomicLong(0);

    /**
     * The reader to use to receive events and responses from asterisk.
     */
    private ManagerReader reader;

    /**
     * The writer to use to send actions to asterisk.
     */
    private ManagerWriter writer;

    /**
     * The protocol identifer Asterisk sends on connect wrapped into an object to
     * be used as mutex.
     */
    private final ProtocolIdentifierWrapper protocolIdentifier;
    
    /**
     * The version of the Asterisk server we are connected to.
     */
    private AsteriskVersion version;

    /**
     * Contains the registered handlers that process the ManagerResponses.<br>
     * Key is the internalActionId of the Action sent and value the
     * corresponding ResponseListener.
     */
    private final Map<String, ManagerResponseListener> responseListeners;

    /**
     * Contains the event handlers that handle ResponseEvents for the
     * sendEventGeneratingAction methods.<br>
     * Key is the internalActionId of the Action sent and value the
     * corresponding EventHandler.
     */
    private final Map<String, ManagerEventListener> responseEventListeners;

    /**
     * Contains the event handlers that users registered.
     */
    private final List<ManagerEventListener> eventListeners;
    
    /**
     * <code>true</code> while reconnecting.
     */
    private boolean reconnecting = false;

    protected ManagerConnectionState state = INITIAL;
    
    private String eventMask;
    
    /**
     * Creates a new instance.
     */
    public ManagerConnectionImpl()
    {
        this.asteriskServer = new AsteriskServer();

        this.responseListeners = new HashMap<String, ManagerResponseListener>();
        this.responseEventListeners = new HashMap<String, ManagerEventListener>();
        this.eventListeners = new ArrayList<ManagerEventListener>();
        this.protocolIdentifier = new ProtocolIdentifierWrapper();
    }

    // the following two methods can be overriden when running test cases to
    // return a mock object
    protected ManagerReader createReader(Dispatcher dispatcher, AsteriskServer server)
    {
        return new ManagerReaderImpl(dispatcher, server);
    }

    protected ManagerWriter createWriter()
    {
        return new ManagerWriterImpl();
    }

    /**
     * Sets the hostname of the asterisk server to connect to.<br>
     * Default is <code>localhost</code>.
     * 
     * @param hostname the hostname to connect to
     */
    public void setHostname(String hostname)
    {
        this.asteriskServer.setHostname(hostname);
    }

    /**
     * Sets the port to use to connect to the asterisk server. This is the port
     * specified in asterisk's <code>manager.conf</code> file.<br>
     * Default is 5038.
     * 
     * @param port the port to connect to
     */
    public void setPort(int port)
    {
        this.asteriskServer.setPort(port);
    }

    /**
     * Sets the username to use to connect to the asterisk server. This is the
     * username specified in asterisk's <code>manager.conf</code> file.
     * 
     * @param username the username to use for login
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Sets the password to use to connect to the asterisk server. This is the
     * password specified in Asterisk's <code>manager.conf</code> file.
     * 
     * @param password the password to use for login
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Sets the time in milliseconds the synchronous method
     * {@link #sendAction(ManagerAction)} will wait for a response before
     * throwing a TimeoutException.<br>
     * Default is 2000.
     * 
     * @param defaultResponseTimeout default response timeout in milliseconds
     * @since 0.2
     */
    public void setDefaultResponseTimeout(long defaultResponseTimeout)
    {
        this.defaultResponseTimeout = defaultResponseTimeout;
    }

    /**
     * Sets the time in milliseconds the synchronous method
     * {@link #sendEventGeneratingAction(EventGeneratingAction)} will wait for a
     * response and the last response event before throwing a TimeoutException.<br>
     * Default is 5000.
     * 
     * @param defaultEventTimeout default event timeout in milliseconds
     * @since 0.2
     */
    public void setDefaultEventTimeout(long defaultEventTimeout)
    {
        this.defaultEventTimeout = defaultEventTimeout;
    }

    /**
     * Set to <code>true</code> to try reconnecting to ther asterisk serve
     * even if the reconnection attempt threw an AuthenticationFailedException.<br>
     * Default is <code>true</code>.
     */
    public void setKeepAliveAfterAuthenticationFailure(
            boolean keepAliveAfterAuthenticationFailure)
    {
        this.keepAliveAfterAuthenticationFailure = keepAliveAfterAuthenticationFailure;
    }

    /* Implementation of ManagerConnection interface */

    public void registerUserEventClass(Class userEventClass)
    {
        if (reader == null)
        {
            reader = createReader(this, asteriskServer);
        }

        reader.registerEventClass(userEventClass);
    }

    public void setSocketTimeout(int socketTimeout)
    {
        this.socketTimeout = socketTimeout;
    }

    public synchronized void login() throws IOException, AuthenticationFailedException,
            TimeoutException
    {
        login(null);
    }

    public synchronized void login(String eventMask) throws IOException, AuthenticationFailedException, TimeoutException
    {
        if (state != INITIAL && state != DISCONNECTED)
        {
            throw new IllegalStateException("Login may only be perfomed when in state " +
                    "INITIAL or DISCONNECTED, but connection is in state " + state);
        }

        state = CONNECTING;
        this.eventMask = eventMask;
        try
        {
            doLogin(defaultResponseTimeout, eventMask);
        }
        finally
        {
            if (state != CONNECTED)
            {
                state = DISCONNECTED;
            }
        }
    }

    /**
     * Does the real login, following the steps outlined below.<br>
     * <ol>
     * <li>Connects to the asterisk server by calling {@link #connect()} if not
     * already connected
     * <li>Waits until the protocol identifier is received but not longer than timeout ms.
     * <li>Sends a {@link ChallengeAction} requesting a challenge for authType
     * MD5.
     * <li>When the {@link ChallengeResponse} is received a {@link LoginAction}
     * is sent using the calculated key (MD5 hash of the password appended to
     * the received challenge).
     * </ol>
     * 
     * @param timeout the maximum time to wait for the protocol identifier (in
     *            ms)
     * @param eventMask the event mask. Set to "on" if all events should be send,
     *            "off" if not events should be sent or a combination of
     *            "system", "call" and "log" (separated by ',') to specify what
     *            kind of events should be sent.
     * @throws AuthenticationFailedException if username or password are
     *             incorrect and the login action returns an error or if the MD5
     *             hash cannot be computed. The connection is closed in this
     *             case.
     * @throws TimeoutException if a timeout occurs while waiting for the
     *             protocol identifier. The connection is closed in this case.
     */
    protected synchronized void doLogin(long timeout, String eventMask) throws IOException, AuthenticationFailedException, TimeoutException
    {
        ChallengeAction challengeAction;
        ManagerResponse challengeResponse;
        String challenge;
        String key;
        LoginAction loginAction;
        ManagerResponse loginResponse;

        if (socket == null)
        {
            connect();
        }

        synchronized (protocolIdentifier)
        {
            if (protocolIdentifier.value == null)
            {
                try
                {
                    protocolIdentifier.wait(timeout);
                }
                catch (InterruptedException e)
                {
                    // swallow
                }
            }

            if (protocolIdentifier.value == null)
            {
                disconnect();
                throw new TimeoutException(
                        "Timeout waiting for protocol identifier");
            }
        }

        challengeAction = new ChallengeAction("MD5");
        try
        {
            challengeResponse = sendAction(challengeAction);
        }
        catch(Exception e)
        {
            disconnect();
            throw new AuthenticationFailedException("Unable to send challenge action", e);
        }
        
        if (challengeResponse instanceof ChallengeResponse)
        {
            challenge = ((ChallengeResponse) challengeResponse).getChallenge();
        }
        else
        {
            disconnect();
            throw new AuthenticationFailedException(
                    "Unable to get challenge from Asterisk. ChallengeAction returned: " 
                    + challengeResponse.getMessage());
        }

        try
        {
            MessageDigest md;

            md = MessageDigest.getInstance("MD5");
            if (challenge != null)
            {
                md.update(challenge.getBytes());
            }
            if (password != null)
            {
                md.update(password.getBytes());
            }
            key = ManagerUtil.toHexString(md.digest());
        }
        catch (NoSuchAlgorithmException ex)
        {
            disconnect();
            throw new AuthenticationFailedException(
                    "Unable to create login key using MD5 Message Digest", ex);
        }

        loginAction = new LoginAction(username, "MD5", key, eventMask);
        try
        {
            loginResponse = sendAction(loginAction);
        }
        catch(Exception e)
        {
            disconnect();
            throw new AuthenticationFailedException("Unable to send login action", e);
        }

        if (loginResponse instanceof ManagerError)
        {
            disconnect();
            throw new AuthenticationFailedException(loginResponse.getMessage());
        }

        state = CONNECTED; 

        logger.info("Successfully logged in");

        version = determineVersion();
        writer.setTargetVersion(version);

        logger.info("Determined Asterisk version: " + version);

        // generate pseudo event indicating a successful login
        ConnectEvent connectEvent = new ConnectEvent(asteriskServer);
        connectEvent.setProtocolIdentifier(getProtocolIdentifier());
        connectEvent.setDateReceived(DateUtil.getDate());
        // TODO could this cause a deadlock?
        dispatchEvent(connectEvent);
    }
    
    protected AsteriskVersion determineVersion() throws IOException, TimeoutException
    {
        ManagerResponse showVersionFilesResponse;

        // increase timeout as output is quite large
        showVersionFilesResponse = sendAction(new CommandAction("show version files pbx.c"), 
                defaultResponseTimeout * 2);
        if (showVersionFilesResponse instanceof CommandResponse)
        {
            List showVersionFilesResult;

            showVersionFilesResult = ((CommandResponse) showVersionFilesResponse).getResult();
            if (showVersionFilesResult != null && showVersionFilesResult.size() > 0)
            {
                String line1;

                line1 = (String) showVersionFilesResult.get(0); 
                if (line1 != null && line1.startsWith("File"))
                {
                    return AsteriskVersion.ASTERISK_1_2;
                }
            }
        }
        
        return AsteriskVersion.ASTERISK_1_0;
    }

    protected synchronized void connect() throws IOException
    {
        logger.info("Connecting to " + asteriskServer.getHostname() + " port "
                + asteriskServer.getPort());

        if (reader == null)
        {
            logger.debug("Creating reader for " + asteriskServer);
            reader = createReader(this, asteriskServer);
        }

        if (writer == null)
        {
            logger.debug("Creating writer");
            writer = createWriter();
        }

        logger.debug("Creating socket");
        socket = createSocket();

        logger.debug("Passing socket to reader");
        reader.setSocket(socket);

        if (readerThread == null || !readerThread.isAlive() || reader.isDead())
        {
            logger.debug("Creating and starting reader thread");
            readerThread = new Thread(reader, "ManagerReader-" + readerThreadNum.getAndIncrement());
            readerThread.setDaemon(true);
            readerThread.start();
        }

        logger.debug("Passing socket to writer");
        writer.setSocket(socket);
    }

    protected SocketConnectionFacade createSocket() throws IOException
    {
        return new SocketConnectionFacadeImpl(asteriskServer.getHostname(),
                asteriskServer.getPort(), socketTimeout);
    }

    /**
     * Returns <code>true</code> if there is a socket connection to the
     * asterisk server, <code>false</code> otherwise.
     * 
     * @return <code>true</code> if there is a socket connection to the
     *         asterisk server, <code>false</code> otherwise.
     */
    public synchronized boolean isConnected()
    {
        return socket != null && !reconnecting && socket.isConnected();
    }

    public synchronized void logoff() throws IllegalStateException
    {
        if (state != CONNECTED && state != RECONNECTING)
        {
            throw new IllegalStateException("Logoff may only be perfomed when in state " +
                    "CONNECTED or RECONNECTING, but connection is in state " + state);
        }

        state = DISCONNECTING;

        if (socket != null)
        {
            if (!reconnecting)
            {
                try
                {
                    sendAction(new LogoffAction());
                }
                catch(Exception e)
                {
                    logger.warn("Unable to send LogOff action", e);
                }
            }
        }
        cleanup();
        state = DISCONNECTED;
    }

    /**
     * Closes the socket connection.
     */
    protected synchronized void disconnect()
    {
        if (socket != null)
        {
            logger.info("Closing socket.");
            try
            {
                socket.close();
            }
            catch (IOException ex)
            {
                logger.warn("Unable to close socket: " + ex.getMessage());
            }
            socket = null;
        }
        protocolIdentifier.value = null;
    }

    public ManagerResponse sendAction(ManagerAction action) throws IOException,
            TimeoutException, IllegalArgumentException, IllegalStateException
    {
        return sendAction(action, defaultResponseTimeout);
    }

    public ManagerResponse sendAction(ManagerAction action, long timeout)
            throws IOException, TimeoutException, IllegalArgumentException,
            IllegalStateException
    {
        ResponseHandlerResult result;
        ManagerResponseListener callbackHandler;

        result = new ResponseHandlerResult();
        callbackHandler = new DefaultResponseListener(result);

        synchronized (result)
        {
            sendAction(action, callbackHandler);
            // only wait if we did not yet receive the response.
            // Responses may be returned really fast.
            if (result.getResponse() == null)
            {
                try
                {
                    result.wait(timeout);
                }
                catch (InterruptedException ex)
                {
                    logger.warn("Interrupted while waiting for result");
                }
            }
        }
    
        // still no response?
        if (result.getResponse() == null)
        {
            throw new TimeoutException("Timeout waiting for response to "
                    + action.getAction()
                    + (action.getActionId() == null ? "" : " (actionId: " + action.getActionId() + ")"));
        }

        return result.getResponse();
    }

    @SuppressWarnings("deprecation")
    public void sendAction(ManagerAction action,
            ManagerResponseHandler callbackHandler) throws IOException,
            IllegalArgumentException, IllegalStateException
    {
        sendAction(action, new ManagerResponseListenerAdapter(callbackHandler));
    }

    public void sendAction(ManagerAction action,
            ManagerResponseListener callbackHandler) throws IOException,
            IllegalArgumentException, IllegalStateException
    {
        String internalActionId;

        if (action == null)
        {
            throw new IllegalArgumentException(
                    "Unable to send action: action is null.");
        }

        if ((state == CONNECTING || state == RECONNECTING) 
                && (action instanceof ChallengeAction || action instanceof LoginAction))
        {
            // when (re-)connecting challenge and login actions are ok.
        }
        else if (state == DISCONNECTING && action instanceof LogoffAction)
        {
            // when disconnecting logoff action is ok.
        }
        else if (state != CONNECTED)
        {
            throw new IllegalStateException("Actions may only be sent when in state " +
                    "CONNECTED, but connection is in state " + state);
        }

        if (socket == null)
        {
            throw new IllegalStateException("Unable to send "
                    + action.getAction() + " action: socket not connected.");
        }

        internalActionId = createInternalActionId();

        // if the callbackHandler is null the user is obviously not interested
        // in the response, thats fine.
        if (callbackHandler != null)
        {
            synchronized (this.responseListeners)
            {
                this.responseListeners.put(internalActionId, callbackHandler);
            }
        }

        writer.sendAction(action, internalActionId);
    }

    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action)
            throws IOException, EventTimeoutException,
            IllegalArgumentException, IllegalStateException
    {
        return sendEventGeneratingAction(action, defaultEventTimeout);
    }

    public ResponseEvents sendEventGeneratingAction(
            EventGeneratingAction action, long timeout) throws IOException,
            EventTimeoutException, IllegalArgumentException,
            IllegalStateException
    {
        ResponseEventsImpl responseEvents;
        ResponseEventHandler responseEventHandler;
        String internalActionId;

        if (action == null)
        {
            throw new IllegalArgumentException(
                    "Unable to send action: action is null.");
        }
        else if (action.getActionCompleteEventClass() == null)
        {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass for " + 
                    action.getClass().getName() + " is null.");
        }
        else if (!ResponseEvent.class.isAssignableFrom(action
                .getActionCompleteEventClass()))
        {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass (" + 
                    action.getActionCompleteEventClass().getName() + ") for " +
                    action.getClass().getName() + " is not a ResponseEvent.");
        }

        // TODO
        if (socket == null)
        {
            throw new IllegalStateException("Unable to send "
                    + action.getAction() + " action: not connected.");
        }

        responseEvents = new ResponseEventsImpl();
        responseEventHandler = new ResponseEventHandler(responseEvents, action
                .getActionCompleteEventClass());

        internalActionId = createInternalActionId();

        // register response handler...
        synchronized (this.responseListeners)
        {
            this.responseListeners.put(internalActionId, responseEventHandler);
        }

        // ...and event handler.
        synchronized (this.responseEventListeners)
        {
            this.responseEventListeners.put(internalActionId,
                    responseEventHandler);
        }

        synchronized (responseEvents)
        {
            writer.sendAction(action, internalActionId);
            try
            {
                responseEvents.wait(timeout);
            }
            catch (InterruptedException ex)
            {
                logger.warn("Interrupted while waiting for response events");
            }
        }

        // still no response or not all events received and timed out?
        if ((responseEvents.getResponse() == null || !responseEvents
                .isComplete()))
        {
            // clean up
            synchronized (this.responseEventListeners)
            {
                this.responseEventListeners.remove(internalActionId);
            }

            throw new EventTimeoutException(
                    "Timeout waiting for response or response events to "
                    + action.getAction()
                    + (action.getActionId() == null ? "" : " (actionId: " + action.getActionId() + ")"),
                    responseEvents);
        }

        // remove the event handler (note: the response handler is removed
        // automatically when the response is received)
        synchronized (this.responseEventListeners)
        {
            this.responseEventListeners.remove(internalActionId);
        }

        return responseEvents;
    }

    /**
     * Creates a new unique internal action id based on the hash code of this
     * connection and a sequence.
     * 
     * @see ManagerUtil#addInternalActionId(String, String)
     * @see ManagerUtil#getInternalActionId(String)
     * @see ManagerUtil#stripInternalActionId(String)
     */
    private String createInternalActionId()
    {
        final StringBuffer sb;

        sb = new StringBuffer();
        sb.append(this.hashCode());
        sb.append("_");
        sb.append(actionIdCount.getAndIncrement());

        return sb.toString();
    }

    public void addEventListener(final ManagerEventListener listener)
    {
        synchronized (this.eventListeners)
        {
            // only add it if its not already there
            if (!this.eventListeners.contains(listener))
            {
                this.eventListeners.add(listener);
            }
        }
    }

    public void removeEventListener(final ManagerEventListener listener)
    {
        synchronized (this.eventListeners)
        {
            if (this.eventListeners.contains(listener))
            {
                this.eventListeners.remove(listener);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void addEventHandler(final ManagerEventHandler eventHandler)
    {
        addEventListener(new ManagerEventListenerAdapter(eventHandler));
    }

    @SuppressWarnings("deprecation")
    public void removeEventHandler(final ManagerEventHandler eventHandler)
    {
        synchronized (this.eventListeners)
        {
            Iterator<ManagerEventListener> i = eventListeners.iterator();
            
            while (i.hasNext())
            {
                ManagerEventListener listener = i.next();
                if (listener instanceof ManagerEventListenerAdapter &&
                    ((ManagerEventListenerAdapter) listener).handler == eventHandler)
                {
                    i.remove();
                }
            }
        }
    }

    public String getProtocolIdentifier()
    {
        return protocolIdentifier.value;
    }

    public ManagerConnectionState getState()
    {
        return state;
    }
    
    public AsteriskServer getAsteriskServer()
    {
        return asteriskServer;
    }

    /* Implementation of Dispatcher: callbacks for ManagerReader */

    /**
     * This method is called by the reader whenever a {@link ManagerResponse} is
     * received. The response is dispatched to the associated
     * {@link ManagerResponseListener}.
     * 
     * @param response the response received by the reader
     * @see ManagerReader
     */
    public void dispatchResponse(ManagerResponse response)
    {
        final String actionId;
        String internalActionId;
        ManagerResponseListener listener;

        // shouldn't happen
        if (response == null)
        {
            logger.error("Unable to dispatch null response");
            return;
        }

        actionId = response.getActionId();
        internalActionId = null;
        listener = null;

        if (actionId != null)
        {
            internalActionId = ManagerUtil.getInternalActionId(actionId);
            response.setActionId(ManagerUtil.stripInternalActionId(actionId));
        }

        logger.debug("Dispatching response with internalActionId '"
                + internalActionId + "':\n" + response);

        if (internalActionId != null)
        {
            synchronized (this.responseListeners)
            {
                listener = responseListeners.get(internalActionId);
                if (listener != null)
                {
                    this.responseListeners.remove(internalActionId);
                }
                else
                {
                    // when using the async sendAction it's ok not to register a
                    // callback so if we don't find a response handler thats ok
                    logger.debug("No response listener registered for "
                            + "internalActionId '" + internalActionId + "'");
                }
            }
        }
        else
        {
            logger.error("Unable to retrieve internalActionId from response: "
                    + "actionId '" + actionId + "':\n" + response);
        }

        if (listener != null)
        {
            try
            {
                listener.onManagerResponse(response);
            }
            catch (RuntimeException e)
            {
                logger.warn("Unexpected exception in response listener "
                        + listener.getClass().getName(), e);
            }
        }
    }

    /**
     * This method is called by the reader whenever a ManagerEvent is received.
     * The event is dispatched to all registered ManagerEventHandlers.
     * 
     * @param event the event received by the reader
     * @see #addEventListener(ManagerEventListener)
     * @see #removeEventListener(ManagerEventListener)
     * @see ManagerReader
     */
    public void dispatchEvent(ManagerEvent event)
    {
        // shouldn't happen
        if (event == null)
        {
            logger.error("Unable to dispatch null event");
            return;
        }

        logger.debug("Dispatching event:\n" + event.toString());

        // dispatch ResponseEvents to the appropriate responseEventHandler
        if (event instanceof ResponseEvent)
        {
            ResponseEvent responseEvent;
            String internalActionId;

            responseEvent = (ResponseEvent) event;
            internalActionId = responseEvent.getInternalActionId();
            if (internalActionId != null)
            {
                synchronized (responseEventListeners)
                {
                    ManagerEventListener listener;

                    listener = responseEventListeners.get(internalActionId);
                    if (listener != null)
                    {
                        try
                        {
                            listener.onManagerEvent(event);
                        }
                        catch (RuntimeException e)
                        {
                            logger.warn("Unexpected exception in event listener "
                                    + listener.getClass().getName(), e);
                        }
                    }
                }
            }
            else
            {
                // ResponseEvent without internalActionId:
                // this happens if the same event class is used as response event
                // and as an event that is not triggered by a Manager command
                // example: QueueMemberStatusEvent.
                //logger.debug("ResponseEvent without "
                //        + "internalActionId:\n" + responseEvent);
            }
        }
        if (event instanceof DisconnectEvent)
        {
            if (state == CONNECTED)
            {
                state = RECONNECTING;
                cleanup();
                reconnectThread = new Thread(new Runnable()
                {
                    public void run()
                    {
                        reconnect();
                    }
                });
                reconnectThread.setName("ReconnectThread-" + reconnectThreadNum.getAndIncrement());
                reconnectThread.setDaemon(true);
                reconnectThread.start();
            }
        }
        if (event instanceof ProtocolIdentifierReceivedEvent)
        {
            ProtocolIdentifierReceivedEvent protocolIdentifierReceivedEvent;
            String protocolIdentifier;

            protocolIdentifierReceivedEvent = (ProtocolIdentifierReceivedEvent) event;
            protocolIdentifier = protocolIdentifierReceivedEvent.getProtocolIdentifier();
            setProtocolIdentifier(protocolIdentifier);
        }

        // dispatch to listeners registered by users
        synchronized (eventListeners)
        {
            for (ManagerEventListener listener : eventListeners)
            {
                try
                {
                    listener.onManagerEvent(event);
                }
                catch (RuntimeException e)
                {
                    logger.warn("Unexpected exception in eventHandler "
                            + listener.getClass().getName(), e);
                }
            }
        }
    }

    /**
     * This method is called when a {@link ProtocolIdentifierReceivedEvent} is received 
     * from the reader. Having received a correct protocol identifier is the precodition
     * for logging in.
     * 
     * @param identifier the protocol version used by the Asterisk
     *            server.
     */
    private void setProtocolIdentifier(final String identifier)
    {
        logger.info("Connected via " + identifier);

        if (!"Asterisk Call Manager/1.0".equals(identifier) &&
            !"Asterisk Call Manager/1.2".equals(identifier))
        {
            logger.warn("Unsupported protocol version '" + identifier
                    + "'. Use at your own risk.");
        }

        synchronized (protocolIdentifier)
        {
            protocolIdentifier.value = identifier;
            protocolIdentifier.notify();
        }
    }

    /**
     * Reconnects to the asterisk server when the connection is lost.<br>
     * While keepAlive is <code>true</code> we will try to reconnect.
     * Reconnection attempts will be stopped when the {@link #logoff()} method
     * is called or when the login after a successful reconnect results in an
     * {@link AuthenticationFailedException} suggesting that the manager
     * credentials have changed and keepAliveAfterAuthenticationFailure is not
     * set.<br>
     * This method is called when a {@link DisconnectEvent} is received from the
     * reader.
     */
    private void reconnect()
    {
        int numTries;

        // try to reconnect
        numTries = 0;
        while (state == RECONNECTING)
        {
            try
            {
                if (numTries < 10)
                {
                    // try to reconnect quite fast for the firt 10 times
                    // this succeeds if the server has just been restarted
                    Thread.sleep(RECONNECTION_INTERVAL_1);
                }
                else
                {
                    // slow down after 10 unsuccessful attempts asuming a
                    // shutdown of the server
                    Thread.sleep(RECONNECTION_INTERVAL_2);
                }
            }
            catch (InterruptedException e1)
            {
                // ignore
            }

            try
            {
                connect();

                try
                {
                    doLogin(defaultResponseTimeout, eventMask);
                    logger.info("Successfully reconnected.");
                    // everything is ok again, so we leave
                    // when successful doLogin set the state to CONNECTED so no need to adjust it 
                    break;
                }
                catch (AuthenticationFailedException e1)
                {
                    if (keepAliveAfterAuthenticationFailure)
                    {
                        logger.error("Unable to log in after reconnect: "
                                + e1.getMessage());
                    }
                    else
                    {
                        logger.error("Unable to log in after reconnect: "
                                + e1.getMessage() + ". Giving up.");
                        state = DISCONNECTED;
                    }
                }
                catch (TimeoutException e1)
                {
                    // shouldn't happen - but happens!
                    logger.error("TimeoutException while trying to log in "
                            + "after reconnect.");
                }
            }
            catch (IOException e)
            {
                // server seems to be still down, just continue to attempt
                // reconnection
                logger.warn("Exception while trying to reconnect: "
                        + e.getMessage());
            }
            numTries++;
        }
    }
    
    private void cleanup()
    {
        disconnect();
        this.readerThread = null;
    }

    /* Helper classes */

    /**
     * A simple data object to store a ManagerResult.
     */
    private class ResponseHandlerResult implements Serializable
    {
        /**
         * Serializable version identifier
         */
        private static final long serialVersionUID = 7831097958568769220L;
        private ManagerResponse response;

        public ResponseHandlerResult()
        {
        }

        public ManagerResponse getResponse()
        {
            return this.response;
        }

        public void setResponse(ManagerResponse response)
        {
            this.response = response;
        }
    }

    /**
     * A simple response handler that stores the received response in a
     * ResponseHandlerResult for further processing.
     */
    private class DefaultResponseListener
            implements
                ManagerResponseListener,
                Serializable
    {
        /**
         * Serializable version identifier
         */
        private static final long serialVersionUID = 2926598671855316803L;
        private ResponseHandlerResult result;

        /**
         * Creates a new instance.
         * 
         * @param result the result to store the response in
         */
        public DefaultResponseListener(ResponseHandlerResult result)
        {
            this.result = result;
        }

        public void onManagerResponse(ManagerResponse response)
        {
            synchronized (result)
            {
                result.setResponse(response);
                result.notify();
            }
        }
    }

    /**
     * A combinded event and response handler that adds received events and the
     * response to a ResponseEvents object.
     */
    @SuppressWarnings("unchecked")
    private class ResponseEventHandler
            implements
                ManagerEventListener,
                ManagerResponseListener,
                Serializable
    {
        /**
         * Serializable version identifier
         */
        private static final long serialVersionUID = 2926598671855316803L;
        private final ResponseEventsImpl events;
        private final Class actionCompleteEventClass;

        /**
         * Creates a new instance.
         * 
         * @param events the ResponseEventsImpl to store the events in
         * @param actionCompleteEventClass the type of event that indicates that
         *            all events have been received
         * @param thread the thread to interrupt when the
         *            actionCompleteEventClass has been received
         */
        public ResponseEventHandler(ResponseEventsImpl events,
                Class actionCompleteEventClass)
        {
            this.events = events;
            this.actionCompleteEventClass = actionCompleteEventClass;
        }

        public void onManagerEvent(ManagerEvent event)
        {
            synchronized (events)
            {
                // should always be a ResponseEvent, anyway...
                if (event instanceof ResponseEvent)
                {
                    ResponseEvent responseEvent;
    
                    responseEvent = (ResponseEvent) event;
                    events.addEvent(responseEvent);
                }
    
                // finished?
                if (actionCompleteEventClass.isAssignableFrom(event.getClass()))
                {
                    events.setComplete(true);
                    // notify if action complete event and response have been received
                    if (events.getResponse() != null)
                    {
                        events.notify();
                    }
                }
            }
        }

        public void onManagerResponse(ManagerResponse response)
        {
            synchronized (events)
            {
                events.setRepsonse(response);
                if (response instanceof ManagerError)
                {
                    events.setComplete(true);
                }

                // finished?
                // notify if action complete event and response have been received
                if (events.isComplete())
                {
                    events.notify();
                }
            }
        }
    }
    
    private class ProtocolIdentifierWrapper
    {
        String value;
    }

    // to be removed in 0.4
    
    @SuppressWarnings("deprecation") 
    private class ManagerResponseListenerAdapter implements ManagerResponseListener
    {
        ManagerResponseHandler handler;

        public ManagerResponseListenerAdapter(ManagerResponseHandler handler)
        {
            this.handler = handler;
        }

        public void onManagerResponse(ManagerResponse response)
        {
            handler.handleResponse(response);
        }
    }

    @SuppressWarnings("deprecation") 
    private class ManagerEventListenerAdapter implements ManagerEventListener
    {
        ManagerEventHandler handler;

        public ManagerEventListenerAdapter(ManagerEventHandler handler)
        {
            this.handler = handler;
        }

        public void onManagerEvent(ManagerEvent event)
        {
            handler.handleEvent(event);
        }
    }
}
