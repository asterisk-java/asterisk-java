package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.ConfbridgeListRoomsCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * Lists data about all active conferences. ConfbridgeListRoomsEvent will follow as separate events,
 * followed by a final event called ConfbridgeListRoomsComplete.
 *
 * @since 1.0.0
 */
public class ConfbridgeListRoomsAction extends AbstractManagerAction implements EventGeneratingAction
{
    private static final long serialVersionUID = 1L;

    @Override
    public String getAction()
    {
        return "ConfbridgeListRooms";
    }

    @Override
    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return ConfbridgeListRoomsCompleteEvent.class;
    }
}
