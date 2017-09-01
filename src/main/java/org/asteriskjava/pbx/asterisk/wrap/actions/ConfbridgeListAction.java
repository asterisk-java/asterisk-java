package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.manager.action.ManagerAction;

public class ConfbridgeListAction extends AbstractManagerAction implements EventGeneratingAction
{

    @Override
    public ManagerAction getAJAction()
    {
        return new org.asteriskjava.manager.action.ConfbridgeListAction();
    }
    // Logger logger = LogManager.getLogger();

    @Override
    public org.asteriskjava.manager.action.EventGeneratingAction getAJEventGeneratingAction()
    {
        org.asteriskjava.manager.action.ConfbridgeListAction action = new org.asteriskjava.manager.action.ConfbridgeListAction();
        action.setActionId(this.getActionId());
        return action;
    }
}
