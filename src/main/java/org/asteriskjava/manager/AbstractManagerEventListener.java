package org.asteriskjava.manager;

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
import org.asteriskjava.manager.event.BridgeEvent;
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
import org.asteriskjava.manager.event.DialEvent;
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
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
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
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.SoftHangupRequestEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnparkedCallEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.event.ZapShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.ZapShowChannelsEvent;

/**
 * Utility class that provides a protected handler method for each concrete manager event.
 * Makes life easier by removing the need to code endless if-then-else constructs with instanceof
 * checking for the events you are interested in.  
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
 *     protected void handleEvent(StatusEvent event)
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
    protected void handleEvent(AgentCallbackLoginEvent event)
    {
    }

    protected void handleEvent(AgentCallbackLogoffEvent event)
    {
    }

    protected void handleEvent(AgentCalledEvent event)
    {
    }

    protected void handleEvent(AgentLoginEvent event)
    {
    }

    protected void handleEvent(AgentLogoffEvent event)
    {
    }

    protected void handleEvent(AlarmClearEvent event)
    {
    }

    protected void handleEvent(AlarmEvent event)
    {
    }

    protected void handleEvent(CdrEvent event)
    {
    }

    protected void handleEvent(ChanSpyStartEvent event)
    {
    }
   
    protected void handleEvent(ChanSpyStopEvent event)
    {
    }

    protected void handleEvent(ConnectEvent event)
    {
    }
  protected void handleEvent(DAHDIChannelEvent event)
    {
    }
  protected void handleEvent(SoftHangupRequestEvent event)
    {
    }

    protected void handleEvent(DialEvent event)
    {
    }
    
    protected void handleEvent(HangupRequestEvent event)
       {
       }
    protected void handleEvent(DisconnectEvent event)
    {
    }

    protected void handleEvent(DndStateEvent event)
    {
    }

    protected void handleEvent(ExtensionStatusEvent event)
    {
    }

    protected void handleEvent(HoldedCallEvent event)
    {
    }

    protected void handleEvent(HoldEvent event)
    {
    }

    protected void handleEvent(LogChannelEvent event)
    {
    }

    protected void handleEvent(MessageWaitingEvent event)
    {
    }

    protected void handleEvent(NewExtenEvent event)
    {
    }

    protected void handleEvent(PeerStatusEvent event)
    {
    }

    protected void handleEvent(ProtocolIdentifierReceivedEvent event)
    {
    }

    protected void handleEvent(QueueEvent event)
    {
    }

    protected void handleEvent(RegistryEntryEvent event)
    {
    }

    protected void handleEvent(RegistryEvent event)
    {
    }

    protected void handleEvent(ReloadEvent event)
    {
    }

    protected void handleEvent(RenameEvent event)
    {
    }

    protected void handleEvent(ShutdownEvent event)
    {
    }

    protected void handleEvent(UserEvent event)
    {
    }

    protected void handleEvent(AgentCompleteEvent event)
    {
    }

    protected void handleEvent(AgentConnectEvent event)
    {
    }

    protected void handleEvent(AgentDumpEvent event)
    {
    }

    protected void handleEvent(FaxReceivedEvent event)
    {
    }

    protected void handleEvent(NewCallerIdEvent event)
    {
    }

    protected void handleEvent(HangupEvent event)
    {
    }

    protected void handleEvent(NewChannelEvent event)
    {
    }

    protected void handleEvent(NewStateEvent event)
    {
    }

    protected void handleEvent(MeetMeJoinEvent event)
    {
    }

    protected void handleEvent(MeetMeLeaveEvent event)
    {
    }

    protected void handleEvent(MeetMeMuteEvent event)
    {
    }

    protected void handleEvent(MeetMeTalkingEvent event)
    {
    }

    protected void handleEvent(ParkedCallGiveUpEvent event)
    {
    }

    protected void handleEvent(ParkedCallTimeOutEvent event)
    {
    }

    protected void handleEvent(UnparkedCallEvent event)
    {
    }

    protected void handleEvent(QueueMemberAddedEvent event)
    {
    }

    protected void handleEvent(QueueMemberPausedEvent event)
    {
    }

    protected void handleEvent(QueueMemberRemovedEvent event)
    {
    }

    protected void handleEvent(AgentsCompleteEvent event)
    {
    }

    protected void handleEvent(AgentsEvent event)
    {
    }

    protected void handleEvent(DbGetResponseEvent event)
    {
    }
    protected void handleEvent(DongleNewSMSBase64Event event)
    {
    }
    protected void handleEvent(DongleStatusEvent event)
    {
    }
    protected void handleEvent(DongleCENDEvent event)
    {
    }
    protected void handleEvent(DongleCallStateChangeEvent event)
    {
    }
    protected void handleEvent(DongleNewSMSEvent event)
    {
    }
    protected void handleEvent(DongleNewCMGREvent event)
    {
    }
    protected void handleEvent(DongleDeviceEntryEvent event)
    {
    }
    protected void handleEvent(JoinEvent event)
    {
    }

    protected void handleEvent(LeaveEvent event)
    {
    }

    protected void handleEvent(BridgeEvent event)
    {
    }

    protected void handleEvent(OriginateResponseEvent event)
    {
    }

    protected void handleEvent(ParkedCallEvent event)
    {
    }

    protected void handleEvent(ParkedCallsCompleteEvent event)
    {
    }

    protected void handleEvent(PeerEntryEvent event)
    {
    }

    protected void handleEvent(PeerlistCompleteEvent event)
    {
    }

    protected void handleEvent(QueueEntryEvent event)
    {
    }
    
    protected void handleEvent(QueueMemberEvent event)
    {
    }

    protected void handleEvent(QueueMemberStatusEvent event)
    {
    }

    protected void handleEvent(QueueParamsEvent event)
    {
    }

    protected void handleEvent(QueueStatusCompleteEvent event)
    {
    }

    protected void handleEvent(RegistrationsCompleteEvent event)
    {
    }

    protected void handleEvent(StatusCompleteEvent event)
    {
    }

    protected void handleEvent(StatusEvent event)
    {
    }

    protected void handleEvent(ZapShowChannelsCompleteEvent event)
    {
    }

    protected void handleEvent(DahdiShowChannelsCompleteEvent event)
    {
    }
    protected void handleEvent(ZapShowChannelsEvent event)
    {
    }

    protected void handleEvent(DahdiShowChannelsEvent event)
    {
    }

    protected void handleEvent(CoreShowChannelEvent event)
    {
    }

    protected void handleEvent(CoreShowChannelsCompleteEvent event)
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
        if (event instanceof AgentCallbackLoginEvent)
        {
            handleEvent((AgentCallbackLoginEvent) event);
        }
        else if (event instanceof AgentCallbackLogoffEvent)
        {
            handleEvent((AgentCallbackLogoffEvent) event);
        }
        else if (event instanceof AgentCalledEvent)
        {
            handleEvent((AgentCalledEvent) event);
        }
        else if (event instanceof AgentLoginEvent)
        {
            handleEvent((AgentLoginEvent) event);
        }
        else if (event instanceof AgentLogoffEvent)
        {
            handleEvent((AgentLogoffEvent) event);
        }
        else if (event instanceof AlarmClearEvent)
        {
            handleEvent((AlarmClearEvent) event);
        }
        else if (event instanceof AlarmEvent)
        {
            handleEvent((AlarmEvent) event);
        }
        else if (event instanceof CdrEvent)
        {
            handleEvent((CdrEvent) event);
        }
        else if (event instanceof ConnectEvent)
        {
            handleEvent((ConnectEvent) event);
        }
         else if (event instanceof DAHDIChannelEvent)
        {
            handleEvent((DAHDIChannelEvent) event);
        }
        else if (event instanceof SoftHangupRequestEvent)
        {
            handleEvent((SoftHangupRequestEvent) event);
        }
        else if (event instanceof DialEvent)
        {
            handleEvent((DialEvent) event);
        }
        else if (event instanceof HangupRequestEvent)
        {
            handleEvent((HangupRequestEvent) event);
        }
        else if (event instanceof DisconnectEvent)
        {
            handleEvent((DisconnectEvent) event);
        }
        else if (event instanceof DndStateEvent)
        {
            handleEvent((DndStateEvent) event);
        }
        else if (event instanceof ExtensionStatusEvent)
        {
            handleEvent((ExtensionStatusEvent) event);
        }
        else if (event instanceof HoldedCallEvent)
        {
            handleEvent((HoldedCallEvent) event);
        }
        else if (event instanceof HoldEvent)
        {
            handleEvent((HoldEvent) event);
        }
        else if (event instanceof LogChannelEvent)
        {
            handleEvent((LogChannelEvent) event);
        }
        else if (event instanceof MessageWaitingEvent)
        {
            handleEvent((MessageWaitingEvent) event);
        }
        else if (event instanceof NewExtenEvent)
        {
            handleEvent((NewExtenEvent) event);
        }
        else if (event instanceof PeerStatusEvent)
        {
            handleEvent((PeerStatusEvent) event);
        }
        else if (event instanceof ProtocolIdentifierReceivedEvent)
        {
            handleEvent((ProtocolIdentifierReceivedEvent) event);
        }
        else if (event instanceof JoinEvent)
        {
            handleEvent((JoinEvent) event);
        }
        else if (event instanceof LeaveEvent)
        {
            handleEvent((LeaveEvent) event);
        }
        else if (event instanceof QueueEvent)
        {
            handleEvent((QueueEvent) event);
        }
        else if (event instanceof RegistrationsCompleteEvent)
        {
            handleEvent((RegistrationsCompleteEvent) event);
        }
        else if (event instanceof RegistryEntryEvent)
        {
            handleEvent((RegistryEntryEvent) event);
        }
        else if (event instanceof RegistryEvent)
        {
            handleEvent((RegistryEvent) event);
        }
        else if (event instanceof ReloadEvent)
        {
            handleEvent((ReloadEvent) event);
        }
        else if (event instanceof RenameEvent)
        {
            handleEvent((RenameEvent) event);
        }
        else if (event instanceof ShutdownEvent)
        {
            handleEvent((ShutdownEvent) event);
        }
        else if (event instanceof UserEvent)
        {
            handleEvent((UserEvent) event);
        }
        else if (event instanceof AgentCompleteEvent)
        {
            handleEvent((AgentCompleteEvent) event);
        }
        else if (event instanceof AgentConnectEvent)
        {
            handleEvent((AgentConnectEvent) event);
        }
        else if (event instanceof AgentDumpEvent)
        {
            handleEvent((AgentDumpEvent) event);
        }
        else if (event instanceof FaxReceivedEvent)
        {
            handleEvent((FaxReceivedEvent) event);
        }
        else if (event instanceof NewCallerIdEvent)
        {
            handleEvent((NewCallerIdEvent) event);
        }
        else if (event instanceof HangupEvent)
        {
            handleEvent((HangupEvent) event);
        }
        else if (event instanceof NewChannelEvent)
        {
            handleEvent((NewChannelEvent) event);
        }
        else if (event instanceof NewStateEvent)
        {
            handleEvent((NewStateEvent) event);
        }
        else if (event instanceof MeetMeJoinEvent)
        {
            handleEvent((MeetMeJoinEvent) event);
        }
        else if (event instanceof DongleNewSMSBase64Event)
        {
            handleEvent((DongleNewSMSBase64Event) event);
        }
         else if (event instanceof DongleCENDEvent)
        {
            handleEvent((DongleCENDEvent) event);
        }
          else if (event instanceof DongleCallStateChangeEvent)
        {
            handleEvent((DongleCallStateChangeEvent) event);
        }
           else if (event instanceof DongleNewSMSEvent)
        {
            handleEvent((DongleNewSMSEvent) event);
        }
            else if (event instanceof DongleNewCMGREvent)
        {
            handleEvent((DongleNewCMGREvent) event);
        }
        else if (event instanceof DongleStatusEvent)
        {
            handleEvent((DongleStatusEvent) event);
        }
        else if (event instanceof DongleDeviceEntryEvent)
        {
            handleEvent((DongleDeviceEntryEvent) event);
        }
        else if (event instanceof MeetMeLeaveEvent)
        {
            handleEvent((MeetMeLeaveEvent) event);
        }
        else if (event instanceof MeetMeMuteEvent)
        {
            handleEvent((MeetMeMuteEvent) event);
        }
        else if (event instanceof MeetMeTalkingEvent)
        {
            handleEvent((MeetMeTalkingEvent) event);
        }
        else if (event instanceof ParkedCallGiveUpEvent)
        {
            handleEvent((ParkedCallGiveUpEvent) event);
        }
        else if (event instanceof ParkedCallTimeOutEvent)
        {
            handleEvent((ParkedCallTimeOutEvent) event);
        }
        else if (event instanceof UnparkedCallEvent)
        {
            handleEvent((UnparkedCallEvent) event);
        }
        else if (event instanceof QueueMemberAddedEvent)
        {
            handleEvent((QueueMemberAddedEvent) event);
        }
        else if (event instanceof QueueMemberPausedEvent)
        {
            handleEvent((QueueMemberPausedEvent) event);
        }
        else if (event instanceof QueueMemberRemovedEvent)
        {
            handleEvent((QueueMemberRemovedEvent) event);
        }
        else if (event instanceof AgentsCompleteEvent)
        {
            handleEvent((AgentsCompleteEvent) event);
        }
        else if (event instanceof AgentsEvent)
        {
            handleEvent((AgentsEvent) event);
        }
        else if (event instanceof DbGetResponseEvent)
        {
            handleEvent((DbGetResponseEvent) event);
        }
        else if (event instanceof BridgeEvent)
        {
            handleEvent((BridgeEvent) event);
        }
        else if (event instanceof OriginateResponseEvent)
        {
            handleEvent((OriginateResponseEvent) event);
        }
        else if (event instanceof ParkedCallEvent)
        {
            handleEvent((ParkedCallEvent) event);
        }
        else if (event instanceof ParkedCallsCompleteEvent)
        {
            handleEvent((ParkedCallsCompleteEvent) event);
        }
        else if (event instanceof PeerEntryEvent)
        {
            handleEvent((PeerEntryEvent) event);
        }
        else if (event instanceof PeerlistCompleteEvent)
        {
            handleEvent((PeerlistCompleteEvent) event);
        }
        else if (event instanceof QueueEntryEvent)
        {
            handleEvent((QueueEntryEvent) event);
        }
        else if (event instanceof QueueMemberEvent)
        {
            handleEvent((QueueMemberEvent) event);
        }
        else if (event instanceof QueueMemberStatusEvent)
        {
            handleEvent((QueueMemberStatusEvent) event);
        }
        else if (event instanceof QueueParamsEvent)
        {
            handleEvent((QueueParamsEvent) event);
        }
        else if (event instanceof QueueStatusCompleteEvent)
        {
            handleEvent((QueueStatusCompleteEvent) event);
        }
        else if (event instanceof StatusCompleteEvent)
        {
            handleEvent((StatusCompleteEvent) event);
        }
        else if (event instanceof StatusEvent)
        {
            handleEvent((StatusEvent) event);
        }
        else if (event instanceof ZapShowChannelsCompleteEvent)
        {
            handleEvent((ZapShowChannelsCompleteEvent) event);
        }
        else if (event instanceof DahdiShowChannelsCompleteEvent)
        {
            handleEvent((DahdiShowChannelsCompleteEvent) event);
        }
        else if (event instanceof ZapShowChannelsEvent)
        {
            handleEvent((ZapShowChannelsEvent) event);
        }
        else if (event instanceof DahdiShowChannelsEvent)
        {
            handleEvent((DahdiShowChannelsEvent) event);
        }
        else if (event instanceof CoreShowChannelEvent)
        {
            handleEvent((CoreShowChannelEvent) event);
        }
        else if (event instanceof CoreShowChannelsCompleteEvent)
        {
            handleEvent((CoreShowChannelsCompleteEvent) event);
        }
    }
}
