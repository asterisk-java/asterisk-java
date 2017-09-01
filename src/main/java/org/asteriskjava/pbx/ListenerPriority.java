package org.asteriskjava.pbx;

public enum ListenerPriority
{
	/**
	 * Provide access to events in near real time for specialised services that
	 * need priority access to events. Realtime events are sent before any other
	 * listeners get a chance to process the events. With the event dispatcher
	 * running in its own thread. This means that they are passed without delay.
	 * You must also ensure that you don't delay the processing of these
	 * messages as there may be real time event listeners. If you are concerned
	 * that you are going to delay the processing then you need to add in your
	 * own ManagerEventQueue to insulate other priority listeners from your
	 * tardiness. NOTE: you cannot rely on the LiveChannelManager as it
	 * processes events from the standard event queue. This means that Rename
	 * and Masquerade events MAY (very likely) not have been process when the
	 * realtime listener recieves an event.
	 */

	REALTIME(100), CRITICAL(10), HIGH(8), NORMAL(6), LOW(4);

	// The priority of the listeners. Higher priority listeners receive
	// events first.
	private int _priority;

	ListenerPriority(int priority)
	{
		this._priority = priority;
	}

	public int compare(ListenerPriority rhs)
	{
		return rhs._priority - this._priority;
	}
}