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
     *            exceeded. <br>
     *            <br>
     *            NOTE: Max rate is 1000 per second
     */
    public RateLimiter(double perSecond)
    {
        step = Math.max(1, (long) (ONE_THOUSAND_MILLIS / perSecond));
    }

    void acquire() throws InterruptedException
    {
        // can't use Locker here, as Locker uses this class
        long delay;
        long now;
        synchronized (this)
        {
            now = System.currentTimeMillis();
            delay = next;
            if (next < now)
            {
                next = now + step;
            }
            else
            {
                next += step;
            }
        }

        long timeRemaining = delay - now;
        if (timeRemaining > 0)
        {
            // Very important that this sleep does NOT happen in the
            // synchronized block
            Thread.sleep(timeRemaining);
        }

    }

    public boolean tryAcquire()
    {
        // can't use Locker here, as Locker uses this class
        synchronized (this)
        {
            long now = System.currentTimeMillis();
            if (next < now)
            {
                next = now + step;
                return true;
            }
            return false;
        }
    }
}
