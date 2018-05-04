package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BridgeCreateEvent extends AbstractBridgeEvent
{
	private String bridgevideosourcemode;

	/**
	 * @param bridgevideosourcemode the bridgevideosourcemode to set
	 */
	public void setBridgevideosourcemode(String bridgevideosourcemode) {
		this.bridgevideosourcemode = bridgevideosourcemode;
	}

	/**
	 * @return the bridgevideosourcemode
	 */
	public String getBridgevideosourcemode() {
		return bridgevideosourcemode;
	}

	private static final long serialVersionUID = 1L;

	public BridgeCreateEvent(Object source)
	{
		super(source);
	}

	
}
