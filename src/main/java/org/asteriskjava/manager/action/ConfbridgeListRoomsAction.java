package org.asteriskjava.manager.action;

import org.asteriskjava.ami.action.api.AbstractManagerAction;
import org.asteriskjava.ami.action.api.response.event.ResponseEvent;
import org.asteriskjava.manager.event.ConfbridgeListRoomsCompleteEvent;

/**
 * Lists data about all active conferences. ConfbridgeListRoomsEvent will follow
 * as separate events, followed by a final event called
 * ConfbridgeListRoomsComplete.
 *
 * @since 1.0.0
 */
public class ConfbridgeListRoomsAction extends AbstractManagerAction implements EventGeneratingAction {
    private static final long serialVersionUID = 1L;

    /**
     * note this requires the "reporting" WRITE permission
     */

    @Override
    public String getAction() {
        return "ConfbridgeListRooms";
    }

    @Override
    public Class<? extends ResponseEvent> getActionCompleteEventClass() {
        return ConfbridgeListRoomsCompleteEvent.class;
    }
}
