package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.ami.action.api.ManagerAction;

public class CommandAction extends AbstractManagerAction {

    private String _command;

    public CommandAction(final String command) {
        this._command = command;
    }

    public CommandAction() {
    }

    @Override
    public ManagerAction getAJAction() {
        final org.asteriskjava.ami.action.api.CommandAction action = new org.asteriskjava.ami.action.api.CommandAction();
        action.setCommand(this._command);
        action.setActionId(this.getActionId());

        return action;
    }

    public String toString() {
        return "CommandAction: command=" + this._command; //$NON-NLS-1$
    }

}
