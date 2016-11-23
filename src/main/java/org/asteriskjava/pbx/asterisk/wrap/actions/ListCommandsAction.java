package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.apache.log4j.Logger;

public class ListCommandsAction extends AbstractManagerAction
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ListCommandsAction.class);

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
