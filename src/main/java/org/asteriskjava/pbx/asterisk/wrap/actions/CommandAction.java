package org.asteriskjava.pbx.asterisk.wrap.actions;

public class CommandAction extends AbstractManagerAction
{

	private String _command;

	public CommandAction(final String command)
	{
		this._command = command;
	}

	public CommandAction()
	{
	}

	@Override
	public org.asteriskjava.manager.action.ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.CommandAction action = new org.asteriskjava.manager.action.CommandAction();
		action.setCommand(this._command);
		action.setActionId(this.getActionId());

		return action;
	}

	public String toString()
	{
		return "CommandAction: command=" + this._command; //$NON-NLS-1$
	}

}
