package org.asteriskjava.pbx;

import org.apache.log4j.Logger;

public class PBXException extends Exception
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PBXException.class);

	public PBXException(String message, final Exception e)
	{
		super(message, e);
	}

	public PBXException(final String message)
	{
		super(message);
	}

	public PBXException(Throwable e)
	{
		super(e);
	}
}
