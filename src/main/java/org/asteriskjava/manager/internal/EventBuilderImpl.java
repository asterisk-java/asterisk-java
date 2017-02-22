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

import org.asteriskjava.manager.event.*;

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

    EventBuilderImpl()
    {
        this.registeredEventClasses = new HashMap<>();
        registerBuiltinEventClasses();
    }

    @SuppressWarnings({"deprecation"})
    private void registerBuiltinEventClasses()
    {
        // please add new event classes alphabetically
        registerEventClass(AgentCallbackLoginEvent.class);
        registerEventClass(AgentCallbackLogoffEvent.class);
        registerEventClass(AgentCalledEvent.class);
        registerEventClass(AgentConnectEvent.class);
        registerEventClass(AgentCompleteEvent.class);
        registerEventClass(AgentDumpEvent.class);
        registerEventClass(AgentLoginEvent.class);
        registerEventClass(AgentLogoffEvent.class);
        registerEventClass(AgentRingNoAnswerEvent.class);
        registerEventClass(AgentsEvent.class);
        registerEventClass(AgentsCompleteEvent.class);
        registerEventClass(AgiExecEndEvent.class);
        registerEventClass(AgiExecEvent.class);
        registerEventClass(AgiExecStartEvent.class);
        registerEventClass(AntennaLevelEvent.class);
        registerEventClass(AsyncAgiEvent.class);
        registerEventClass(AsyncAgiEndEvent.class);
        registerEventClass(AsyncAgiExecEvent.class);
        registerEventClass(AsyncAgiStartEvent.class);
        registerEventClass(AlarmEvent.class);
        registerEventClass(AlarmClearEvent.class);
        registerEventClass(BridgeCreateEvent.class);
        registerEventClass(BridgeDestroyEvent.class);
        registerEventClass(BridgeEnterEvent.class);
        registerEventClass(BridgeEvent.class);
        registerEventClass(BridgeExecEvent.class);
        registerEventClass(BridgeLeaveEvent.class);
        registerEventClass(BridgeMergeEvent.class);
        registerEventClass(BlindTransferEvent.class);
        registerEventClass(AttendedTransferEvent.class);
        registerEventClass(CdrEvent.class);
        registerEventClass(CelEvent.class);
        registerEventClass(ChallengeSentEvent.class);
        registerEventClass(ChannelReloadEvent.class);
        registerEventClass(ChannelUpdateEvent.class);
        registerEventClass(ChanSpyStartEvent.class);
        registerEventClass(ChanSpyStopEvent.class);
        registerEventClass(ConfbridgeEndEvent.class);
        registerEventClass(ConfbridgeJoinEvent.class);
        registerEventClass(ConfbridgeLeaveEvent.class);
        registerEventClass(ConfbridgeListEvent.class);
        registerEventClass(ConfbridgeListCompleteEvent.class);
        registerEventClass(ConfbridgeListRoomsEvent.class);
        registerEventClass(ConfbridgeListRoomsCompleteEvent.class);
        registerEventClass(ConfbridgeStartEvent.class);
        registerEventClass(ConfbridgeTalkingEvent.class);
        registerEventClass(CoreShowChannelEvent.class);
        registerEventClass(CoreShowChannelsCompleteEvent.class);
        registerEventClass(DAHDIChannelEvent.class);
        registerEventClass(DahdiShowChannelsEvent.class);
        registerEventClass(DahdiShowChannelsCompleteEvent.class);
        registerEventClass(DbGetResponseEvent.class);
        registerEventClass(DeviceStateChangeEvent.class);
        registerEventClass(DialBeginEvent.class);
        registerEventClass(DialEndEvent.class);
        registerEventClass(DialEvent.class);
        registerEventClass(DndStateEvent.class);
        registerEventClass(DongleNewSMSBase64Event.class);
        registerEventClass(DongleCENDEvent.class);
        registerEventClass(DongleCallStateChangeEvent.class);
        registerEventClass(DongleNewSMSEvent.class);
        registerEventClass(DongleNewCMGREvent.class);
        registerEventClass(DongleStatusEvent.class);
        registerEventClass(DongleDeviceEntryEvent.class);
        registerEventClass(DongleShowDevicesCompleteEvent.class);
        registerEventClass(DtmfBeginEvent.class);
        registerEventClass(DtmfEndEvent.class);
        registerEventClass(DtmfEvent.class);
        registerEventClass(ExtensionStatusEvent.class);
        registerEventClass(FaxDocumentStatusEvent.class);
        registerEventClass(FaxReceivedEvent.class);
        registerEventClass(FaxStatusEvent.class);
        registerEventClass(FullyBootedEvent.class);
        registerEventClass(HangupEvent.class);
        registerEventClass(HangupRequestEvent.class);
        registerEventClass(HoldedCallEvent.class);
        registerEventClass(HoldEvent.class);
        registerEventClass(InvalidPasswordEvent.class);
        registerEventClass(JabberEventEvent.class);
        registerEventClass(JitterBufStatsEvent.class);
        registerEventClass(JoinEvent.class);
        registerEventClass(LeaveEvent.class);
        registerEventClass(LinkEvent.class);
        registerEventClass(ListDialplanEvent.class);
        registerEventClass(LocalBridgeEvent.class);
        registerEventClass(LogChannelEvent.class);
        registerEventClass(NewConnectedLineEvent.class);
        registerEventClass(MasqueradeEvent.class);
        registerEventClass(MeetMeEndEvent.class);
        registerEventClass(MeetMeJoinEvent.class);
        registerEventClass(MeetMeLeaveEvent.class);
        registerEventClass(MeetMeMuteEvent.class);
        registerEventClass(MeetMeTalkingEvent.class);
        registerEventClass(MeetMeTalkingRequestEvent.class);
        registerEventClass(MeetMeStopTalkingEvent.class);
        registerEventClass(MessageWaitingEvent.class);
        registerEventClass(ModuleLoadReportEvent.class);
        registerEventClass(MonitorStartEvent.class);
        registerEventClass(MonitorStopEvent.class);
        registerEventClass(MusicOnHoldEvent.class);
        registerEventClass(MusicOnHoldStartEvent.class);
        registerEventClass(MusicOnHoldStopEvent.class);
        registerEventClass(NewAccountCodeEvent.class);
        registerEventClass(NewCallerIdEvent.class);
        registerEventClass(NewChannelEvent.class);
        registerEventClass(NewExtenEvent.class);
        registerEventClass(NewStateEvent.class);
        registerEventClass(OriginateFailureEvent.class);
        registerEventClass(OriginateSuccessEvent.class);
        registerEventClass(OriginateResponseEvent.class);
        registerEventClass(ParkedCallGiveUpEvent.class);
        registerEventClass(ParkedCallEvent.class);
        registerEventClass(ParkedCallTimeOutEvent.class);
        registerEventClass(ParkedCallsCompleteEvent.class);
        registerEventClass(PausedEvent.class);
        registerEventClass(PeerEntryEvent.class);
        registerEventClass(PeerlistCompleteEvent.class);
        registerEventClass(PeersEvent.class);
        registerEventClass(PeerStatusEvent.class);
        registerEventClass(PickupEvent.class);
        registerEventClass(PriEventEvent.class);
        registerEventClass(QueueCallerAbandonEvent.class);
        registerEventClass(QueueCallerJoinEvent.class);
        registerEventClass(QueueCallerLeaveEvent.class);
        registerEventClass(QueueEntryEvent.class);
        registerEventClass(QueueMemberAddedEvent.class);
        registerEventClass(QueueMemberEvent.class);
        registerEventClass(QueueMemberPausedEvent.class);
        registerEventClass(QueueMemberPauseEvent.class);
        registerEventClass(QueueMemberPenaltyEvent.class);
        registerEventClass(QueueMemberRemovedEvent.class);
        registerEventClass(QueueMemberStatusEvent.class);
        registerEventClass(QueueParamsEvent.class);
        registerEventClass(QueueStatusCompleteEvent.class);
        registerEventClass(QueueSummaryCompleteEvent.class);
        registerEventClass(QueueSummaryEvent.class);
        registerEventClass(ReceiveFaxEvent.class);
        registerEventClass(RegistrationsCompleteEvent.class);
        registerEventClass(RegistryEntryEvent.class);
        registerEventClass(RegistryEvent.class);
        registerEventClass(ReloadEvent.class);
        registerEventClass(RenameEvent.class);
        registerEventClass(RtcpReceivedEvent.class);
        registerEventClass(RtcpSentEvent.class);
        registerEventClass(RtpReceiverStatEvent.class);
        registerEventClass(RtpSenderStatEvent.class);
        registerEventClass(SendFaxStatusEvent.class);
        registerEventClass(SendFaxEvent.class);
        registerEventClass(SkypeAccountStatusEvent.class);
        registerEventClass(SkypeBuddyEntryEvent.class);
        registerEventClass(SkypeBuddyListCompleteEvent.class);
        registerEventClass(SkypeBuddyStatusEvent.class);
        registerEventClass(SkypeChatMessageEvent.class);
        registerEventClass(SkypeLicenseEvent.class);
        registerEventClass(SkypeLicenseListCompleteEvent.class);
        registerEventClass(ShowDialplanCompleteEvent.class);
        registerEventClass(ShutdownEvent.class);
        registerEventClass(SoftHangupRequestEvent.class);
        registerEventClass(StatusEvent.class);
        registerEventClass(StatusCompleteEvent.class);
        registerEventClass(SuccessfulAuthEvent.class);
        registerEventClass(T38FaxStatusEvent.class);
        registerEventClass(TransferEvent.class);
        registerEventClass(UnholdEvent.class);
        registerEventClass(UnpausedEvent.class);
        registerEventClass(UnlinkEvent.class);
        registerEventClass(UnparkedCallEvent.class);
        registerEventClass(VarSetEvent.class);
        registerEventClass(VoicemailUserEntryCompleteEvent.class);
        registerEventClass(VoicemailUserEntryEvent.class);
        registerEventClass(ZapShowChannelsEvent.class);
        registerEventClass(ZapShowChannelsCompleteEvent.class);
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
            defaultConstructor = clazz.getConstructor(new Class[]{Object.class});
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
            logger.info("No event class registered for event type '" + eventType + "', attributes: " + attributes
                    + ". Please report at https://github.com/asterisk-java/asterisk-java/issues");
            return null;
        }

        try
        {
            constructor = eventClass.getConstructor(new Class[]{Object.class});
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
            logger.error("Couldn't remove event type " + eventClass);
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
