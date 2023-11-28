package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.ami.action.ManagerAction;

public class ConfbridgeKickAction extends AbstractManagerAction {

    private final String channel;
    private String room;

    public ConfbridgeKickAction(final String room, String channel) {
        this.channel = channel;
        this.room = room;
    }

    @Override
    public ManagerAction getAJAction() {
        final org.asteriskjava.manager.action.ConfbridgeKickAction action = new org.asteriskjava.manager.action.ConfbridgeKickAction();
        action.setActionId(this.getActionId());
        action.setChannel(channel);
        action.setConference(room);

        return action;
    }

    public String getChannel() {
        return channel;
    }

    public String getRoom() {
        return room;
    }
}
