package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class AsyncAgiEndEvent extends AsyncAgiEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AsyncAgiEndEvent(Object source)
	{
		super(source);
		setSubEvent(SUB_EVENT_END);
	}
}
