package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asteriskjava.manager.event.BridgeEnterEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.BridgeLeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Track the current members of a bridge, emmitting BridgeEvents when 2 members
 * join or breakup
 * 
 * @author rsutton
 */
class BridgeState
{
    final Log logger = LogFactory.getLog(getClass());

    private final Map<String, BridgeEnterEvent> members = new HashMap<>();

    public BridgeState()
    {
    }

    ManagerEvent destroy()
    {
        return null;
    }

    /**
     * if there are exactly 2 members in the bridge, return a BridgeEvent
     * 
     * @param event
     * @return
     */
    ManagerEvent addMember(BridgeEnterEvent event)
    {
        members.put(event.getChannel(), event);
        logger.info("Members size " + members.size() + " " + event);
        
        if (members.size() != 2)
        {
            return null;
        }

        BridgeEvent bridgeEvent = buildBridgeEvent(
                BridgeEvent.BRIDGE_STATE_LINK,
                members.values());

        logger.info("Bridge " + bridgeEvent.getChannel1() + " " + bridgeEvent.getChannel2());

        return bridgeEvent;
    }

    /**
     * If there are exactly 2 members in the bridge, return a BridgeEvent
     * 
     * @param event
     * @return
     */

    ManagerEvent removeMember(BridgeLeaveEvent event)
    {
        BridgeEnterEvent removed = members.remove(event.getChannel());

        // If we didn't remove anything, or we aren't at exactly 2 members,
        // there's nothing else for us to do
        if (removed == null || members.size() != 2)
        {
            return null;
        }

        return buildBridgeEvent(
                BridgeEvent.BRIDGE_STATE_UNLINK,
                members.values());
    }
    
    private BridgeEvent buildBridgeEvent(String bridgeState, Collection<BridgeEnterEvent> events)
    {
        List<BridgeEnterEvent> remaining = new ArrayList<>(events);

        BridgeEvent bridgeEvent = new BridgeEvent(this);

        bridgeEvent.setCallerId1(remaining.get(0).getCallerIdNum());
        bridgeEvent.setUniqueId1(remaining.get(0).getUniqueId());
        bridgeEvent.setChannel1(remaining.get(0).getChannel());

        bridgeEvent.setCallerId2(remaining.get(1).getCallerIdNum());
        bridgeEvent.setUniqueId2(remaining.get(1).getUniqueId());
        bridgeEvent.setChannel2(remaining.get(1).getChannel());

        bridgeEvent.setBridgeState(bridgeState);
        bridgeEvent.setDateReceived(new Date());

        return bridgeEvent;
    }
}
