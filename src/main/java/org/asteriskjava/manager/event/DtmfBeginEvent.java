package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class DtmfBeginEvent extends DtmfEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DtmfBeginEvent(Object source)
	{
		super(source);
		setBegin(true);
		setEnd(false);
	}
}
