package org.asteriskjava.pbx.asterisk.wrap.response;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ManagerResponse
{

	private Date _dateReceived;
	private String _actionId;

	/**
	 * The server from which this response has been received (only used with
	 * AstManProxy).
	 */
	final private String _server;
	final private String _response;
	final private String _eventList;
	final private String message;
	final private String _uniqueId;
	final private Map<String, Object> _attributes;

	public ManagerResponse(org.asteriskjava.manager.response.ManagerResponse response)
	{
		this._dateReceived = response.getDateReceived();
		this._actionId = response.getActionId();
		this._server = response.getServer();
		this._response = response.getResponse();
		this._eventList = response.getEventList();
		this._uniqueId = response.getUniqueId();
		this._attributes = response.getAttributes();
		this.message = response.getMessage();
	}

	public Date getDateReceived()
	{
		return this._dateReceived;
	}

	public String getActionId()
	{
		return this._actionId;
	}

	public String getServer()
	{
		return this._server;
	}

	public String getResponse()
	{
		return this._response;
	}

	public String getEventList()
	{
		return this._eventList;
	}

	public String getMessage()
	{
		return this.message;
	}

	public String getUniqueId()
	{
		return this._uniqueId;
	}

	public Map<String, Object> getAttributes()
	{
		return this._attributes;
	}

	public String getAttribute(String key)
	{
		return (String) this._attributes.get(key.toLowerCase(Locale.ENGLISH));
	}

	public boolean isSuccess()
	{
		return this._response.compareToIgnoreCase("Success") == 0; //$NON-NLS-1$
	}

}
