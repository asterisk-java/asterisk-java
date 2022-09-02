package org.asteriskjava.pbx.internal.core;

import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;

import java.util.EventListener;

public interface CoherentManagerEventListener extends EventListener {
    /**
     * This method is called when an event is received.
     *
     * @param event the event that has been received
     */
    void onManagerEvent(ManagerEvent event);
}
