package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.manager.event.BridgeCreateEvent;
import org.asteriskjava.manager.event.BridgeDestroyEvent;
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
public class BridgeState
{
    final Log logger = LogFactory.getLog(getClass());

    BridgeCreateEvent created;
    BridgeDestroyEvent destroyed;
    private final Map<String, BridgeEnterEvent> members = new ConcurrentHashMap<>();

    public BridgeState(BridgeCreateEvent event)
    {
        created = event;
    }

    public ManagerEvent destroy(BridgeDestroyEvent event)
    {
        destroyed = event;
        return null;

    }

    /**
     * if there are exactly 2 members in the bridge, return a BridgeEvent
     * 
     * @param event
     * @return
     */
    public ManagerEvent addMember(BridgeEnterEvent event)
    {
        final String channel = event.getChannel();
        members.put(event.getChannel(), event);
        logger.info("Members size " + members.size() + " " + event);
        List<BridgeEnterEvent> memberList = new LinkedList<>();
        memberList.addAll(members.values());
        if (memberList.size() == 2)
        {
            BridgeEvent bridgeEvent = new BridgeEvent(this);

            bridgeEvent.setCallerId1(memberList.get(0).getCallerIdNum());
            bridgeEvent.setCallerId2(memberList.get(1).getCallerIdNum());
            bridgeEvent.setUniqueId1(memberList.get(0).getUniqueId());
            bridgeEvent.setUniqueId2(memberList.get(1).getUniqueId());
            bridgeEvent.setChannel1(memberList.get(0).getChannel());
            bridgeEvent.setChannel2(memberList.get(1).getChannel());
            bridgeEvent.setBridgeState(BridgeEvent.BRIDGE_STATE_LINK);
            bridgeEvent.setDateReceived(new Date());

            logger.info("Bridge " + bridgeEvent.getChannel1() + " " + bridgeEvent.getChannel2());

            return bridgeEvent;
        }
        return null;

    }

    /**
     * If there are exactly 2 members in the bridge, return a BridgeEvent
     * 
     * @param event
     * @return
     */

    public ManagerEvent removeMember(BridgeLeaveEvent event)
    {
        BridgeEvent bridgeEvent = null;
        for (BridgeEnterEvent member : members.values())
        {
            if (member.getChannel().equalsIgnoreCase(event.getChannel()))
            {
                List<BridgeEnterEvent> memberList = new LinkedList<>();
                memberList.addAll(members.values());
                if (memberList.size() == 2)
                {

                    bridgeEvent = new BridgeEvent(this);

                    bridgeEvent.setCallerId1(memberList.get(0).getCallerIdNum());
                    bridgeEvent.setCallerId2(memberList.get(1).getCallerIdNum());
                    bridgeEvent.setUniqueId1(memberList.get(0).getUniqueId());
                    bridgeEvent.setUniqueId2(memberList.get(1).getUniqueId());
                    bridgeEvent.setChannel1(memberList.get(0).getChannel());
                    bridgeEvent.setChannel2(memberList.get(1).getChannel());
                    bridgeEvent.setBridgeState(BridgeEvent.BRIDGE_STATE_UNLINK);
                    bridgeEvent.setDateReceived(new Date());
                }
                members.remove(member.getChannel());

                return bridgeEvent;

            }
        }

        return bridgeEvent;
    }

}
