package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.ami.action.api.ManagerAction;

public class PingAction extends AbstractManagerAction {

    @Override
    public ManagerAction getAJAction() {
        final org.asteriskjava.ami.action.api.PingAction action = new org.asteriskjava.ami.action.api.PingAction();
        action.setActionId(this.getActionId());
        return action;
    }

    public String toString() {
        return "PingAction"; //$NON-NLS-1$
    }

}
