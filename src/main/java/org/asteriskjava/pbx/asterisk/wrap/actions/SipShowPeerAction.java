package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.pbx.EndPoint;

public class SipShowPeerAction extends AbstractManagerAction
{

	private final EndPoint _peer;

	public SipShowPeerAction(final EndPoint peer)
	{
		this._peer = peer;
	}

	@Override
	public ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.SipShowPeerAction action = new org.asteriskjava.manager.action.SipShowPeerAction();
		action.setActionId(this.getActionId());
		action.setPeer(this._peer.getSimpleName());
		return action;
	}

	public String toString()
	{
		return "SipShowPeerAction: peer=" + this._peer; //$NON-NLS-1$
	}

}
