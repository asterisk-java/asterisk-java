package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.ami.action.api.ManagerAction;
import org.asteriskjava.pbx.Channel;

public class HangupAction extends AbstractManagerAction {

    private final Channel _channel;
    private Integer _cause;

    public HangupAction(final Channel channel) {
        this._channel = channel;
    }

    @Override
    public ManagerAction getAJAction() {
        final org.asteriskjava.manager.action.HangupAction action = new org.asteriskjava.manager.action.HangupAction();
        action.setActionId(this.getActionId());
        action.setCause(this._cause);
        action.setChannel(this._channel.getChannelName());

        return action;
    }

    public Channel getChannel() {
        return this._channel;
    }

    public String toString() {
        return "HangupAction: channel=" + this._channel + " cause=" + this._cause; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
