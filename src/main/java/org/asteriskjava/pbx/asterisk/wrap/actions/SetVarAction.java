package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.pbx.Channel;

public class SetVarAction extends AbstractManagerAction
{

	private final Channel _channel;
	private String _variableName;
	private final String _value;

	public SetVarAction(final Channel channel, final String variableName, final String value)
	{
		this._channel = channel;
		this._variableName = variableName;
		this._value = value;
	}

	@Override
	public org.asteriskjava.manager.action.ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.SetVarAction action = new org.asteriskjava.manager.action.SetVarAction();
		action.setActionId(this.getActionId());
		action.setChannel(this._channel.getChannelName());
		action.setVariable(this._variableName);
		action.setValue(this._value);

		return action;
	}

	public void setVariable(final String variableName)
	{
		this._variableName = variableName;

	}

	public String toString()
	{
		return "SetVarAction: channel=" + this._channel + " variableName=" + this._variableName + " value=" + this._value; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
