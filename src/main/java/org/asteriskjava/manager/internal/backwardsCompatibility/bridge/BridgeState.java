package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    private BridgeEvent buildBridgeEvent(String bridgeState, List<BridgeEnterEvent> members)
    {
        BridgeEvent bridgeEvent = new BridgeEvent(this);
        int index1, index2;

        if (this.compareUniqueId(members.get(0).getUniqueId(),
                                 members.get(1).getUniqueId()) < 0)
        {
            index1 = 0;
            index2 = 1;
        }
        else
        {
            index1 = 1;
            index2 = 0;
        }
        bridgeEvent.setCallerId1(members.get(index1).getCallerIdNum());
        bridgeEvent.setUniqueId1(members.get(index1).getUniqueId());
        bridgeEvent.setChannel1(members.get(index1).getChannel());

        bridgeEvent.setCallerId2(members.get(index2).getCallerIdNum());
        bridgeEvent.setUniqueId2(members.get(index2).getUniqueId());
        bridgeEvent.setChannel2(members.get(index2).getChannel());

        bridgeEvent.setBridgeState(bridgeState);
        bridgeEvent.setDateReceived(new Date());

        return bridgeEvent;
    }

    /**
     * Compares two Asteirsk Unique ID.
     *
     * @param id1 1st Asterisk Unique ID, for example "1099015093.165".
     * @param id2 2nd Asterisk Unique ID, for example "1099015093.166".
     * @return Return 0 if id1 == id2.
     * Return less then 0 if id1 < id2.
     * Return greater then 0 if id1 > id2.
     */
    private int compareUniqueId(String id1, String id2)
    {
        Pattern uniqueIdPattern = Pattern.compile("^([0-9]+)\\.([0-9]+)$");
        Matcher uniqueId1Matcher = uniqueIdPattern.matcher(id1);
        Matcher uniqueId2Matcher = uniqueIdPattern.matcher(id2);

        boolean find1 = uniqueId1Matcher.find();
        boolean find2 = uniqueId2Matcher.find();

        if (find1 && find2)
        {
            // 1501234567.890 -> epochtime: 1501234567 | serial: 890
            int epochtime1 = Integer.valueOf(uniqueId1Matcher.group(1));
            int epochtime2 = Integer.valueOf(uniqueId2Matcher.group(1));
            int serial1 = Integer.valueOf(uniqueId1Matcher.group(2));
            int serial2 = Integer.valueOf(uniqueId2Matcher.group(2));

            if (epochtime1 == epochtime2)
            {
                return Integer.compare(serial1, serial2);
            }

            return Integer.compare(epochtime1, epochtime2);

        }
        else if (!find1 && find2)
        {
            // id1 < id2
            return -1;
        }
        else if (find1 && !find2)
        {
            // id1 > id2
            return 1;
        }
	// Both of inputs are invalid value: id1 == id2
        return 0;
    }
}
