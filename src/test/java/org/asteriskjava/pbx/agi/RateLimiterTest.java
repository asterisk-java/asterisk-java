package org.asteriskjava.pbx.agi;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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

            System.out.println(System.currentTimeMillis() + " test1 " + i);
            Thread.sleep(10);
        }

        // this should have taken around 5 seconds
        assertTrue(System.currentTimeMillis() - now > 4000L);
    }

    @Test
    public void test2() throws InterruptedException
    {
        long now = System.currentTimeMillis();
        RateLimiter limiter = new RateLimiter(3);
        AtomicLong counter = new AtomicLong();
        AtomicBoolean stop = new AtomicBoolean(false);
        Runnable runner = () -> {
            for (int i = 0; i < 15 && !stop.get(); i++)
            {
                try
                {
                    limiter.acquire();
                    System.out.println(System.currentTimeMillis() + " test2 " + i);
                    counter.getAndIncrement();
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        };

        new Thread(runner).start();
        new Thread(runner).start();
        new Thread(runner).start();

        while (counter.intValue() < 15)
        {
            Thread.sleep(10);
        }
        stop.set(true);
        Thread.sleep(1000);

        // this should have taken around 5 seconds
        assertTrue(System.currentTimeMillis() - now > 4000L);
    }

    @Test
    public void test3() throws InterruptedException
    {
        long now = System.currentTimeMillis();
        RateLimiter limiter = new RateLimiter(3);
        AtomicLong counter = new AtomicLong();
        AtomicBoolean stop = new AtomicBoolean(false);

        Runnable runner = () -> {
            for (int i = 0; i < 15 && !stop.get();)
            {
                try
                {
                    if (limiter.tryAcquire())
                    {
                        i++;
                        System.out.println(System.currentTimeMillis() + " test3 " + i);
                        counter.getAndIncrement();

                    }
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        };

        new Thread(runner).start();
        new Thread(runner).start();
        new Thread(runner).start();

        while (counter.intValue() < 15)
        {
            Thread.sleep(10);
        }
        stop.set(true);
        // this should have taken around 5 seconds
        assertTrue(System.currentTimeMillis() - now > 4000L);
    }
}
