package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;

public class ResponseEvent extends ManagerEvent
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ResponseEvent.class);

	private final String actionId;
	private final String internalActionId;

	public ResponseEvent(final org.asteriskjava.manager.event.ResponseEvent event)
	{
		super(event);
		this.actionId = event.getActionId();
		this.internalActionId = event.getInternalActionId();
	}

	public String getActionId()
	{
		return this.actionId;
	}

	public String getInternalActionId()
	{
		return this.internalActionId;
	}

}
