package org.asteriskjava.pbx.asterisk.wrap.actions;

public class SipPeersAction extends AbstractManagerAction implements EventGeneratingAction
{

	public SipPeersAction()
	{
	}

	@Override
	public org.asteriskjava.manager.action.ManagerAction getAJAction()
	{
		return new org.asteriskjava.manager.action.SipPeersAction();
	}

	@Override
	public org.asteriskjava.manager.action.EventGeneratingAction getAJEventGeneratingAction()
	{
		org.asteriskjava.manager.action.SipPeersAction action = new org.asteriskjava.manager.action.SipPeersAction();
		action.setActionId(this.getActionId());
		return action;
	}

	public String toString()
	{
		return "SipPeersAction"; //$NON-NLS-1$
	}

}
