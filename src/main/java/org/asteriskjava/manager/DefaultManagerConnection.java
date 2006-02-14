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
package org.asteriskjava.manager;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.io.SocketConnectionFacade;
import org.asteriskjava.io.impl.SocketConnectionFacadeImpl;
import org.asteriskjava.manager.action.ChallengeAction;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.LoginAction;
import org.asteriskjava.manager.action.LogoffAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.impl.ManagerReaderImpl;
import org.asteriskjava.manager.impl.ManagerWriterImpl;
import org.asteriskjava.manager.impl.ResponseEventsImpl;
import org.asteriskjava.manager.impl.Util;
import org.asteriskjava.manager.response.ChallengeResponse;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;


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
 * @version $Id: DefaultManagerConnection.java,v 1.36 2006/01/10 23:09:11 srt Exp $
 */
public class DefaultManagerConnection implements ManagerConnection, Dispatcher
{
    /**
     * Instance logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Used to construct the internalActionId.
     */
    private long actionIdCount = 0;

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
     * The time the calling thread is sleeping between checking if a reponse or
     * the protocol identifer has been received.
     */
    private long sleepTime = 50;

    /**
     * Should we continue to reconnect after an authentication failure?
     */
    private boolean keepAliveAfterAuthenticationFailure = false;

    /**
     * The socket to use for TCP/IP communication with Asterisk.
     */
    private SocketConnectionFacade socket;

    /**
     * The thread that runs the reader.
     */
    private Thread readerThread;

    /**
     * The reader to use to receive events and responses from asterisk.
     */
    private ManagerReader reader;

    /**
     * The writer to use to send actions to asterisk.
     */
    private ManagerWriter writer;

    /**
     * The protocol identifer Asterisk sends on connect.
     */
    private String protocolIdentifier;
    
    /**
     * The version of the Asterisk server we are connected to.
     */
    private AsteriskVersion version;

    /**
     * Contains the registered handlers that process the ManagerResponses.<br>
     * Key is the internalActionId of the Action sent and value the
     * corresponding ResponseHandler.
     */
    private final Map<String, ManagerResponseHandler> responseHandlers;

    /**
     * Contains the event handlers that handle ResponseEvents for the
     * sendEventGeneratingAction methods.<br>
     * Key is the internalActionId of the Action sent and value the
     * corresponding EventHandler.
     */
    private final Map<String, ManagerEventHandler> responseEventHandlers;

    /**
     * Contains the event handlers that users registered.
     */
    private final List<ManagerEventHandler> eventHandlers;

    /**
     * Should we attempt to reconnect when the connection is lost?<br>
     * This is set to <code>true</code> after successful login and to
     * <code>false</code> after logoff or after an authentication failure when
     * keepAliveAfterAuthenticationFailure is <code>false</code>.
     */
    protected boolean keepAlive = false;

    /**
     * Creates a new instance.
     */
    public DefaultManagerConnection()
    {
        this.asteriskServer = new AsteriskServer();

        this.responseHandlers = new HashMap<String, ManagerResponseHandler>();
        this.responseEventHandlers = new HashMap<String, ManagerEventHandler>();
        this.eventHandlers = new ArrayList<ManagerEventHandler>();
    }

    /**
     * Creates a new instance with the given connection parameters.
     * 
     * @param hostname the hostname of the Asterisk server to connect to.
     * @param port the port where Asterisk listens for incoming Manager API
     *            connections, usually 5038.
     * @param username the username to use for login
     * @param password the password to use for login
     */
    public DefaultManagerConnection(String hostname, int port, String username,
            String password)
    {
        this();

        setHostname(hostname);
        setPort(port);
        setUsername(username);
        setPassword(password);
    }

    // the following two methods can be overriden when running test cases to
    // return a mock object
    protected ManagerReader createReader(Dispatcher dispatcher,
            AsteriskServer server)
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
     * Sets the time in milliseconds the synchronous sendAction methods
     * {@link #sendAction(ManagerAction)} will wait for a response before
     * throwing a TimeoutException.<br>
     * Default is 2000.
     * 
     * @param defaultTimeout default timeout in milliseconds
     * @deprecated use {@link #setDefaultResponseTimeout(long)} instead
     */
    public void setDefaultTimeout(long defaultTimeout)
    {
        setDefaultResponseTimeout(defaultTimeout);
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
     * Sets the time in milliseconds the synchronous methods
     * {@link #sendAction(ManagerAction)} and
     * {@link #sendAction(ManagerAction, long)} will sleep between two checks
     * for the arrival of a response. This value should be rather small.<br>
     * The sleepTime attribute is also used when checking for the protocol
     * identifer.<br>
     * Default is 50.
     * 
     * @param sleepTime time in milliseconds to sleep between two checks for the
     *            arrival of a response or the protocol identifier
     * @deprecated this has been replaced by an interrupt based response
     *             checking approach.
     */
    public void setSleepTime(long sleepTime)
    {
        this.sleepTime = sleepTime;
    }

    /**
     * Set to <code>true</code> to try reconnecting to ther asterisk serve
     * even if the reconnection attempt threw an AuthenticationFailedException.<br>
     * Default is <code>false</code>.
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

    /**
     * Logs in to the asterisk manager using asterisk's MD5 based
     * challenge/response protocol. The login is delayed until the protocol
     * identifier has been received by the reader.
     * 
     * @throws AuthenticationFailedException if the username and/or password are
     *             incorrect
     * @throws TimeoutException if no response is received within the specified
     *             timeout period
     * @see ChallengeAction
     * @see LoginAction
     */
    public void login() throws IOException, AuthenticationFailedException,
            TimeoutException
    {
        login(defaultResponseTimeout);
    }

    /**
     * Does the real login, following the steps outlined below.<br>
     * <ol>
     * <li>Connects to the asterisk server by calling {@link #connect()} if not
     * already connected
     * <li>Waits until the protocol identifier is received. This is checked
     * every {@link #sleepTime} ms but not longer than timeout ms in total.
     * <li>Sends a {@link ChallengeAction} requesting a challenge for authType
     * MD5.
     * <li>When the {@link ChallengeResponse} is received a {@link LoginAction}
     * is sent using the calculated key (MD5 hash of the password appended to
     * the received challenge).
     * </ol>
     * 
     * @param timeout the maximum time to wait for the protocol identifier (in
     *            ms)
     * @throws AuthenticationFailedException if username or password are
     *             incorrect and the login action returns an error or if the MD5
     *             hash cannot be computed. The connection is closed in this
     *             case.
     * @throws TimeoutException if a timeout occurs either while waiting for the
     *             protocol identifier or when sending the challenge or login
     *             action. The connection is closed in this case.
     */
    private void login(long timeout) throws IOException,
            AuthenticationFailedException, TimeoutException
    {
        long start;
        long timeSpent;
        ChallengeAction challengeAction;
        ChallengeResponse challengeResponse;
        String challenge;
        String key;
        LoginAction loginAction;
        ManagerResponse loginResponse;

        if (socket == null)
        {
            connect();
        }

        start = System.currentTimeMillis();
        while (getProtocolIdentifier() == null)
        {
            try
            {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
            }

            timeSpent = System.currentTimeMillis() - start;
            if (getProtocolIdentifier() == null && timeSpent > timeout)
            {
                disconnect();
                throw new TimeoutException(
                        "Timeout waiting for protocol identifier");
            }
        }

        challengeAction = new ChallengeAction("MD5");
        challengeResponse = (ChallengeResponse) sendAction(challengeAction);

        challenge = challengeResponse.getChallenge();

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
            key = Util.toHexString(md.digest());
        }
        catch (NoSuchAlgorithmException ex)
        {
            disconnect();
            throw new AuthenticationFailedException(
                    "Unable to create login key using MD5 Message Digest", ex);
        }

        loginAction = new LoginAction(username, "MD5", key);
        loginResponse = sendAction(loginAction);
        if (loginResponse instanceof ManagerError)
        {
            disconnect();
            throw new AuthenticationFailedException(loginResponse.getMessage());
        }

        // successfully logged in so assure that we keep trying to reconnect
        // when disconnected
        this.keepAlive = true;

        logger.info("Successfully logged in");
        
        this.version = determineVersion();
        this.writer.setTargetVersion(version);
        
        logger.info("Determined Asterisk version: " + version);
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

        if (this.reader == null)
        {
            this.reader = createReader(this, asteriskServer);
        }

        if (this.writer == null)
        {
            this.writer = createWriter();
        }

        this.socket = createSocket();

        this.reader.setSocket(socket);
        this.readerThread = new Thread(reader, "ManagerReader");
        this.readerThread.setDaemon(true);
        this.readerThread.start();

        this.writer.setSocket(socket);
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
        return socket != null && socket.isConnected(); // JDK 1.4
        // return socket != null;
    }

    /**
     * Sends a {@link LogoffAction} and disconnects from the server.
     */
    public synchronized void logoff() throws IOException, TimeoutException
    {
        LogoffAction logoffAction;

        // stop reconnecting when we got disconnected
        this.keepAlive = false;

        logoffAction = new LogoffAction();

        if (socket != null)
        {
            sendAction(logoffAction);
            disconnect();
        }
    }

    /**
     * Closes the socket connection.
     */
    private synchronized void disconnect()
    {
        if (this.socket != null)
        {
            logger.info("Closing socket.");
            try
            {
                this.socket.close();
            }
            catch (IOException ex)
            {
                logger.warn("Unable to close socket: " + ex.getMessage());
            }
            this.socket = null;
        }
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
        long start;
        long timeSpent;
        ResponseHandlerResult result;
        ManagerResponseHandler callbackHandler;

        result = new ResponseHandlerResult();
        callbackHandler = new DefaultResponseHandler(result, Thread
                .currentThread());

        sendAction(action, callbackHandler);

        start = System.currentTimeMillis();
        timeSpent = 0;
        while (result.getResponse() == null)
        {
            try
            {
                Thread.sleep(timeout - timeSpent);
            }
            catch (InterruptedException ex)
            {
            }

            // still no response and timed out?
            timeSpent = System.currentTimeMillis() - start;
            if (result.getResponse() == null && timeSpent > timeout)
            {
                throw new TimeoutException("Timeout waiting for response to "
                        + action.getAction());
            }
        }

        return result.getResponse();
    }

    public void sendAction(ManagerAction action,
            ManagerResponseHandler callbackHandler) throws IOException,
            IllegalArgumentException, IllegalStateException
    {
        String internalActionId;

        if (action == null)
        {
            throw new IllegalArgumentException(
                    "Unable to send action: action is null.");
        }

        if (socket == null)
        {
            throw new IllegalStateException("Unable to send "
                    + action.getAction() + " action: not connected.");
        }

        internalActionId = createInternalActionId();

        // if the callbackHandler is null the user is obviously not interested
        // in the response, thats fine.
        if (callbackHandler != null)
        {
            synchronized (this.responseHandlers)
            {
                this.responseHandlers.put(internalActionId, callbackHandler);
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
        long start;
        long timeSpent;

        if (action == null)
        {
            throw new IllegalArgumentException(
                    "Unable to send action: action is null.");
        }
        else if (action.getActionCompleteEventClass() == null)
        {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass is null.");
        }
        else if (!ResponseEvent.class.isAssignableFrom(action
                .getActionCompleteEventClass()))
        {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass is not a ResponseEvent.");
        }

        if (socket == null)
        {
            throw new IllegalStateException("Unable to send "
                    + action.getAction() + " action: not connected.");
        }

        responseEvents = new ResponseEventsImpl();
        responseEventHandler = new ResponseEventHandler(responseEvents, action
                .getActionCompleteEventClass(), Thread.currentThread());

        internalActionId = createInternalActionId();

        // register response handler...
        synchronized (this.responseHandlers)
        {
            this.responseHandlers.put(internalActionId, responseEventHandler);
        }

        // ...and event handler.
        synchronized (this.responseEventHandlers)
        {
            this.responseEventHandlers.put(internalActionId,
                    responseEventHandler);
        }

        writer.sendAction(action, internalActionId);

        // let's wait to see what we get
        start = System.currentTimeMillis();
        timeSpent = 0;
        while (responseEvents.getResponse() == null
                || !responseEvents.isComplete())
        {
            try
            {
                Thread.sleep(timeout - timeSpent);
            }
            catch (InterruptedException ex)
            {
            }

            // still no response or not all events received and timed out?
            timeSpent = System.currentTimeMillis() - start;
            if ((responseEvents.getResponse() == null || !responseEvents
                    .isComplete())
                    && timeSpent > timeout)
            {
                // clean up
                synchronized (this.responseEventHandlers)
                {
                    this.responseEventHandlers.remove(internalActionId);
                }

                throw new EventTimeoutException(
                        "Timeout waiting for response or response events to "
                                + action.getAction(), responseEvents);
            }
        }

        // remove the event handler (note: the response handler is removed
        // automatically when the response is received)
        synchronized (this.responseEventHandlers)
        {
            this.responseEventHandlers.remove(internalActionId);
        }

        return responseEvents;
    }

    /**
     * Creates a new unique internal action id based on the hash code of this
     * connection and a sequence.
     * 
     * @see Util#addInternalActionId(String, String)
     * @see Util#getInternalActionId(String)
     * @see Util#stripInternalActionId(String)
     */
    private String createInternalActionId()
    {
        final StringBuffer sb;

        sb = new StringBuffer();
        sb.append(this.hashCode());
        sb.append("_");
        sb.append(this.actionIdCount++);

        return sb.toString();
    }

    public void addEventHandler(final ManagerEventHandler eventHandler)
    {
        synchronized (this.eventHandlers)
        {
            // only add it if its not already there
            if (!this.eventHandlers.contains(eventHandler))
            {
                this.eventHandlers.add(eventHandler);
            }
        }
    }

    public void removeEventHandler(final ManagerEventHandler eventHandler)
    {
        synchronized (this.eventHandlers)
        {
            if (this.eventHandlers.contains(eventHandler))
            {
                this.eventHandlers.remove(eventHandler);
            }
        }
    }

    public String getProtocolIdentifier()
    {
        return this.protocolIdentifier;
    }

    public AsteriskServer getAsteriskServer()
    {
        return asteriskServer;
    }

    /* Implementation of Dispatcher: callbacks for ManagerReader */

    /**
     * This method is called by the reader whenever a {@link ManagerResponse} is
     * received. The response is dispatched to the associated
     * {@link ManagerResponseHandler}.
     * 
     * @param response the response received by the reader
     * @see ManagerReader
     */
    public void dispatchResponse(ManagerResponse response)
    {
        final String actionId;
        String internalActionId;
        ManagerResponseHandler responseHandler;

        // shouldn't happen
        if (response == null)
        {
            logger.error("Unable to dispatch null response");
            return;
        }

        actionId = response.getActionId();
        internalActionId = null;
        responseHandler = null;

        if (actionId != null)
        {
            internalActionId = Util.getInternalActionId(actionId);
            response.setActionId(Util.stripInternalActionId(actionId));
        }

        logger.debug("Dispatching response with internalActionId '"
                + internalActionId + "':\n" + response);

        if (internalActionId != null)
        {
            synchronized (this.responseHandlers)
            {
                responseHandler = (ManagerResponseHandler) this.responseHandlers
                        .get(internalActionId);
                if (responseHandler != null)
                {
                    this.responseHandlers.remove(internalActionId);
                }
                else
                {
                    // when using the async sendAction it's ok not to register a
                    // callback so if we don't find a response handler thats ok
                    logger.debug("No response handler registered for "
                            + "internalActionId '" + internalActionId + "'");
                }
            }
        }
        else
        {
            logger.error("Unable to retrieve internalActionId from response: "
                    + "actionId '" + actionId + "':\n" + response);
        }

        if (responseHandler != null)
        {
            try
            {
                responseHandler.handleResponse(response);
            }
            catch (RuntimeException e)
            {
                logger.warn("Unexpected exception in responseHandler "
                        + responseHandler.getClass().getName(), e);
            }
        }
    }

    /**
     * This method is called by the reader whenever a ManagerEvent is received.
     * The event is dispatched to all registered ManagerEventHandlers.
     * 
     * @param event the event received by the reader
     * @see #addEventHandler(ManagerEventHandler)
     * @see #removeEventHandler(ManagerEventHandler)
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
                synchronized (responseEventHandlers)
                {
                    ManagerEventHandler eventHandler;

                    eventHandler = (ManagerEventHandler) responseEventHandlers
                            .get(internalActionId);
                    if (eventHandler != null)
                    {
                        try
                        {
                            eventHandler.handleEvent(event);
                        }
                        catch (RuntimeException e)
                        {
                            logger.warn("Unexpected exception in eventHandler "
                                    + eventHandler.getClass().getName(), e);
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

        // dispatch to eventHandlers registered by users
        synchronized (eventHandlers)
        {
            for (ManagerEventHandler eventHandler : eventHandlers)
            {
                try
                {
                    eventHandler.handleEvent(event);
                }
                catch (RuntimeException e)
                {
                    logger.warn("Unexpected exception in eventHandler "
                            + eventHandler.getClass().getName(), e);
                }
            }
        }

        // process special events
        if (event instanceof ConnectEvent)
        {
            ConnectEvent connectEvent;
            String protocolIdentifier;

            connectEvent = (ConnectEvent) event;
            protocolIdentifier = connectEvent.getProtocolIdentifier();
            setProtocolIdentifier(protocolIdentifier);
        }
        else if (event instanceof DisconnectEvent)
        {
            reconnect();
        }
    }

    /**
     * This method is called when a {@link ConnectEvent} is received from the
     * reader. Having received a correct protocol identifier is the precodition
     * for logging in.
     * 
     * @param protocolIdentifier the protocol version used by the Asterisk
     *            server.
     */
    private void setProtocolIdentifier(final String protocolIdentifier)
    {
        logger.info("Connected via " + protocolIdentifier);

        if (!"Asterisk Call Manager/1.0".equals(protocolIdentifier) &&
            !"Asterisk Call Manager/1.2".equals(protocolIdentifier))
        {
            logger.warn("Unsupported protocol version '" + protocolIdentifier
                    + "'. Use at your own risk.");
        }

        this.protocolIdentifier = protocolIdentifier;

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

        // clean up at first
        disconnect();

        // try to reconnect
        numTries = 0;
        while (this.keepAlive)
        {
            try
            {
                if (numTries < 10)
                {
                    // try to reconnect quite fast for the firt 10 times
                    // this succeeds if the server has just been restarted
                    Thread.sleep(50);
                }
                else
                {
                    // slow down after 10 unsuccessful attempts asuming a
                    // shutdown of the server
                    Thread.sleep(5000);
                }
            }
            catch (InterruptedException e1)
            {
                // it's ok to wake us
            }

            try
            {
                connect();

                try
                {
                    login();
                    logger.info("Successfully reconnected.");
                    // everything is ok again, so we leave
                    break;
                }
                catch (AuthenticationFailedException e1)
                {
                    if (this.keepAliveAfterAuthenticationFailure)
                    {
                        logger.error("Unable to log in after reconnect.");
                    }
                    else
                    {
                        logger.error("Unable to log in after reconnect. "
                                + "Giving up.");
                        this.keepAlive = false;
                    }
                }
                catch (TimeoutException e1)
                {
                    // shouldn't happen
                    logger.error("TimeoutException while trying to log in "
                            + "after reconnect.");
                    synchronized (this)
                    {
                        socket.close();
                    }
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
    private class DefaultResponseHandler
            implements
                ManagerResponseHandler,
                Serializable
    {
        /**
         * Serializable version identifier
         */
        private static final long serialVersionUID = 2926598671855316803L;
        private ResponseHandlerResult result;
        private final Thread thread;

        /**
         * Creates a new instance.
         * 
         * @param result the result to store the response in
         * @param thread the thread to interrupt when the response has been
         *            received
         */
        public DefaultResponseHandler(ResponseHandlerResult result,
                Thread thread)
        {
            this.result = result;
            this.thread = thread;
        }

        public void handleResponse(ManagerResponse response)
        {
            result.setResponse(response);
            thread.interrupt();
        }
    }

    /**
     * A combinded event and response handler that adds received events and the
     * response to a ResponseEvents object.
     */
    @SuppressWarnings("unchecked")
    private class ResponseEventHandler
            implements
                ManagerEventHandler,
                ManagerResponseHandler,
                Serializable
    {
        /**
         * Serializable version identifier
         */
        private static final long serialVersionUID = 2926598671855316803L;
        private final ResponseEventsImpl events;
        private final Class actionCompleteEventClass;
        private final Thread thread;

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
                Class actionCompleteEventClass, Thread thread)
        {
            this.events = events;
            this.actionCompleteEventClass = actionCompleteEventClass;
            this.thread = thread;
        }

        public void handleEvent(ManagerEvent event)
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
                synchronized (events)
                {
                    events.setComplete(true);
                    if (events.getResponse() != null)
                    {
                        thread.interrupt();
                    }
                }
            }
        }

        public void handleResponse(ManagerResponse response)
        {
            synchronized (events)
            {
                events.setRepsonse(response);
                if (response instanceof ManagerError)
                {
                    events.setComplete(true);
                }

                // finished?
                if (events.isComplete())
                {
                    thread.interrupt();
                }
            }
        }
    }
}
