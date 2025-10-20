package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.ami.action.api.ManagerAction;

public class ListCommandsAction extends AbstractManagerAction {

    @Override
    public ManagerAction getAJAction() {
        final org.asteriskjava.ami.action.api.ListCommandsAction action = new org.asteriskjava.ami.action.api.ListCommandsAction();
        action.setActionId(this.getActionId());

        return action;
    }

    public String toString() {
        return "ListCommandsAction"; //$NON-NLS-1$
    }

}
