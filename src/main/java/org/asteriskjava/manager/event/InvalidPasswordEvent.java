package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/23/15.
 */
public class InvalidPasswordEvent extends ManagerEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String severity;
	private Integer eventVersion;
	private String eventtv;
	private String sessionid;
	private String receivedHash;
	private String localaddress;
	private String accountId;
	private String receivedChallenge;
	private String service;
	private String remoteAddress;
	private String challenge;

	public InvalidPasswordEvent(Object source)
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

	public String getEventtv()
	{
		return eventtv;
	}

	public void setEventtv(String eventtv)
	{
		this.eventtv = eventtv;
	}

	public String getSessionid()
	{
		return sessionid;
	}

	public void setSessionid(String sessionid)
	{
		this.sessionid = sessionid;
	}

	public String getReceivedHash()
	{
		return receivedHash;
	}

	public void setReceivedHash(String receivedHash)
	{
		this.receivedHash = receivedHash;
	}

	public String getLocaladdress()
	{
		return localaddress;
	}

	public void setLocaladdress(String localaddress)
	{
		this.localaddress = localaddress;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getReceivedChallenge()
	{
		return receivedChallenge;
	}

	public void setReceivedChallenge(String receivedChallenge)
	{
		this.receivedChallenge = receivedChallenge;
	}

	public String getService()
	{
		return service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public String getRemoteAddress()
	{
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress)
	{
		this.remoteAddress = remoteAddress;
	}

	public String getChallenge()
	{
		return challenge;
	}

	public void setChallenge(String challenge)
	{
		this.challenge = challenge;
	}
}
