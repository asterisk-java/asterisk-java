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

import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;


/**
 * The main interface to talk to an Asterisk server via the Asterisk Manager
 * API.<br>
 * The ManagerConnection repesents a connection to an Asterisk Server and is
 * capable to send Actions and receive Responses and Events. It does not add any
 * valuable functionality but rather provides a Java view to Asterisk's Manager
 * API (freeing you from TCP/IP connection and parsing stuff).<br>
 * It is used as the foundation for higher leveled interfaces like the
 * AsteriskManager.<br>
 * A concrete implementation of this interface can be obtained from a
 * ManagerConnectionFactory.
 * 
 * @see org.asteriskjava.manager.ManagerConnectionFactory
 * @author srt
 * @version $Id: ManagerConnection.java,v 1.12 2005/10/10 20:33:55 srt Exp $
 */
public interface ManagerConnection
{
    /**
     * Registers a new user event type.<br>
     * Asterisk allows you to send custom events via the UserEvent application.
     * If you choose to send such events you can extend the abstract class
     * UserEvent provide a name for your new event and optionally add your own
     * attributes. After registering a user event type Asterisk-Java will handle
     * such events the same way it handles the internal events and inform your
     * registered event handlers.<br>
     * Note: If you write your own Asterisk applications that use Asterisk's
     * <code>manager_event()</code> function directly and don't use the
     * channel and uniqueid attributes provided by UserEvent you can also
     * register events that directly subclass ManagerEvent.
     * 
     * @see org.asteriskjava.manager.event.UserEvent
     * @see ManagerEvent
     * @param userEventClass the class of the user event to register.
     */
    void registerUserEventClass(Class userEventClass);

    /**
     * The timeout to use when connecting the the Asterisk server.<br>
     * Default is 0, that is using Java's built-in default.
     * 
     * @param socketTimeout the timeout value to be used in milliseconds.
     * @see java.net.Socket#connect(java.net.SocketAddress, int)
     * @since 0.2
     */
    public void setSocketTimeout(int socketTimeout);

    /**
     * Logs in to the Asterisk server with the username and password specified
     * when this connection was created.
     * 
     * @throws IOException if the network connection is disrupted.
     * @throws AuthenticationFailedException if the username and/or password are
     *             incorrect or the ChallengeResponse could not be built.
     * @throws TimeoutException if no response is received within the default
     *             timeout period. If the implementation uses challenge/response
     *             this can either be a timeout of the ChallengeAction or the
     *             LoginAction; otherwise it is always a timeout of the
     *             LoginAction.
     * @see org.asteriskjava.manager.action.LoginAction
     * @see org.asteriskjava.manager.action.ChallengeAction
     */
    void login() throws IOException, AuthenticationFailedException,
            TimeoutException;

    /**
     * Logs in to the Asterisk server with the username and password specified
     * when this connection was created and a given event mask.
     * 
     * @param events the event mask. Set to "on" if all events should be send,
     *            "off" if not events should be sent or a combination of
     *            "system", "call" and "log" (separated by ',') to specify what
     *            kind of events should be sent.
     * @throws IOException if the network connection is disrupted.
     * @throws AuthenticationFailedException if the username and/or password are
     *             incorrect or the ChallengeResponse could not be built.
     * @throws TimeoutException if no response is received within the default
     *             timeout period. If the implementation uses challenge/response
     *             this can either be a timeout of the ChallengeAction or the
     *             LoginAction; otherwise it is always a timeout of the
     *             LoginAction.
     * @since 0.3
     * @see org.asteriskjava.manager.action.LoginAction
     * @see org.asteriskjava.manager.action.ChallengeAction
     */
    void login(String events) throws IOException, AuthenticationFailedException,
            TimeoutException;

    /**
     * Sends a LogoffAction to the Asterisk server and disconnects.
     * 
     * @throws IOException if the network connection is disrupted.
     * @throws TimeoutException if no response to the logoff action is received
     *             within the default timeout period.
     * @see org.asteriskjava.manager.action.LogoffAction
     */
    void logoff() throws IOException, TimeoutException;

    /**
     * Returns the protocol identifier, i.e. a string like "Asterisk Call
     * Manager/1.0".
     * 
     * @return the protocol identifier of the Asterisk Manager Interface in use
     *         if it has already been received; <code>null</code> otherwise
     */
    String getProtocolIdentifier();

    /**
     * Sends a ManagerAction to the Asterisk server and waits for the
     * corresponding ManagerResponse.
     * 
     * @param action the action to send to the Asterisk server
     * @return the corresponding response received from the Asterisk server
     * @throws IOException if the network connection is disrupted.
     * @throws TimeoutException if no response is received within the default
     *             timeout period.
     * @throws IllegalArgumentException if the action is <code>null</code>.
     * @throws IllegalStateException if you are not connected to an Asterisk
     *             server.
     * @see #sendAction(ManagerAction, long)
     * @see #sendAction(ManagerAction, ManagerResponseHandler)
     */
    ManagerResponse sendAction(ManagerAction action) throws IOException,
            TimeoutException, IllegalArgumentException, IllegalStateException;

    /**
     * Sends a ManagerAction to the Asterisk server and waits for the
     * corresponding ManagerResponse.
     * 
     * @param action the action to send to the Asterisk server
     * @param timeout milliseconds to wait for the response before throwing a
     *            TimeoutException
     * @return the corresponding response received from the Asterisk server
     * @throws IOException if the network connection is disrupted.
     * @throws TimeoutException if no response is received within the given
     *             timeout period.
     * @throws IllegalArgumentException if the action is <code>null</code>.
     * @throws IllegalStateException if you are not connected to an Asterisk
     *             server.
     * @see #sendAction(ManagerAction, ManagerResponseHandler)
     */
    ManagerResponse sendAction(ManagerAction action, long timeout)
            throws IOException, TimeoutException, IllegalArgumentException,
            IllegalStateException;

    /**
     * Sends a ManagerAction to the Asterisk server and registers a callback
     * handler to be called when the corresponding ManagerResponse is received.
     * 
     * @param action the action to send to the Asterisk server
     * @param callbackHandler the callback handler to call when the response is
     *            received or <code>null</code> if you are not interested in
     *            the response
     * @throws IOException if the network connection is disrupted.
     * @throws IllegalArgumentException if the action is <code>null</code>.
     * @throws IllegalStateException if you are not connected to an Asterisk
     *             server.
     */
    void sendAction(ManagerAction action, ManagerResponseHandler callbackHandler)
            throws IOException, IllegalArgumentException, IllegalStateException;

    /**
     * Sends a EventGeneratingAction to the Asterisk server and waits for the
     * corresponding ManagerResponse and the ResponseEvents.<br>
     * EventGeneratingActions are ManagerActions that don't return their
     * response in the corresponding ManagerResponse but send a series of events
     * that contain the payload.<br>
     * This method will block until the correpsonding action complete event has
     * been received. The action complete event is determined by
     * {@link EventGeneratingAction#getActionCompleteEventClass()}.<br>
     * Examples for EventGeneratingActions are the
     * {@link org.asteriskjava.manager.action.StatusAction}, the
     * {@link org.asteriskjava.manager.action.QueueAction} or the
     * {@link org.asteriskjava.manager.action.AgentsAction}.
     * 
     * @param action the action to send to the Asterisk server
     * @return a ResponseEvents that contains the corresponding response and
     *         response events received from the Asterisk server
     * @throws IOException if the network connection is disrupted.
     * @throws EventTimeoutException if no response or not all response events are
     *             received within the given timeout period.
     * @throws IllegalArgumentException if the action is <code>null</code>,
     *             the actionCompleteEventClass property of the action is
     *             <code>null</code> or if actionCompleteEventClass is not a
     *             ResponseEvent.
     * @throws IllegalStateException if you are not connected to an Asterisk
     *             server.
     * @see EventGeneratingAction
     * @see org.asteriskjava.manager.event.ResponseEvent
     * @since 0.2
     */
    ResponseEvents sendEventGeneratingAction(EventGeneratingAction action)
            throws IOException, EventTimeoutException, IllegalArgumentException,
            IllegalStateException;

    /**
     * Sends a EventGeneratingAction to the Asterisk server and waits for the
     * corresponding ManagerResponse and the ResponseEvents.
     * EventGeneratingActions are ManagerActions that don't return their
     * response in the corresponding ManagerResponse but send a series of events
     * that contain the payload.<br>
     * This method will block until the correpsonding action complete event has
     * been received. The action complete event is determined by
     * {@link EventGeneratingAction#getActionCompleteEventClass()}.<br>
     * Examples for EventGeneratingActions are the
     * {@link org.asteriskjava.manager.action.StatusAction}, the
     * {@link org.asteriskjava.manager.action.QueueAction} or the
     * {@link org.asteriskjava.manager.action.AgentsAction}.
     * 
     * @param action the action to send to the Asterisk server
     * @param timeout milliseconds to wait for the response and the action
     *                complete event before throwing a TimeoutException
     * @return a ResponseEvents that contains the corresponding response and
     *         response events received from the Asterisk server
     * @throws IOException if the network connection is disrupted.
     * @throws EventTimeoutException if no response or not all response events are
     *             received within the given timeout period.
     * @throws IllegalArgumentException if the action is <code>null</code>,
     *             the actionCompleteEventClass property of the action is
     *             <code>null</code> or if actionCompleteEventClass is not a
     *             ResponseEvent.
     * @throws IllegalStateException if you are not connected to an Asterisk
     *             server.
     * @see EventGeneratingAction
     * @see org.asteriskjava.manager.event.ResponseEvent
     * @since 0.2
     */
    ResponseEvents sendEventGeneratingAction(EventGeneratingAction action,
            long timeout) throws IOException, EventTimeoutException,
            IllegalArgumentException, IllegalStateException;

    /**
     * Registers an event handler to be called whenever an
     * {@link org.asteriskjava.manager.event.ManagerEvent} is receiced from the
     * Asterisk server.<br>
     * Event handlers are notified about new events in the same order as they
     * were registered via addEventHandler.
     * 
     * @param eventHandler the handler to call whenever a manager event is
     *            received
     * @see #removeEventHandler(ManagerEventHandler)
     */
    void addEventHandler(ManagerEventHandler eventHandler);

    /**
     * Unregisters a previously registered event handler.<br>
     * Does nothing if the given event handler hasn't be been regiered before.
     * 
     * @param eventHandler the event handle to unregister
     * @see #addEventHandler(ManagerEventHandler)
     */
    void removeEventHandler(ManagerEventHandler eventHandler);

    /**
     * Returns the Asterisk server of this connection.
     * 
     * @return the Asterisk server of this connection.
     */
    AsteriskServer getAsteriskServer();

    /**
     * Checks if the connection to the Asterisk server is established.
     * 
     * @return <code>true</code> if the connection is established,
     *         <code>false</code> otherwise.
     * @since 0.2
     */
    boolean isConnected();
}
