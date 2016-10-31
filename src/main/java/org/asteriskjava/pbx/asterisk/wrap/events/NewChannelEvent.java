package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.InvalidChannelName;

public class NewChannelEvent extends AbstractChannelStateEvent
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(NewChannelEvent.class);

	private final String accountCode;
	private final String context;
	private final String exten;

	public NewChannelEvent(final org.asteriskjava.manager.event.NewChannelEvent event) throws InvalidChannelName
	{
		super(event);
		this.accountCode = event.getAccountCode();
		this.context = event.getContext();
		this.exten = event.getExten();
	}

	public String getAccountCode()
	{
		return this.accountCode;
	}

	public String getContext()
	{
		return this.context;
	}

	public String getExten()
	{
		return this.exten;
	}

	public String toString()
	{
		return "NewChannelEvent: channel: " + this.getChannel() + " context=" + this.context + " exten=" + this.exten + " state=" + this.getChannelStateDesc(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

}
