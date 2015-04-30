package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class AgiExecStartEvent extends AgiExecEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AgiExecStartEvent(Object source)
	{
		super(source);
		setSubEvent(SUB_EVENT_START);
	}
}
