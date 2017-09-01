package org.asteriskjava.pbx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * This is a utility class designed to wait for an activity to complete. When
 * starting an activity for that you want to wait until completion pass this
 * class as the iCallback listener and then call waitForCompletion.
 * waitForCompletion will not return until the activity completes.
 * 
 * @author bsutton
 * @param <T>
 */
public class CompletionAdaptor<T extends Activity> implements ActivityCallback<T>
{
    private static final Log logger = LogFactory.getLog(CompletionAdaptor.class);

    CountDownLatch _latch = new CountDownLatch(1);

    @Override
    public void progress(final T activity, ActivityStatusEnum status, String message)
    {
        if (status == ActivityStatusEnum.FAILURE || status == ActivityStatusEnum.SUCCESS)
        {
            _latch.countDown();
        }
    }

    public void waitForCompletion(long timeout, TimeUnit unit)
    {
        // wait until the activity completes
        try
        {
            if (!_latch.await(timeout, unit))
            {
                Exception e = new Exception("Timeout waiting for activity to complete (" + timeout + " " + unit + ")");
                logger.error(e, e);
            }
        }
        catch (final InterruptedException e)
        {
            // should never happen
            CompletionAdaptor.logger.error(e, e);
        }

    }

}
