package org.asteriskjava.pbx.asterisk.wrap.response;

import java.util.List;

import org.apache.log4j.Logger;

public class CommandResponse extends ManagerResponse
{

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CommandResponse.class);
	private List<String> result;

	public CommandResponse(org.asteriskjava.manager.response.CommandResponse response)
	{
		super(response);
		this.result = response.getResult();

	}

	public List<String> getResult()
	{
		return this.result;
	}
}
