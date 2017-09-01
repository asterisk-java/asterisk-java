package org.asteriskjava.pbx.asterisk.wrap.actions;

public class PingAction extends AbstractManagerAction
{

    @Override
    public org.asteriskjava.manager.action.ManagerAction getAJAction()
    {
        final org.asteriskjava.manager.action.PingAction action = new org.asteriskjava.manager.action.PingAction();
        action.setActionId(this.getActionId());
        return action;
    }

    public String toString()
    {
        return "PingAction"; //$NON-NLS-1$
    }

}
