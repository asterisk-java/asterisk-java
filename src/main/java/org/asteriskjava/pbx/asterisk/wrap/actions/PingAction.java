package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.apache.log4j.Logger;

public class PingAction extends AbstractManagerAction
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PingAction.class);

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
