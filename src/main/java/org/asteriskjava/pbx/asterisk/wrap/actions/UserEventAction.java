package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.asterisk.wrap.userEvents.UserEvent;

abstract public class UserEventAction extends AbstractManagerAction
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserEventAction.class);

	private String _actionId;

	public UserEventAction(UserEvent event)
	{
		// NOOP
	}

	public void setActionId(String actionId)
	{
		this._actionId = actionId;
	}

	public String getActionId()
	{
		return this._actionId;
	}

}
