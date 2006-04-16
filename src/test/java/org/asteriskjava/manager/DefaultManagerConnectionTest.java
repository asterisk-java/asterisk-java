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
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

import org.asteriskjava.io.SocketConnectionFacade;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.ProtocolIdentifierReceivedEvent;
import org.asteriskjava.manager.response.ManagerResponse;

public class DefaultManagerConnectionTest extends TestCase
{
    protected SocketConnectionFacade mockSocket;
    protected ManagerWriterMock mockWriter;
    protected ManagerReaderMock mockReader;
    protected MockedDefaultManagerConnection dmc;
    protected AsteriskServer asteriskServer;

    protected void setUp() throws Exception
    {
        asteriskServer = new AsteriskServer("localhost", 5038);

        mockWriter = new ManagerWriterMock();
        mockWriter.setExpectedUsername("username");
        // md5 sum of 12345password
        mockWriter.setExpectedKey("40b1b887502902a8ce61a16e44630f7c");

        mockReader = new ManagerReaderMock();

        mockSocket = createMock(SocketConnectionFacade.class);

        dmc = new MockedDefaultManagerConnection(mockReader, mockWriter,
                mockSocket);

        dmc.setDefaultResponseTimeout(20);

        mockWriter.setDispatcher(dmc);
        mockWriter.setAsteriskServer(asteriskServer);
    }

    public void testDefaultConstructor()
    {
        assertEquals("Invalid default hostname", "localhost", dmc
                .getAsteriskServer().getHostname());
        assertEquals("Invalid default port", 5038, dmc.getAsteriskServer()
                .getPort());
    }

    public void testFullConstructor()
    {
        dmc = new MockedDefaultManagerConnection("host", 1234, "u", "p");

        assertEquals("Invalid hostname", "host", dmc.getAsteriskServer()
                .getHostname());
        assertEquals("Invalid port", 1234, dmc.getAsteriskServer().getPort());
        assertEquals("Invalid username", "u", dmc.getUsername());
        assertEquals("Invalid password", "p", dmc.getPassword());
    }

    public void testRegisterUserEventClass()
    {
        ManagerReader managerReader;

        managerReader = createMock(ManagerReader.class);

        managerReader.registerEventClass(MyUserEvent.class);
        replay(managerReader);

        dmc = new MockedDefaultManagerConnection(managerReader, mockWriter,
                mockSocket);
        dmc.registerUserEventClass(MyUserEvent.class);

        assertEquals("unexpected call to createSocket", 0,
                dmc.createSocketCalls);
        assertEquals("unexpected call to createWriter", 0,
                dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        verify(managerReader);
    }

    public void testLogin() throws Exception
    {
        MockedManagerEventHandler managerEventHandler;

        managerEventHandler = new MockedManagerEventHandler();

        replay(mockSocket);

        dmc.setUsername("username");
        dmc.setPassword("password");
        dmc.addEventHandler(managerEventHandler);
        dmc.login();

        assertEquals("createSocket not called 1 time", 1, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("challenge action not sent 1 time", 1,
                mockWriter.challengeActionsSent);
        assertEquals("login action not sent 1 time", 1,
                mockWriter.loginActionsSent);
        // 1 other action sent to determine version
        assertEquals("unexpected other actions sent", 1,
                mockWriter.otherActionsSent);

        assertEquals("setSocket() not called 1 time", 1,
                mockReader.setSocketCalls);
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        try
        {
            Thread.sleep(10);
        }
        catch (InterruptedException e)
        {
            // ugly hack to make this work when the thread is interrupted coz a
            // response has been received but the ManagerConnection was not yet 
            // sleeping
            Thread.sleep(10);
        }
        assertEquals("run() not called 1 time", 1, mockReader.runCalls);
        assertEquals("unexpected call to die()", 0, mockReader.dieCalls);

        assertTrue("keepAlive not set", dmc.getKeepAlive());

        assertEquals("must have handled exactly two events", 2,
                managerEventHandler.eventsHandled.size());

        assertTrue(
                "first event handled must be a ProtocolIdentifierReceivedEvent",
                managerEventHandler.eventsHandled.get(0) instanceof ProtocolIdentifierReceivedEvent);

        assertTrue(
                "second event handled must be a ConnectEvent",
                managerEventHandler.eventsHandled.get(1) instanceof ConnectEvent);

        verify(mockSocket);
    }

    public void testLoginIncorrectKey() throws Exception
    {
        mockSocket.close();
        replay(mockSocket);

        mockWriter.setExpectedUsername("username");
        // md5 sum of 12345password
        mockWriter.setExpectedKey("40b1b887502902a8ce61a16e44630f7c");

        dmc.setUsername("username");
        dmc.setPassword("wrong password");

        try
        {
            dmc.login();
            fail("No AuthenticationFailedException thrown");
        }
        catch (AuthenticationFailedException e)
        {
        }

        assertEquals("createSocket not called 1 time", 1, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("challenge action not sent 1 time", 1,
                mockWriter.challengeActionsSent);
        assertEquals("login action not sent 1 time", 1,
                mockWriter.loginActionsSent);
        assertEquals("unexpected other actions sent", 0,
                mockWriter.otherActionsSent);

        assertEquals("setSocket() not called 1 time", 1,
                mockReader.setSocketCalls);
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            // ugly hack to make this work when the thread is interrupted coz a
            // response has been received but the ManagerConnection was not yet 
            // sleeping
            Thread.sleep(100);
        }
        assertEquals("run() not called 1 time", 1, mockReader.runCalls);
        assertEquals("unexpected call to die()", 0, mockReader.dieCalls);

        verify(mockSocket);
    }

    public void testLoginIOExceptionOnConnect() throws Exception
    {
        replay(mockSocket);

        dmc.setThrowIOExceptionOnFirstSocketCreate(true);
        try
        {
            dmc.login();
            fail("No IOException thrown");
        }
        catch (IOException e)
        {
        }

        assertEquals("createSocket not called 1 time", 1, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("unexpected challenge action sent", 0,
                mockWriter.challengeActionsSent);
        assertEquals("unexpected login action sent", 0,
                mockWriter.loginActionsSent);
        assertEquals("unexpected other actions sent", 0,
                mockWriter.otherActionsSent);

        assertEquals("unexpected call to setSocket()", 0,
                mockReader.setSocketCalls);
        assertEquals("unexpected call to run()", 0, mockReader.runCalls);
        assertEquals("unexpected call to die()", 0, mockReader.dieCalls);

        verify(mockSocket);
    }

    public void testLoginTimeoutOnConnect() throws Exception
    {
        mockSocket.close();
        replay(mockSocket);

        // provoke timeout
        mockWriter.setSendProtocolIdentifierReceivedEvent(false);

        try
        {
            dmc.login();
            fail("No TimeoutException on login()");
        }
        catch (TimeoutException e)
        {
            assertEquals("Timeout waiting for protocol identifier", e
                    .getMessage());
        }

        assertEquals("createSocket not called 1 time", 1, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("unexpected challenge action sent", 0,
                mockWriter.challengeActionsSent);
        assertEquals("unexpected login action sent", 0,
                mockWriter.loginActionsSent);
        assertEquals("unexpected other actions sent", 0,
                mockWriter.otherActionsSent);

        assertEquals("setSocket() not called 1 time", 1,
                mockReader.setSocketCalls);
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        Thread.sleep(10);
        assertEquals("run() not called 1 time", 1, mockReader.runCalls);
        assertEquals("unexpected call to die()", 0, mockReader.dieCalls);

        verify(mockSocket);
    }

    public void testLoginTimeoutOnChallengeAction() throws Exception
    {
        replay(mockSocket);

        // provoke timeout
        mockWriter.setSendResponse(false);

        try
        {
            dmc.login();
            fail("No TimeoutException on login()");
        }
        catch (TimeoutException e)
        {
            assertEquals("Timeout waiting for response to Challenge", e
                    .getMessage());
        }

        assertEquals("createSocket not called 1 time", 1, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("challenge action not sent 1 time", 1,
                mockWriter.challengeActionsSent);
        assertEquals("unexpected login action sent", 0,
                mockWriter.loginActionsSent);
        assertEquals("unexpected other actions sent", 0,
                mockWriter.otherActionsSent);

        assertEquals("setSocket() not called 1 time", 1,
                mockReader.setSocketCalls);
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        Thread.sleep(10);
        assertEquals("run() not called 1 time", 1, mockReader.runCalls);
        assertEquals("unexpected call to die()", 0, mockReader.dieCalls);

        verify(mockSocket);
    }

    public void testLogoffWhenConnected() throws Exception
    {
        mockSocket.close();
        replay(mockSocket);

        // fake connect
        dmc.connect();

        dmc.logoff();

        assertEquals("logoff action not sent 1 time", 1,
                mockWriter.logoffActionsSent);
        verify(mockSocket);
    }

    public void testLogoffWhenNotConnected() throws Exception
    {
        replay(mockSocket);

        dmc.logoff();

        assertEquals("unexpected logoff action sent", 0,
                mockWriter.logoffActionsSent);
        verify(mockSocket);
    }

    public void testSendActionWithNullAction() throws Exception
    {
        // fake connect
        dmc.connect();
        try
        {
            dmc.sendAction(null);
            fail("No IllgealArgumentException thrown");
        }
        catch (IllegalArgumentException e)
        {
        }
    }

    public void testSendActionWhenNotConnected() throws Exception
    {
        StatusAction statusAction;

        statusAction = new StatusAction();

        try
        {
            dmc.sendAction(statusAction);
            fail("No IllegalStateException thrown");
        }
        catch (IllegalStateException e)
        {
        }
    }

    public void testSendAction() throws Exception
    {
        StatusAction statusAction;
        ManagerResponse response;

        statusAction = new StatusAction();
        statusAction.setActionId("123");

        // fake connect
        dmc.connect();
        response = dmc.sendAction(statusAction);

        assertEquals("incorrect actionId in action", "123", statusAction.getActionId());
        assertEquals("incorrect actionId in response", "123", response.getActionId());
        assertEquals("incorrect response", "Success", response.getResponse());

        assertEquals("other actions not sent 1 time", 1,
                mockWriter.otherActionsSent);
    }

    public void testSendActionTimeout() throws Exception
    {
        StatusAction statusAction;

        statusAction = new StatusAction();
        statusAction.setActionId("123");

        // fake connect
        dmc.connect();
        // provoke timeout
        mockWriter.setSendResponse(false);
        try
        {
            dmc.sendAction(statusAction);
            fail("No TimeoutException thrown");
        }
        catch (TimeoutException e)
        {
        }

        assertEquals("other actions not sent 1 time", 1,
                mockWriter.otherActionsSent);
    }

    public void testDispatchResponseUnexpectedResponse()
    {
        ManagerResponse response;

        response = new ManagerResponse();
        // internalActionId: 123_0
        response.setActionId("123_0-abc");
        response.setResponse("Success");

        // expected result is ignoring the response and logging
        dmc.dispatchResponse(response);
    }

    public void testDispatchResponseMissingInternalActionId()
    {
        ManagerResponse response;

        response = new ManagerResponse();
        response.setActionId("abc");
        response.setResponse("Success");

        // expected result is ignoring the response and logging
        dmc.dispatchResponse(response);
    }

    public void testDispatchResponseNullActionId()
    {
        ManagerResponse response;

        response = new ManagerResponse();
        response.setActionId(null);
        response.setResponse("Success");

        // expected result is ignoring the response and logging
        dmc.dispatchResponse(response);
    }

    public void testDispatchResponseNullResponse()
    {
        // expected result is ignoring and logging
        dmc.dispatchResponse(null);
    }

    public void testReconnect() throws Exception
    {
        DisconnectEvent disconnectEvent;

        replay(mockSocket);
        disconnectEvent = new DisconnectEvent(asteriskServer);

        // fake successful login
        dmc.setKeepAlive(true);

        dmc.setUsername("username");
        dmc.setPassword("password");

        dmc.dispatchEvent(disconnectEvent);

        assertEquals("createSocket not called 1 time", 1, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("challenge action not sent 1 time", 1,
                mockWriter.challengeActionsSent);
        assertEquals("login action not sent 1 time", 1,
                mockWriter.loginActionsSent);
        // 1 other action sent to determine version
        assertEquals("unexpected other actions sent", 1,
                mockWriter.otherActionsSent);

        assertTrue("keepAlive not enabled", dmc.getKeepAlive());

        verify(mockSocket);
    }

    public void testReconnectWithIOException() throws Exception
    {
        DisconnectEvent disconnectEvent;

        replay(mockSocket);
        disconnectEvent = new DisconnectEvent(asteriskServer);

        // fake successful login
        dmc.setKeepAlive(true);

        dmc.setThrowIOExceptionOnFirstSocketCreate(true);

        dmc.setUsername("username");
        dmc.setPassword("password");

        dmc.dispatchEvent(disconnectEvent);

        assertEquals("createSocket not called 1 time", 2, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("challenge action not sent 1 time", 1,
                mockWriter.challengeActionsSent);
        assertEquals("login action not sent 1 time", 1,
                mockWriter.loginActionsSent);
        // 1 other action sent to determine version
        assertEquals("unexpected other actions sent", 1,
                mockWriter.otherActionsSent);

        assertTrue("keepAlive not enabled", dmc.getKeepAlive());

        verify(mockSocket);
    }

    public void testReconnectWithAuthenticationFailure() throws Exception
    {
        DisconnectEvent disconnectEvent;

        mockSocket.close();
        replay(mockSocket);
        disconnectEvent = new DisconnectEvent(asteriskServer);

        // fake successful login
        dmc.setKeepAlive(true);

        dmc.setUsername("username");

        dmc.dispatchEvent(disconnectEvent);

        assertEquals("createSocket not called 1 time", 1, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("challenge action not sent 1 time", 1,
                mockWriter.challengeActionsSent);
        assertEquals("login action not sent 1 time", 1,
                mockWriter.loginActionsSent);
        assertEquals("unexpected other actions sent", 0,
                mockWriter.otherActionsSent);

        assertFalse("keepAlive not disabled", dmc.getKeepAlive());

        verify(mockSocket);
    }

    public void testReconnectWithKeepAliveAfterAuthenticationFailure()
            throws Exception
    {
        DisconnectEvent disconnectEvent;

        // 2 unsuccessful attempts
        mockSocket.close();
        mockSocket.close();
        replay(mockSocket);
        disconnectEvent = new DisconnectEvent(asteriskServer);

        // fake successful login
        dmc.setKeepAlive(true);

        // to prevent an infinite loop we will be able to log in after two
        // unsuccessful attempts
        // even if the password is not correct.
        dmc.setKeepAliveAfterAuthenticationFailure(true);
        dmc.setUsername("username");

        dmc.dispatchEvent(disconnectEvent);

        assertEquals("createSocket not called 3 time", 3, dmc.createSocketCalls);
        assertEquals("createWriter not called 1 time", 1, dmc.createWriterCalls);
        assertEquals("createReader not called 1 time", 1, dmc.createReaderCalls);

        assertEquals("challenge action not sent 3 time", 3,
                mockWriter.challengeActionsSent);
        assertEquals("login action not sent 3 time", 3,
                mockWriter.loginActionsSent);
        // 1 other action sent to determine version
        assertEquals("unexpected other actions sent", 1,
                mockWriter.otherActionsSent);

        assertTrue("keepAlive not enabled", dmc.getKeepAlive());

        verify(mockSocket);
    }

    @SuppressWarnings("unchecked")
    public void testDispatchEventWithMultipleEventHandlers()
    {
        final int count = 20;
        ManagerEvent event;
        final List<Integer> list;

        // verify that event handlers are called in the correct order
        event = new NewChannelEvent(new AsteriskServer());
        list = createMock(List.class);
        for (int i = 0; i < count; i++)
        {
            final int index = i;
            expect(list.add(new Integer(index))).andReturn(true);
            dmc.addEventHandler(new ManagerEventHandler()
            {
                public void handleEvent(ManagerEvent event)
                {
                    list.add(new Integer(index));
                }
            });
        }

        replay(list);
        dmc.dispatchEvent(event);
        verify(list);
    }

    private class MockedManagerEventHandler implements ManagerEventHandler
    {
        List<ManagerEvent> eventsHandled;

        public MockedManagerEventHandler()
        {
            this.eventsHandled = new ArrayList<ManagerEvent>();
        }

        public void handleEvent(ManagerEvent event)
        {
            eventsHandled.add(event);
        }
    }

    private class MockedDefaultManagerConnection
            extends
                DefaultManagerConnection
    {
        ManagerReader mockReader;
        ManagerWriter mockWriter;
        SocketConnectionFacade mockSocket;

        private boolean throwIOExceptionOnFirstSocketCreate = false;

        public int createReaderCalls = 0;
        public int createWriterCalls = 0;
        public int createSocketCalls = 0;

        public MockedDefaultManagerConnection(ManagerReader mockReader,
                ManagerWriter mockWriter, SocketConnectionFacade mockSocket)
        {
            super();
            this.mockReader = mockReader;
            this.mockWriter = mockWriter;
            this.mockSocket = mockSocket;
        }

        public MockedDefaultManagerConnection(String hostname, int port,
                String username, String password)
        {
            super(hostname, port, username, password);
        }

        public void setThrowIOExceptionOnFirstSocketCreate(
                boolean throwIOExceptionOnSocketCreate)
        {
            this.throwIOExceptionOnFirstSocketCreate = throwIOExceptionOnSocketCreate;
        }

        public String getUsername()
        {
            return username;
        }

        public String getPassword()
        {
            return password;
        }

        public void setKeepAlive(boolean keepAlive)
        {
            this.keepAlive = keepAlive;
        }

        public boolean getKeepAlive()
        {
            return keepAlive;
        }

        protected ManagerReader createReader(Dispatcher d, AsteriskServer s)
        {
            createReaderCalls++;
            return mockReader;
        }

        protected ManagerWriter createWriter()
        {
            createWriterCalls++;
            return mockWriter;
        }

        protected SocketConnectionFacade createSocket() throws IOException
        {
            createSocketCalls++;

            if (throwIOExceptionOnFirstSocketCreate && createSocketCalls == 1)
            {
                throw new IOException();
            }
            return mockSocket;
        }
    }
}
