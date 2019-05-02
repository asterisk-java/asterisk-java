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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.PeersEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.ReflectionUtil;

/**
 * Default implementation of the EventBuilder interface.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.event.ManagerEvent
 */
class EventBuilderImpl extends AbstractBuilder implements EventBuilder
{
    private static final Set<String> ignoredAttributes = new HashSet<>(Arrays.asList("event"));
    private Map<String, Class< ? >> registeredEventClasses;
    private final Set<String> eventClassNegativeCache = new HashSet<>();

    private static final Log logger = LogFactory.getLog(EventBuilderImpl.class);

    private final static Set<Class<ManagerEvent>> knownManagerEventClasses = ReflectionUtil
            .loadClasses("org.asteriskjava.manager.event", ManagerEvent.class);

    EventBuilderImpl()
    {
        this.registeredEventClasses = new HashMap<>();
        registerBuiltinEventClasses();
    }

    /**
     * register the known ManagerEventClasses for this builder
     */
    private void registerBuiltinEventClasses()
    {
        for (Class< ? extends ManagerEvent> managerEventClass : knownManagerEventClasses)
        {
            registerEventClass(managerEventClass);
        }
    }

    public final void registerEventClass(Class< ? extends ManagerEvent> clazz) throws IllegalArgumentException
    {
        String className;
        String eventType;

        className = clazz.getName();
        eventType = className.substring(className.lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH);

        if (eventType.endsWith("event"))
        {
            eventType = eventType.substring(0, eventType.length() - "event".length());
        }

        if (UserEvent.class.isAssignableFrom(clazz) && !eventType.startsWith("userevent"))
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
     *            {@link ManagerEvent}.
     * @throws IllegalArgumentException if clazz is not a valid event class.
     */
    public final void registerEventClass(String eventType, Class< ? extends ManagerEvent> clazz)
            throws IllegalArgumentException
    {
        Constructor< ? > defaultConstructor;

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
            defaultConstructor = clazz.getConstructor(Object.class);
        }
        catch (NoSuchMethodException ex)
        {
            throw new IllegalArgumentException(clazz + " has no usable constructor");
        }

        if ((defaultConstructor.getModifiers() & Modifier.PUBLIC) == 0)
        {
            throw new IllegalArgumentException(clazz + " has no public default constructor");
        }

        registeredEventClasses.put(eventType.toLowerCase(Locale.US), clazz);

        logger.debug("Registered event type '" + eventType + "' (" + clazz + ")");
    }

    @SuppressWarnings("unchecked")
    public ManagerEvent buildEvent(Object source, Map<String, Object> attributes)
    {
        ManagerEvent event;
        String eventType = null;
        Class< ? > eventClass;
        Constructor< ? > constructor;

        if (attributes.get("event") == null)
        {
            logger.error("No event type in properties");
            return null;
        }

        if (attributes.get("event") instanceof List)
        {
            List< ? > eventNames = (List< ? >) attributes.get("event");
            if (!eventNames.isEmpty() && "PeerEntry".equals(eventNames.get(0)))
            {
                // List of PeerEntry events was received (AJ-329)
                // Convert map of lists to list of maps - one map for each
                // PeerEntry event
                int peersAmount = attributes.get("listitems") != null
                        ? Integer.parseInt((String) attributes.get("listitems"))
                        : eventNames.size() - 1; // Last event is
                                                 // PeerlistComplete
                List<Map<String, Object>> peersAttributes = new ArrayList<>();
                for (Map.Entry<String, Object> attribute : attributes.entrySet())
                {
                    String key = attribute.getKey();
                    Object value = attribute.getValue();
                    for (int i = 0; i < peersAmount; i++)
                    {
                        Map<String, Object> peerAttrs;
                        if (peersAttributes.size() > i)
                        {
                            peerAttrs = peersAttributes.get(i);
                        }
                        else
                        {
                            peerAttrs = new HashMap<>();
                            peersAttributes.add(i, peerAttrs);
                        }
                        if (value instanceof List)
                        {
                            peerAttrs.put(key, ((List< ? >) value).get(i));
                        }
                        else if (value instanceof String && !"listitems".equals(key))
                        {
                            peerAttrs.put(key, value);
                        }
                    }
                }
                attributes.put("peersAttributes", peersAttributes);
                eventType = "peers";
            }
        }
        else
        {
            if (!(attributes.get("event") instanceof String))
            {
                logger.error("Event type is not a String or List");
                return null;
            }

            eventType = ((String) attributes.get("event")).toLowerCase(Locale.US);

            // Change in Asterisk 1.4 where the name of the UserEvent is sent as
            // property instead
            // of the event name (AJ-48)
            if ("userevent".equals(eventType))
            {
                String userEventType;

                if (attributes.get("userevent") == null)
                {
                    logger.error("No user event type in properties");
                    return null;
                }
                if (!(attributes.get("userevent") instanceof String))
                {
                    logger.error("User event type is not a String");
                    return null;
                }

                userEventType = ((String) attributes.get("userevent")).toLowerCase(Locale.US);
                eventType = eventType + userEventType;
            }
        }

        eventClass = registeredEventClasses.get(eventType);
        if (eventClass == null)
        {
            if (eventClassNegativeCache.add(eventType))
            {
                logger.info("No event class registered for event type '" + eventType + "', attributes: " + attributes
                        + ". Please report at https://github.com/asterisk-java/asterisk-java/issues");
            }
            return null;
        }

        try
        {
            constructor = eventClass.getConstructor(Object.class);
        }
        catch (NoSuchMethodException ex)
        {
            logger.error("Unable to get constructor of " + eventClass.getName(), ex);
            return null;
        }

        try
        {
            event = (ManagerEvent) constructor.newInstance(source);
        }
        catch (Exception ex)
        {
            logger.error("Unable to create new instance of " + eventClass.getName(), ex);
            return null;
        }

        if (attributes.get("peersAttributes") != null && attributes.get("peersAttributes") instanceof List)
        {
            // Fill Peers event with list of PeerEntry events (AJ-329)
            PeersEvent peersEvent = (PeersEvent) event;
            // TODO: This cast is very ugly, we should review how attributes are
            // being passed around.
            for (Map<String, Object> peerAttrs : (List<Map<String, Object>>) attributes.get("peersAttributes"))
            {
                PeerEntryEvent peerEntryEvent = new PeerEntryEvent(source);
                setAttributes(peerEntryEvent, peerAttrs, ignoredAttributes);
                List<PeerEntryEvent> peerEntryEvents = peersEvent.getChildEvents();
                if (peerEntryEvents == null)
                {
                    peerEntryEvents = new ArrayList<>();
                    peersEvent.setChildEvents(peerEntryEvents);
                }
                peerEntryEvents.add(peerEntryEvent);
            }
            peersEvent.setActionId(peersEvent.getChildEvents().get(0).getActionId());
        }
        else
        {
            setAttributes(event, attributes, ignoredAttributes);
        }

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
                responseEvent.setInternalActionId(ManagerUtil.getInternalActionId(actionId));
            }
        }

        return event;
    }

    @Override
    public void deregisterEventClass(Class< ? extends ManagerEvent> eventClass)
    {

        Set<String> toRemove = new HashSet<>();
        for (Entry<String, Class< ? >> registered : registeredEventClasses.entrySet())
        {
            if (registered.getValue().equals(eventClass))
            {
                toRemove.add(registered.getKey());
            }
        }
        if (toRemove.isEmpty())
        {
            logger.warn("Couldn't remove event type " + eventClass);
        }
        else
        {
            for (String key : toRemove)
            {
                registeredEventClasses.remove(key);
                logger.warn("Removed event type " + key);
            }
        }

    }
}
