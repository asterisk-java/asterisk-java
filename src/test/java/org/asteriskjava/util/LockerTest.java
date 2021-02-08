package org.asteriskjava.util;

import org.asteriskjava.util.Locker.LockCloser;
import org.junit.Test;

public class LockerTest
{

    private final Lockable sync = new Lockable();

    @Test
    public void test()
    {
        int seconds = 2;

        int a = 0;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < seconds * 1000)
        {
            try (LockCloser closer = sync.withLock())
            {
                a++;
            }
        }
        System.out.println(a / seconds + " Locker  per second");

        Locker.enable();
        a = 0;
        start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < seconds * 1000)
        {
            try (LockCloser closer = sync.withLock())
            {
                a++;
            }
        }
        System.out.println(a / seconds + " Locker With Diags  per second");

        a = 0;
        start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < seconds * 1000)
        {
            synchronized (sync)
            {
                a++;
            }
        }
        System.out.println(a / seconds + " Synchronized  per second");

    }

}
