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

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.core.socket.SocketConnectionAdapter;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.CoreSettingsAction;
import org.asteriskjava.manager.action.PingAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManagerConnectionImplTest {
    protected SocketConnectionAdapter mockSocket;
    protected ManagerWriterMock mockWriter;
    protected ManagerReaderMock mockReader;
    protected MockedManagerConnectionImpl mc;

    @BeforeEach
    void setUp() {
        mockWriter = new ManagerWriterMock();
        mockWriter.setExpectedUsername("username");
        // md5 sum of 12345password
        mockWriter.setExpectedKey("40b1b887502902a8ce61a16e44630f7c");

        mockReader = new ManagerReaderMock();
        mockSocket = mock(SocketConnectionAdapter.class);
        mc = new MockedManagerConnectionImpl(mockReader, mockWriter, mockSocket);
        mockWriter.setDispatcher(mc);
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("localhost", mc.getHostname(), "Invalid default hostname");
        assertEquals(5038, mc.getPort(), "Invalid default port");
    }

    @Test
    void testRegisterUserEventClass() {
        ManagerReader managerReader;

        managerReader = mock(ManagerReader.class);

        managerReader.registerEventClass(MyUserEvent.class);

        mc = new MockedManagerConnectionImpl(managerReader, mockWriter, mockSocket);
        mc.registerUserEventClass(MyUserEvent.class);

        assertEquals(0, mc.createSocketCalls, "unexpected call to createSocket");
        assertEquals(0, mc.createWriterCalls, "unexpected call to createWriter");
        assertEquals(1, mc.createReaderCalls, "createReader not called 1 time");
    }

    @Test
    void testLogin() throws Exception {
        MockedManagerEventListener listener;
        long startTime;
        long endTime;
        long duration;

        listener = new MockedManagerEventListener();

        mc.setUsername("username");
        mc.setPassword("password");
        mc.addEventListener(listener);
        mc.setDefaultResponseTimeout(5000);

        startTime = System.currentTimeMillis();
        mc.login();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;

        assertEquals(1, mc.createSocketCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createWriterCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createSocket not called 1 time");

        assertEquals(1, mockWriter.challengeActionsSent, "challenge action not sent 1 time");
        assertEquals(1, mockWriter.loginActionsSent, "login action not sent 1 time");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");
        assertEquals(1, mockReader.setSocketCalls, "setSocket() not called 1 time");
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // ugly hack to make this work when the thread is interrupted coz a
            // response has been received but the ManagerConnection was not yet
            // sleeping
            Thread.sleep(100);
        }
        assertEquals(1, mockReader.runCalls, "run() not called 1 time");
        assertEquals(0, mockReader.dieCalls, "unexpected call to die()");

        assertEquals(ManagerConnectionState.CONNECTED, mc.getState(), "state is not CONNECTED");

        assertEquals(1, listener.eventsHandled.size(), "must have handled exactly one events");
        /*
         * assertTrue(
         * "first event handled must be a ProtocolIdentifierReceivedEvent",
         * listener.eventsHandled.get(0) instanceof
         * ProtocolIdentifierReceivedEvent);
         */
        assertTrue(listener.eventsHandled.get(0) instanceof ConnectEvent, "event handled must be a ConnectEvent");

        assertTrue(duration <= 2000,
                "login() took longer than 2 second, probably a notify error (duration was " + duration + " is msec)");
    }

    @Test
    void testLoginIncorrectKey() throws Exception {
        mockSocket.close();

        mockWriter.setExpectedUsername("username");
        // md5 sum of 12345password
        mockWriter.setExpectedKey("40b1b887502902a8ce61a16e44630f7c");

        mc.setUsername("username");
        mc.setPassword("wrong password");

        try {
            mc.login();
            fail("No AuthenticationFailedException thrown");
        } catch (AuthenticationFailedException e) {
        }

        assertEquals(1, mc.createSocketCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createWriterCalls, "createWriter not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createReader not called 1 time");

        assertEquals(1, mockWriter.challengeActionsSent, "challenge action not sent 1 time");
        assertEquals(1, mockWriter.loginActionsSent, "login action not sent 1 time");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");
        assertEquals(1, mockReader.setSocketCalls, "setSocket() not called 1 time");
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // ugly hack to make this work when the thread is interrupted coz a
            // response has been received but the ManagerConnection was not yet
            // sleeping
            Thread.sleep(100);
        }
        assertEquals(1, mockReader.runCalls, "run() not called 1 time");
        assertEquals(0, mockReader.dieCalls, "unexpected call to die()");
    }

    @Test
    void testLoginIOExceptionOnConnect() throws Exception {
        mc.setThrowIOExceptionOnFirstSocketCreate(true);
        try {
            mc.login();
            fail("No IOException thrown");
        } catch (IOException e) {
        }

        assertEquals(1, mc.createSocketCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createWriterCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createSocket not called 1 time");

        assertEquals(0, mockWriter.challengeActionsSent, "unexpected challenge action sent");
        assertEquals(0, mockWriter.loginActionsSent, "unexpected login action sent");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");

        assertEquals(0, mockReader.setSocketCalls, "unexpected call to setSocket()");
        assertEquals(0, mockReader.runCalls, "unexpected call to run()");
        assertEquals(0, mockReader.dieCalls, "unexpected call to die()");
    }

    @Test
    void testLoginTimeoutOnConnect() throws Exception {
        mc.setDefaultResponseTimeout(50);

        mockSocket.close();

        // provoke timeout
        mockWriter.setSendProtocolIdentifierReceivedEvent(false);

        try {
            mc.login();
            fail("No TimeoutException on login()");
        } catch (TimeoutException e) {
            assertEquals("Timeout waiting for protocol identifier", e.getMessage());
        }

        assertEquals(1, mc.createSocketCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createWriterCalls, "createWriter not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createReader not called 1 time");

        assertEquals(0, mockWriter.challengeActionsSent, "unexpected challenge action sent");
        assertEquals(0, mockWriter.loginActionsSent, "unexpected login action sent");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");

        assertEquals(1, mockReader.setSocketCalls, "setSocket() not called 1 time");
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        Thread.sleep(10);
        assertEquals(1, mockReader.runCalls, "run() not called 1 time");
        assertEquals(0, mockReader.dieCalls, "unexpected call to die()");
    }

    @Test
    void testLoginTimeoutOnChallengeAction() throws Exception {
        mc.setDefaultResponseTimeout(200);

        mockSocket.close();

        // provoke timeout
        mockWriter.setSendResponse(false);

        try {
            mc.login();
            fail("No TimeoutException on login()");
        } catch (AuthenticationFailedException e) {
            assertEquals("Unable to send challenge action", e.getMessage());
            assertEquals("Timeout waiting for response to Challenge", e.getCause().getMessage());
            assertTrue(e.getCause() instanceof TimeoutException);
        }

        assertEquals(1, mc.createSocketCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createWriterCalls, "createWriter not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createReader not called 1 time");

        assertEquals(1, mockWriter.challengeActionsSent, "challenge action not sent 1 time");
        assertEquals(0, mockWriter.loginActionsSent, "unexpected login action sent");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");

        assertEquals(1, mockReader.setSocketCalls, "setSocket() not called 1 time");
        // Some time for the reader thread to be started. Otherwise run() might
        // not yet have been
        // called.
        Thread.sleep(10);
        assertEquals(1, mockReader.runCalls, "run() not called 1 time");
        assertEquals(0, mockReader.dieCalls, "unexpected call to die()");
    }

    @Test
    void testLogoffWhenConnected() throws Exception {
        mockSocket.close();

        // fake connect
        mc.connect();
        mc.setState(ManagerConnectionState.CONNECTED);

        mc.logoff();

        assertEquals(1, mockWriter.logoffActionsSent, "logoff action not sent 1 time");
    }

    @Test
    void testLogoffWhenNotConnected() {
        try {
            mc.logoff();
            fail("Expected IllegalStateException when calling logoff when not connected");
        } catch (IllegalStateException e) {
            // fine
        }

        assertEquals(0, mockWriter.logoffActionsSent, "unexpected logoff action sent");
    }

    @Test
    void testSendActionWithNullAction() throws Exception {
        // fake connect
        mc.connect();
        try {
            mc.sendAction(null);
            fail("No IllgealArgumentException thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    void testSendActionWhenNotConnected() throws Exception {
        StatusAction statusAction;

        statusAction = new StatusAction();

        try {
            mc.sendAction(statusAction);
            fail("No IllegalStateException thrown");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    void testSendAction() throws Exception {
        StatusAction statusAction;
        ManagerResponse response;

        statusAction = new StatusAction();
        statusAction.setActionId("123");

        // fake connect
        mc.connect();
        mc.setState(ManagerConnectionState.CONNECTED);
        response = mc.sendAction(statusAction);

        assertEquals("123", statusAction.getActionId(), "incorrect actionId in action");
        assertEquals("123", response.getActionId(), "incorrect actionId in response");
        assertEquals("Success", response.getResponse(), "incorrect response");

        assertEquals(1, mockWriter.otherActionsSent, "other actions not sent 1 time");
    }

    @Test
    void testSendActionTimeout() throws Exception {
        StatusAction statusAction;

        statusAction = new StatusAction();
        statusAction.setActionId("123");

        mc.setDefaultResponseTimeout(200);
        // fake connect
        mc.connect();
        mc.setState(ManagerConnectionState.CONNECTED);

        // provoke timeout
        mockWriter.setSendResponse(false);
        try {
            mc.sendAction(statusAction);
            fail("No TimeoutException thrown");
        } catch (TimeoutException e) {
        }

        assertEquals(1, mockWriter.otherActionsSent, "other actions not sent 1 time");
    }

    @Test
    void testDispatchResponseUnexpectedResponse() {
        ManagerResponse response;

        response = new ManagerResponse();
        // internalActionId: 123_0
        response.setActionId("123_0-abc");
        response.setResponse("Success");

        // expected result is ignoring the response and logging
        mc.dispatchResponse(response, null);
    }

    @Test
    void testDispatchResponseMissingInternalActionId() {
        ManagerResponse response;

        response = new ManagerResponse();
        response.setActionId("abc");
        response.setResponse("Success");

        // expected result is ignoring the response and logging
        mc.dispatchResponse(response, null);
    }

    @Test
    void testDispatchResponseNullActionId() {
        ManagerResponse response;

        response = new ManagerResponse();
        response.setActionId(null);
        response.setResponse("Success");

        // expected result is ignoring the response and logging
        mc.dispatchResponse(response, null);
    }

    @Test
    void testDispatchResponseNullResponse() {
        // expected result is ignoring and logging
        mc.dispatchResponse(null, null);
    }

    @Test
    void testReconnect() throws Exception {
        DisconnectEvent disconnectEvent;

        disconnectEvent = new DisconnectEvent(this);

        // fake successful login
        mc.setState(ManagerConnectionState.CONNECTED);

        mc.setUsername("username");
        mc.setPassword("password");

        mc.dispatchEvent(disconnectEvent, null);

        // wait for reconnect thread to do its work
        Thread.sleep(1000);

        assertEquals(1, mc.createSocketCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createWriterCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createSocket not called 1 time");

        assertEquals(1, mockWriter.challengeActionsSent, "challenge action not sent 1 time");
        assertEquals(1, mockWriter.loginActionsSent, "login action not sent 1 time");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");

        assertEquals(ManagerConnectionState.CONNECTED, mc.getState(), "state is not CONNECTED");
    }

    @Test
    void testReconnectWithIOException() throws Exception {
        DisconnectEvent disconnectEvent;

        disconnectEvent = new DisconnectEvent(this);

        // fake successful login
        mc.setState(ManagerConnectionState.CONNECTED);

        mc.setThrowIOExceptionOnFirstSocketCreate(true);

        mc.setUsername("username");
        mc.setPassword("password");

        mc.dispatchEvent(disconnectEvent, null);

        // wait for reconnect thread to do its work
        Thread.sleep(1000);
        assertEquals(2, mc.createSocketCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createWriterCalls, "createSocket not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createSocket not called 1 time");

        assertEquals(1, mockWriter.challengeActionsSent, "challenge action not sent 1 time");
        assertEquals(1, mockWriter.loginActionsSent, "login action not sent 1 time");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");

        assertEquals(ManagerConnectionState.CONNECTED, mc.getState(), "state is not CONNECTED");

    }

    @Test
    void testReconnectWithTimeoutException() throws Exception {
        DisconnectEvent disconnectEvent;

        mockSocket.close();
        disconnectEvent = new DisconnectEvent(this);

        // fake successful login
        mc.setState(ManagerConnectionState.CONNECTED);

        mc.setThrowTimeoutExceptionOnFirstLogin(true);

        mc.setUsername("username");
        mc.setPassword("password");

        mc.dispatchEvent(disconnectEvent, null);

        // wait for reconnect thread to do its work
        Thread.sleep(1000);

        assertEquals(2, mc.createSocketCalls, "createSocket not called 2 time");
        assertEquals(1, mc.createWriterCalls, "createWriter not called 1 time");
        assertEquals(1, mc.createReaderCalls, "createWriter not called 1 time");

        assertEquals(1, mockWriter.challengeActionsSent, "challenge action not sent 1 time");
        assertEquals(1, mockWriter.loginActionsSent, "login action not sent 1 time");
        assertEquals(0, mockWriter.otherActionsSent, "unexpected other actions sent");

        assertEquals(ManagerConnectionState.CONNECTED, mc.getState(), "state is not CONNECTED");
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDispatchEventWithMultipleEventHandlers() {
        final int count = 20;
        ManagerEvent event;
        final List<Integer> list;

        // verify that event handlers are called in the correct order
        event = new NewChannelEvent(this);
        list = mock(List.class);
        for (int i = 0; i < count; i++) {
            final int index = i;
            when(list.add(index)).thenReturn(true);
            mc.addEventListener(new ManagerEventListener() {
                public void onManagerEvent(ManagerEvent event) {
                    list.add(index);
                }
            });
        }

        mc.dispatchEvent(event, null);
    }

    @Test
    void testIsShowVersionCommandAction() {
        assertTrue(mc.isShowVersionCommandAction(new CoreSettingsAction()));
        assertTrue(mc.isShowVersionCommandAction(new CommandAction("core show version")));
        assertFalse(mc.isShowVersionCommandAction(new PingAction()));
    }

    private class MockedManagerEventListener implements ManagerEventListener {
        List<ManagerEvent> eventsHandled;

        public MockedManagerEventListener() {
            this.eventsHandled = new ArrayList<ManagerEvent>();
        }

        public void onManagerEvent(ManagerEvent event) {
            eventsHandled.add(event);
        }
    }

    private class MockedManagerConnectionImpl extends ManagerConnectionImpl {
        ManagerReader mockReader;
        ManagerWriter mockWriter;
        SocketConnectionAdapter mockSocket;

        private boolean throwIOExceptionOnFirstSocketCreate = false;
        private boolean throwTimeoutExceptionOnFirstLogin = false;

        public int createReaderCalls = 0;
        public int createWriterCalls = 0;
        public int createSocketCalls = 0;

        public int loginCalls = 0;

        public MockedManagerConnectionImpl(ManagerReader mockReader, ManagerWriter mockWriter,
                                           SocketConnectionAdapter mockSocket) {
            super();
            this.mockReader = mockReader;
            this.mockWriter = mockWriter;
            this.mockSocket = mockSocket;
        }

        void setThrowTimeoutExceptionOnFirstLogin(boolean b) {
            this.throwTimeoutExceptionOnFirstLogin = b;
        }

        void setThrowIOExceptionOnFirstSocketCreate(boolean b) {
            this.throwIOExceptionOnFirstSocketCreate = b;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        void setState(ManagerConnectionState state) {
            this.state = state;
        }

        @Override
        protected ManagerReader createReader(Dispatcher d, Object source) {
            createReaderCalls++;
            return mockReader;
        }

        @Override
        protected ManagerWriter createWriter() {
            createWriterCalls++;
            return mockWriter;
        }

        @Override
        protected SocketConnectionAdapter createSocket() throws IOException {
            createSocketCalls++;

            if (throwIOExceptionOnFirstSocketCreate && createSocketCalls == 1) {
                throw new IOException();
            }
            return mockSocket;
        }

        @Override
        protected synchronized void doLogin(long timeout, String events)
                throws IOException, AuthenticationFailedException, TimeoutException {
            loginCalls++;

            if (throwTimeoutExceptionOnFirstLogin && loginCalls == 1) {
                disconnect();
                throw new TimeoutException("Provoked timeout");
            }
            super.doLogin(timeout, events);
        }

        @Override
        protected AsteriskVersion determineVersion() {
            return null;
        }
    }
}
