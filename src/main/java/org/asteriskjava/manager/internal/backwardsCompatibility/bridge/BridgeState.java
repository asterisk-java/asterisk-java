package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import java.util.ArrayList;
import java.util.Collections;
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
    private final Log logger = LogFactory.getLog(getClass());

    private static final BridgeEnterEventComparator BRIDGE_ENTER_EVENT_COMPARATOR = new BridgeEnterEventComparator();

    private final Map<String, BridgeEnterEvent> members = new HashMap<>();

    ManagerEvent destroy()
    {
        synchronized (members) {
            members.clear();
        }
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
        List<BridgeEnterEvent> remaining = null;

        synchronized (members)
        {
            if (members.put(event.getChannel(), event) == null
                    && members.size() == 2)
            {
                remaining = new ArrayList<>(members.values());
            }
        }

        if (remaining == null)
        {
            return null;
        }

        logger.info("Members size " + remaining.size() + " " + event);

        BridgeEvent bridgeEvent = buildBridgeEvent(
                BridgeEvent.BRIDGE_STATE_LINK,
                remaining);

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
        List<BridgeEnterEvent> remaining = null;

        synchronized (members)
        {
            if (members.remove(event.getChannel()) != null
                    && members.size() == 2)
            {
                remaining = new ArrayList<>(members.values());
            }
        }

        // If we didn't remove anything, or we aren't at exactly 2 members,
        // there's nothing else for us to do
        if (remaining == null)
        {
            return null;
        }

        return buildBridgeEvent(
                BridgeEvent.BRIDGE_STATE_UNLINK,
                remaining);
    }

    private BridgeEvent buildBridgeEvent(String bridgeState, List<BridgeEnterEvent> members) {
        Collections.sort(members, BRIDGE_ENTER_EVENT_COMPARATOR);

        BridgeEvent bridgeEvent = new BridgeEvent(this);

        bridgeEvent.setCallerId1(members.get(0).getCallerIdNum());
        bridgeEvent.setUniqueId1(members.get(0).getUniqueId());
        bridgeEvent.setChannel1(members.get(0).getChannel());

        bridgeEvent.setCallerId2(members.get(1).getCallerIdNum());
        bridgeEvent.setUniqueId2(members.get(1).getUniqueId());
        bridgeEvent.setChannel2(members.get(1).getChannel());

        bridgeEvent.setBridgeState(bridgeState);
        bridgeEvent.setDateReceived(new Date());

        return bridgeEvent;
    }
}
