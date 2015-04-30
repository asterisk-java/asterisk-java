package org.asteriskjava.manager.event;

public class DialEndEvent extends DialEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String language;
	private String destlanguage;

	public DialEndEvent(Object source)
	{
		super(source);
		setSubEvent(SUBEVENT_END);
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getDestLanguage()
	{
		return destlanguage;
	}

	public void setDestLanguage(String destlanguage)
	{
		this.destlanguage = destlanguage;
	}
}
