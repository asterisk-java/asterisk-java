/*
 * Copyright 2004-2022 Asterisk-Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.manager.internal;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.api.*;
import org.asteriskjava.ami.action.api.response.*;
import org.asteriskjava.lock.Lockable;
import org.asteriskjava.lock.LockableList;
import org.asteriskjava.lock.LockableMap;
import org.asteriskjava.lock.Locker.LockCloser;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.UserEventAction;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.pbx.util.LogTime;
import org.asteriskjava.util.DateUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.SocketConnectionFacade;
import org.asteriskjava.util.internal.SocketConnectionFacadeImpl;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.asteriskjava.manager.ManagerConnectionState.*;

/**
 * Internal implementation of the ManagerConnection interface.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.ManagerConnectionFactory
 */
public class ManagerConnectionImpl extends Lockable implements ManagerConnection, Dispatcher {
    private static final int RECONNECTION_INTERVAL_1 = 50;
    private static final int RECONNECTION_INTERVAL_2 = 5000;
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final int DEFAULT_PORT = 5038;
    private static final int RECONNECTION_VERSION_INTERVAL = 500;
    private static final int MAX_VERSION_ATTEMPTS = 4;
    private static final String CMD_SHOW_VERSION = "core show version";

    // NOTE: identifier is AMI_VERSION, defined in include/asterisk/manager.h
    // AMI version consists of MAJOR.BREAKING.NON-BREAKING.
    private static final String[] SUPPORTED_AMI_VERSIONS = {
            "2.6", // Asterisk 13
            "2.7", // Asterisk 13.2
            "2.8", // Asterisk >13.5
            "2.9", // Asterisk >13.3
            "3.1", // Asterisk =14.3
            "3.2", // Asterisk 14.4.0
            "4.0", // Asterisk 15
            "5.0", // Asterisk 16
            "6.0", // Asterisk 17
            "7.0", // Asterisk 18
            "8.0", // Asterisk 19
            "9.0", // Asterisk 20
    };

    private static final AtomicLong idCounter = new AtomicLong(0);

    /**
     * Instance logger.
     */
    private final static Log logger = LogFactory.getLog(ManagerConnectionImpl.class);

    private final long id;

    /**
     * Used to construct the internalActionId.
     */
    private AtomicLong actionIdCounter = new AtomicLong(0);

    /* Config attributes */
    /**
     * Hostname of the Asterisk server to connect to.
     */
    private String hostname = DEFAULT_HOSTNAME;

    /**
     * TCP port to connect to.
     */
    private int port = DEFAULT_PORT;

    /**
     * <code>true</code> to use SSL for the connection, <code>false</code> for a
     * plain text connection.
     */
    private boolean ssl = false;

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
     * Encoding used for transmission of strings.
     */
    private Charset encoding = StandardCharsets.UTF_8;

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
     * Closes the connection (and reconnects) if no input has been read for the
     * given amount of milliseconds. A timeout of zero is interpreted as an
     * infinite timeout.
     *
     * @see Socket#setSoTimeout(int)
     */
    private int socketReadTimeout = 0;

    /**
     * <code>true</code> to continue to reconnect after an authentication
     * failure.
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
    private final AtomicLong readerThreadCounter = new AtomicLong(0);

    private final AtomicLong reconnectThreadCounter = new AtomicLong(0);

    /**
     * The reader to use to receive events and responses from asterisk.
     */
    private ManagerReader reader;

    /**
     * The writer to use to send actions to asterisk.
     */
    private ManagerWriter writer;

    /**
     * The protocol identifer Asterisk sends on connect wrapped into an object
     * to be used as mutex.
     */
    private final ProtocolIdentifierWrapper protocolIdentifier;

    /**
     * The version of the Asterisk server we are connected to.
     */
    private AsteriskVersion version;

    /**
     * Contains the registered handlers that process the ManagerResponses.
     * <p/>
     * Key is the internalActionId of the Action sent and value the
     * corresponding ResponseListener.
     */
    private final LockableMap<String, SendActionCallback> responseListeners;

    /**
     * Contains the event handlers that handle ResponseEvents for the
     * sendEventGeneratingAction methods.
     * <p/>
     * Key is the internalActionId of the Action sent and value the
     * corresponding EventHandler.
     */
    private final LockableMap<String, ManagerEventListener> responseEventListeners;

    /**
     * Contains the event handlers that users registered.
     */
    private final LockableList<ManagerEventListener> eventListeners;

    protected ManagerConnectionState state = INITIAL;

    private String eventMask;

    /**
     * Creates a new instance.
     */
    public ManagerConnectionImpl() {
        this.id = idCounter.getAndIncrement();
        this.responseListeners = new LockableMap<>(new HashMap<>());
        this.responseEventListeners = new LockableMap<>(new HashMap<>());
        this.eventListeners = new LockableList<>(new ArrayList<>());
        this.protocolIdentifier = new ProtocolIdentifierWrapper();
    }

    // the following two methods can be overriden when running test cases to
    // return a mock object
    protected ManagerReader createReader(Dispatcher dispatcher, Object source) {
        return new ManagerReaderImpl(dispatcher, source);
    }

    protected ManagerWriter createWriter() {
        return new ManagerWriterImpl();
    }

    /**
     * Sets the hostname of the asterisk server to connect to.
     * <p/>
     * Default is <code>localhost</code>.
     *
     * @param hostname the hostname to connect to
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Sets the port to use to connect to the asterisk server. This is the port
     * specified in asterisk's <code>manager.conf</code> file.
     * <p/>
     * Default is 5038.
     *
     * @param port the port to connect to
     */
    public void setPort(int port) {
        if (port <= 0) {
            this.port = DEFAULT_PORT;
        } else {
            this.port = port;
        }
    }

    /**
     * Sets whether to use SSL. <br>
     * Default is false.
     *
     * @param ssl <code>true</code> to use SSL for the connection,
     *            <code>false</code> for a plain text connection.
     * @since 0.3
     */
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    /**
     * Sets the username to use to connect to the asterisk server. This is the
     * username specified in asterisk's <code>manager.conf</code> file.
     *
     * @param username the username to use for login
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password to use to connect to the asterisk server. This is the
     * password specified in Asterisk's <code>manager.conf</code> file.
     *
     * @param password the password to use for login
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    /**
     * Sets the time in milliseconds the synchronous method
     * {@link #sendAction(ManagerAction)} will wait for a response before
     * throwing a TimeoutException. <br>
     * Default is 2000.
     *
     * @param defaultResponseTimeout default response timeout in milliseconds
     * @since 0.2
     */
    public void setDefaultResponseTimeout(long defaultResponseTimeout) {
        this.defaultResponseTimeout = defaultResponseTimeout;
    }

    /**
     * Sets the time in milliseconds the synchronous method
     * {@link #sendEventGeneratingAction(EventGeneratingAction)} will wait for a
     * response and the last response event before throwing a TimeoutException.
     * <br>
     * Default is 5000.
     *
     * @param defaultEventTimeout default event timeout in milliseconds
     * @since 0.2
     */
    public void setDefaultEventTimeout(long defaultEventTimeout) {
        this.defaultEventTimeout = defaultEventTimeout;
    }

    /**
     * Set to <code>true</code> to try reconnecting to ther asterisk serve even
     * if the reconnection attempt threw an AuthenticationFailedException. <br>
     * Default is <code>true</code>.
     *
     * @param keepAliveAfterAuthenticationFailure <code>true</code> to try
     *                                            reconnecting to ther asterisk serve even if the reconnection
     *                                            attempt threw an AuthenticationFailedException,
     *                                            <code>false</code> otherwise.
     */
    public void setKeepAliveAfterAuthenticationFailure(boolean keepAliveAfterAuthenticationFailure) {
        this.keepAliveAfterAuthenticationFailure = keepAliveAfterAuthenticationFailure;
    }

    /* Implementation of ManagerConnection interface */

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Charset getEncoding() {
        return encoding;
    }

    public AsteriskVersion getVersion() {
        return version;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public InetAddress getRemoteAddress() {
        return socket.getRemoteAddress();
    }

    public int getRemotePort() {
        return socket.getRemotePort();
    }

    public void registerUserEventClass(Class<? extends ManagerEvent> userEventClass) {
        if (reader == null) {
            reader = createReader(this, this);
        }

        reader.registerEventClass(userEventClass);
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setSocketReadTimeout(int socketReadTimeout) {
        this.socketReadTimeout = socketReadTimeout;
    }

    public void login() throws IOException, AuthenticationFailedException, TimeoutException {
        login(null);
    }

    public void login(String eventMask) throws IOException, AuthenticationFailedException, TimeoutException {
        try (LockCloser closer = this.withLock()) {
            if (state != INITIAL && state != DISCONNECTED) {
                throw new IllegalStateException("Login may only be perfomed when in state "
                        + "INITIAL or DISCONNECTED, but connection is in state " + state);
            }

            state = CONNECTING;
            this.eventMask = eventMask;
            try {
                doLogin(defaultResponseTimeout, eventMask);
            } finally {
                if (state != CONNECTED) {
                    state = DISCONNECTED;
                }
            }
        }
    }

    /**
     * Does the real login, following the steps outlined below. <br>
     * <ol>
     * <li>Connects to the asterisk server by calling {@link #connect()} if not
     * already connected
     * <li>Waits until the protocol identifier is received but not longer than
     * timeout ms.
     * <li>Sends a {@link ChallengeAction} requesting a challenge for authType
     * MD5.
     * <li>When the {@link ChallengeActionResponse} is received a {@link LoginAction}
     * is sent using the calculated key (MD5 hash of the password appended to
     * the received challenge).
     * </ol>
     *
     * @param timeout   the maximum time to wait for the protocol identifier (in
     *                  ms)
     * @param eventMask the event mask. Set to "on" if all events should be
     *                  send, "off" if not events should be sent or a combination of
     *                  "system", "call" and "log" (separated by ',') to specify what
     *                  kind of events should be sent.
     * @throws IOException                   if there is an i/o problem.
     * @throws AuthenticationFailedException if username or password are
     *                                       incorrect and the login action returns an error or if the MD5
     *                                       hash cannot be computed. The connection is closed in this
     *                                       case.
     * @throws TimeoutException              if a timeout occurs while waiting for the
     *                                       protocol identifier. The connection is closed in this case.
     */
    protected void doLogin(long timeout, String eventMask)
            throws IOException, AuthenticationFailedException, TimeoutException {
        try (LockCloser closer = this.withLock()) {
            ChallengeAction challengeAction;
            ManagerActionResponse challengeResponse;
            String challenge;
            String key;
            LoginAction loginAction;
            ManagerActionResponse loginResponse;

            if (socket == null) {
                connect();
            }

            if (protocolIdentifier.getValue() == null) {
                try {
                    protocolIdentifier.await(timeout);
                } catch (InterruptedException e) // NOPMD
                {
                    Thread.currentThread().interrupt();
                }
            }

            if (protocolIdentifier.getValue() == null) {
                disconnect();
                if (reader != null && reader.getTerminationException() != null) {
                    throw reader.getTerminationException();
                }
                throw new TimeoutException("Timeout waiting for protocol identifier");
            }

            challengeAction = new ChallengeAction();
            challengeAction.setAuthType(AuthType.MD5);
            try {
                challengeResponse = sendAction(challengeAction);
            } catch (Exception e) {
                disconnect();
                throw new AuthenticationFailedException("Unable to send challenge action", e);
            }

            if (challengeResponse instanceof ChallengeActionResponse) {
                challenge = ((ChallengeActionResponse) challengeResponse).getChallenge();
            } else {
                disconnect();
                throw new AuthenticationFailedException("Unable to get challenge from Asterisk. ChallengeAction returned: "
                        + challengeResponse.getMessage());
            }

            key = md5Hex(challenge + password);

            EnumSet<EventMask> eventMasks = null;
            if (eventMask != null) {
                eventMasks = stream(eventMask.split(","))
                        .map(EventMask::valueOf)
                        .collect(toCollection(() -> EnumSet.noneOf(EventMask.class)));
            }

            loginAction = new LoginAction(username, AuthType.MD5, key);
            loginAction.setEvents(eventMasks);
            try {
                loginResponse = sendAction(loginAction);
            } catch (Exception e) {
                disconnect();
                throw new AuthenticationFailedException("Unable to send login action", e);
            }

            if (loginResponse.getResponse() == ResponseType.Error) {
                disconnect();
                throw new AuthenticationFailedException(loginResponse.getMessage());
            }

            logger.info("Successfully logged in");

            version = determineVersion();

            state = CONNECTED;

            writer.setTargetVersion(version);

            logger.info("Determined Asterisk version: " + version);

            // generate pseudo event indicating a successful login
            ConnectEvent connectEvent = new ConnectEvent(this);
            connectEvent.setProtocolIdentifier(getProtocolIdentifier());
            connectEvent.setDateReceived(DateUtil.getDate());
            // TODO could this cause a deadlock?
            fireEvent(connectEvent, null);
        }
    }

    protected AsteriskVersion determineVersion() throws IOException, TimeoutException {
        int attempts = 0;

        logger.info("Got asterisk protocol identifier version " + protocolIdentifier.getValue());

        while (attempts++ < MAX_VERSION_ATTEMPTS) {
            try {
                AsteriskVersion version = determineVersionByCoreSettings();
                if (version != null)
                    return version;
            } catch (Exception e) {
            }

            try {
                AsteriskVersion version = determineVersionByCoreShowVersion();
                if (version != null)
                    return version;
            } catch (Exception e) {
            }

            try {
                Thread.sleep(RECONNECTION_VERSION_INTERVAL);
            } catch (Exception ex) {
                // ignore
            } // NOPMD
        }

        logger.error("Unable to determine asterisk version, assuming " + AsteriskVersion.DEFAULT_VERSION
                + "... you should expect problems to follow.");
        return AsteriskVersion.DEFAULT_VERSION;
    }

    /**
     * Get asterisk version by 'core settings' actions. This is supported from
     * Asterisk 1.6 onwards.
     *
     * @return
     * @throws Exception
     */
    protected AsteriskVersion determineVersionByCoreSettings() throws Exception {

        ManagerActionResponse response = sendAction(new CoreSettingsAction());
        if (!(response instanceof CoreSettingsActionResponse)) {
            // NOTE: you need system or reporting permissions
            logger.info("Could not get core settings, do we have the necessary permissions?");
            return null;
        }

        String ver = ((CoreSettingsActionResponse) response).getAsteriskVersion();
        return AsteriskVersion.getDetermineVersionFromString("Asterisk " + ver);
    }

    /**
     * Determine version by the 'core show version' command. This needs
     * 'command' permissions.
     *
     * @return
     * @throws Exception
     */
    protected AsteriskVersion determineVersionByCoreShowVersion() throws Exception {
        final ManagerActionResponse coreShowVersionResponse = sendAction(new CommandAction(CMD_SHOW_VERSION));

        if (coreShowVersionResponse == null || !(coreShowVersionResponse instanceof CommandActionResponse)) {
            // this needs 'command' permissions
            logger.info("Could not get response for 'core show version'");
            return null;
        }

        final List<String> coreShowVersionResult = ((CommandActionResponse) coreShowVersionResponse).getOutput();
        if (coreShowVersionResult == null || coreShowVersionResult.isEmpty()) {
            logger.warn("Got empty response for 'core show version'");
            return null;
        }

        final String coreLine = coreShowVersionResult.get(0);
        return AsteriskVersion.getDetermineVersionFromString(coreLine);
    }

    protected void connect() throws IOException {
        try (LockCloser closer = this.withLock()) {
            logger.info("Connecting to " + hostname + ":" + port);

            if (reader == null) {
                logger.debug("Creating reader for " + hostname + ":" + port);
                reader = createReader(this, this);
            }

            if (writer == null) {
                logger.debug("Creating writer");
                writer = createWriter();
            }

            logger.debug("Creating socket");
            socket = createSocket();

            logger.debug("Passing socket to reader");
            reader.setSocket(socket);

            if (readerThread == null || !readerThread.isAlive() || reader.isDead()) {
                logger.debug("Creating and starting reader thread");
                readerThread = new Thread(reader);
                readerThread.setName(
                        "Asterisk-Java ManagerConnection-" + id + "-Reader-" + readerThreadCounter.getAndIncrement());
                readerThread.setDaemon(true);
                readerThread.start();
            }

            logger.debug("Passing socket to writer");
            writer.setSocket(socket);
        }
    }

    protected SocketConnectionFacade createSocket() throws IOException {
        return new SocketConnectionFacadeImpl(hostname, port, ssl, socketTimeout, socketReadTimeout, encoding);
    }

    public void logoff() throws IllegalStateException {
        try (LockCloser closer = this.withLock()) {
            if (state != CONNECTED && state != RECONNECTING) {
                throw new IllegalStateException("Logoff may only be perfomed when in state "
                        + "CONNECTED or RECONNECTING, but connection is in state " + state);
            }

            state = DISCONNECTING;

            if (socket != null) {
                try {
                    sendAction(new LogoffAction());
                } catch (Exception e) {
                    logger.warn("Unable to send LogOff action", e);
                }
            }
            cleanup();
            state = DISCONNECTED;
        }
    }

    /**
     * Closes the socket connection.
     */
    protected void disconnect() {
        try (LockCloser closer = this.withLock()) {
            if (socket != null) {
                logger.info("Closing socket.");
                try {
                    socket.close();
                } catch (IOException ex) {
                    logger.warn("Unable to close socket: " + ex.getMessage());
                }
                socket = null;
            }
            protocolIdentifier.reset();
        }
    }

    public ManagerActionResponse sendAction(ManagerAction action)
            throws IOException, TimeoutException, IllegalArgumentException, IllegalStateException {
        return sendAction(action, defaultResponseTimeout);
    }

    /**
     * Implements synchronous sending of "simple" actions.
     *
     * @param timeout - in milliseconds
     */
    public ManagerActionResponse sendAction(ManagerAction action, long timeout)
            throws IOException, TimeoutException, IllegalArgumentException, IllegalStateException {

        DefaultSendActionCallback callbackHandler = new DefaultSendActionCallback();
        ManagerActionResponse response = null;
        try {
            sendAction(action, callbackHandler);

            // definitely return null for the response of user events
            if (action instanceof UserEventAction) {
                return null;
            }
            try {
                response = callbackHandler.waitForResponse(timeout);

                // no response?
                if (response == null) {
                    throw new TimeoutException("Timeout waiting for response to " + action.getAction()
                            + (action.getActionId() == null
                            ? ""
                            : " (actionId: " + action.getActionId() + "), Timeout=" + timeout + " Action="
                            + action.getAction()));
                }
            } catch (InterruptedException ex) {
                logger.warn("Interrupted while waiting for result");
                Thread.currentThread().interrupt();
            }
        } finally {
            callbackHandler.dispose();
        }
        return response;
    }

    public void sendAction(ManagerAction action, SendActionCallback callback)
            throws IOException, IllegalArgumentException, IllegalStateException {
        final String internalActionId;

        if (action == null) {
            throw new IllegalArgumentException("Unable to send action: action is null.");
        }

        // In general sending actions is only allowed while connected, though
        // there are a few exceptions, these are handled here:
        if ((state == CONNECTING || state == RECONNECTING) && (action instanceof ChallengeAction
                || action instanceof LoginAction || isShowVersionCommandAction(action))) {
            // when (re-)connecting challenge and login actions are ok.
        } // NOPMD
        else if (state == DISCONNECTING && action instanceof LogoffAction) {
            // when disconnecting logoff action is ok.
        } // NOPMD
        else if (state != CONNECTED) {
            throw new IllegalStateException(
                    "Actions may only be sent when in state " + "CONNECTED, but connection is in state " + state);
        }

        if (socket == null) {
            throw new IllegalStateException("Unable to send " + action.getAction() + " action: socket not connected.");
        }

        internalActionId = createInternalActionId();

        // if the callbackHandler is null the user is obviously not interested
        // in the response, thats fine.
        if (callback != null) {
            try (LockCloser closer = this.responseListeners.withLock()) {
                this.responseListeners.put(internalActionId, callback);
            }
        }

        Class<? extends ManagerActionResponse> responseClass = getExpectedResponseClass(action.getClass());
        if (responseClass != null) {
            reader.expectResponseClass(internalActionId, responseClass);
        }

        writer.sendAction(action, internalActionId);
    }

    boolean isShowVersionCommandAction(ManagerAction action) {
        if (action instanceof CoreSettingsAction)
            return true;

        if (action instanceof CommandAction) {
            String cmd = ((CommandAction) action).getCommand();
            return CMD_SHOW_VERSION.equals(cmd);
        }

        return false;
    }

    private Class<? extends ManagerActionResponse> getExpectedResponseClass(Class<? extends ManagerAction> actionClass) {
        final ExpectedResponse annotation = actionClass.getAnnotation(ExpectedResponse.class);
        if (annotation == null) {
            return null;
        }

        return annotation.value();
    }

    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action)
            throws IOException, EventTimeoutException, IllegalArgumentException, IllegalStateException {
        return sendEventGeneratingAction(action, defaultEventTimeout);
    }

    /*
     * Implements synchronous sending of event generating actions.
     */
    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, long timeout)
            throws IOException, EventTimeoutException, IllegalArgumentException, IllegalStateException {
        final ResponseEventsImpl responseEvents;
        final ResponseEventHandler responseEventHandler;
        final String internalActionId;

        if (action == null) {
            throw new IllegalArgumentException("Unable to send action: action is null.");
        } else if (action.getActionCompleteEventClass() == null) {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass for " + action.getClass().getName() + " is null.");
        } else if (!ResponseEvent.class.isAssignableFrom(action.getActionCompleteEventClass())) {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass (" + action.getActionCompleteEventClass().getName()
                            + ") for " + action.getClass().getName() + " is not a ResponseEvent.");
        }

        if (state != CONNECTED) {
            throw new IllegalStateException(
                    "Actions may only be sent when in state " + "CONNECTED but connection is in state " + state);
        }

        responseEvents = new ResponseEventsImpl();
        responseEventHandler = new ResponseEventHandler(responseEvents, action.getActionCompleteEventClass());

        internalActionId = createInternalActionId();

        try {
            // register response handler...
            try (LockCloser closer = this.responseListeners.withLock()) {
                this.responseListeners.put(internalActionId, responseEventHandler);
            }

            // ...and event handler.
            try (LockCloser closer = this.responseEventListeners.withLock()) {
                this.responseEventListeners.put(internalActionId, responseEventHandler);
            }

            writer.sendAction(action, internalActionId);
            // only wait if response has not yet arrived.
            if (responseEvents.getResponse() == null || !responseEvents.isComplete()) {
                try {
                    responseEvents.await(timeout);
                } catch (InterruptedException e) {
                    logger.warn("Interrupted while waiting for response events.");
                    Thread.currentThread().interrupt();
                }
            }

            // still no response or not all events received and timed out?
            if (responseEvents.getResponse() == null || !responseEvents.isComplete()) {
                throw new EventTimeoutException(
                        "Timeout waiting for response or response events to " + action.getAction()
                                + (action.getActionId() == null ? "" : " (actionId: " + action.getActionId() + ")"),
                        responseEvents);
            }

        } finally {
            // remove the event handler
            try (LockCloser closer = this.responseEventListeners.withLock()) {
                this.responseEventListeners.remove(internalActionId);
            }

            // Note: The response handler should have already been removed
            // when the response was received, however we remove it here
            // just in case it was never received.
            try (LockCloser closer = this.responseListeners.withLock()) {
                this.responseListeners.remove(internalActionId);
            }

        }

        return responseEvents;
    }

    public void sendEventGeneratingAction(EventGeneratingAction action, SendEventGeneratingActionCallback callback)
            throws IOException, IllegalArgumentException, IllegalStateException {
        if (action == null) {
            throw new IllegalArgumentException("Unable to send action: action is null.");
        } else if (action.getActionCompleteEventClass() == null) {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass for " + action.getClass().getName() + " is null.");
        } else if (!ResponseEvent.class.isAssignableFrom(action.getActionCompleteEventClass())) {
            throw new IllegalArgumentException(
                    "Unable to send action: actionCompleteEventClass (" + action.getActionCompleteEventClass().getName()
                            + ") for " + action.getClass().getName() + " is not a ResponseEvent.");
        }

        if (state != CONNECTED) {
            throw new IllegalStateException(
                    "Actions may only be sent when in state " + "CONNECTED but connection is in state " + state);
        }

        final String internalActionId = createInternalActionId();

        if (callback != null) {
            AsyncEventGeneratingResponseHandler responseEventHandler = new AsyncEventGeneratingResponseHandler(
                    action.getActionCompleteEventClass(), callback);

            // register response handler...
            try (LockCloser closer = this.responseListeners.withLock()) {
                this.responseListeners.put(internalActionId, responseEventHandler);
            }

            // ...and event handler.
            try (LockCloser closer = this.responseEventListeners.withLock()) {
                this.responseEventListeners.put(internalActionId, responseEventHandler);
            }
        }

        writer.sendAction(action, internalActionId);
    }

    /**
     * Creates a new unique internal action id based on the hash code of this
     * connection and a sequence.
     *
     * @return a new internal action id
     * @see ManagerUtil#addInternalActionId(String, String)
     * @see ManagerUtil#getInternalActionId(String)
     * @see ManagerUtil#stripInternalActionId(String)
     */
    private String createInternalActionId() {
        final StringBuilder sb;

        sb = new StringBuilder();
        sb.append(this.hashCode());
        sb.append("_");
        sb.append(actionIdCounter.getAndIncrement());

        return sb.toString();
    }

    public void addEventListener(final ManagerEventListener listener) {
        try (LockCloser closer = this.eventListeners.withLock()) {
            // only add it if its not already there
            if (!this.eventListeners.contains(listener)) {
                this.eventListeners.add(listener);
            }
        }
    }

    public void removeEventListener(final ManagerEventListener listener) {
        try (LockCloser closer = this.eventListeners.withLock()) {
            if (this.eventListeners.contains(listener)) {
                this.eventListeners.remove(listener);
            }
        }
    }

    public String getProtocolIdentifier() {
        return protocolIdentifier.getValue();
    }

    public ManagerConnectionState getState() {
        return state;
    }

    /* Implementation of Dispatcher: callbacks for ManagerReader */

    /**
     * This method is called by the reader whenever a {@link ManagerActionResponse} is
     * received. The response is dispatched to the associated
     * {@link SendActionCallback}.
     *
     * @param response the response received by the reader
     * @see ManagerReader
     */
    public void dispatchResponse(ManagerActionResponse response, Integer requiredHandlingTime) {
        final String actionId;
        String internalActionId;
        SendActionCallback listener;

        // shouldn't happen
        if (response == null) {
            logger.error("Unable to dispatch null response. This should never happen. Please file a bug.");
            return;
        }

        actionId = response.getActionId();
        internalActionId = null;
        listener = null;

        if (actionId != null) {
            internalActionId = ManagerUtil.getInternalActionId(actionId);
            response.setActionId(ManagerUtil.stripInternalActionId(actionId));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Dispatching response with internalActionId '" + internalActionId + "':\n" + response);
        }

        if (internalActionId != null) {
            try (LockCloser closer = this.responseListeners.withLock()) {
                listener = responseListeners.get(internalActionId);
                if (listener != null) {
                    this.responseListeners.remove(internalActionId);
                } else {
                    // when using the async sendAction it's ok not to register a
                    // callback so if we don't find a response handler thats ok
                    logger.debug("No response listener registered for " + "internalActionId '" + internalActionId + "'");
                }
            }
        } else {
            logger.error(
                    "Unable to retrieve internalActionId from response: " + "actionId '" + actionId + "':\n" + response);
        }

        if (listener != null) {
            LogTime timer = new LogTime();
            try {
                listener.onResponse(response);
            } catch (Exception e) {
                logger.warn("Unexpected exception in response listener " + listener.getClass().getName(), e);
            } finally {
                if (requiredHandlingTime != null && timer.timeTaken() > requiredHandlingTime) {
                    logger.warn("Slow processing of event " + listener.getClass().getCanonicalName() + " "
                            + timer.timeTaken() + "MS \n" + response);
                }
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
    public void dispatchEvent(ManagerEvent event, Integer requiredHandlingTime) {
        // shouldn't happen
        if (event == null) {
            logger.error("Unable to dispatch null event. This should never happen. Please file a bug.");
            return;
        }
        dispatchLegacyEventIfNeeded(event, requiredHandlingTime);
        if (logger.isDebugEnabled()) {
            logger.debug("Dispatching event:\n" + event.toString());
        }

        // Some events need special treatment besides forwarding them to the
        // registered eventListeners (clients)
        // These events are handled here at first:

        // Dispatch ResponseEvents to the appropriate responseEventListener
        if (event instanceof ResponseEvent) {
            ResponseEvent responseEvent;
            String internalActionId;

            responseEvent = (ResponseEvent) event;
            internalActionId = responseEvent.getInternalActionId();
            if (internalActionId != null) {
                try (LockCloser closer = responseEventListeners.withLock()) {
                    ManagerEventListener listener;

                    listener = responseEventListeners.get(internalActionId);
                    if (listener != null) {
                        LogTime timer = new LogTime();
                        try {
                            listener.onManagerEvent(event);
                        } catch (Exception e) {
                            logger.warn("Unexpected exception in response event listener " + listener.getClass().getName(),
                                    e);
                        } finally {
                            if (requiredHandlingTime != null && timer.timeTaken() > requiredHandlingTime) {
                                logger.warn("Slow processing of event " + listener.getClass().getCanonicalName() + " "
                                        + timer.timeTaken() + "MS \n" + event);
                            }
                        }
                    }
                }
            } else {
                // ResponseEvent without internalActionId:
                // this happens if the same event class is used as response
                // event
                // and as an event that is not triggered by a Manager command
                // Example: QueueMemberStatusEvent.
                // logger.debug("ResponseEvent without "
                // + "internalActionId:\n" + responseEvent);
            } // NOPMD
        }
        if (event instanceof DisconnectEvent) {
            cleanupActionListeners((DisconnectEvent) event);
            // When we receive get disconnected while we are connected start
            // a new reconnect thread and set the state to RECONNECTING.
            try (LockCloser closer = this.withLock()) {
                if (state == CONNECTED) {
                    state = RECONNECTING;
                    // close socket if still open and remove reference to
                    // readerThread
                    // After sending the DisconnectThread that thread will die
                    // anyway.
                    cleanup();
                    Thread reconnectThread = new Thread(new Runnable() {

                        public void run() {
                            reconnect();
                        }
                    });
                    reconnectThread.setName("Asterisk-Java ManagerConnection-" + id + "-Reconnect-"
                            + reconnectThreadCounter.getAndIncrement());
                    reconnectThread.setDaemon(true);
                    reconnectThread.start();
                    // now the DisconnectEvent is dispatched to registered
                    // eventListeners
                    // (clients) and after that the ManagerReaderThread is gone.
                    // So effectively we replaced the reader thread by a
                    // ReconnectThread.
                } else {
                    // when we receive a DisconnectEvent while not connected we
                    // ignore it and do not send it to clients
                    return;
                }
            }
        }
        if (event instanceof ProtocolIdentifierReceivedEvent) {
            ProtocolIdentifierReceivedEvent protocolIdentifierReceivedEvent;
            String protocolIdentifier;

            protocolIdentifierReceivedEvent = (ProtocolIdentifierReceivedEvent) event;
            protocolIdentifier = protocolIdentifierReceivedEvent.getProtocolIdentifier();
            setProtocolIdentifier(protocolIdentifier);
            // no need to send this event to clients
            return;
        }

        fireEvent(event, requiredHandlingTime);
    }

    /**
     * Enro 2015-03 Workaround to continue having Legacy Events from Asterisk
     * 13.
     */
    private void dispatchLegacyEventIfNeeded(ManagerEvent event, Integer requiredHandlingTime) {
        if (event instanceof DialBeginEvent) {
            DialEvent legacyEvent = new DialEvent((DialBeginEvent) event);
            dispatchEvent(legacyEvent, requiredHandlingTime);
        }
    }

    /**
     * Notifies all {@link ManagerEventListener}s registered by users.
     *
     * @param event the event to propagate
     */
    private void fireEvent(ManagerEvent event, Integer requiredHandlingTime) {
        try (LockCloser closer = eventListeners.withLock()) {
            for (ManagerEventListener listener : eventListeners) {
                LogTime timer = new LogTime();
                try {
                    listener.onManagerEvent(event);
                } catch (RuntimeException e) {
                    logger.warn("Unexpected exception in eventHandler " + listener.getClass().getName(), e);
                } finally {
                    if (requiredHandlingTime != null && timer.timeTaken() > requiredHandlingTime) {
                        logger.warn("Slow processing of event " + listener.getClass().getCanonicalName() + " "
                                + timer.timeTaken() + "MS \n" + event);
                    }
                }
            }
        }
    }

    private boolean isSupportedProtocolIdentifier(final String identifier) {

        // Normal version checks
        for (String supportedVersion : SUPPORTED_AMI_VERSIONS) {
            String prefix = "Asterisk Call Manager/" + supportedVersion + ".";
            if (identifier.startsWith(prefix)) {
                return true;
            }
        }

        // Other cases
        if ("OpenPBX Call Manager/1.0".equals(identifier))
            return true;
        if ("CallWeaver Call Manager/1.0".equals(identifier))
            return true;
        if (identifier.startsWith("Asterisk Call Manager Proxy/"))
            return true;

        return false;
    }

    /**
     * This method is called when a {@link ProtocolIdentifierReceivedEvent} is
     * received from the reader. Having received a correct protocol identifier
     * is the precondition for logging in.
     *
     * @param identifier the protocol version used by the Asterisk server.
     */
    private void setProtocolIdentifier(final String identifier) {
        logger.info("Connected via " + identifier);

        if (identifier == null || !isSupportedProtocolIdentifier(identifier)) {
            logger.warn("Unsupported protocol version '" + identifier + "'. Use at your own risk.");
        }
        protocolIdentifier.setValue(identifier);
    }

    /**
     * Reconnects to the asterisk server when the connection is lost. <br>
     * While keepAlive is <code>true</code> we will try to reconnect.
     * Reconnection attempts will be stopped when the {@link #logoff()} method
     * is called or when the login after a successful reconnect results in an
     * {@link AuthenticationFailedException} suggesting that the manager
     * credentials have changed and keepAliveAfterAuthenticationFailure is not
     * set. <br>
     * This method is called when a {@link DisconnectEvent} is received from the
     * reader.
     */
    private void reconnect() {
        int numTries;

        // try to reconnect
        numTries = 0;
        while (true) {
            try {
                if (numTries < 10) {
                    // try to reconnect quite fast for the firt 10 times
                    // this succeeds if the server has just been restarted
                    Thread.sleep(RECONNECTION_INTERVAL_1);
                } else {
                    // slow down after 10 unsuccessful attempts asuming a
                    // shutdown of the server
                    Thread.sleep(RECONNECTION_INTERVAL_2);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            try {
                try (LockCloser closer = this.withLock()) {
                    if (state != RECONNECTING) {
                        break;
                    }
                    connect();

                    try {
                        doLogin(defaultResponseTimeout, eventMask);
                        logger.info("Successfully reconnected.");
                        // everything is ok again, so we leave
                        // when successful doLogin set the state to CONNECTED so
                        // no
                        // need to adjust it
                        break;
                    } catch (AuthenticationFailedException e1) {
                        if (keepAliveAfterAuthenticationFailure) {
                            logger.error("Unable to log in after reconnect: " + e1.getMessage());
                        } else {
                            logger.error("Unable to log in after reconnect: " + e1.getMessage() + ". Giving up.");
                            state = DISCONNECTED;
                        }
                    } catch (TimeoutException e1) {
                        // shouldn't happen - but happens!
                        logger.error("TimeoutException while trying to log in " + "after reconnect.");
                    }
                }
            } catch (IOException e) {
                // server seems to be still down, just continue to attempt
                // reconnection
                String message = e.getClass().getSimpleName();
                if (e.getMessage() != null) {
                    message = e.getMessage();
                }

                logger.warn("Exception while trying to reconnect: " + message);
                try {
                    // Where multiple connections are present, spread out
                    // their reconnect attempts and prevent hard loop.
                    long randomSleep = (long) (Math.random() * 100);
                    TimeUnit.MILLISECONDS.sleep(50 + randomSleep);
                } catch (InterruptedException e1) {
                    logger.error(e1);
                }
            }
            numTries++;
        }
    }

    /**
     * Notify pending {@link #responseListeners} and
     * {@link #responseEventListeners} so the synchronous ones can unblock,
     * clears those listener collections.
     *
     * @param event
     */
    private void cleanupActionListeners(DisconnectEvent event) {
        HashMap<String, SendActionCallback> oldResponseListeners = null;

        try (LockCloser closer = responseListeners.withLock()) {
            // Store remaining response listeners to be notified outside of
            // synchronized
            oldResponseListeners = new HashMap<String, SendActionCallback>(responseListeners);
            responseListeners.clear();
        }

        // Clear pending responseListeners that will not receive their responses
        for (SendActionCallback responseListener : oldResponseListeners.values()) {
            // Allows to unblock waiting sendAction() calls
            try {
                responseListener.onResponse(null);
            } catch (Exception ex) {
                logger.warn("Exception notifying responseListener.onResponse(null)", ex);
            }
        }

        HashMap<String, ManagerEventListener> oldResponseEventListeners = null;
        try (LockCloser closer = responseEventListeners.withLock()) {
            // Store remaining responseEventListeners to be notified outside of
            // synchronized
            oldResponseEventListeners = new HashMap<String, ManagerEventListener>(responseEventListeners);
            responseEventListeners.clear();
        }

        // Remove those already cleaned up via oldResponseListeners
        // TODO or should all be notified?
        for (String discardedInternalActionId : oldResponseListeners.keySet()) {
            oldResponseEventListeners.remove(discardedInternalActionId);
        }

        // Notify remaining responseEventListeners.
        // These could be EventGeneratingAction handlers that have received a
        // response but have not yet received the end event.
        for (ManagerEventListener responseEventListener : oldResponseEventListeners.values()) {
            try {
                // Allows to unblock waiting sendAction() calls
                responseEventListener.onManagerEvent(event);
            } catch (Exception ex) {
                logger.warn("Exception notifying responseListener.onManagerEvent(DisconnectEvent)", ex);
            }
        }
    }

    private void cleanup() {
        disconnect();
        this.readerThread = null;
    }

    @Override
    public String toString() {
        StringBuilder sb;

        sb = new StringBuilder("ManagerConnection[");
        sb.append("id='").append(id).append("',");
        sb.append("hostname='").append(hostname).append("',");
        sb.append("port=").append(port).append(",");
        sb.append("systemHashcode=").append(System.identityHashCode(this)).append("]");

        return sb.toString();
    }

    /* Helper classes */

    /**
     * A simple data object to store a ManagerResult.
     */
    private static class DefaultSendActionCallback implements SendActionCallback, Serializable {
        private static final long serialVersionUID = 7831097958568769220L;
        private ManagerActionResponse response;
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile boolean disposed = false;
        private LogTime timer = new LogTime();

        private ManagerActionResponse waitForResponse(long timeout) throws InterruptedException {
            latch.await(timeout, TimeUnit.MILLISECONDS);
            return this.response;
        }

        @Override
        public void onResponse(ManagerActionResponse response) {
            this.response = response;
            if (disposed) {
                logger.error("Response arrived after Disposal and assumably Timeout " + response + " elapsed: "
                        + timer.timeTaken() + "(MS)");
                logger.error("" + response.getDateReceived());
            }
            latch.countDown();
        }

        private void dispose() {
            disposed = true;
        }

    }

    /**
     * A combinded event and response handler that adds received events and the
     * response to a ResponseEvents object.
     */
    private static class ResponseEventHandler implements ManagerEventListener, SendActionCallback {
        private final ResponseEventsImpl events;
        private final Class<?> actionCompleteEventClass;

        /**
         * Creates a new instance.
         *
         * @param events                   the ResponseEventsImpl to store the events in
         * @param actionCompleteEventClass the type of event that indicates that
         *                                 all events have been received
         */
        public ResponseEventHandler(ResponseEventsImpl events, Class<?> actionCompleteEventClass) {
            this.events = events;
            this.actionCompleteEventClass = actionCompleteEventClass;
        }

        public void onManagerEvent(ManagerEvent event) {
            if (event instanceof DisconnectEvent) {
                // Set flag that must not wait for the response
                events.setComplete(true);
                // unblock potentially waiting synchronous call to
                // sendEventGeneratingAction(EventGeneratingAction action, long
                // timeout)
                events.countDown();
                return;
            }

            // should always be a ResponseEvent, anyway...
            if (event instanceof ResponseEvent) {
                ResponseEvent responseEvent;

                responseEvent = (ResponseEvent) event;
                events.addEvent(responseEvent);
            }

            // finished?
            if (actionCompleteEventClass.isAssignableFrom(event.getClass())) {
                events.setComplete(true);
                // notify if action complete event and response have been
                // received
                if (events.getResponse() != null) {
                    events.countDown();
                }
            }
        }

        public void onResponse(ManagerActionResponse response) {
            // If disconnected
            if (response == null) {
                // Set flag that must not wait for the response
                events.setComplete(true);
                // unblock potentially waiting synchronous call to
                // sendEventGeneratingAction(EventGeneratingAction action, long
                // timeout)
                events.countDown();
                return;
            }

            events.setRepsonse(response);
            if (response instanceof ManagerError) {
                events.setComplete(true);
            }

            // finished?
            // notify if action complete event and response have been
            // received
            if (events.isComplete()) {
                events.countDown();
            }
        }
    }

    private class AsyncEventGeneratingResponseHandler implements SendActionCallback, ManagerEventListener {
        private final Class<? extends ResponseEvent> actionCompleteEventClass;
        private final SendEventGeneratingActionCallback callback;

        private final ResponseEventsImpl events;

        public AsyncEventGeneratingResponseHandler(Class<? extends ResponseEvent> actionCompleteEventClass,
                                                   SendEventGeneratingActionCallback callback) {
            this.actionCompleteEventClass = actionCompleteEventClass;
            this.callback = callback;

            this.events = new ResponseEventsImpl();
        }

        @Override
        public void onManagerEvent(ManagerEvent event) {
            if (event instanceof DisconnectEvent) {
                callback.onResponse(events);
                return;
            }

            // should always be a ResponseEvent, anyway...
            if (false == (event instanceof ResponseEvent)) {
                return;
            }

            ResponseEvent responseEvent = (ResponseEvent) event;
            events.addEvent(responseEvent);

            if (actionCompleteEventClass.isAssignableFrom(event.getClass())) {
                events.setComplete(true);
                String internalActionId = responseEvent.getInternalActionId();
                try (LockCloser closer = responseEventListeners.withLock()) {
                    responseEventListeners.remove(internalActionId);
                }
                callback.onResponse(events);
            }
        }

        @Override
        public void onResponse(ManagerActionResponse response) {
            // If disconnected
            if (response == null) {
                callback.onResponse(events);
                return;
            }

            events.setRepsonse(response);
            if (response instanceof ManagerError) {
                events.setComplete(true);
            }

            // finished?
            if (events.isComplete()) {
                // invoke callback
                callback.onResponse(events);
            }
        }
    }

    @Override
    public void deregisterEventClass(Class<? extends ManagerEvent> eventClass) {
        if (reader == null) {
            reader = createReader(this, this);
        }

        reader.deregisterEventClass(eventClass);

    }

    @Override
    public void stop() {
        // NO_OP
    }
}
