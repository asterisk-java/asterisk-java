package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.asteriskjava.manager.event.BridgeCreateEvent;
import org.asteriskjava.manager.event.BridgeDestroyEvent;
import org.asteriskjava.manager.event.BridgeEnterEvent;
import org.asteriskjava.manager.event.BridgeLeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Backwards compatibility for bridge events. <br>
 * <br>
 * Asterisk 13 uses BridgeCreate, BridgeEnter, BridgeLeave and BridgeDestroy
 * events. <br>
 * <br>
 * Here we track active bridges and simulate BridgeEvent's for them allowing
 * legacy code to still work with BridgeEvent's
 * 
 * @author rsutton
 */
public class BridgesActive
{
    private final Log logger = LogFactory.getLog(BridgesActive.class);

    private final ConcurrentMap<String, BridgeState> activeBridges = new ConcurrentHashMap<>();

    public ManagerEvent handleEvent(ManagerEvent event)
    {
        if (event instanceof BridgeCreateEvent)
        {
            return createBridge((BridgeCreateEvent) event);
        }
        else if (event instanceof BridgeDestroyEvent)
        {
            return destroyBridge((BridgeDestroyEvent) event);
        }
        else if (event instanceof BridgeEnterEvent)
        {
            return enterBridge((BridgeEnterEvent) event);
        }
        else if (event instanceof BridgeLeaveEvent)
        {
            return leaveBridge((BridgeLeaveEvent) event);
        }
        return null;
    }

    ManagerEvent createBridge(BridgeCreateEvent event)
    {
        activeBridges.putIfAbsent(event.getBridgeUniqueId(), new BridgeState());
        return null;
    }

    ManagerEvent destroyBridge(BridgeDestroyEvent event)
    {
        BridgeState state = activeBridges.remove(event.getBridgeUniqueId());
        if (state != null)
        {
            return state.destroy();
        }
        logger.info("Cant find bridge for id " + event.getBridgeUniqueId());
        return null;
    }

    ManagerEvent enterBridge(BridgeEnterEvent event)
    {
        BridgeState state = activeBridges.get(event.getBridgeUniqueId());
        if (state != null)
        {
            return state.addMember(event);
        }
        logger.error("Cant find bridge for id " + event.getBridgeUniqueId());
        return null;
    }

    ManagerEvent leaveBridge(BridgeLeaveEvent event)
    {
        BridgeState state = activeBridges.get(event.getBridgeUniqueId());
        if (state != null)
        {
            return state.removeMember(event);
        }
        logger.info("Cant find bridge for id " + event.getBridgeUniqueId());
        return null;
    }
}
