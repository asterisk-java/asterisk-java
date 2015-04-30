package org.asteriskjava.manager.event;

public class PickupEvent extends ManagerEvent
{
    private String channel;
    private String targetchannel;
    
	public PickupEvent(Object source) 
	{
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getTargetchannel()
	{
		return targetchannel;
	}

	public void setTargetchannel(String targetchannel)
	{
		this.targetchannel = targetchannel;
	}

}
