package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.pbx.Channel;

public class GetVarAction extends AbstractManagerAction
{

	private final Channel _channel;
	private String _variableName;

	public GetVarAction(final Channel channel, String variableName)
	{
		this._channel = channel;
		this._variableName = variableName;
	}

	@Override
	public org.asteriskjava.manager.action.ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.GetVarAction action = new org.asteriskjava.manager.action.GetVarAction();
		action.setActionId(this.getActionId());
		action.setChannel(this._channel.getChannelName());
		action.setVariable(this._variableName);

		return action;
	}

	public String toString()
	{
		return "GetVarAction: channel=" + this._channel + " variableName=" + this._variableName; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
