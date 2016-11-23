package org.asteriskjava.pbx.asterisk.wrap.response;

import org.apache.log4j.Logger;

public class ManagerError extends ManagerResponse
{

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ManagerError.class);

	public ManagerError(org.asteriskjava.manager.response.ManagerError error)
	{
		super(error);
	}

}
