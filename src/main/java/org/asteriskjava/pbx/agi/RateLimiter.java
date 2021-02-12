package org.asteriskjava.pbx.agi;

public class RateLimiter
{
    private static final long ONE_THOUSAND_MILLIS = 1000L;
    long next = 0;
    private final long step;

    /**
     * thread safe!
     * 
     * @param perSecond - number of 'acquire()' calls allowed per second, a call
     *            to acquire() will block(sleep) if the per second limit is
     *            exceeded
     */
    public RateLimiter(int perSecond)
    {
        step = ONE_THOUSAND_MILLIS / perSecond;
    }

    void acquire() throws InterruptedException
    {
        // can't use Locker here, as Locker uses this class
        long delay;
        synchronized (this)
        {
            delay = next;
            if (next < System.currentTimeMillis())
            {
                next = System.currentTimeMillis() + step;
            }
            else
            {
                next += step;
            }
        }

        long now = System.currentTimeMillis();
        long timeRemaining = delay - now;
        if (timeRemaining > 0)
        {
            Thread.sleep(timeRemaining);
        }

    }

    public boolean tryAcquire() throws InterruptedException
    {
        // can't use Locker here, as Locker uses this class
        synchronized (this)
        {
            if (next < System.currentTimeMillis())
            {
                next = System.currentTimeMillis() + step;
                return true;
            }
            return false;
        }
    }
}
