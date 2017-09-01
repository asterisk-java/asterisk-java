package org.asteriskjava.pbx.internal.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import org.asteriskjava.pbx.asterisk.wrap.actions.ManagerAction;
import org.asteriskjava.pbx.asterisk.wrap.events.AgentCalledEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.AgentConnectEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ConfbridgeListCompleteEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ConfbridgeListEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ConnectEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.DialEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.DisconnectEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.DndStateEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ExtensionStatusEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.HangupEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.LinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.MasqueradeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.MeetMeJoinEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.MeetMeLeaveEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewChannelEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewStateEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.OriginateResponseEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ParkedCallEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.PeerEntryEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.PeerStatusEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.PeerlistCompleteEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.QueueCallerLeaveEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.RenameEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusCompleteEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.UnlinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.UnparkedCallEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.VarSetEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.CommandResponse;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerError;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * This class maps asterisk-java events to our internal events that use iChannel
 * rather than raw channel names.
 * 
 * @author bsutton
 */
@SuppressWarnings("deprecation")
public class CoherentEventFactory
{
    private static final Log logger = LogFactory.getLog(CoherentEventFactory.class);

    // Events
    static Hashtable<Class< ? extends org.asteriskjava.manager.event.ManagerEvent>, Class< ? extends ManagerEvent>> mapEvents = new Hashtable<>();

    // Response
    static Hashtable<Class< ? extends org.asteriskjava.manager.event.ResponseEvent>, Class< ? extends ResponseEvent>> mapResponses = new Hashtable<>();

    // Actions
    // static Hashtable<Class<? extends ManagerAction>, Class<? extends
    // org.asteriskjava.manager.action.ManagerAction>> mapActions = new
    // Hashtable<>();

    // static initialiser
    static
    {
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.AgentCalledEvent.class, AgentCalledEvent.class);

        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.AgentConnectEvent.class, AgentConnectEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.QueueCallerLeaveEvent.class,
                QueueCallerLeaveEvent.class);

        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.BridgeEvent.class, BridgeEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.ConnectEvent.class, ConnectEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.DialEvent.class, DialEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.DisconnectEvent.class, DisconnectEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.DndStateEvent.class, DndStateEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.ExtensionStatusEvent.class,
                ExtensionStatusEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.HangupEvent.class, HangupEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.LinkEvent.class, LinkEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.MasqueradeEvent.class, MasqueradeEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.MeetMeJoinEvent.class, MeetMeJoinEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.MeetMeLeaveEvent.class, MeetMeLeaveEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.NewChannelEvent.class, NewChannelEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.NewStateEvent.class, NewStateEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.ParkedCallEvent.class, ParkedCallEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.PeerStatusEvent.class, PeerStatusEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.RenameEvent.class, RenameEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.ResponseEvent.class, ResponseEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.UnlinkEvent.class, UnlinkEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.UnparkedCallEvent.class, UnparkedCallEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.VarSetEvent.class, VarSetEvent.class);

        // response events
        CoherentEventFactory.mapResponses.put(org.asteriskjava.manager.event.OriginateResponseEvent.class,
                OriginateResponseEvent.class);
        CoherentEventFactory.mapResponses.put(org.asteriskjava.manager.event.PeerEntryEvent.class, PeerEntryEvent.class);
        CoherentEventFactory.mapResponses.put(org.asteriskjava.manager.event.PeerlistCompleteEvent.class,
                PeerlistCompleteEvent.class);
        CoherentEventFactory.mapResponses.put(org.asteriskjava.manager.event.ResponseEvent.class, ResponseEvent.class);
        CoherentEventFactory.mapResponses.put(org.asteriskjava.manager.event.StatusCompleteEvent.class,
                StatusCompleteEvent.class);
        CoherentEventFactory.mapResponses.put(org.asteriskjava.manager.event.StatusEvent.class, StatusEvent.class);

        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.ConfbridgeListEvent.class,
                ConfbridgeListEvent.class);
        CoherentEventFactory.mapEvents.put(org.asteriskjava.manager.event.ConfbridgeListCompleteEvent.class,
                ConfbridgeListCompleteEvent.class);

        // Actions
        // CoherentEventFactory.mapActions.put( BridgeAction.class,
        // org.asteriskjava.manager.action.BridgeAction.class);
        // CoherentEventFactory.mapActions.put( CommandAction.class,
        // org.asteriskjava.manager.action.CommandAction.class);
        // CoherentEventFactory.mapActions.put( DbGetAction.class,
        // org.asteriskjava.manager.action.DbGetAction.class);
        // CoherentEventFactory.mapActions.put( GetVarAction.class,
        // org.asteriskjava.manager.action.GetVarAction.class);
        // CoherentEventFactory.mapActions.put( HangupAction.class,
        // org.asteriskjava.manager.action.HangupAction.class);
        // CoherentEventFactory.mapActions.put( ListCommandsAction.class,
        // org.asteriskjava.manager.action.ListCommandsAction.class);
        // CoherentEventFactory.mapActions.put( MonitorAction.class,
        // org.asteriskjava.manager.action.MonitorAction.class);
        // CoherentEventFactory.mapActions.put( MuteAudioAction.class,
        // org.asteriskjava.manager.action.MuteAudioAction.class);
        // CoherentEventFactory.mapActions.put( OriginateAction.class,
        // org.asteriskjava.manager.action.OriginateAction.class);
        // CoherentEventFactory.mapActions.put( PingAction.class,
        // org.asteriskjava.manager.action.PingAction.class);
        // CoherentEventFactory.mapActions.put( PlayDtmfAction.class,
        // org.asteriskjava.manager.action.PlayDtmfAction.class);
        // CoherentEventFactory.mapActions.put( RedirectAction.class,
        // org.asteriskjava.manager.action.RedirectAction.class);
        // CoherentEventFactory.mapActions.put( SetVarAction.class,
        // org.asteriskjava.manager.action.SetVarAction.class);
        // CoherentEventFactory.mapActions.put( SipPeersAction.class,
        // org.asteriskjava.manager.action.SipPeersAction.class);
        // CoherentEventFactory.mapActions.put( SipShowPeerAction.class,
        // org.asteriskjava.manager.action.SipShowPeerAction.class);
        // CoherentEventFactory.mapActions.put( StatusAction.class,
        // org.asteriskjava.manager.action.StatusAction.class);
        // CoherentEventFactory.mapActions.put( UpdateConfigAction.class,
        // org.asteriskjava.manager.action.UpdateConfigAction.class);

    }

    public static Class< ? extends ManagerEvent> getShadowEvent(org.asteriskjava.manager.event.ManagerEvent event)
    {
        Class< ? extends ManagerEvent> result = CoherentEventFactory.mapEvents.get(event.getClass());
        if (result == null)
        {
            Class< ? extends ResponseEvent> response = CoherentEventFactory.mapResponses.get(event.getClass());
            result = response;
        }

        return result;

    }

    public static ManagerEvent build(final org.asteriskjava.manager.event.ManagerEvent event)
    {
        ManagerEvent iEvent = null;

        Class< ? extends ManagerEvent> target = null;

        if (event instanceof org.asteriskjava.manager.event.ResponseEvent)
            target = CoherentEventFactory.mapResponses.get(event.getClass());
        else
            target = CoherentEventFactory.mapEvents.get(event.getClass());

        if (target == null)
        {
            logger.warn("The given event " + event.getClass().getName() + " is not supported "); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {

            try
            {
                iEvent = target.getDeclaredConstructor(event.getClass()).newInstance(event);
            }
            catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e)
            {
                CoherentEventFactory.logger.error(e, e);

            }
        }
        return iEvent;
    }

    public static ResponseEvent build(org.asteriskjava.manager.event.ResponseEvent event)
    {
        ResponseEvent response = null;

        final Class< ? extends ResponseEvent> target = CoherentEventFactory.mapResponses.get(event.getClass());

        if (target == null)
        {
            logger.warn("The given event " + event.getClass().getName() + " is not supported "); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {

            try
            {
                final Constructor< ? extends ResponseEvent> declaredConstructor = target
                        .getDeclaredConstructor(event.getClass());
                response = declaredConstructor.newInstance(event);
            }
            catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e)
            {
                CoherentEventFactory.logger.error(e, e);

            }
        }
        return response;
    }

    public static ManagerResponse build(org.asteriskjava.manager.response.ManagerResponse response)
    {
        ManagerResponse result;
        if (response instanceof org.asteriskjava.manager.response.CommandResponse)
            result = new CommandResponse((org.asteriskjava.manager.response.CommandResponse) response);
        else if (response instanceof org.asteriskjava.manager.response.ManagerError)
            result = new ManagerError((org.asteriskjava.manager.response.ManagerError) response);
        else
            result = new ManagerResponse(response);
        return result;

    }

    public static org.asteriskjava.manager.action.ManagerAction build(ManagerAction action)
    {
        org.asteriskjava.manager.action.ManagerAction result = null;

        // final Class<? extends org.asteriskjava.manager.action.ManagerAction>
        // target = CoherentEventFactory.mapActions.get(action.getClass());

        if (logger.isDebugEnabled())
            logger.debug("Action " + action); //$NON-NLS-1$

        // if (target == null)
        // {
        // logger.warn("The given action " + action.getClass().getName() + " is
        // not supported "); //$NON-NLS-1$ //$NON-NLS-2$
        // }
        // else
        {
            result = action.getAJAction();
        }
        return result;

    }

}
