package org.asteriskjava.pbx.asterisk.wrap.actions;

/**
 * Automatically generates a globally unique action id (based on the systems mac
 * address
 * 
 * @author bsutton
 * 
 */
abstract public class AbstractManagerAction implements ManagerAction
{

	private static long _nextActionId = 1;

	private String _actionId;

	AbstractManagerAction()
	{
		this._actionId = generateActionId();
	}

	public String getActionId()
	{
		return this._actionId;
	}

	synchronized public String generateActionId()
	{

		return "" + _nextActionId++;
	}

	protected void setActionId(String actionId)
	{
		this._actionId = actionId;
	}

}
