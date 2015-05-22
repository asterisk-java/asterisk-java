package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class MusicOnHoldStartEvent extends MusicOnHoldEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MusicOnHoldStartEvent(Object source)
	{
		super(source);
		setState(STATE_START);
	}
}
