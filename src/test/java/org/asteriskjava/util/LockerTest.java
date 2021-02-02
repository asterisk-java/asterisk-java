package org.asteriskjava.util;

import org.asteriskjava.util.Locker.LockCloser;
import org.junit.Test;

public class LockerTest
{

    private final Object sync = new Object();

    @Test
    public void test()
    {
        int seconds = 2;

        int a = 0;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < seconds * 1000)
        {
            try (LockCloser closer = Locker.lock(sync))
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
            try (LockCloser closer = Locker.lock(sync))
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
