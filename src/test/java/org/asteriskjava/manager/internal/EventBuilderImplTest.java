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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.AbstractChannelEvent;
import org.asteriskjava.manager.event.AgentCalledEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.LogChannelEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MeetMeLeaveEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.PeersEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.RtpReceiverStatEvent;
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.T38FaxStatusEvent;
import org.asteriskjava.manager.event.TransferEvent;
import org.junit.Before;
import org.junit.Test;

public class EventBuilderImplTest
{
    private EventBuilder eventBuilder;
    private Map<String, Object> properties;

    @Before
    public void setUp()
    {
        this.eventBuilder = new EventBuilderImpl();
        this.properties = new HashMap<String, Object>();
    }

    @Test
    public void testRegisterEvent()
    {
        eventBuilder.registerEventClass(NewChannelEvent.class);
    }

    @Test
    public void testRegisterUserEventWithA()
    {
        ManagerEvent event;

        eventBuilder.registerEventClass(A.class);
        properties.put("event", "UserEventA");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof A);
    }

    @Test
    public void testRegisterUserEventWithBEvent()
    {
        ManagerEvent event;

        eventBuilder.registerEventClass(BEvent.class);
        properties.put("event", "UserEventB");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof BEvent);
    }

    @Test
    public void testRegisterUserEventWithUserEventC()
    {
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventC.class);
        properties.put("event", "UserEventC");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof UserEventC);
    }

    @Test
    public void testRegisterUserEventWithUserEventCAndAsterisk14()
    {
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventC.class);
        properties.put("event", "UserEvent");
        properties.put("userevent", "C");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof UserEventC);
    }

    @Test
    public void testRegisterUserEventWithUserEventDEvent()
    {
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventDEvent.class);
        properties.put("event", "UserEventD");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof UserEventDEvent);
    }

    @Test
    public void testRegisterEventWithAbstractEvent()
    {
        try
        {
            eventBuilder.registerEventClass(AbstractChannelEvent.class);
            fail("registerEvent() must not accept abstract classes");
        }
        catch (IllegalArgumentException ex)
        {
        }
    }

    /*
     * public void testGetSetters() { Map setters; EventBuilderImpl eventBuilder
     * = getEventBuilder(); setters =
     * eventBuilder.getSetters(NewChannelEvent.class); assertTrue("Setter not
     * found", setters.containsKey("callerid")); }
     */

    @Test
    public void testBuildEventWithMixedCaseSetter()
    {
        String callerid = "1234";
        NewChannelEvent event;

        properties.put("event", "Newchannel");
        properties.put("callerid", callerid);
        event = (NewChannelEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", NewChannelEvent.class, event.getClass());
        assertEquals("String property not set correctly", callerid, event.getCallerIdNum());
        assertEquals("Source not set correctly", this, event.getSource());
    }

    @Test
    public void testBuildEventWithIntegerProperty()
    {
        String channel = "SIP/1234";
        Integer priority = 1;
        NewExtenEvent event;

        properties.put("event", "newexten");
        properties.put("channel", channel);
        properties.put("priority", priority.toString());
        event = (NewExtenEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", NewExtenEvent.class, event.getClass());
        assertEquals("String property not set correctly", channel, event.getChannel());
        assertEquals("Integer property not set correctly", priority, event.getPriority());
    }

    @Test
    public void testBuildEventWithBooleanProperty()
    {
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "True");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", ShutdownEvent.class, event.getClass());
        assertEquals("Boolean property not set correctly", Boolean.TRUE, event.getRestart());
    }

    @Test
    public void testBuildEventWithBooleanPropertyOfValueYes()
    {
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "yes");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", ShutdownEvent.class, event.getClass());
        assertEquals("Boolean property not set correctly", Boolean.TRUE, event.getRestart());
    }

    @Test
    public void testBuildEventWithBooleanPropertyOfValueNo()
    {
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "NO");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", ShutdownEvent.class, event.getClass());
        assertEquals("Boolean property not set correctly", Boolean.FALSE, event.getRestart());
    }

    @Test
    public void testBuildEventWithUnregisteredEvent()
    {
        ManagerEvent event;

        properties.put("event", "Nonexisting");
        event = eventBuilder.buildEvent(this, properties);

        assertNull(event);
    }

    @Test
    public void testBuildEventWithEmptyAttributes()
    {
        ManagerEvent event;

        event = eventBuilder.buildEvent(this, properties);

        assertNull(event);
    }

    @Test
    public void testBuildEventWithResponseEvent()
    {
        ManagerEvent event;

        properties.put("event", "StatusComplete");
        properties.put("actionid", "1234#origId");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", StatusCompleteEvent.class, event.getClass());
        assertEquals("ActionId not set correctly", "origId", ((ResponseEvent) event).getActionId());
    }

    @Test
    public void testBuildEventWithSourceProperty()
    {
        ManagerEvent event;

        properties.put("event", "Cdr");
        properties.put("source", "source value");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Src property not set correctly", "source value", ((CdrEvent) event).getSrc());
    }

    @Test
    public void testBuildEventWithClassProperty()
    {
        ManagerEvent event;

        properties.put("event", "MusicOnHold");
        properties.put("class", "default");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("ClassName property not set correctly", "default", ((MusicOnHoldEvent) event).getClassName());
    }

    @Test
    public void testBuildEventWithSpecialCharacterProperty()
    {
        ManagerEvent event;

        properties.put("event", "Hangup");
        properties.put("cause-txt", "some text");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("CauseTxt property not set correctly", "some text", ((HangupEvent) event).getCauseTxt());
    }

    @Test
    public void testBuildEventWithCidCallingPres()
    {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "123 (nice description)");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("CidCallingPres property not set correctly", Integer.valueOf(123),
                ((NewCallerIdEvent) event).getCidCallingPres());
        assertEquals("CidCallingPresTxt property not set correctly", "nice description",
                ((NewCallerIdEvent) event).getCidCallingPresTxt());
    }

    @Test
    public void testBuildEventWithCidCallingPresAndEmptyTxt()
    {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "123 ()");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("CidCallingPres property not set correctly", Integer.valueOf(123),
                ((NewCallerIdEvent) event).getCidCallingPres());
        assertNull("CidCallingPresTxt property not set correctly (must be null)",
                ((NewCallerIdEvent) event).getCidCallingPresTxt());
    }

    @Test
    public void testBuildEventWithCidCallingPresAndMissingTxt()
    {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "123");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("CidCallingPres property not set correctly", Integer.valueOf(123),
                ((NewCallerIdEvent) event).getCidCallingPres());
        assertNull("CidCallingPresTxt property not set correctly (must be null)",
                ((NewCallerIdEvent) event).getCidCallingPresTxt());
    }

    @Test
    public void testBuildEventWithInvalidCidCallingPres()
    {
        ManagerEvent event;

        properties.put("event", "Newcallerid");
        properties.put("cid-callingpres", "abc");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertNull("CidCallingPres property not set correctly (must be null)",
                ((NewCallerIdEvent) event).getCidCallingPres());
        assertNull("CidCallingPresTxt property not set correctly (must be null)",
                ((NewCallerIdEvent) event).getCidCallingPresTxt());
    }

    @Test
    public void testBuildEventWithReason()
    {
        ManagerEvent event;

        properties.put("event", "LogChannel");
        properties.put("reason", "123 - a reason");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Reason property not set correctly", Integer.valueOf(123), ((LogChannelEvent) event).getReason());
        assertEquals("ReasonTxt property not set correctly", "a reason", ((LogChannelEvent) event).getReasonTxt());
    }

    @Test
    public void testBuildEventWithTimestamp()
    {
        ManagerEvent event;

        properties.put("event", "NewChannel");
        properties.put("timestamp", "1159310429.569108");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Timestamp property not set correctly", 1159310429.569108D, event.getTimestamp(), 0.0001);
    }

    @Test
    public void testBuildEventWithLong()
    {
        ManagerEvent event;

        properties.put("event", "MeetmeLeave");
        properties.put("duration", "569108");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Duration property not set correctly", new Long(569108), ((MeetMeLeaveEvent) event).getDuration());
    }

    @Test
    public void testBuildEventWithDouble()
    {
        ManagerEvent event;

        properties.put("event", "RTPReceiverStat");
        properties.put("transit", "12.3456");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Transit property not set correctly", 12.3456, ((RtpReceiverStatEvent) event).getTransit(), 0.0001);
    }

    @Test
    public void testBuildEventWithNullLiteral()
    {
        CdrEvent event;

        properties.put("event", "Cdr");
        properties.put("channel", "<none>");
        event = (CdrEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", CdrEvent.class, event.getClass());
        assertNull("Property with value \"<none>\" is not null", event.getChannel());
    }

    @Test
    public void testBuildEventWithDashInPropertyName()
    {
        TransferEvent event;

        properties.put("event", "Transfer");
        properties.put("sip-callid", "12345");
        event = (TransferEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", TransferEvent.class, event.getClass());
        assertEquals("Property SIP-Callid is not set correctly", "12345", event.getSipCallId());
    }

    @Test
    public void testBuildEventForRtpReceiverStatEventAJ162()
    {
        RtpReceiverStatEvent event;

        properties.put("event", "RtpReceiverStat");
        properties.put("ssrc", "3776236237");
        properties.put("receivedpackets", "0");
        event = (RtpReceiverStatEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", RtpReceiverStatEvent.class, event.getClass());
        assertEquals("Property SSRC is not set correctly", 3776236237l, (long) event.getSsrc());
    }

    @Test
    public void testBuildEventForRtpReceiverStatEventAJ139()
    {
        RtpReceiverStatEvent event;

        properties.put("event", "RtpReceiverStat");
        properties.put("receivedpackets", "0");
        event = (RtpReceiverStatEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", RtpReceiverStatEvent.class, event.getClass());
        assertEquals("Property receivedPacket is not set correctly", 0, (long) event.getReceivedPackets());
    }

    @Test
    public void testBuildEventWithMapProperty()
    {
        AgentCalledEvent event;

        properties.put("event", "AgentCalled");
        properties.put("variable", Arrays.asList("var1=val1", "var2=val2"));
        event = (AgentCalledEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", AgentCalledEvent.class, event.getClass());
        assertEquals("Property variables[var1] is not set correctly", "val1", event.getVariables().get("var1"));
        assertEquals("Property variables[var2] is not set correctly", "val2", event.getVariables().get("var2"));
        assertEquals("Invalid size of variables property", 2, event.getVariables().size());
    }

    @Test
    public void testBuildEventWithMapPropertyAndOnlyOneEntry()
    {
        AgentCalledEvent event;

        properties.put("event", "AgentCalled");
        properties.put("variable", "var1=val1");
        event = (AgentCalledEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", AgentCalledEvent.class, event.getClass());
        assertEquals("Property variables[var1] is not set correctly", "val1", event.getVariables().get("var1"));
        assertEquals("Invalid size of variables property", 1, event.getVariables().size());
    }

    @Test
    public void testBuildEventWithSpace()
    {
        T38FaxStatusEvent event;

        properties.put("event", "T38FaxStatus");
        properties.put("t38 session duration", "120");
        event = (T38FaxStatusEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", T38FaxStatusEvent.class, event.getClass());
        assertEquals("Property 'T38 Session Duration' is not set correctly", "120", event.getT38SessionDuration());
    }

    @Test
    public void testBuildEventWithPeerEntryEventList()
    {
        PeersEvent event;

        properties.put("event", Arrays.asList("PeerEntry", "PeerEntry", "PeerEntry", "PeerlistComplete"));
        properties.put("actionid", Arrays.asList("1378144905_4#123", "1378144905_4#123", "1378144905_4#123"));
        properties.put("objectname", Arrays.asList("a101", "a102", "a103"));
        properties.put("status", Arrays.asList("OK", "UNKNOWN", "LAGGED"));
        properties.put("listitems", "3");
        event = (PeersEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", PeersEvent.class, event.getClass());
        assertEquals("ActionId is invalid", "123", event.getActionId());
        assertEquals("Property events[objectname] is not set correctly", "a101",
                event.getChildEvents().get(0).getObjectName());
        assertEquals("Property events[status] is not set correctly", "OK", event.getChildEvents().get(0).getStatus());
        assertEquals("Property events[objectname] is not set correctly", "a102",
                event.getChildEvents().get(1).getObjectName());
        assertNull("UNKNOWN literal returns NULL status", event.getChildEvents().get(1).getStatus());
        assertEquals("Property events[objectname] is not set correctly", "a103",
                event.getChildEvents().get(2).getObjectName());
        assertEquals("Property events[status] is not set correctly", "LAGGED", event.getChildEvents().get(2).getStatus());
        assertEquals("Invalid size of peers property", 3, event.getChildEvents().size());
    }

    @Test
    public void testBuildEventWithOnePeerEntryEventList()
    {
        PeersEvent event;

        properties.put("event", Arrays.asList("PeerEntry", "PeerlistComplete"));
        properties.put("actionid", "1378144905_4#123");
        properties.put("objectname", "a101");
        properties.put("status", "OK");
        properties.put("listitems", "1");
        event = (PeersEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", PeersEvent.class, event.getClass());
        assertEquals("ActionId is invalid", "123", event.getActionId());
        assertEquals("Property events[objectname] is not set correctly", "a101",
                event.getChildEvents().get(0).getObjectName());
        assertEquals("Property events[status] is not set correctly", "OK", event.getChildEvents().get(0).getStatus());
        assertEquals("Invalid size of peers property", 1, event.getChildEvents().size());
    }
}
