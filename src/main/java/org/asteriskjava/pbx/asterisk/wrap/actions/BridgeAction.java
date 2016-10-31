package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.pbx.Channel;

public class BridgeAction extends AbstractManagerAction
{

	private final Channel _lhs;
	private final Channel _rhs;

	public BridgeAction(final Channel lhs, final Channel rhs)
	{
		this._lhs = lhs;
		this._rhs = rhs;
	}

	@Override
	public org.asteriskjava.manager.action.ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.BridgeAction action = new org.asteriskjava.manager.action.BridgeAction();
		action.setActionId(this.getActionId());
		action.setChannel1(this._lhs.getChannelName());
		action.setChannel2(this._rhs.getChannelName());
		return action;
	}

	public String toString()
	{
		return "BridgeAction: lhs=" + this._lhs + " rhs=" + this._rhs; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
