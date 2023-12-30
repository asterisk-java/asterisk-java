package org.asteriskjava.manager.action;

import org.asteriskjava.ami.action.api.AbstractManagerAction;
import org.asteriskjava.ami.action.api.response.event.ResponseEvent;
import org.asteriskjava.manager.event.DongleShowDevicesCompleteEvent;


public class DongleShowDevicesAction extends AbstractManagerAction
        implements
        EventGeneratingAction {
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 8697000330085466825L;

    /**
     * Creates a new DahdiShowChannelsAction.
     */
    public DongleShowDevicesAction() {

    }

    /**
     * Returns the name of this action, i.e. "DahdiShowChannels".
     */
    @Override
    public String getAction() {
        return "DongleShowDevices";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass() {
        return DongleShowDevicesCompleteEvent.class;
    }
}
