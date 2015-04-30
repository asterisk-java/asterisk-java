package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class AsyncAgiStartEvent extends AsyncAgiEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AsyncAgiStartEvent(Object source)
	{
		super(source);
		setSubEvent(SUB_EVENT_START);
	}
}
