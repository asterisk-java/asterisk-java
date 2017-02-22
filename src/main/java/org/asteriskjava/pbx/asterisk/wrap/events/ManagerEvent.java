package org.asteriskjava.pbx.asterisk.wrap.events;

import java.util.EventObject;

import org.apache.log4j.Logger;

public class ManagerEvent extends EventObject
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ManagerEvent.class);

	public ManagerEvent(Object source)
	{
		super(source);
	}

}
