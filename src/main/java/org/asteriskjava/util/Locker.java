package org.asteriskjava.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Locker
{

    private static final Log logger = LogFactory.getLog(Locker.class);

    private static volatile boolean diags = false;
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Map<Integer, LockStats> lockObjectMap = new WeakHashMap<>();

    private static final Object mapSync = new Object();

    // keep references to LockStats so the WeakHashMap won't remove them between
    // reporting intervals
    private static final List<LockStats> keepList = new LinkedList<>();

    public static LockCloser lock(final Object sync)
    {
        LockStats finalStats;
        synchronized (mapSync)
        {
            // find or create the semaphore for locking
            LockStats stats = lockObjectMap.get(System.identityHashCode(sync));
            if (stats == null)
            {
                String name = getCaller(sync);

                stats = new LockStats(sync, name);

                lockObjectMap.put(System.identityHashCode(sync), stats);
                if (diags)
                {
                    keepList.add(stats);
                }
            }
            finalStats = stats;
        }

        try
        {
            if (diags)
            {
                return lockWithDiags(finalStats);
            }
            return simpleLock(finalStats);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * determine the caller to Locker
     * 
     * @param object
     * @return
     */
    private static String getCaller(Object object)
    {
        Exception ex = new Exception();
        StackTraceElement[] trace = ex.getStackTrace();
        String name = object.getClass().getCanonicalName();
        for (StackTraceElement element : trace)
        {
            if (element.getFileName() != null && !element.getFileName().contains(Locker.class.getSimpleName()))
            {
                name = element.getFileName() + " " + element.getMethodName() + " " + element.getLineNumber() + " "
                        + element.getClassName();
                break;
            }
        }

        return name;
    }

    public interface LockCloser extends AutoCloseable
    {
        public void close();
    }

    private static LockCloser simpleLock(LockStats stats) throws InterruptedException
    {
        Semaphore lock = stats.semaphore;
        lock.acquire();

        return new LockCloser()
        {

            @Override
            public void close()
            {
                lock.release();
            }
        };
    }

    private static LockCloser lockWithDiags(LockStats stats) throws InterruptedException
    {

        int offset = stats.requested.incrementAndGet();
        long waitStart = System.currentTimeMillis();

        Semaphore lock = stats.semaphore;
        lock.acquire();
        stats.acquired.getAndIncrement();
        long acquiredAt = System.currentTimeMillis();

        return new LockCloser()
        {

            @Override
            public void close()
            {
                // ignore any wait that may have been caused by Locker code, so
                // count the waiters before we release the lock
                long waiters = stats.requested.get() - offset;
                stats.waited.addAndGet((int) waiters);

                // release the lock
                lock.release();

                // count the time waiting and holding the lock
                int holdTime = (int) (System.currentTimeMillis() - acquiredAt);
                int waitTime = (int) (acquiredAt - waitStart);
                stats.totalWaitTime.addAndGet(waitTime);
                stats.totalHoldTime.addAndGet(holdTime);

                if (waiters > 0 && holdTime > 1)
                {
                    // some threads waited
                    String message = "Lock held for (" + holdTime + "MS), " + waiters
                            + " threads waited for some of that time! " + getCaller(stats.object);
                    logger.warn(message);
                    if (holdTime > stats.getAverageHoldTime() * 10.0)
                    {
                        Exception trace = new Exception(message);
                        logger.error(trace, trace);
                    }
                }

                if (holdTime > stats.getAverageHoldTime() * 4.0)
                {
                    // long hold!
                    String message = "Lock hold of lock (" + holdTime + "MS), average is " + stats.getAverageHoldTime() + " "
                            + getCaller(stats.object);

                    logger.warn(message);
                    if (holdTime > stats.getAverageHoldTime() * 10.0)
                    {
                        Exception trace = new Exception(message);
                        logger.error(trace, trace);
                    }
                }
            }
        };

    }

    /**
     * start dumping lock stats once per minute, can't be stopped once started.
     */
    public static void enable()
    {
        if (!diags)
        {
            diags = true;
            executor.scheduleWithFixedDelay(() -> {
                dumpStats();
            }, 1, 1, TimeUnit.MINUTES);
        }
        else
        {
            logger.warn("Already enabled");
        }

    }

    private static void dumpStats()
    {
        List<LockStats> stats = new LinkedList<>();

        synchronized (mapSync)
        {
            stats.addAll(lockObjectMap.values());
            keepList.clear();
        }

        for (LockStats stat : stats)
        {
            if (stat.waited.get() > 0)
            {
                logger.warn(stat);
            }
        }

        logger.warn("Dump Lock Stats finished.");
    }

    static class LockStats
    {

        @Override
        public String toString()
        {
            return "LockStats [totalWaitTime=" + totalWaitTime + ", totalHoldTime=" + totalHoldTime + ", waited=" + waited
                    + ", acquired=" + acquired + ", object=" + name + "]";
        }

        final Semaphore semaphore = new Semaphore(1, true);

        // this reference to object stops the WeakRefHashMap removing this
        // LockStats from
        // the map
        final Object object;

        final String name;

        final AtomicInteger totalWaitTime = new AtomicInteger();
        final AtomicInteger totalHoldTime = new AtomicInteger();
        final AtomicInteger requested = new AtomicInteger();
        final AtomicInteger waited = new AtomicInteger();
        final AtomicInteger acquired = new AtomicInteger();

        LockStats(Object object, String name)
        {
            this.object = object;
            this.name = name;
        }

        double getAverageHoldTime()
        {
            long hold = totalHoldTime.get();
            long count = acquired.get();
            if (count < 10)
            {
                return 250.0;
            }
            return Math.max(hold / count, 50);
        }
    }

}
