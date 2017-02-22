package org.asteriskjava.pbx.agi;

import static org.junit.Assert.assertTrue;

import org.asteriskjava.pbx.agi.RateLimiter;
import org.junit.Test;

public class RateLimiterTest
{

    @Test
    public void test() throws InterruptedException
    {
        long now = System.currentTimeMillis();
        RateLimiter limiter = new RateLimiter(3);
        for (int i = 0; i < 15; i++)
        {
            limiter.acquire();

            System.out.println(System.currentTimeMillis());
            Thread.sleep(100);
        }

        // this should have taken around 5 seconds
        assertTrue(System.currentTimeMillis() - now > 4000L);
    }

}
