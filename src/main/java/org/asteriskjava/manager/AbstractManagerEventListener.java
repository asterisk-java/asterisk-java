package org.asteriskjava.manager;

import java.lang.reflect.Method;

import org.asteriskjava.manager.event.AgentCallbackLoginEvent;
import org.asteriskjava.manager.event.AgentCallbackLogoffEvent;
import org.asteriskjava.manager.event.AgentCalledEvent;
import org.asteriskjava.manager.event.AgentCompleteEvent;
import org.asteriskjava.manager.event.AgentConnectEvent;
import org.asteriskjava.manager.event.AgentDumpEvent;
import org.asteriskjava.manager.event.AgentLoginEvent;
import org.asteriskjava.manager.event.AgentLogoffEvent;
import org.asteriskjava.manager.event.AgentRingNoAnswerEvent;
import org.asteriskjava.manager.event.AgentsCompleteEvent;
import org.asteriskjava.manager.event.AgentsEvent;
import org.asteriskjava.manager.event.AgiExecEndEvent;
import org.asteriskjava.manager.event.AgiExecEvent;
import org.asteriskjava.manager.event.AgiExecStartEvent;
import org.asteriskjava.manager.event.AlarmClearEvent;
import org.asteriskjava.manager.event.AlarmEvent;
import org.asteriskjava.manager.event.AntennaLevelEvent;
import org.asteriskjava.manager.event.AsyncAgiEvent;
import org.asteriskjava.manager.event.AttendedTransferEvent;
import org.asteriskjava.manager.event.BlindTransferEvent;
import org.asteriskjava.manager.event.BridgeCreateEvent;
import org.asteriskjava.manager.event.BridgeDestroyEvent;
import org.asteriskjava.manager.event.BridgeEnterEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.BridgeExecEvent;
import org.asteriskjava.manager.event.BridgeLeaveEvent;
import org.asteriskjava.manager.event.BridgeMergeEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.CelEvent;
import org.asteriskjava.manager.event.ChallengeResponseFailedEvent;
import org.asteriskjava.manager.event.ChallengeSentEvent;
import org.asteriskjava.manager.event.ChanSpyStartEvent;
import org.asteriskjava.manager.event.ChanSpyStopEvent;
import org.asteriskjava.manager.event.ChannelReloadEvent;
import org.asteriskjava.manager.event.ChannelUpdateEvent;
import org.asteriskjava.manager.event.ConfbridgeEndEvent;
import org.asteriskjava.manager.event.ConfbridgeJoinEvent;
import org.asteriskjava.manager.event.ConfbridgeLeaveEvent;
import org.asteriskjava.manager.event.ConfbridgeListCompleteEvent;
import org.asteriskjava.manager.event.ConfbridgeListEvent;
import org.asteriskjava.manager.event.ConfbridgeListRoomsCompleteEvent;
import org.asteriskjava.manager.event.ConfbridgeListRoomsEvent;
import org.asteriskjava.manager.event.ConfbridgeStartEvent;
import org.asteriskjava.manager.event.ConfbridgeTalkingEvent;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.ContactStatusEvent;
import org.asteriskjava.manager.event.CoreShowChannelEvent;
import org.asteriskjava.manager.event.CoreShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.DAHDIChannelEvent;
import org.asteriskjava.manager.event.DahdiShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.DahdiShowChannelsEvent;
import org.asteriskjava.manager.event.DbGetResponseEvent;
import org.asteriskjava.manager.event.DeviceStateChangeEvent;
import org.asteriskjava.manager.event.DialBeginEvent;
import org.asteriskjava.manager.event.DialEndEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DialStateEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.DndStateEvent;
import org.asteriskjava.manager.event.DongleCENDEvent;
import org.asteriskjava.manager.event.DongleCallStateChangeEvent;
import org.asteriskjava.manager.event.DongleDeviceEntryEvent;
import org.asteriskjava.manager.event.DongleNewCMGREvent;
import org.asteriskjava.manager.event.DongleNewSMSBase64Event;
import org.asteriskjava.manager.event.DongleNewSMSEvent;
import org.asteriskjava.manager.event.DongleShowDevicesCompleteEvent;
import org.asteriskjava.manager.event.DongleStatusEvent;
import org.asteriskjava.manager.event.DtmfBeginEvent;
import org.asteriskjava.manager.event.DtmfEndEvent;
import org.asteriskjava.manager.event.DtmfEvent;
import org.asteriskjava.manager.event.EndpointList;
import org.asteriskjava.manager.event.EndpointListComplete;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import org.asteriskjava.manager.event.FaxDocumentStatusEvent;
import org.asteriskjava.manager.event.FaxReceivedEvent;
import org.asteriskjava.manager.event.FaxStatusEvent;
import org.asteriskjava.manager.event.FullyBootedEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HangupRequestEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.HoldedCallEvent;
import org.asteriskjava.manager.event.InvalidPasswordEvent;
import org.asteriskjava.manager.event.JabberEventEvent;
import org.asteriskjava.manager.event.JitterBufStatsEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ListDialplanEvent;
import org.asteriskjava.manager.event.LocalBridgeEvent;
import org.asteriskjava.manager.event.LocalOptimizationBeginEvent;
import org.asteriskjava.manager.event.LocalOptimizationEndEvent;
import org.asteriskjava.manager.event.LogChannelEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MasqueradeEvent;
import org.asteriskjava.manager.event.MeetMeEndEvent;
import org.asteriskjava.manager.event.MeetMeJoinEvent;
import org.asteriskjava.manager.event.MeetMeLeaveEvent;
import org.asteriskjava.manager.event.MeetMeMuteEvent;
import org.asteriskjava.manager.event.MeetMeTalkingEvent;
import org.asteriskjava.manager.event.MeetMeTalkingRequestEvent;
import org.asteriskjava.manager.event.MessageWaitingEvent;
import org.asteriskjava.manager.event.ModuleLoadReportEvent;
import org.asteriskjava.manager.event.MonitorStartEvent;
import org.asteriskjava.manager.event.MonitorStopEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;
import org.asteriskjava.manager.event.MusicOnHoldStartEvent;
import org.asteriskjava.manager.event.MusicOnHoldStopEvent;
import org.asteriskjava.manager.event.NewAccountCodeEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewConnectedLineEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;
import org.asteriskjava.manager.event.ParkedCallsCompleteEvent;
import org.asteriskjava.manager.event.PausedEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.PeerStatusEvent;
import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.event.PeersEvent;
import org.asteriskjava.manager.event.PickupEvent;
import org.asteriskjava.manager.event.PriEventEvent;
import org.asteriskjava.manager.event.ProtocolIdentifierReceivedEvent;
import org.asteriskjava.manager.event.QueueCallerAbandonEvent;
import org.asteriskjava.manager.event.QueueCallerJoinEvent;
import org.asteriskjava.manager.event.QueueCallerLeaveEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberPenaltyEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.QueueStatusCompleteEvent;
import org.asteriskjava.manager.event.QueueSummaryCompleteEvent;
import org.asteriskjava.manager.event.QueueSummaryEvent;
import org.asteriskjava.manager.event.ReceiveFaxEvent;
import org.asteriskjava.manager.event.RegistrationsCompleteEvent;
import org.asteriskjava.manager.event.RegistryEntryEvent;
import org.asteriskjava.manager.event.RegistryEvent;
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.RtcpReceivedEvent;
import org.asteriskjava.manager.event.RtcpSentEvent;
import org.asteriskjava.manager.event.RtpReceiverStatEvent;
import org.asteriskjava.manager.event.RtpSenderStatEvent;
import org.asteriskjava.manager.event.SendFaxEvent;
import org.asteriskjava.manager.event.SendFaxStatusEvent;
import org.asteriskjava.manager.event.ShowDialplanCompleteEvent;
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.SkypeAccountStatusEvent;
import org.asteriskjava.manager.event.SkypeBuddyEntryEvent;
import org.asteriskjava.manager.event.SkypeBuddyListCompleteEvent;
import org.asteriskjava.manager.event.SkypeBuddyStatusEvent;
import org.asteriskjava.manager.event.SkypeChatMessageEvent;
import org.asteriskjava.manager.event.SkypeLicenseEvent;
import org.asteriskjava.manager.event.SkypeLicenseListCompleteEvent;
import org.asteriskjava.manager.event.SoftHangupRequestEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.SuccessfulAuthEvent;
import org.asteriskjava.manager.event.T38FaxStatusEvent;
import org.asteriskjava.manager.event.TransferEvent;
import org.asteriskjava.manager.event.UnholdEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.event.UnpausedEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.event.VarSetEvent;
import org.asteriskjava.manager.event.VoicemailUserEntryCompleteEvent;
import org.asteriskjava.manager.event.VoicemailUserEntryEvent;
import org.asteriskjava.manager.event.ZapShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.ZapShowChannelsEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

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
public abstract class AbstractManagerEventListener implements ManagerEventListener
{
    Log logger = LogFactory.getLog(AbstractManagerEventListener.class);

	public void handleEvent(AgentCompleteEvent event)
    {
    }

	public void handleEvent(AgentConnectEvent event)
    {
    }

	public void handleEvent(AgentDumpEvent event)
    {
    }

	public void handleEvent(AgentRingNoAnswerEvent event)
    {
    }

	public void handleEvent(AttendedTransferEvent event)
    {
    }

	public void handleEvent(BlindTransferEvent event)
    {
    }

	public void handleEvent(BridgeCreateEvent event)
    {
    }

	public void handleEvent(BridgeDestroyEvent event)
    {
    }

	public void handleEvent(BridgeEnterEvent event)
    {
    }

	public void handleEvent(BridgeLeaveEvent event)
    {
    }

	public void handleEvent(HangupEvent event)
    {
    }

	public void handleEvent(NewChannelEvent event)
    {
    }

	public void handleEvent(NewStateEvent event)
    {
    }

	public void handleEvent(ConfbridgeEndEvent event)
    {
    }

	public void handleEvent(ConfbridgeJoinEvent event)
    {
    }

	public void handleEvent(ConfbridgeLeaveEvent event)
    {
    }

	public void handleEvent(ConfbridgeStartEvent event)
    {
    }

	public void handleEvent(ConfbridgeTalkingEvent event)
    {
    }

	public void handleEvent(AntennaLevelEvent event)
    {
    }

	public void handleEvent(HangupRequestEvent event)
    {
    }

	public void handleEvent(NewCallerIdEvent event)
    {
    }

	public void handleEvent(SoftHangupRequestEvent event)
    {
    }

	public void handleEvent(FaxDocumentStatusEvent event)
    {
    }

	public void handleEvent(FaxReceivedEvent event)
    {
    }

	public void handleEvent(FaxStatusEvent event)
    {
    }

	public void handleEvent(SendFaxEvent event)
    {
    }

	public void handleEvent(SendFaxStatusEvent event)
    {
    }

	public void handleEvent(T38FaxStatusEvent event)
    {
    }

	public void handleEvent(HoldEvent event)
    {
    }

	public void handleEvent(UnholdEvent event)
    {
    }

	public void handleEvent(MeetMeJoinEvent event)
    {
    }

	public void handleEvent(MeetMeLeaveEvent event)
    {
    }

	public void handleEvent(MeetMeMuteEvent event)
    {
    }

	public void handleEvent(MeetMeTalkingEvent event)
    {
    }

	public void handleEvent(MeetMeTalkingRequestEvent event)
    {
    }

	public void handleEvent(MonitorStartEvent event)
    {
    }

	public void handleEvent(MonitorStopEvent event)
    {
    }

	public void handleEvent(ParkedCallEvent event)
    {
    }

	public void handleEvent(QueueMemberAddedEvent event)
    {
    }

	public void handleEvent(QueueMemberPausedEvent event)
    {
    }

	public void handleEvent(QueueMemberRemovedEvent event)
    {
    }

	public void handleEvent(RtcpReceivedEvent event)
    {
    }

	public void handleEvent(RtcpSentEvent event)
    {
    }

	public void handleEvent(RtpReceiverStatEvent event)
    {
    }

	public void handleEvent(RtpSenderStatEvent event)
    {
    }

	public void handleEvent(AgentCallbackLoginEvent event)
    {
    }

	public void handleEvent(AgentCallbackLogoffEvent event)
    {
    }

	public void handleEvent(AgentCalledEvent event)
    {
    }

	public void handleEvent(AgentLoginEvent event)
    {
    }

	public void handleEvent(AgentLogoffEvent event)
    {
    }

	public void handleEvent(AgiExecEvent event)
    {
    }

	public void handleEvent(AgiExecEndEvent event)
    {
    }

	public void handleEvent(AgiExecStartEvent event)
    {
    }

	public void handleEvent(AlarmClearEvent event)
    {
    }

	public void handleEvent(AlarmEvent event)
    {
    }

	public void handleEvent(BridgeEvent event)
    {
    }

	public void handleEvent(LinkEvent event)
    {
    }

	public void handleEvent(UnlinkEvent event)
    {
    }

	public void handleEvent(BridgeExecEvent event)
    {
    }

	public void handleEvent(BridgeMergeEvent event)
    {
    }

	public void handleEvent(CdrEvent event)
    {
    }

	public void handleEvent(CelEvent event)
    {
    }

	public void handleEvent(ChallengeResponseFailedEvent event)
    {
    }

	public void handleEvent(ChallengeSentEvent event)
    {
    }

	public void handleEvent(ChannelReloadEvent event)
    {
    }

	public void handleEvent(ChannelUpdateEvent event)
    {
    }

	public void handleEvent(ChanSpyStartEvent event)
    {
    }

	public void handleEvent(ChanSpyStopEvent event)
    {
    }

	public void handleEvent(ConnectEvent event)
    {
    }

	public void handleEvent(ContactStatusEvent event)
    {
    }

	public void handleEvent(DAHDIChannelEvent event)
    {
    }

	public void handleEvent(DeviceStateChangeEvent event)
    {
    }

	public void handleEvent(DialEvent event)
    {
    }

	public void handleEvent(DialBeginEvent event)
    {
    }

	public void handleEvent(DialEndEvent event)
    {
    }

	public void handleEvent(DialStateEvent event)
    {
    }

	public void handleEvent(DisconnectEvent event)
    {
    }

	public void handleEvent(DndStateEvent event)
    {
    }

	public void handleEvent(DongleCallStateChangeEvent event)
    {
    }

	public void handleEvent(DongleCENDEvent event)
    {
    }

	public void handleEvent(DongleNewCMGREvent event)
    {
    }

	public void handleEvent(DongleNewSMSBase64Event event)
    {
    }

	public void handleEvent(DongleNewSMSEvent event)
    {
    }

	public void handleEvent(DongleStatusEvent event)
    {
    }

	public void handleEvent(DtmfEvent event)
    {
    }

	public void handleEvent(DtmfBeginEvent event)
    {
    }

	public void handleEvent(DtmfEndEvent event)
    {
    }

	public void handleEvent(ExtensionStatusEvent event)
    {
    }

	public void handleEvent(FullyBootedEvent event)
    {
    }

	public void handleEvent(HoldedCallEvent event)
    {
    }

	public void handleEvent(InvalidPasswordEvent event)
    {
    }

	public void handleEvent(JabberEventEvent event)
    {
    }

	public void handleEvent(JitterBufStatsEvent event)
    {
    }

	public void handleEvent(LocalBridgeEvent event)
    {
    }

	public void handleEvent(LocalOptimizationBeginEvent event)
    {
    }

	public void handleEvent(LocalOptimizationEndEvent event)
    {
    }

	public void handleEvent(LogChannelEvent event)
    {
    }

	public void handleEvent(MasqueradeEvent event)
    {
    }

	public void handleEvent(MeetMeEndEvent event)
    {
    }

	public void handleEvent(MessageWaitingEvent event)
    {
    }

	public void handleEvent(ModuleLoadReportEvent event)
    {
    }

	public void handleEvent(MusicOnHoldEvent event)
    {
    }

	public void handleEvent(MusicOnHoldStartEvent event)
    {
    }

	public void handleEvent(MusicOnHoldStopEvent event)
    {
    }

	public void handleEvent(NewAccountCodeEvent event)
    {
    }

	public void handleEvent(NewConnectedLineEvent event)
    {
    }

	public void handleEvent(NewExtenEvent event)
    {
    }

	public void handleEvent(PeerStatusEvent event)
    {
    }

	public void handleEvent(PickupEvent event)
    {
    }

	public void handleEvent(PriEventEvent event)
    {
    }

	public void handleEvent(ProtocolIdentifierReceivedEvent event)
    {
    }

	public void handleEvent(QueueEvent event)
    {
    }

	public void handleEvent(JoinEvent event)
    {
    }

	public void handleEvent(LeaveEvent event)
    {
    }

	public void handleEvent(QueueCallerAbandonEvent event)
    {
    }

	public void handleEvent(QueueCallerJoinEvent event)
    {
    }

	public void handleEvent(QueueCallerLeaveEvent event)
    {
    }

	public void handleEvent(QueueMemberPenaltyEvent event)
    {
    }

	public void handleEvent(ReceiveFaxEvent event)
    {
    }

	public void handleEvent(RegistryEvent event)
    {
    }

	public void handleEvent(ReloadEvent event)
    {
    }

	public void handleEvent(RenameEvent event)
    {
    }

	public void handleEvent(ResponseEvent event)
    {
    }

	public void handleEvent(AgentsCompleteEvent event)
    {
    }

	public void handleEvent(AgentsEvent event)
    {
    }

	public void handleEvent(AsyncAgiEvent event)
    {
    }

	public void handleEvent(ConfbridgeListCompleteEvent event)
    {
    }

	public void handleEvent(ConfbridgeListEvent event)
    {
    }

	public void handleEvent(ConfbridgeListRoomsCompleteEvent event)
    {
    }

	public void handleEvent(ConfbridgeListRoomsEvent event)
    {
    }

	public void handleEvent(CoreShowChannelEvent event)
    {
    }

	public void handleEvent(CoreShowChannelsCompleteEvent event)
    {
    }

	public void handleEvent(DahdiShowChannelsCompleteEvent event)
    {
    }

	public void handleEvent(DahdiShowChannelsEvent event)
    {
    }

	public void handleEvent(DbGetResponseEvent event)
    {
    }

	public void handleEvent(DongleDeviceEntryEvent event)
    {
    }

	public void handleEvent(DongleShowDevicesCompleteEvent event)
    {
    }

	public void handleEvent(EndpointList event)
    {
    }

	public void handleEvent(EndpointListComplete event)
    {
    }

	public void handleEvent(ListDialplanEvent event)
    {
    }

	public void handleEvent(OriginateResponseEvent event)
    {
    }

	public void handleEvent(ParkedCallsCompleteEvent event)
    {
    }

	public void handleEvent(PeerEntryEvent event)
    {
    }

	public void handleEvent(PeerlistCompleteEvent event)
    {
    }

	public void handleEvent(PeersEvent event)
    {
    }

	public void handleEvent(QueueEntryEvent event)
    {
    }

	public void handleEvent(QueueMemberEvent event)
    {
    }

	public void handleEvent(QueueParamsEvent event)
    {
    }

	public void handleEvent(QueueStatusCompleteEvent event)
    {
    }

	public void handleEvent(QueueSummaryCompleteEvent event)
    {
    }

	public void handleEvent(QueueSummaryEvent event)
    {
    }

	public void handleEvent(RegistrationsCompleteEvent event)
    {
    }

	public void handleEvent(RegistryEntryEvent event)
    {
    }

	public void handleEvent(ShowDialplanCompleteEvent event)
    {
    }

	public void handleEvent(SkypeBuddyEntryEvent event)
    {
    }

	public void handleEvent(SkypeBuddyListCompleteEvent event)
    {
    }

	public void handleEvent(SkypeLicenseEvent event)
    {
    }

	public void handleEvent(SkypeLicenseListCompleteEvent event)
    {
    }

	public void handleEvent(StatusCompleteEvent event)
    {
    }

	public void handleEvent(StatusEvent event)
    {
    }

	public void handleEvent(VoicemailUserEntryCompleteEvent event)
    {
    }

	public void handleEvent(VoicemailUserEntryEvent event)
    {
    }

	public void handleEvent(ZapShowChannelsCompleteEvent event)
    {
    }

	public void handleEvent(ZapShowChannelsEvent event)
    {
    }

	public void handleEvent(ShutdownEvent event)
    {
    }

	public void handleEvent(SkypeAccountStatusEvent event)
    {
    }

	public void handleEvent(SkypeBuddyStatusEvent event)
    {
    }

	public void handleEvent(SkypeChatMessageEvent event)
    {
    }

	public void handleEvent(SuccessfulAuthEvent event)
    {
    }

	public void handleEvent(TransferEvent event)
    {
    }

	public void handleEvent(UserEvent event)
    {
    }

	public void handleEvent(PausedEvent event)
    {
    }

	public void handleEvent(UnpausedEvent event)
    {
    }

	public void handleEvent(VarSetEvent event)
    {
    }

    /**
     * Dispatches to the appropriate handleEvent(...) method.
     *
     * @param event the event to handle
     */
    @Override
    public void onManagerEvent(ManagerEvent event)
    {

        // if this turns out to be slow, we could consider caching the reflection lookup

        try
        {

            Method method = this.getClass().getMethod("handleEvent", new Class[]{event.getClass()});
            if (method != null)
            {
                method.invoke(this, event);
                return;
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
        }

        logger.error("The event " + event.getClass()
                + " couldn't be mapped to a method in AbstractManagerEventListener.java, someone should add it!");

    }
}
