package org.asteriskjava.pbx;

public interface CallerID
{
	/**
	 * Gets the number component of the caller id.
	 * 
	 * @return the callerid's number
	 */
	String getNumber();

	/**
	 * Gets the name component of the caller id.
	 * 
	 * @return the callerid's name
	 */
	String getName();

	/**
	 * Controls whether this caller id should be presented to the remote end.
	 * 
	 * @param hide
	 *            - if true then the caller id will be hidden.
	 */
	void setHideCallerID(boolean hide);

	/**
	 * 
	 * @return true if the caller id is marked as hidden.
	 */
	boolean isHideCallerID();

	/**
	 * Returns true if the caller id number is blank or equal to 'unknown'.
	 * 
	 * @return
	 */
	boolean isUnknown();

}
