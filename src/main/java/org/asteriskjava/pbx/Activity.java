package org.asteriskjava.pbx;

public interface Activity
{
	/**
	 * If an activity fails the getLastError contains a description of the cause
	 * of the error. This method should be called from the 'complete' method of
	 * the iCallBack listener.
	 * 
	 * @return error message which caused the activity to fail.
	 */
	Throwable getLastException();

	/**
	 * Set to true once the activity has suceeded.
	 * 
	 * @return
	 */
	boolean isSuccess();

}
