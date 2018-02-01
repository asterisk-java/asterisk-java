package org.asteriskjava.pbx.internal.core;

import java.util.Set;

import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;

/**
 * Allows a object to receive Manager event notification which are pre-filtered.
 * 
 * @author bsutton
 * 
 */
public interface FilteredManagerListener<T>
{
	/**
	 * Provides a unique name for the listener.
	 * 
	 * @return a unique name for the listener.
	 */
	String getName();

	/**
	 * Called whenever the listener is first added and any time any listener is
	 * removed to refresh the list of required events.
	 * 
	 * @return list of required events.
	 */
	Set<Class<? extends ManagerEvent>> requiredEvents();

	/**
	 * Called for each manager event that the connection receives for which the
	 * filterEvent returned true.
	 * 
	 * @param event
	 */
	void onManagerEvent(T event);

	/**
	 * Set the listeners priority. Higher priority listeners have events
	 * dispatched to them before lower priority listeners.
	 * 
	 * @return
	 */
	ListenerPriority getPriority();
}
