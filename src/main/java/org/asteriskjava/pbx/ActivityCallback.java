package org.asteriskjava.pbx;

/**
 * Provides a generic callback mechansim which allows tracking of the progress
 * of any of the async methods provided by iPBX.
 */

public interface ActivityCallback<T extends Activity>
{
	/**
	 * Called as soon as the task being tracked is started.
	 * 
	 * @param activity
	 *            - the activity which is being monitored
	 */
	void start(T activity);

	/**
	 * Called periodically to indicate progress with the task being tracked. The
	 * number of progress messages sent is dependant on the task. Some tasks may
	 * not send any progress messages.
	 * 
	 * @param activity
	 *            - the activity which is being monitored
	 * @param message
	 */
	void progess(T activity, String message);

	/**
	 * Called when the task being tracked has completed.
	 * 
	 * @param activity
	 *            - the activity which is being monitored
	 * @param success
	 *            - true if the activity completed successfully.
	 */
	void completed(T activity, boolean success);

}
