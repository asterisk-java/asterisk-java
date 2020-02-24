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
import org.asteriskjava.manager.event.AgentsCompleteEvent;
import org.asteriskjava.manager.event.AgentsEvent;
import org.asteriskjava.manager.event.AlarmClearEvent;
import org.asteriskjava.manager.event.AlarmEvent;
import org.asteriskjava.manager.event.BlindTransferEvent;
import org.asteriskjava.manager.event.BridgeCreateEvent;
import org.asteriskjava.manager.event.BridgeDestroyEvent;
import org.asteriskjava.manager.event.BridgeEnterEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.BridgeLeaveEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ChanSpyStartEvent;
import org.asteriskjava.manager.event.ChanSpyStopEvent;
import org.asteriskjava.manager.event.ConnectEvent;
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
import org.asteriskjava.manager.event.DongleStatusEvent;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import org.asteriskjava.manager.event.FaxReceivedEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HangupRequestEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.HoldedCallEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.LogChannelEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MeetMeJoinEvent;
import org.asteriskjava.manager.event.MeetMeLeaveEvent;
import org.asteriskjava.manager.event.MeetMeMuteEvent;
import org.asteriskjava.manager.event.MeetMeTalkingEvent;
import org.asteriskjava.manager.event.MessageWaitingEvent;
import org.asteriskjava.manager.event.MusicOnHoldStartEvent;
import org.asteriskjava.manager.event.MusicOnHoldStopEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewConnectedLineEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;
import org.asteriskjava.manager.event.ParkedCallGiveUpEvent;
import org.asteriskjava.manager.event.ParkedCallTimeOutEvent;
import org.asteriskjava.manager.event.ParkedCallsCompleteEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.PeerStatusEvent;
import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.event.ProtocolIdentifierReceivedEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.QueueStatusCompleteEvent;
import org.asteriskjava.manager.event.RegistrationsCompleteEvent;
import org.asteriskjava.manager.event.RegistryEntryEvent;
import org.asteriskjava.manager.event.RegistryEvent;
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.RtcpReceivedEvent;
import org.asteriskjava.manager.event.RtcpSentEvent;
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.SoftHangupRequestEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnholdEvent;
import org.asteriskjava.manager.event.UnparkedCallEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.event.VarSetEvent;
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

    public void handleEvent(AlarmClearEvent event)
    {
    }

    public void handleEvent(AlarmEvent event)
    {
    }

    public void handleEvent(CdrEvent event)
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

    public void handleEvent(DAHDIChannelEvent event)
    {
    }

    public void handleEvent(SoftHangupRequestEvent event)
    {
    }

    public void handleEvent(MusicOnHoldStartEvent event)
	{
	}

    public void handleEvent(MusicOnHoldStopEvent event)
	{
	}

	public void handleEvent(UnholdEvent event)
	{
	}

    public void handleEvent(BlindTransferEvent event)
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

    public void handleEvent(HangupRequestEvent event)
    {
    }

    public void handleEvent(DisconnectEvent event)
    {
    }

    public void handleEvent(DndStateEvent event)
    {
    }

    public void handleEvent(ExtensionStatusEvent event)
    {
    }

    public void handleEvent(HoldedCallEvent event)
    {
    }

    public void handleEvent(HoldEvent event)
    {
    }

    public void handleEvent(LogChannelEvent event)
    {
    }

    public void handleEvent(MessageWaitingEvent event)
    {
    }

    public void handleEvent(NewExtenEvent event)
    {
    }

    public void handleEvent(PeerStatusEvent event)
    {
    }

    public void handleEvent(ProtocolIdentifierReceivedEvent event)
    {
    }

    public void handleEvent(QueueEvent event)
    {
    }

    public void handleEvent(RegistryEntryEvent event)
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

    public void handleEvent(ShutdownEvent event)
    {
    }

    public void handleEvent(UserEvent event)
    {
    }

    public void handleEvent(AgentCompleteEvent event)
    {
    }

    public void handleEvent(AgentConnectEvent event)
    {
    }

    public void handleEvent(AgentDumpEvent event)
    {
    }

    public void handleEvent(FaxReceivedEvent event)
    {
    }

    public void handleEvent(NewCallerIdEvent event)
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

    public void handleEvent(ParkedCallGiveUpEvent event)
    {
    }

    public void handleEvent(ParkedCallTimeOutEvent event)
    {
    }

    public void handleEvent(UnparkedCallEvent event)
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

    public void handleEvent(AgentsCompleteEvent event)
    {
    }

    public void handleEvent(AgentsEvent event)
    {
    }

    public void handleEvent(DbGetResponseEvent event)
    {
    }

    public void handleEvent(DongleNewSMSBase64Event event)
    {
    }

    public void handleEvent(DongleStatusEvent event)
    {
    }

    public void handleEvent(DongleCENDEvent event)
    {
    }

    public void handleEvent(DongleCallStateChangeEvent event)
    {
    }

    public void handleEvent(DongleNewSMSEvent event)
    {
    }

    public void handleEvent(DongleNewCMGREvent event)
    {
    }

    public void handleEvent(DongleDeviceEntryEvent event)
    {
    }

    public void handleEvent(JoinEvent event)
    {
    }

    public void handleEvent(LeaveEvent event)
    {
    }

    public void handleEvent(BridgeEvent event)
    {
    }

    public void handleEvent(OriginateResponseEvent event)
    {
    }

    public void handleEvent(ParkedCallEvent event)
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

    public void handleEvent(QueueEntryEvent event)
    {
    }

    public void handleEvent(QueueMemberEvent event)
    {
    }

    public void handleEvent(QueueMemberStatusEvent event)
    {
    }

    public void handleEvent(QueueParamsEvent event)
    {
    }

    public void handleEvent(QueueStatusCompleteEvent event)
    {
    }

    public void handleEvent(RegistrationsCompleteEvent event)
    {
    }

    public void handleEvent(StatusCompleteEvent event)
    {
    }

    public void handleEvent(StatusEvent event)
    {
    }

    public void handleEvent(ZapShowChannelsCompleteEvent event)
    {
    }

    public void handleEvent(DahdiShowChannelsCompleteEvent event)
    {
    }

    public void handleEvent(ZapShowChannelsEvent event)
    {
    }

    public void handleEvent(DahdiShowChannelsEvent event)
    {
    }

    public void handleEvent(CoreShowChannelEvent event)
    {
    }

    public void handleEvent(CoreShowChannelsCompleteEvent event)
    {
    }

	public void handleEvent(DialStateEvent event)
	{
	}

	public void handleEvent(VarSetEvent event)
	{
	}

	public void handleEvent(DeviceStateChangeEvent event)
	{
	}

	public void handleEvent(NewConnectedLineEvent event)
	{
	}

	public void handleEvent(BridgeCreateEvent event)
	{
	}

	public void handleEvent(BridgeEnterEvent event)
	{
	}

	public void handleEvent(BridgeDestroyEvent event)
	{
	}

	public void handleEvent(BridgeLeaveEvent event)
	{
	}

	public void handleEvent(RtcpSentEvent event)
	{
	}

	public void handleEvent(RtcpReceivedEvent event)
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
