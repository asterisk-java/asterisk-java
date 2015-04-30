package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BridgeDestroyEvent extends AbstractBridgeEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BridgeDestroyEvent(Object source)
	{
		super(source);
	}
}
