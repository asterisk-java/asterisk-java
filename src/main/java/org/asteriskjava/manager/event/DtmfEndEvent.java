package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class DtmfEndEvent extends DtmfEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer durationMs;

	public DtmfEndEvent(Object source)
	{
		super(source);
		setBegin(false);
		setEnd(true);
	}

	public Integer getDurationMs()
	{
		return durationMs;
	}

	public void setDurationMs(Integer durationMs)
	{
		this.durationMs = durationMs;
	}
}
