package org.asteriskjava.manager;

import org.asteriskjava.ami.action.api.response.event.DbGetResponseEvent;
import org.asteriskjava.ami.action.api.response.event.ResponseEvent;
import org.asteriskjava.ami.event.api.ManagerEvent;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import java.lang.reflect.Method;

/**
 * Utility class that provides a protected handler method for each concrete
 * manager event. Makes life easier by removing the need to code endless
 * if-then-else constructs with instanceof checking for the events you are
 * interested in.
 * <p>
 * Kindly donated by Steve Prior.
 * <p>
 * Example based on HelloEvents from the tutorial:
 *
 * <pre>
 * public class HelloEvents extends AbstractManagerEventListener
 * {
 *     private ManagerConnection managerConnection;
 *
 *     public HelloEvents(String machine, String userid, String password) throws IOException
 *     {
 *         ManagerConnectionFactory factory = new ManagerConnectionFactory(machine, userid, password);
 *         this.managerConnection = factory.createManagerConnection();
 *     }
 *
 *     public void run() throws Exception
 *     {
 *         // register for events
 *         managerConnection.addEventListener(this);
 *
 *         // connect to Asterisk and log in
 *         managerConnection.login();
 *
 *         // request channel state
 *         managerConnection.sendAction(new StatusAction());
 *
 *         // wait 10 seconds for events to come in
 *         Thread.sleep(10000);
 *
 *         // and finally log off and disconnect
 *         managerConnection.logoff();
 *     }
 *
 *     public void handleEvent(StatusEvent event)
 *     {
 *         System.out.println(event.getChannel() + &quot;:&quot; + event.getState());
 *     }
 *
 *     public static void main(String[] args) throws Exception
 *     {
 *         HelloEvents helloEvents;
 *         helloEvents = new HelloEvents(&quot;machine&quot;, &quot;userid&quot;, &quot;password&quot;);
 *         helloEvents.run();
 *     }
 * }
 * </pre>
 *
 * @author srt
 * @since 0.3
 */
public abstract class AbstractManagerEventListener implements ManagerEventListener {

    private static final Log LOGGER = LogFactory.getLog(AbstractManagerEventListener.class);

    public void handleEvent(AgentCompleteEvent event) {
    }

    public void handleEvent(AgentConnectEvent event) {
    }

    public void handleEvent(AgentDumpEvent event) {
    }

    public void handleEvent(AgentRingNoAnswerEvent event) {
    }

    public void handleEvent(AttendedTransferEvent event) {
    }

    public void handleEvent(BlindTransferEvent event) {
    }

    public void handleEvent(BridgeCreateEvent event) {
    }

    public void handleEvent(BridgeDestroyEvent event) {
    }

    public void handleEvent(BridgeEnterEvent event) {
    }

    public void handleEvent(BridgeLeaveEvent event) {
    }

    public void handleEvent(HangupEvent event) {
    }

    public void handleEvent(NewChannelEvent event) {
    }

    public void handleEvent(NewStateEvent event) {
    }

    public void handleEvent(ConfbridgeEndEvent event) {
    }

    public void handleEvent(ConfbridgeJoinEvent event) {
    }

    public void handleEvent(ConfbridgeLeaveEvent event) {
    }

    public void handleEvent(ConfbridgeStartEvent event) {
    }

    public void handleEvent(ConfbridgeTalkingEvent event) {
    }

    public void handleEvent(AntennaLevelEvent event) {
    }

    public void handleEvent(HangupRequestEvent event) {
    }

    public void handleEvent(NewCallerIdEvent event) {
    }

    public void handleEvent(SoftHangupRequestEvent event) {
    }

    public void handleEvent(FaxDocumentStatusEvent event) {
    }

    public void handleEvent(FaxReceivedEvent event) {
    }

    public void handleEvent(FaxStatusEvent event) {
    }

    public void handleEvent(SendFaxEvent event) {
    }

    public void handleEvent(SendFaxStatusEvent event) {
    }

    public void handleEvent(T38FaxStatusEvent event) {
    }

    public void handleEvent(HoldEvent event) {
    }

    public void handleEvent(UnholdEvent event) {
    }

    public void handleEvent(MeetMeJoinEvent event) {
    }

    public void handleEvent(MeetMeLeaveEvent event) {
    }

    public void handleEvent(MeetMeMuteEvent event) {
    }

    public void handleEvent(MeetMeTalkingEvent event) {
    }

    public void handleEvent(MeetMeTalkingRequestEvent event) {
    }

    public void handleEvent(MonitorStartEvent event) {
    }

    public void handleEvent(MonitorStopEvent event) {
    }

    public void handleEvent(ParkedCallEvent event) {
    }

    public void handleEvent(QueueMemberAddedEvent event) {
    }

    public void handleEvent(QueueMemberPausedEvent event) {
    }

    public void handleEvent(QueueMemberRemovedEvent event) {
    }

    public void handleEvent(RtcpReceivedEvent event) {
    }

    public void handleEvent(RtcpSentEvent event) {
    }

    public void handleEvent(RtpReceiverStatEvent event) {
    }

    public void handleEvent(RtpSenderStatEvent event) {
    }

    public void handleEvent(AgentCallbackLoginEvent event) {
    }

    public void handleEvent(AgentCallbackLogoffEvent event) {
    }

    public void handleEvent(AgentCalledEvent event) {
    }

    public void handleEvent(AgentLoginEvent event) {
    }

    public void handleEvent(AgentLogoffEvent event) {
    }

    public void handleEvent(AgiExecEvent event) {
    }

    public void handleEvent(AgiExecEndEvent event) {
    }

    public void handleEvent(AgiExecStartEvent event) {
    }

    public void handleEvent(AlarmClearEvent event) {
    }

    public void handleEvent(AlarmEvent event) {
    }

    public void handleEvent(BridgeEvent event) {
    }

    @SuppressWarnings("deprecation")
    public void handleEvent(LinkEvent event) {
    }

    @SuppressWarnings("deprecation")
    public void handleEvent(UnlinkEvent event) {
    }

    public void handleEvent(BridgeExecEvent event) {
    }

    public void handleEvent(BridgeMergeEvent event) {
    }

    public void handleEvent(CdrEvent event) {
    }

    public void handleEvent(CelEvent event) {
    }

    public void handleEvent(ChallengeResponseFailedEvent event) {
    }

    public void handleEvent(ChallengeSentEvent event) {
    }

    public void handleEvent(ChannelReloadEvent event) {
    }

    public void handleEvent(ChannelUpdateEvent event) {
    }

    public void handleEvent(ChanSpyStartEvent event) {
    }

    public void handleEvent(ChanSpyStopEvent event) {
    }

    public void handleEvent(ConnectEvent event) {
    }

    public void handleEvent(ContactStatusEvent event) {
    }

    public void handleEvent(DAHDIChannelEvent event) {
    }

    public void handleEvent(DeviceStateChangeEvent event) {
    }

    public void handleEvent(DialEvent event) {
    }

    public void handleEvent(DialBeginEvent event) {
    }

    public void handleEvent(DialEndEvent event) {
    }

    public void handleEvent(DialStateEvent event) {
    }

    public void handleEvent(DisconnectEvent event) {
    }

    public void handleEvent(DndStateEvent event) {
    }

    public void handleEvent(DongleCallStateChangeEvent event) {
    }

    public void handleEvent(DongleCENDEvent event) {
    }

    public void handleEvent(DongleNewCMGREvent event) {
    }

    public void handleEvent(DongleNewSMSBase64Event event) {
    }

    public void handleEvent(DongleNewSMSEvent event) {
    }

    public void handleEvent(DongleStatusEvent event) {
    }

    public void handleEvent(DtmfEvent event) {
    }

    public void handleEvent(DtmfBeginEvent event) {
    }

    public void handleEvent(DtmfEndEvent event) {
    }

    public void handleEvent(ExtensionStatusEvent event) {
    }

    public void handleEvent(FullyBootedEvent event) {
    }

    public void handleEvent(HoldedCallEvent event) {
    }

    public void handleEvent(InvalidPasswordEvent event) {
    }

    public void handleEvent(JabberEventEvent event) {
    }

    public void handleEvent(JitterBufStatsEvent event) {
    }

    public void handleEvent(LocalBridgeEvent event) {
    }

    public void handleEvent(LocalOptimizationBeginEvent event) {
    }

    public void handleEvent(LocalOptimizationEndEvent event) {
    }

    public void handleEvent(LogChannelEvent event) {
    }

    public void handleEvent(MasqueradeEvent event) {
    }

    public void handleEvent(MeetMeEndEvent event) {
    }

    public void handleEvent(MessageWaitingEvent event) {
    }

    public void handleEvent(ModuleLoadReportEvent event) {
    }

    public void handleEvent(MusicOnHoldEvent event) {
    }

    public void handleEvent(MusicOnHoldStartEvent event) {
    }

    public void handleEvent(MusicOnHoldStopEvent event) {
    }

    public void handleEvent(NewAccountCodeEvent event) {
    }

    public void handleEvent(NewConnectedLineEvent event) {
    }

    public void handleEvent(NewExtenEvent event) {
    }

    public void handleEvent(PeerStatusEvent event) {
    }

    public void handleEvent(PickupEvent event) {
    }

    public void handleEvent(PriEventEvent event) {
    }

    public void handleEvent(ProtocolIdentifierReceivedEvent event) {
    }

    public void handleEvent(QueueEvent event) {
    }

    public void handleEvent(JoinEvent event) {
    }

    public void handleEvent(LeaveEvent event) {
    }

    public void handleEvent(QueueCallerAbandonEvent event) {
    }

    public void handleEvent(QueueCallerJoinEvent event) {
    }

    public void handleEvent(QueueCallerLeaveEvent event) {
    }

    public void handleEvent(QueueMemberPenaltyEvent event) {
    }

    public void handleEvent(ReceiveFaxEvent event) {
    }

    public void handleEvent(RegistryEvent event) {
    }

    public void handleEvent(ReloadEvent event) {
    }

    public void handleEvent(RenameEvent event) {
    }

    public void handleEvent(ResponseEvent event) {
    }

    public void handleEvent(AgentsCompleteEvent event) {
    }

    public void handleEvent(AgentsEvent event) {
    }

    public void handleEvent(AsyncAgiEvent event) {
    }

    public void handleEvent(ConfbridgeListCompleteEvent event) {
    }

    public void handleEvent(ConfbridgeListEvent event) {
    }

    public void handleEvent(ConfbridgeListRoomsCompleteEvent event) {
    }

    public void handleEvent(ConfbridgeListRoomsEvent event) {
    }

    public void handleEvent(CoreShowChannelEvent event) {
    }

    public void handleEvent(CoreShowChannelsCompleteEvent event) {
    }

    public void handleEvent(DahdiShowChannelsCompleteEvent event) {
    }

    public void handleEvent(DahdiShowChannelsEvent event) {
    }

    public void handleEvent(DbGetResponseEvent event) {
    }

    public void handleEvent(DongleDeviceEntryEvent event) {
    }

    public void handleEvent(DongleShowDevicesCompleteEvent event) {
    }

    public void handleEvent(EndpointList event) {
    }

    public void handleEvent(EndpointListComplete event) {
    }

    public void handleEvent(ListDialplanEvent event) {
    }

    public void handleEvent(OriginateResponseEvent event) {
    }

    public void handleEvent(ParkedCallsCompleteEvent event) {
    }

    public void handleEvent(PeerEntryEvent event) {
    }

    public void handleEvent(PeerlistCompleteEvent event) {
    }

    public void handleEvent(PeersEvent event) {
    }

    public void handleEvent(QueueEntryEvent event) {
    }

    public void handleEvent(QueueMemberEvent event) {
    }

    public void handleEvent(QueueParamsEvent event) {
    }

    public void handleEvent(QueueStatusCompleteEvent event) {
    }

    public void handleEvent(QueueSummaryCompleteEvent event) {
    }

    public void handleEvent(QueueSummaryEvent event) {
    }

    public void handleEvent(RegistrationsCompleteEvent event) {
    }

    public void handleEvent(RegistryEntryEvent event) {
    }

    public void handleEvent(ShowDialplanCompleteEvent event) {
    }

    public void handleEvent(SkypeBuddyEntryEvent event) {
    }

    public void handleEvent(SkypeBuddyListCompleteEvent event) {
    }

    public void handleEvent(SkypeLicenseEvent event) {
    }

    public void handleEvent(SkypeLicenseListCompleteEvent event) {
    }

    public void handleEvent(StatusCompleteEvent event) {
    }

    public void handleEvent(StatusEvent event) {
    }

    public void handleEvent(VoicemailUserEntryCompleteEvent event) {
    }

    public void handleEvent(VoicemailUserEntryEvent event) {
    }

    public void handleEvent(ZapShowChannelsCompleteEvent event) {
    }

    public void handleEvent(ZapShowChannelsEvent event) {
    }

    public void handleEvent(ShutdownEvent event) {
    }

    public void handleEvent(SkypeAccountStatusEvent event) {
    }

    public void handleEvent(SkypeBuddyStatusEvent event) {
    }

    public void handleEvent(SkypeChatMessageEvent event) {
    }

    public void handleEvent(SuccessfulAuthEvent event) {
    }

    public void handleEvent(TransferEvent event) {
    }

    public void handleEvent(UserEvent event) {
    }

    public void handleEvent(PausedEvent event) {
    }

    public void handleEvent(UnpausedEvent event) {
    }

    public void handleEvent(VarSetEvent event) {
    }

    public void handleEvent(EndpointDetail event) {
    }

    public void handleEvent(AorDetail event) {
    }

    public void handleEvent(ContactStatusDetail event) {
    }

    public void handleEvent(EndpointDetailComplete event) {
    }

    public void handleEvent(InvalidAccountId event) {
    }

    /**
     * Dispatches to the appropriate handleEvent(...) method.
     *
     * @param event the event to handle
     */
    @Override
    public void onManagerEvent(ManagerEvent event) {

        // if this turns out to be slow, we could consider caching the
        // reflection lookup
        try {
            Method method = this.getClass().getMethod("handleEvent", event.getClass());
            if (method != null) {
                method.invoke(this, event);
                return;
            }
        } catch (Exception e) {
            LOGGER.error(e, e);
        }

        LOGGER.error("The event " + event.getClass()
                + " couldn't be mapped to a method in AbstractManagerEventListener.java, someone should add it!");
    }
}
