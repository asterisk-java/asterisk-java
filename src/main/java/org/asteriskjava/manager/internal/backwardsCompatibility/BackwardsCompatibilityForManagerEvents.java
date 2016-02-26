package org.asteriskjava.manager.internal.backwardsCompatibility;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.internal.backwardsCompatibility.bridge.BridgesActive;
import org.asteriskjava.manager.internal.backwardsCompatibility.meetme.MeetmeCompatibility;

public class BackwardsCompatibilityForManagerEvents
{
    // Logger logger = LogManager.getLogger();
    BridgesActive bridges = new BridgesActive();
    MeetmeCompatibility meetme = new MeetmeCompatibility();

    public ManagerEvent handleEvent(ManagerEvent event)
    {
        ManagerEvent newEvent = bridges.handleEvent(event);
        if (newEvent == null)
        {
            newEvent = meetme.handleEvent(event);
        }
        return newEvent;
    }

}
