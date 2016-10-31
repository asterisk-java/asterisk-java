package org.asteriskjava.pbx.asterisk.wrap.events;

import java.util.ArrayList;
import java.util.Collection;

public class ResponseEvents
{

	ArrayList<ResponseEvent> events = new ArrayList<>();

	/**
	 * Returns a Collection of ManagerEvents that have been received including
	 * the last one that indicates completion.
	 * 
	 * @return a Collection of ManagerEvents received.
	 */
	public Collection<ResponseEvent> getEvents()
	{
		return this.events;
	}

	public void add(ResponseEvent event)
	{
		this.events.add(event);

	}
}
