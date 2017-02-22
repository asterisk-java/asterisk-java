package org.asteriskjava.pbx.agi;

import java.util.LinkedList;
import java.util.List;

public class RateLimiter
{
    private static final long ONE_THOUSAND_MILLIS = 1000L;
    List<Long> available = new LinkedList<>();

    /**
     * this is NOT thread safe!
     * 
     * @param perSecond - number of 'acquire()' calls allowed per second, a call
     *            to acquire() will block(sleep) if the per second limit is
     *            exceeded
     */
    RateLimiter(int perSecond)
    {
        long now = System.currentTimeMillis();
        for (int i = 0; i < perSecond; i++)
        {
            available.add(now - ONE_THOUSAND_MILLIS);
        }
    }

    void acquire() throws InterruptedException
    {
        long now = System.currentTimeMillis();
        Long next = available.remove(0);
        long timeRemaining = next - now;
        if (timeRemaining > 0)
        {
            Thread.sleep(timeRemaining);
        }
        available.add(System.currentTimeMillis() + ONE_THOUSAND_MILLIS);
    }
}
