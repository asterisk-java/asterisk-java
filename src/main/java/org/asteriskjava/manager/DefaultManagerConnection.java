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
import org.asteriskjava.manager.internal.ManagerConnectionImpl;
import org.asteriskjava.manager.response.ManagerResponse;

/**
 * Default implemention of the {@link org.asteriskjava.manager.ManagerConnection}
 * interface.<br>
 * Generelly avoid direct use of this class. Use a
 * {@link org.asteriskjava.manager.ManagerConnectionFactory} to
 * obtain a {@link org.asteriskjava.manager.ManagerConnection} instead.<br>
 * When using a dependency injection framework like the Spring Framework direct usage for
 * wiring up beans that require a {@link org.asteriskjava.manager.ManagerConnection}
 * property is fine though.<br>
 * Note that the DefaultManagerConnection will create one new Thread
 * for reading data from Asterisk once it is
 * {@link org.asteriskjava.manager.ManagerConnectionState#CONNECTING}.
 * 
 * @see org.asteriskjava.manager.ManagerConnectionFactory
 * @author srt
 * @version $Id$
 */
public class DefaultManagerConnection implements ManagerConnection
{
    private ManagerConnectionImpl impl;

    /**
     * Creates a new instance.
     */
    public DefaultManagerConnection()
    {
        this.impl = new ManagerConnectionImpl();
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
    public DefaultManagerConnection(String hostname, int port, String username, String password)
    {
        this();
        setHostname(hostname);
        setPort(port);
        setUsername(username);
        setPassword(password);
    }

    /**
     * Sets the hostname of the Asterisk server to connect to.<br>
     * Default is <code>localhost</code>.
     * 
     * @param hostname the hostname to connect to
     */
    public void setHostname(String hostname)
    {
        impl.setHostname(hostname);
    }

    /**
     * Sets the port to use to connect to the Asterisk server. This is the port
     * specified in Asterisk's <code>manager.conf</code> file.<br>
     * Default is 5038.
     * 
     * @param port the port to connect to
     */
    public void setPort(int port)
    {
        impl.setPort(port);
    }

    /**
     * Sets the username to use to connect to the Asterisk server. This is the
     * username specified in Asterisk's <code>manager.conf</code> file.
     * 
     * @param username the username to use for login
     */
    public void setUsername(String username)
    {
        impl.setUsername(username);
    }

    /**
     * Sets the password to use to connect to the Asterisk server. This is the
     * password specified in Asterisk's <code>manager.conf</code> file.
     * 
     * @param password the password to use for login
     */
    public void setPassword(String password)
    {
        impl.setPassword(password);
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
        impl.setDefaultResponseTimeout(defaultTimeout);
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
        impl.setDefaultResponseTimeout(defaultResponseTimeout);
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
        impl.setDefaultEventTimeout(defaultEventTimeout);
    }

    /**
     * This method is deprecated and will be removed in Asterisk-Java 0.4.<br>
     * It does nothing.
     * 
     * @deprecated no longer needed as we now use an interrupt based response
     *             checking approach.
     */
    public void setSleepTime(long sleepTime)
    {

    }

    /**
     * Set to <code>true</code> to try reconnecting to ther asterisk serve
     * even if the reconnection attempt threw an AuthenticationFailedException.<br>
     * Default is <code>true</code>.
     */
    public void setKeepAliveAfterAuthenticationFailure(
            boolean keepAliveAfterAuthenticationFailure)
    {
        impl.setKeepAliveAfterAuthenticationFailure(keepAliveAfterAuthenticationFailure);
    }

    /* Implementation of ManagerConnection interface */

    public String getHostname()
    {
        return impl.getHostname();
    }

    public int getPort()
    {
        return impl.getPort();
    }

    public void registerUserEventClass(Class userEventClass)
    {
        impl.registerUserEventClass(userEventClass);
    }

    public void setSocketTimeout(int socketTimeout)
    {
        impl.setSocketTimeout(socketTimeout);
    }

    public void login() throws IllegalStateException, IOException, 
            AuthenticationFailedException, TimeoutException
    {
        impl.login();
    }

    public void login(String events) throws IllegalStateException, IOException, 
            AuthenticationFailedException, TimeoutException
    {
        impl.login(events);
    }

    public boolean isConnected()
    {
        return impl.isConnected();
    }

    public void logoff() throws IllegalStateException
    {
        impl.logoff();
    }

    public ManagerResponse sendAction(ManagerAction action) throws IOException,
            TimeoutException, IllegalArgumentException, IllegalStateException
    {
        return impl.sendAction(action);
    }

    public ManagerResponse sendAction(ManagerAction action, long timeout)
            throws IOException, TimeoutException, IllegalArgumentException,
            IllegalStateException
    {
        return impl.sendAction(action, timeout);
    }

    @SuppressWarnings("deprecation")
    public void sendAction(ManagerAction action,
            ManagerResponseHandler callbackHandler) throws IOException,
            IllegalArgumentException, IllegalStateException
    {
        impl.sendAction(action, callbackHandler);
    }

    public void sendAction(ManagerAction action,
            ManagerResponseListener callbackHandler) throws IOException,
            IllegalArgumentException, IllegalStateException
    {
        impl.sendAction(action, callbackHandler);
    }

    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action)
            throws IOException, EventTimeoutException,
            IllegalArgumentException, IllegalStateException
    {
        return impl.sendEventGeneratingAction(action);
    }

    public ResponseEvents sendEventGeneratingAction(
            EventGeneratingAction action, long timeout) throws IOException,
            EventTimeoutException, IllegalArgumentException,
            IllegalStateException
    {
        return impl.sendEventGeneratingAction(action, timeout);
    }

    public void addEventListener(final ManagerEventListener listener)
    {
        impl.addEventListener(listener);
    }

    public void removeEventListener(final ManagerEventListener listener)
    {
        impl.removeEventListener(listener);
    }

    @SuppressWarnings("deprecation")
    public void addEventHandler(final ManagerEventHandler eventHandler)
    {
        impl.addEventHandler(eventHandler);
    }

    @SuppressWarnings("deprecation")
    public void removeEventHandler(final ManagerEventHandler eventHandler)
    {
        impl.removeEventHandler(eventHandler);
    }

    public String getProtocolIdentifier()
    {
        return impl.getProtocolIdentifier();
    }

    public ManagerConnectionState getState()
    {
        return impl.getState();
    }
}
