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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.asteriskjava.manager.event.*;

public class EventBuilderImplTest extends TestCase
{
    private EventBuilder eventBuilder;

    @Override
   public void setUp()
    {
        this.eventBuilder = new EventBuilderImpl();
    }

    public void testRegisterEvent()
    {
        eventBuilder.registerEventClass(NewChannelEvent.class);
    }

    public void testRegisterUserEventWithA()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        eventBuilder.registerEventClass(A.class);
        properties.put("event", "UserEventA");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof A);
    }

    public void testRegisterUserEventWithBEvent()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        eventBuilder.registerEventClass(BEvent.class);
        properties.put("event", "UserEventB");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof BEvent);
    }

    public void testRegisterUserEventWithUserEventC()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventC.class);
        properties.put("event", "UserEventC");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof UserEventC);
    }

    public void testRegisterUserEventWithUserEventCAndAsterisk14()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventC.class);
        properties.put("event", "UserEvent");
        properties.put("userevent", "C");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof UserEventC);
    }

    public void testRegisterUserEventWithUserEventDEvent()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        eventBuilder.registerEventClass(UserEventDEvent.class);
        properties.put("event", "UserEventD");
        event = eventBuilder.buildEvent(this, properties);

        assertTrue("Wrong type", event instanceof UserEventDEvent);
    }

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

    public void testRegisterEventWithWrongClass()
    {
        try
        {
            eventBuilder.registerEventClass(String.class);
            fail("registerEvent() must only accept subclasses of ManagerEvent");
        }
        catch (IllegalArgumentException ex)
        {
        }
    }

    /*
     * public void testGetSetters() { Map setters; EventBuilderImpl eventBuilder =
     * getEventBuilder(); setters =
     * eventBuilder.getSetters(NewChannelEvent.class); assertTrue("Setter not
     * found", setters.containsKey("callerid")); }
     */

    public void testBuildEventWithMixedCaseSetter()
    {
        Map<String, String> properties = new HashMap<String, String>();
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

    public void testBuildEventWithIntegerProperty()
    {
        Map<String, String> properties = new HashMap<String, String>();
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

    public void testBuildEventWithBooleanProperty()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "True");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", ShutdownEvent.class, event.getClass());
        assertEquals("Boolean property not set correctly", Boolean.TRUE, event.getRestart());
    }

    public void testBuildEventWithBooleanPropertyOfValueYes()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "yes");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", ShutdownEvent.class, event.getClass());
        assertEquals("Boolean property not set correctly", Boolean.TRUE, event.getRestart());
    }

    public void testBuildEventWithBooleanPropertyOfValueNo()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ShutdownEvent event;

        eventBuilder.registerEventClass(ShutdownEvent.class);
        properties.put("event", "shutdown");
        properties.put("restart", "NO");
        event = (ShutdownEvent) eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", ShutdownEvent.class, event.getClass());
        assertEquals("Boolean property not set correctly", Boolean.FALSE, event.getRestart());
    }

    public void testBuildEventWithUnregisteredEvent()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        properties.put("event", "Nonexisting");
        event = eventBuilder.buildEvent(this, properties);

        assertNull(event);
    }

    public void testBuildEventWithEmptyAttributes()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        event = eventBuilder.buildEvent(this, properties);

        assertNull(event);
    }

    public void testBuildEventWithResponseEvent()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        properties.put("event", "StatusComplete");
        properties.put("actionid", "1234#origId");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Returned event is of wrong type", StatusCompleteEvent.class, event.getClass());
        assertEquals("ActionId not set correctly", "origId", ((ResponseEvent) event).getActionId());
    }

    public void testBuildEventWithSourceProperty()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        properties.put("event", "Cdr");
        properties.put("source", "source value");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Src property not set correctly", "source value", ((CdrEvent) event).getSrc());
    }

    public void testBuildEventWithSpecialCharacterProperty()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        properties.put("event", "Hangup");
        properties.put("cause-txt", "some text");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("CauseTxt property not set correctly", "some text", ((HangupEvent) event).getCauseTxt());
    }

    public void testBuildEventWithCidCallingPres()
    {
        Map<String, String> properties = new HashMap<String, String>();
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

    public void testBuildEventWithCidCallingPresAndEmptyTxt()
    {
        Map<String, String> properties = new HashMap<String, String>();
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

    public void testBuildEventWithCidCallingPresAndMissingTxt()
    {
        Map<String, String> properties = new HashMap<String, String>();
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

    public void testBuildEventWithInvalidCidCallingPres()
    {
        Map<String, String> properties = new HashMap<String, String>();
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

    public void testBuildEventWithReason()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        properties.put("event", "LogChannel");
        properties.put("reason", "123 - a reason");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Reason property not set correctly", Integer.valueOf(123),
                ((LogChannelEvent) event).getReason());
        assertEquals("ReasonTxt property not set correctly", "a reason", 
                ((LogChannelEvent) event).getReasonTxt());
    }

    public void testBuildEventWithTimestamp()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        properties.put("event", "NewChannel");
        properties.put("timestamp", "1159310429.569108");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Timestamp property not set correctly", 1159310429.569108D, event.getTimestamp());
    }

    public void testBuildEventWithLong()
    {
        Map<String, String> properties = new HashMap<String, String>();
        ManagerEvent event;

        properties.put("event", "MeetmeLeave");
        properties.put("duration", "569108");
        event = eventBuilder.buildEvent(this, properties);

        assertNotNull(event);
        assertEquals("Duration property not set correctly", new Long(569108),
                ((MeetMeLeaveEvent) event).getDuration());
    }
}
