package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.asteriskjava.lock.LockableMap;
import org.asteriskjava.lock.Locker.LockCloser;
import org.asteriskjava.manager.event.BridgeEnterEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.BridgeLeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.util.DateUtil;
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
    private static final String HOLDING_BRIDGE_TECH = "holding_bridge";

    private final LockableMap<String, BridgeEnterEvent> members = new LockableMap<>(new HashMap<>());

    ManagerEvent destroy()
    {
        try (LockCloser closer = members.withLock())
        {
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

        if (HOLDING_BRIDGE_TECH.equals(event.getBridgeTechnology()))
        {
            /* channels in a holding bridge aren't bridged to one another */
            return null;
        }

        try (LockCloser closer = members.withLock())
        {
            if (members.put(event.getChannel(), event) == null && members.size() == 2)
            {
                remaining = new ArrayList<>(members.values());
            }
        }

        if (remaining == null)
        {
            return null;
        }

        logger.info("Members size " + remaining.size() + " " + event);

        BridgeEvent bridgeEvent = buildBridgeEvent(BridgeEvent.BRIDGE_STATE_LINK, remaining);

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
        List<BridgeEnterEvent> remaining = new LinkedList<>();

        if (HOLDING_BRIDGE_TECH.equals(event.getBridgeTechnology()))
        {
            /* channels in a holding bridge aren't bridged to one another */
            return null;
        }

        try (LockCloser closer = members.withLock())
        {
            remaining.addAll(members.values());

            if (members.remove(event.getChannel()) != null)
            {
                if (remaining.size() == 2)
                {
                    return buildBridgeEvent(BridgeEvent.BRIDGE_STATE_UNLINK, remaining);
                }
            }
        }

        // If we didn't remove anything, or we aren't at exactly 2 members,
        // there's nothing else for us to do

        return null;

    }

    private BridgeEvent buildBridgeEvent(String bridgeState, List<BridgeEnterEvent> members)
    {
        Collections.sort(members, BRIDGE_ENTER_EVENT_COMPARATOR);

        BridgeEvent bridgeEvent = new BridgeEvent(this);

        bridgeEvent.setCallerId1(members.get(0).getCallerIdNum());
        bridgeEvent.setUniqueId1(members.get(0).getUniqueId());
        bridgeEvent.setChannel1(members.get(0).getChannel());

        bridgeEvent.setCallerId2(members.get(1).getCallerIdNum());
        bridgeEvent.setUniqueId2(members.get(1).getUniqueId());
        bridgeEvent.setChannel2(members.get(1).getChannel());

        bridgeEvent.setBridgeState(bridgeState);
        bridgeEvent.setDateReceived(DateUtil.getDate());

        return bridgeEvent;
    }
}
