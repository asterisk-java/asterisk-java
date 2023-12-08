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

import org.asteriskjava.ami.action.response.ManagerActionResponse;
import org.asteriskjava.ami.action.response.ResponseType;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.util.DateUtil;
import org.asteriskjava.util.SocketConnectionFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManagerReaderImplTest {
    private Date now;
    private MockedDispatcher dispatcher;
    private SocketConnectionFacade socketConnectionFacade;
    private ManagerReader managerReader;

    @BeforeEach
    void setUp() {
        now = new Date();
        DateUtil.overrideCurrentDate(now);
        dispatcher = new MockedDispatcher();
        managerReader = new ManagerReaderImpl(dispatcher, this);

        socketConnectionFacade = mock(SocketConnectionFacade.class);
    }

    @AfterEach
    void tearDown() {
        DateUtil.overrideCurrentDate(null);
    }

    @Test
    void testRunWithoutSocket() {
        try {
            managerReader.run();
            fail("Must throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e instanceof IllegalStateException, "Exception must be of type IllegalStateException");
        }
    }

    @Test
    void testRunReceivingProtocolIdentifier() throws Exception {
        when(socketConnectionFacade.readLine())
                .thenReturn("Asterisk Call Manager/1.0")
                .thenReturn(null);

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(2, dispatcher.dispatchedEvents.size(), "not exactly two events dispatched");

        assertEquals(ProtocolIdentifierReceivedEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a ProtocolIdentifierReceivedEvent");

        assertEquals("Asterisk Call Manager/1.0", ((ProtocolIdentifierReceivedEvent) dispatcher.dispatchedEvents.get(0)).getProtocolIdentifier(), "ProtocolIdentifierReceivedEvent contains incorrect protocol identifier");

        assertEquals(now, dispatcher.dispatchedEvents.get(0).getDateReceived(), "ProtocolIdentifierReceivedEvent contains incorrect dateReceived");
        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(1).getClass(), "second event must be a DisconnectEvent");

        assertEquals(now, dispatcher.dispatchedEvents.get(1).getDateReceived(), "DisconnectEvent contains incorrect dateReceived");
    }

    @Test
    void testRunReceivingEvent() throws Exception {
        when(socketConnectionFacade.readLine())
                .thenReturn("Event: StatusComplete")
                .thenReturn("")
                .thenReturn(null);

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(2, dispatcher.dispatchedEvents.size(), "not exactly two events dispatched");

        assertEquals(StatusCompleteEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a StatusCompleteEvent");
        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(1).getClass(), "second event must be a DisconnectEvent");
    }

    @Test
    void testRunReceivingEventWithMapProperty() throws Exception {
        when(socketConnectionFacade.readLine())
                .thenReturn("Event: AgentCalled")
                .thenReturn("Variable: var1=val1")
                .thenReturn("Variable: var2=val2")
                .thenReturn("")
                .thenReturn(null);

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(2, dispatcher.dispatchedEvents.size(), "not exactly two events dispatched");

        assertEquals(AgentCalledEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a AgentCalledEvent");

        AgentCalledEvent event = (AgentCalledEvent) dispatcher.dispatchedEvents.get(0);
        assertEquals(AgentCalledEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("val1", event.getVariables().get("var1"), "Property variables[var1] is not set correctly");
        assertEquals("val2", event.getVariables().get("var2"), "Property variables[var2] is not set correctly");
        assertEquals(2, event.getVariables().size(), "Invalid size of variables property");

        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(1).getClass(), "second event must be an DisconnectEvent");
    }

    @Test
    void testRunReceivingEventWithMapPropertyAndOnlyOneEntry() throws Exception {
        when(socketConnectionFacade.readLine())
                .thenReturn("Event: AgentCalled")
                .thenReturn("Variable: var1=val1")
                .thenReturn("")
                .thenReturn(null);

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(2, dispatcher.dispatchedEvents.size(), "not exactly two events dispatched");

        assertEquals(AgentCalledEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a AgentCalledEvent");

        AgentCalledEvent event = (AgentCalledEvent) dispatcher.dispatchedEvents.get(0);
        assertEquals(AgentCalledEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("val1", event.getVariables().get("var1"), "Property variables[var1] is not set correctly");
        assertEquals(1, event.getVariables().size(), "Invalid size of variables property");

        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(1).getClass(), "second event must be an DisconnectEvent");
    }

    @Test
    void testWorkaroundForAsteriskBug13319() throws Exception {
        when(socketConnectionFacade.readLine())
                .thenReturn("Event: RTCPReceived")
                .thenReturn("From 192.168.0.1:1234")
                .thenReturn("HighestSequence: 999")
                .thenReturn("")
                .thenReturn(null);

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(2, dispatcher.dispatchedEvents.size(), "not exactly two events dispatched");

        assertEquals(RtcpReceivedEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a RtcpReceivedEvent");

        RtcpReceivedEvent rtcpReceivedEvent = (RtcpReceivedEvent) dispatcher.dispatchedEvents.get(0);
        assertEquals("192.168.0.1", rtcpReceivedEvent.getFromAddress().getHostAddress(), "Invalid from address on RtcpReceivedEvent");
        assertEquals(new Integer(1234), rtcpReceivedEvent.getFromPort(), "Invalid from port on RtcpReceivedEvent");
        assertEquals(new Long(999), rtcpReceivedEvent.getHighestSequence(), "Invalid highest sequence on RtcpReceivedEvent");

        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(1).getClass(), "second event must be a DisconnectEvent");
    }

    // todo fix testRunReceivingUserEvent
    void XtestRunReceivingUserEvent() throws Exception {
        managerReader.registerEventClass(MyUserEvent.class);

        when(socketConnectionFacade.readLine())
                .thenReturn("Event: MyUser")
                .thenReturn("")
                .thenReturn(null);

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(2, dispatcher.dispatchedEvents.size(), "not exactly two events dispatched");

        assertEquals(MyUserEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a MyUserEvent");

        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(1).getClass(), "second event must be a DisconnectEvent");

    }

    @Test
    void testRunReceivingResponse() throws Exception {
        when(socketConnectionFacade.readLine())
                .thenReturn("Response: Success")
                .thenReturn("Message: Authentication accepted")
                .thenReturn("")
                .thenReturn(null);

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(1, dispatcher.dispatchedResponses.size(), "not exactly one response dispatched");

        assertEquals(ManagerActionResponse.class, dispatcher.dispatchedResponses.get(0).getClass(), "first response must be a ManagerResponse");
        assertEquals(ResponseType.Success, dispatcher.dispatchedResponses.get(0).getResponse(), "ManagerResponse contains incorrect response");

        assertEquals("Authentication accepted", dispatcher.dispatchedResponses.get(0).getMessage(), "ManagerResponse contains incorrect message");

        assertEquals("Authentication accepted", dispatcher.dispatchedResponses.get(0).getAttribute("MESSAGE"), "ManagerResponse contains incorrect message (via getAttribute)");

        assertEquals(now, Date.from(dispatcher.dispatchedResponses.get(0).getDateReceived()), "ManagerResponse contains incorrect dateReceived");

        assertEquals(1, dispatcher.dispatchedEvents.size(), "not exactly one events dispatched");

        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a DisconnectEvent");
    }

    @Test
    void testRunReceivingCommandResponse() throws Exception {
        List<String> result = new ArrayList<String>();

        when(socketConnectionFacade.readLine())
                .thenReturn("Response: Follows")
                .thenReturn("ActionID: 678#12345")
                .thenReturn("Line1\nLine2\n--END COMMAND--")
                .thenReturn("")
                .thenReturn(null);

        result.add("Line1");
        result.add("Line2");

        managerReader.setSocket(socketConnectionFacade);
        managerReader.expectResponseClass("678", CommandResponse.class);
        managerReader.run();

        assertEquals(1, dispatcher.dispatchedResponses.size(), "not exactly one response dispatched");

        assertEquals(CommandResponse.class, dispatcher.dispatchedResponses.get(0).getClass(), "first response must be a CommandResponse");

        assertEquals(ResponseType.Follows, dispatcher.dispatchedResponses.get(0).getResponse(), "CommandResponse contains incorrect response");

        assertEquals("678#12345", dispatcher.dispatchedResponses.get(0).getActionId(), "CommandResponse contains incorrect actionId");

        assertEquals("678#12345", dispatcher.dispatchedResponses.get(0).getAttribute("actionId"), "CommandResponse contains incorrect actionId (via getAttribute)");

        assertEquals(result, ((CommandResponse) dispatcher.dispatchedResponses.get(0)).getResult(), "CommandResponse contains incorrect result");

        assertEquals(now, Date.from(dispatcher.dispatchedResponses.get(0).getDateReceived()), "CommandResponse contains incorrect dateReceived");
    }

    @Test
    void testRunCatchingIOException() throws Exception {
        when(socketConnectionFacade.readLine()).thenThrow(new IOException("Something happened to the network..."));

        managerReader.setSocket(socketConnectionFacade);
        managerReader.run();

        assertEquals(0, dispatcher.dispatchedResponses.size(), "must not dispatch a response");

        assertEquals(1, dispatcher.dispatchedEvents.size(), "not exactly one events dispatched");

        assertEquals(DisconnectEvent.class, dispatcher.dispatchedEvents.get(0).getClass(), "first event must be a DisconnectEvent");
    }

    private class MockedDispatcher implements Dispatcher {
        List<ManagerEvent> dispatchedEvents;
        List<ManagerActionResponse> dispatchedResponses;

        public MockedDispatcher() {
            this.dispatchedEvents = new ArrayList<ManagerEvent>();
            this.dispatchedResponses = new ArrayList<ManagerActionResponse>();
        }

        @Override
        public void dispatchResponse(ManagerActionResponse response, Integer requiredHandlingTime) {
            dispatchedResponses.add(response);
        }

        @Override
        public void dispatchEvent(ManagerEvent event, Integer requiredHandlingTime) {
            dispatchedEvents.add(event);
        }

        @Override
        public void stop() {
            // NO_OP
        }
    }
}
