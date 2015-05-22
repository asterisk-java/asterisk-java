package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class MusicOnHoldStopEvent extends MusicOnHoldEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MusicOnHoldStopEvent(Object source)
	{
		super(source);
		setState(STATE_STOP);
	}
}
