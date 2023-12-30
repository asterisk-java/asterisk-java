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

import org.asteriskjava.ami.action.api.response.event.ResponseEvent;
import org.asteriskjava.ami.event.api.ManagerEvent;
import org.asteriskjava.manager.event.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventBuilderImplTest {
    private EventBuilder eventBuilder;
    private Map<String, Object> properties;

    @BeforeEach
    void setUp() {
        this.eventBuilder = new EventBuilderImpl();
        this.properties = new HashMap<String, Object>();
    }

    @Test
    void testRegisterEvent() {
        eventBuilder.registerEventClass(NewChannelEvent.class);
    }

    @Test
    void testRegisterUserEventWithA() {
        ManagerEvent event;

        eventBuilder.registerEventClass(A.class);
        properties.put("event", "UserEventA");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue(event instanceof A, "Wrong type");
    }

    @Test
    void testRegisterUserEventWithBEvent() {
        ManagerEvent event;

        eventBuilder.registerEventClass(BEvent.class);
        properties.put("event", "UserEventB");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue(event instanceof BEvent, "Wrong type");
    }

    @Test
    void testRegisterUserEventWithUserEventC() {
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventC.class);
        properties.put("event", "UserEventC");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue(event instanceof UserEventC, "Wrong type");
    }

    @Test
    void testRegisterUserEventWithUserEventCAndAsterisk14() {
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventC.class);
        properties.put("event", "UserEvent");
        properties.put("userevent", "C");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue(event instanceof UserEventC, "Wrong type");
    }

    @Test
    void testRegisterUserEventWithUserEventDEvent() {
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventDEvent.class);
        properties.put("event", "UserEventD");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue(event instanceof UserEventDEvent, "Wrong type");
    }

    @Test
    void testBuildEventWithOverrideSetter() {
        ManagerEvent event;

        eventBuilder.registerEventClass(EEvent.class);
        properties.put("event", "e");
        properties.put("line", "true");
        properties.put("exten", "1abz");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue(event instanceof EEvent, "Wrong type");
        assertEquals("1abz", event.getExten());
        assertEquals(Integer.valueOf(1), event.getLine());
        assertTrue(((EEvent) event).getLineAsBoolean());
    }

    @Test
    void testRegisterEventWithAbstractEvent() {
        try {
            eventBuilder.registerEventClass(AbstractChannelEvent.class);
            fail("registerEvent() must not accept abstract classes");
        } catch (IllegalArgumentException ex) {
        }
    }

    /*
     * void testGetSetters() { Map setters; EventBuilderImpl eventBuilder
     * = getEventBuilder(); setters =
     * eventBuilder.getSetters(NewChannelEvent.class); assertTrue("Setter not
     * found", setters.containsKey("callerid")); }
     */

    @Test
    void testBuildEventWithMixedCaseSetter() {
        String callerid = "1234";
        NewChannelEvent event;

        properties.put("event", "Newchannel");
        properties.put("callerid", callerid);
        event = (NewChannelEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(NewChannelEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals(callerid, event.getCallerIdNum(), "String property not set correctly");
//        assertEquals(this, event.getSource(), "Source not set correctly");
    }

    @Test
    void testBuildEventWithIntegerProperty() {
        String channel = "SIP/1234";
        Integer priority = 1;
        NewExtenEvent event;

        properties.put("event", "newexten");
        properties.put("channel", channel);
        properties.put("priority", priority.toString());
        event = (NewExtenEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(NewExtenEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals(channel, event.getChannel(), "String property not set correctly");
        assertEquals(priority, event.getPriority(), "Integer property not set correctly");
    }

    @Test
    void testBuildEventWithBooleanProperty() {
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "True");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(ShutdownEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals(Boolean.TRUE, event.getRestart(), "Boolean property not set correctly");
    }

    @Test
    void testBuildEventWithBooleanPropertyOfValueYes() {
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "yes");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(ShutdownEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals(Boolean.TRUE, event.getRestart(), "Boolean property not set correctly");
    }

    @Test
    void testBuildEventWithBooleanPropertyOfValueNo() {
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "NO");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(ShutdownEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals(Boolean.FALSE, event.getRestart(), "Boolean property not set correctly");
    }

    @Test
    void testBuildEventWithUnregisteredEvent() {
        ManagerEvent event;

        properties.put("event", "Nonexisting");
        event = eventBuilder.buildEvent(this, properties);

        assertNull(event);
    }

    @Test
    void testBuildEventWithEmptyAttributes() {
        ManagerEvent event;

        event = eventBuilder.buildEvent(this, properties);

        assertNull(event);
    }

    @Test
    void testBuildEventWithResponseEvent() {
        ManagerEvent event;

        properties.put("event", "StatusComplete");
        properties.put("actionid", "1234#origId");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(StatusCompleteEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("origId", ((ResponseEvent) event).getActionId(), "ActionId not set correctly");
    }

    @Test
    void testBuildEventWithSourceProperty() {
        ManagerEvent event;

        properties.put("event", "Cdr");
        properties.put("source", "source value");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("source value", ((CdrEvent) event).getSrc(), "Src property not set correctly");
    }

    @Test
    void testBuildEventWithClassProperty() {
        ManagerEvent event;

        properties.put("event", "MusicOnHold");
        properties.put("class", "default");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("default", ((MusicOnHoldEvent) event).getClassName(), "ClassName property not set correctly");
    }

    @Test
    void testBuildEventWithSpecialCharacterProperty() {
        ManagerEvent event;

        properties.put("event", "Hangup");
        properties.put("cause-txt", "some text");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("some text", ((HangupEvent) event).getCauseTxt(), "CauseTxt property not set correctly");
    }

    @Test
    void testBuildEventWithCidCallingPres() {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "123 (nice description)");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(Integer.valueOf(123), ((NewCallerIdEvent) event).getCidCallingPres(), "CidCallingPres property not set correctly");
        assertEquals("nice description", ((NewCallerIdEvent) event).getCidCallingPresTxt(), "CidCallingPresTxt property not set correctly");
    }

    @Test
    void testBuildEventWithCidCallingPresAndEmptyTxt() {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "123 ()");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(Integer.valueOf(123), ((NewCallerIdEvent) event).getCidCallingPres(), "CidCallingPres property not set correctly");
        assertNull(((NewCallerIdEvent) event).getCidCallingPresTxt(), "CidCallingPresTxt property not set correctly (must be null)");
    }

    @Test
    void testBuildEventWithCidCallingPresAndMissingTxt() {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "123");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(Integer.valueOf(123), ((NewCallerIdEvent) event).getCidCallingPres(), "CidCallingPres property not set correctly");
        assertNull(((NewCallerIdEvent) event).getCidCallingPresTxt(), "CidCallingPresTxt property not set correctly (must be null)");
    }

    @Test
    void testBuildEventWithInvalidCidCallingPres() {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "abc");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertNull(((NewCallerIdEvent) event).getCidCallingPres(), "CidCallingPres property not set correctly (must be null)");
        assertNull(((NewCallerIdEvent) event).getCidCallingPresTxt(), "CidCallingPresTxt property not set correctly (must be null)");
    }

    @Test
    void testBuildEventWithReason() {
        ManagerEvent event;

        properties.put("event", "LogChannel");
        properties.put("reason", "123 - a reason");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(Integer.valueOf(123), ((LogChannelEvent) event).getReason(), "Reason property not set correctly");
        assertEquals("a reason", ((LogChannelEvent) event).getReasonTxt(), "ReasonTxt property not set correctly");
    }

    @Test
    void testBuildEventWithTimestamp() {
        ManagerEvent event;

        properties.put("event", "NewChannel");
        properties.put("timestamp", "1159310429.569108");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(1159310429.569108D, event.getTimestamp(), 0.0001, "Timestamp property not set correctly");
    }

    @Test
    void testBuildEventWithLong() {
        ManagerEvent event;

        properties.put("event", "MeetmeLeave");
        properties.put("duration", "569108");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(new Long(569108), ((MeetMeLeaveEvent) event).getDuration(), "Duration property not set correctly");
    }

    @Test
    void testBuildEventWithDouble() {
        ManagerEvent event;

        properties.put("event", "RTPReceiverStat");
        properties.put("transit", "12.3456");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(12.3456, ((RtpReceiverStatEvent) event).getTransit(), 0.0001, "Transit property not set correctly");
    }

    @Test
    void testBuildEventWithNullLiteral() {
        CdrEvent event;

        properties.put("event", "Cdr");
        properties.put("channel", "<none>");
        event = (CdrEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(CdrEvent.class, event.getClass(), "Returned event is of wrong type");
        assertNull(event.getChannel(), "Property with value \"<none>\" is not null");
    }

    @Test
    void testBuildEventWithDashInPropertyName() {
        TransferEvent event;

        properties.put("event", "Transfer");
        properties.put("sip-callid", "12345");
        event = (TransferEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(TransferEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("12345", event.getSipCallId(), "Property SIP-Callid is not set correctly");
    }

    @Test
    void testBuildEventForRtpReceiverStatEventAJ162() {
        RtpReceiverStatEvent event;

        properties.put("event", "RtpReceiverStat");
        properties.put("ssrc", "3776236237");
        properties.put("receivedpackets", "0");
        event = (RtpReceiverStatEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(RtpReceiverStatEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals(3776236237l, (long) event.getSsrc(), "Property SSRC is not set correctly");
    }

    @Test
    void testBuildEventForRtpReceiverStatEventAJ139() {
        RtpReceiverStatEvent event;

        properties.put("event", "RtpReceiverStat");
        properties.put("receivedpackets", "0");
        event = (RtpReceiverStatEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(RtpReceiverStatEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals(0, (long) event.getReceivedPackets(), "Property receivedPacket is not set correctly");
    }

    @Test
    void testBuildEventWithMapProperty() {
        AgentCalledEvent event;

        properties.put("event", "AgentCalled");
        properties.put("variable", Arrays.asList("var1=val1", "var2=val2"));
        event = (AgentCalledEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(AgentCalledEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("val1", event.getVariables().get("var1"), "Property variables[var1] is not set correctly");
        assertEquals("val2", event.getVariables().get("var2"), "Property variables[var2] is not set correctly");
        assertEquals(2, event.getVariables().size(), "Invalid size of variables property");
    }

    @Test
    void testBuildEventWithMapPropertyAndOnlyOneEntry() {
        AgentCalledEvent event;

        properties.put("event", "AgentCalled");
        properties.put("variable", "var1=val1");
        event = (AgentCalledEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(AgentCalledEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("val1", event.getVariables().get("var1"), "Property variables[var1] is not set correctly");
        assertEquals(1, event.getVariables().size(), "Invalid size of variables property");
    }

    @Test
    void testBuildEventWithSpace() {
        T38FaxStatusEvent event;

        properties.put("event", "T38FaxStatus");
        properties.put("t38 session duration", "120");
        event = (T38FaxStatusEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(T38FaxStatusEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("120", event.getT38SessionDuration(), "Property 'T38 Session Duration' is not set correctly");
    }

    @Test
    void testBuildEventWithPeerEntryEventList() {
        PeersEvent event;

        properties.put("event", Arrays.asList("PeerEntry", "PeerEntry", "PeerEntry", "PeerlistComplete"));
        properties.put("actionid", Arrays.asList("1378144905_4#123", "1378144905_4#123", "1378144905_4#123"));
        properties.put("objectname", Arrays.asList("a101", "a102", "a103"));
        properties.put("status", Arrays.asList("OK", "UNKNOWN", "LAGGED"));
        properties.put("listitems", "3");
        event = (PeersEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(PeersEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("123", event.getActionId(), "ActionId is invalid");
        assertEquals("a101", event.getChildEvents().get(0).getObjectName(), "Property events[objectname] is not set correctly");
        assertEquals("OK", event.getChildEvents().get(0).getStatus(), "Property events[status] is not set correctly");
        assertEquals("a102", event.getChildEvents().get(1).getObjectName(), "Property events[objectname] is not set correctly");
        assertNull(event.getChildEvents().get(1).getStatus(), "UNKNOWN literal returns NULL status");
        assertEquals("a103", event.getChildEvents().get(2).getObjectName(), "Property events[objectname] is not set correctly");
        assertEquals("LAGGED", event.getChildEvents().get(2).getStatus(), "Property events[status] is not set correctly");
        assertEquals(3, event.getChildEvents().size(), "Invalid size of peers property");
    }

    @Test
    void testBuildEventWithOnePeerEntryEventList() {
        PeersEvent event;

        properties.put("event", Arrays.asList("PeerEntry", "PeerlistComplete"));
        properties.put("actionid", "1378144905_4#123");
        properties.put("objectname", "a101");
        properties.put("status", "OK");
        properties.put("listitems", "1");
        event = (PeersEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals(PeersEvent.class, event.getClass(), "Returned event is of wrong type");
        assertEquals("123", event.getActionId(), "ActionId is invalid");
        assertEquals("a101", event.getChildEvents().get(0).getObjectName(), "Property events[objectname] is not set correctly");
        assertEquals("OK", event.getChildEvents().get(0).getStatus(), "Property events[status] is not set correctly");
        assertEquals(1, event.getChildEvents().size(), "Invalid size of peers property");
    }
}
