package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.asteriskjava.manager.event.BridgeCreateEvent;
import org.asteriskjava.manager.event.BridgeDestroyEvent;
import org.asteriskjava.manager.event.BridgeEnterEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.BridgeLeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;

/**
 * Track the current members of a bridge, emmitting BridgeEvents when 2 members
 * join or breakup
 * 
 * @author rsutton
 */
public class BridgeState
{
    // Logger logger = LogManager.getLogger();

    BridgeCreateEvent created;
    BridgeDestroyEvent destroyed;
    List<BridgeEnterEvent> members = new LinkedList<>();
    Set<String> channels = new HashSet<>();

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
        if (!channels.contains(channel))
        {
            channels.add(channel);
            members.add(event);
            System.out.println("Members size " + members.size() + " " + event);
            if (members.size() == 2)
            {
                BridgeEvent bridgeEvent = new BridgeEvent(this);
                bridgeEvent.setCallerId1(members.get(0).getCallerIdNum());
                bridgeEvent.setCallerId2(members.get(1).getCallerIdNum());
                bridgeEvent.setUniqueId1(members.get(0).getUniqueId());
                bridgeEvent.setUniqueId2(members.get(1).getUniqueId());
                bridgeEvent.setChannel1(members.get(0).getChannel());
                bridgeEvent.setChannel2(members.get(1).getChannel());
                bridgeEvent.setBridgeState(BridgeEvent.BRIDGE_STATE_LINK);
                bridgeEvent.setDateReceived(new Date());

                System.out.println("Bridge " + bridgeEvent.getChannel1() + " " + bridgeEvent.getChannel2());

                return bridgeEvent;
            }
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
        for (BridgeEnterEvent member : members)
        {
            if (member.getChannel().equalsIgnoreCase(event.getChannel()) && members.size() == 2)
            {
                BridgeEvent bridgeEvent = new BridgeEvent(this);
                bridgeEvent.setCallerId1(members.get(0).getCallerIdNum());
                bridgeEvent.setCallerId2(members.get(1).getCallerIdNum());
                bridgeEvent.setUniqueId1(members.get(0).getUniqueId());
                bridgeEvent.setUniqueId2(members.get(1).getUniqueId());
                bridgeEvent.setChannel1(members.get(0).getChannel());
                bridgeEvent.setChannel2(members.get(1).getChannel());
                bridgeEvent.setBridgeState(BridgeEvent.BRIDGE_STATE_UNLINK);
                bridgeEvent.setDateReceived(new Date());

                members.remove(member);

                return bridgeEvent;

            }
        }

        return null;
    }

}
