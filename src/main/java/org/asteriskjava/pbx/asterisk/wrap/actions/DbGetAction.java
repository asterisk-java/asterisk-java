package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.manager.action.ManagerAction;

public class DbGetAction extends AbstractManagerAction
{

	private final String _key;
	private final String _family;

	public DbGetAction(final String family, final String key)
	{
		this._family = family;
		this._key = key;
	}

	@Override
	public ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.DbGetAction action = new org.asteriskjava.manager.action.DbGetAction();
		action.setActionId(this.getActionId());
		action.setKey(this._key);
		action.setFamily(this._family);

		return action;
	}

	public String toString()
	{
		return "DbGetAction: key=" + this._key + " family=" + this._family; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
