package org.asteriskjava.lock;

import org.asteriskjava.lock.Locker.LockCloser;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class LockerTest {

    private final Lockable sync = new Lockable();

    @Test
    void test1() {
        Locker.disable();
        int seconds = 2;

        int a = 0;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < seconds * 1000) {
            try (LockCloser closer = sync.withLock()) {
                a++;
            }
        }
        System.out.println(a / seconds + " Locker  per second");

        Locker.enable();
        a = 0;
        start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < seconds * 1000) {
            try (LockCloser closer = sync.withLock()) {
                a++;
            }
        }
        System.out.println(a / seconds + " Locker With Diags  per second");

        a = 0;
        start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < seconds * 1000) {
            synchronized (sync) {
                a++;
            }
        }
        System.out.println(a / seconds + " Synchronized  per second");

    }

    volatile boolean stop = false;
    int concurrentCounter1 = 0;
    int concurrentCounter2 = 0;
    int concurrentCounter3 = 0;

    @Test
    void testConcurrency() throws InterruptedException {
        Lockable s1 = new Lockable();
        Lockable s2 = new Lockable();
        Lockable s3 = new Lockable();

        int seconds = 2;

        Locker.enable();
        stop = false;
        Runnable runable2 = () -> {
            while (!stop) {
                try (LockCloser closer = s1.withLock()) {
                    concurrentCounter2++;
                }
            }
        };

        new Thread(runable2).start();
        new Thread(runable2).start();
        TimeUnit.SECONDS.sleep(seconds);
        stop = true;
        TimeUnit.SECONDS.sleep(1);
        System.out.println(concurrentCounter2 / seconds + " Locker  per second with 2 threads and diags");

        Locker.disable();
        stop = false;

        Runnable runable = () -> {
            while (!stop) {
                try (LockCloser closer = s2.withLock()) {
                    concurrentCounter1++;
                }
            }
        };
        new Thread(runable).start();
        new Thread(runable).start();
        TimeUnit.SECONDS.sleep(seconds);
        stop = true;
        TimeUnit.SECONDS.sleep(1);
        System.out.println(concurrentCounter1 / seconds + " Locker  per second with 2 threads");

        Locker.dumpStats();
        stop = false;

        Runnable runable3 = () -> {
            while (!stop) {
                synchronized (s3) {
                    concurrentCounter3++;
                }
            }
        };
        new Thread(runable3).start();
        new Thread(runable3).start();
        TimeUnit.SECONDS.sleep(seconds);
        stop = true;
        TimeUnit.SECONDS.sleep(1);
        System.out.println(concurrentCounter3 / seconds + " synchronized  per second with 2 threads");

        System.out.println("\nIt seems wrong that 2 threads with diags is faster,\n"
                + "but the reality is that with the added overhead of diags there is less lock contention so it runs faster.\n"
                + "If more threads are added the two tests produce near identicaly through put as contention is high for both cases then.\n");

    }

}
