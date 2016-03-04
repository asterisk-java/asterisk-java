package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BridgeLeaveEvent extends AbstractBridgeEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uniqueId;
	private String channel;
	private String language;
	private String linkedId;
	
	public BridgeLeaveEvent(Object source)
	{
		super(source);
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getUniqueId()
	{
		return uniqueId;
	}

	public void setUniqueId(String uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

  
}
