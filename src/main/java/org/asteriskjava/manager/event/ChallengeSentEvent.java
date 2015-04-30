package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class ChallengeSentEvent extends ManagerEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String severity;
	private Integer eventVersion;
	private String accountId;
	private String service;
	private String eventtv;
	private String remoteAddress;
	private String localAddress;
	private String challenge;
	private String sessionId;

	public ChallengeSentEvent(Object source)
	{
		super(source);
	}

	public String getSeverity()
	{
		return severity;
	}

	public void setSeverity(String severity)
	{
		this.severity = severity;
	}

	public Integer getEventVersion()
	{
		return eventVersion;
	}

	public void setEventVersion(Integer eventVersion)
	{
		this.eventVersion = eventVersion;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getService()
	{
		return service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public String getEventtv()
	{
		return eventtv;
	}

	public void setEventtv(String eventtv)
	{
		this.eventtv = eventtv;
	}

	public String getRemoteAddress()
	{
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress)
	{
		this.remoteAddress = remoteAddress;
	}

	public String getLocalAddress()
	{
		return localAddress;
	}

	public void setLocalAddress(String localAddress)
	{
		this.localAddress = localAddress;
	}

	public String getChallenge()
	{
		return challenge;
	}

	public void setChallenge(String challenge)
	{
		this.challenge = challenge;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
}
