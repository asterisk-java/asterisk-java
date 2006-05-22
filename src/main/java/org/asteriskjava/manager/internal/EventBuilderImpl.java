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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.AgentCallbackLoginEvent;
import org.asteriskjava.manager.event.AgentCallbackLogoffEvent;
import org.asteriskjava.manager.event.AgentCalledEvent;
import org.asteriskjava.manager.event.AgentCompleteEvent;
import org.asteriskjava.manager.event.AgentConnectEvent;
import org.asteriskjava.manager.event.AgentDumpEvent;
import org.asteriskjava.manager.event.AgentLoginEvent;
import org.asteriskjava.manager.event.AgentLogoffEvent;
import org.asteriskjava.manager.event.AgentsCompleteEvent;
import org.asteriskjava.manager.event.AgentsEvent;
import org.asteriskjava.manager.event.AlarmClearEvent;
import org.asteriskjava.manager.event.AlarmEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.DbGetResponseEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DndStateEvent;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import org.asteriskjava.manager.event.FaxReceivedEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.HoldedCallEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.LogChannelEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MeetMeJoinEvent;
import org.asteriskjava.manager.event.MeetMeLeaveEvent;
import org.asteriskjava.manager.event.MeetMeMuteEvent;
import org.asteriskjava.manager.event.MeetMeStopTalkingEvent;
import org.asteriskjava.manager.event.MeetMeTalkingEvent;
import org.asteriskjava.manager.event.MessageWaitingEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateFailureEvent;
import org.asteriskjava.manager.event.OriginateSuccessEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;
import org.asteriskjava.manager.event.ParkedCallGiveUpEvent;
import org.asteriskjava.manager.event.ParkedCallTimeOutEvent;
import org.asteriskjava.manager.event.ParkedCallsCompleteEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.PeerStatusEvent;
import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.QueueStatusCompleteEvent;
import org.asteriskjava.manager.event.RegistryEvent;
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnholdEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.event.UnparkedCallEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.event.ZapShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.ZapShowChannelsEvent;
import org.asteriskjava.util.AstUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.ReflectionUtil;


/**
 * Default implementation of the EventBuilder interface.
 * 
 * @see org.asteriskjava.manager.event.ManagerEvent
 * @author srt
 * @version $Id$
 */
class EventBuilderImpl implements EventBuilder
{
    private final Log logger = LogFactory.getLog(getClass());
    private Map<String, Class> registeredEventClasses;

    EventBuilderImpl()
    {
        this.registeredEventClasses = new HashMap<String, Class>();
        registerBuiltinEventClasses();
    }

    private void registerBuiltinEventClasses()
    {
        registerEventClass(AgentCallbackLoginEvent.class);
        registerEventClass(AgentCallbackLogoffEvent.class);
        registerEventClass(AgentCalledEvent.class);
        registerEventClass(AgentConnectEvent.class);
        registerEventClass(AgentCompleteEvent.class);
        registerEventClass(AgentDumpEvent.class);
        registerEventClass(AgentLoginEvent.class);
        registerEventClass(AgentLogoffEvent.class);
        registerEventClass(AgentsEvent.class);
        registerEventClass(AgentsCompleteEvent.class);
        registerEventClass(AlarmEvent.class);
        registerEventClass(AlarmClearEvent.class);
        registerEventClass(CdrEvent.class);
        registerEventClass(DbGetResponseEvent.class);
        registerEventClass(DialEvent.class);
        registerEventClass(DndStateEvent.class);
        registerEventClass(ExtensionStatusEvent.class);
        registerEventClass(FaxReceivedEvent.class);
        registerEventClass(HangupEvent.class);
        registerEventClass(HoldedCallEvent.class);
        registerEventClass(HoldEvent.class);
        registerEventClass(JoinEvent.class);
        registerEventClass(LeaveEvent.class);
        registerEventClass(LinkEvent.class);
        registerEventClass(LogChannelEvent.class);
        registerEventClass(MeetMeJoinEvent.class);
        registerEventClass(MeetMeLeaveEvent.class);
        registerEventClass(MeetMeMuteEvent.class);
        registerEventClass(MeetMeTalkingEvent.class);
        registerEventClass(MeetMeStopTalkingEvent.class);
        registerEventClass(MessageWaitingEvent.class);
        registerEventClass(NewCallerIdEvent.class);
        registerEventClass(NewChannelEvent.class);
        registerEventClass(NewExtenEvent.class);
        registerEventClass(NewStateEvent.class);
        registerEventClass(OriginateFailureEvent.class);
        registerEventClass(OriginateSuccessEvent.class);
        registerEventClass(ParkedCallGiveUpEvent.class);
        registerEventClass(ParkedCallEvent.class);
        registerEventClass(ParkedCallTimeOutEvent.class);
        registerEventClass(ParkedCallsCompleteEvent.class);
        registerEventClass(PeerEntryEvent.class);
        registerEventClass(PeerlistCompleteEvent.class);
        registerEventClass(PeerStatusEvent.class);
        registerEventClass(QueueEntryEvent.class);
        registerEventClass(QueueMemberAddedEvent.class);
        registerEventClass(QueueMemberEvent.class);
        registerEventClass(QueueMemberPausedEvent.class);
        registerEventClass(QueueMemberRemovedEvent.class);
        registerEventClass(QueueMemberStatusEvent.class);
        registerEventClass(QueueParamsEvent.class);
        registerEventClass(QueueStatusCompleteEvent.class);
        registerEventClass(RegistryEvent.class);
        registerEventClass(ReloadEvent.class);
        registerEventClass(RenameEvent.class);
        registerEventClass(ShutdownEvent.class);
        registerEventClass(StatusEvent.class);
        registerEventClass(StatusCompleteEvent.class);
        registerEventClass(UnholdEvent.class);
        registerEventClass(UnlinkEvent.class);
        registerEventClass(UnparkedCallEvent.class);
        registerEventClass(ZapShowChannelsEvent.class);
        registerEventClass(ZapShowChannelsCompleteEvent.class);
    }

    public void registerEventClass(Class clazz)
    {
        String className;
        String eventType;

        className = clazz.getName();
        eventType = className.substring(className.lastIndexOf('.') + 1)
                .toLowerCase();

        if (eventType.endsWith("event"))
        {
            eventType = eventType.substring(0, eventType.length()
                    - "event".length());
        }

        if (UserEvent.class.isAssignableFrom(clazz)
                && !eventType.startsWith("userevent"))
        {
            eventType = "userevent" + eventType;
        }

        registerEventClass(eventType, clazz);
    }

    /**
     * Registers a new event class for the event given by eventType.
     * 
     * @param eventType the name of the event to register the class for. For
     *            example "Join".
     * @param clazz the event class to register, must extend
     *            net.sf.asterisk.manager.event.Event.
     */
    public void registerEventClass(String eventType, Class clazz)
    {
        Constructor defaultConstructor;

        if (!ManagerEvent.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(clazz + " is not a ManagerEvent");
        }

        if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0)
        {
            throw new IllegalArgumentException(clazz + " is abstract");
        }

        try
        {
            defaultConstructor = clazz
                    .getConstructor(new Class[]{Object.class});
        }
        catch (NoSuchMethodException ex)
        {
            throw new IllegalArgumentException(clazz
                    + " has no usable constructor");
        }

        if ((defaultConstructor.getModifiers() & Modifier.PUBLIC) == 0)
        {
            throw new IllegalArgumentException(clazz
                    + " has no public default constructor");
        }

        registeredEventClasses.put(eventType.toLowerCase(), clazz);

        logger.debug("Registered event type '" + eventType + "' (" + clazz
                + ")");
    }

    public ManagerEvent buildEvent(Object source, Map<String, String> attributes)
    {
        ManagerEvent event;
        String eventType;
        Class eventClass;
        Constructor constructor;

        if (attributes.get("event") == null)
        {
            logger.error("No event event type in properties");
            return null;
        }

        eventType = ((String) attributes.get("event")).toLowerCase();
        eventClass = (Class) registeredEventClasses.get(eventType);
        if (eventClass == null)
        {
            logger.info("No event class registered for event type '"
                    + eventType + "', attributes: " + attributes);
            return null;
        }

        try
        {
            constructor = eventClass.getConstructor(new Class[]{Object.class});
        }
        catch (NoSuchMethodException ex)
        {
            logger.error("Unable to get constructor of " + eventClass, ex);
            return null;
        }

        try
        {
            event = (ManagerEvent) constructor
                    .newInstance(new Object[]{source});
        }
        catch (Exception ex)
        {
            logger.error("Unable to create new instance of " + eventClass, ex);
            return null;
        }

        setAttributes(event, attributes);

        // ResponseEvents are sent in response to a ManagerAction if the
        // response contains lots of data. They include the actionId of
        // the corresponding ManagerAction.
        if (event instanceof ResponseEvent)
        {
            ResponseEvent responseEvent;
            String actionId;

            responseEvent = (ResponseEvent) event;
            actionId = responseEvent.getActionId();
            if (actionId != null)
            {
                responseEvent.setActionId(ManagerUtil.stripInternalActionId(actionId));
                responseEvent.setInternalActionId(ManagerUtil
                        .getInternalActionId(actionId));
            }
        }

        return event;
    }

    @SuppressWarnings("unchecked")
    private void setAttributes(ManagerEvent event, Map<String, String> attributes)
    {
        Map<String, Method> setters;

        setters = ReflectionUtil.getSetters(event.getClass());
        for (String name : attributes.keySet())
        {
            Object value;
            Class dataType;
            Method setter;

            if ("event".equals(name))
            {
                continue;
            }

            /*
             * The source property needs special handling as it is already
             * defined in java.util.EventObject (the base class of
             * ManagerEvent), so we have to translate it.
             */
            if ("source".equals(name))
            {
                setter = setters.get("src");
            }
            else
            {
                setter = setters.get(stripIllegalCharacters(name));
            }

            if (setter == null)
            {
                logger.error("Unable to set property '" + name + "' on "
                        + event.getClass() + ": no setter");
                continue;
            }

            dataType = setter.getParameterTypes()[0];

            if (dataType == Boolean.class)
            {
                value = new Boolean(AstUtil.isTrue((String) attributes
                        .get(name)));
            }
            else if (dataType.isAssignableFrom(String.class))
            {
                value = attributes.get(name);
            }
            else
            {
                try
                {
                    Constructor constructor = dataType
                            .getConstructor(new Class[]{String.class});
                    value = constructor.newInstance(new Object[]{attributes
                            .get(name)});
                }
                catch (Exception e)
                {
                    logger.error("Unable to convert value '"
                            + attributes.get(name) + "' of property '" + name
                            + "' on " + event.getClass() + " to required type "
                            + dataType, e);
                    continue;
                }
            }

            try
            {
                setter.invoke(event, new Object[]{value});
            }
            catch (Exception e)
            {
                logger.error("Unable to set property '" + name + "' on "
                        + event.getClass(), e);
                continue;
            }
        }
    }

    /**
     * Strips all illegal charaters from the given lower case string.
     * 
     * @param s the original string
     * @return the string with all illegal characters stripped
     */
    private String stripIllegalCharacters(String s)
    {
        char c;
        boolean needsStrip = false;
        StringBuffer sb;

        if (s == null)
        {
            return null;
        }

        for (int i = 0; i < s.length(); i++)
        {
            c = s.charAt(i);
            if (c >= '0' && c <= '9')
            {
                continue;
            }
            else if (c >= 'a' && c <= 'z')
            {
                continue;
            }
            else
            {
                needsStrip = true;
                break;
            }
        }

        if (!needsStrip)
        {
            return s;
        }

        sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++)
        {
            c = s.charAt(i);
            if (c >= '0' && c <= '9')
            {
                sb.append(c);
            }
            else if (c >= 'a' && c <= 'z')
            {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
