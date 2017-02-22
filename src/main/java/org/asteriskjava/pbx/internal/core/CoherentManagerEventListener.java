package org.asteriskjava.pbx.internal.core;

import java.util.EventListener;

import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;

public interface CoherentManagerEventListener extends EventListener
{
	/**
	 * This method is called when an event is received.
	 * 
	 * @param event
	 *            the event that has been received
	 */
	void onManagerEvent(ManagerEvent event);
}
