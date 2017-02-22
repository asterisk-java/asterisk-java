package org.asteriskjava.pbx;

/**
 * Provides a generic callback mechansim which allows tracking of the progress
 * of any of the async methods provided by iPBX.
 */

public interface ActivityCallback<T extends Activity>
{

    /**
     * Called periodically to indicate progress with the task being tracked. The
     * number of progress messages sent is dependant on the task. Every activity
     * will see a ActivityStatusEnum.START and either ActivityStatusEnum.SUCCESS
     * or ActivityStatusEnum.FAILURE
     * 
     * @param activity - the activity which is being monitored
     * @param status TODO
     * @param message
     */

    void progress(T activity, ActivityStatusEnum status, String message);

}
