package org.asteriskjava.pbx.asterisk.wrap.actions;

public class ListCommandsAction extends AbstractManagerAction
{

    @Override
    public org.asteriskjava.manager.action.ManagerAction getAJAction()
    {
        final org.asteriskjava.manager.action.ListCommandsAction action = new org.asteriskjava.manager.action.ListCommandsAction();
        action.setActionId(this.getActionId());

        return action;
    }

    public String toString()
    {
        return "ListCommandsAction"; //$NON-NLS-1$
    }

}
