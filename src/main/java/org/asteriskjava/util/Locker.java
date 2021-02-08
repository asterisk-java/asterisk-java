package org.asteriskjava.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Locker
{

    private static final Log logger = LogFactory.getLog(Locker.class);

    private static volatile boolean diags = false;
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Object sync = new Object();

    // keep references to LockStats so the WeakHashMap won't remove them between
    // reporting intervals
    private static final Map<Long, Lockable> keepList = new HashMap<>();

    public static LockCloser doWithLock(final Lockable lockable)
    {
        if (diags)
        {
            synchronized (sync)
            {
                keepList.put(lockable.getLockableId(), lockable);
            }
        }

        try
        {
            if (diags)
            {
                return lockWithDiags(lockable);
            }
            return simpleLock(lockable);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * determine the caller to Locker
     * 
     * @param semaphore
     * @return
     */
    static String getCaller(Lockable lockable)
    {
        Exception ex = new Exception();
        StackTraceElement[] trace = ex.getStackTrace();
        String name = lockable.getClass().getCanonicalName();
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

    private static LockCloser simpleLock(Lockable stats) throws InterruptedException
    {
        Semaphore lock = stats.getSemaphore();
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

    private static LockCloser lockWithDiags(Lockable lockable) throws InterruptedException
    {

        int offset = lockable.addRequested();
        long waitStart = System.currentTimeMillis();

        int ctr = 0;
        Semaphore lock = lockable.getSemaphore();
        while (!lock.tryAcquire(250, TimeUnit.MILLISECONDS))
        {
            ctr++;
            dumpBlocker(lockable, ctr);
        }
        lockable.setDumped(false);
        lockable.addAcquired();

        long acquiredAt = System.currentTimeMillis();
        lockable.threadHoldingLock.set(Thread.currentThread());

        return new LockCloser()
        {

            @Override
            public void close()
            {
                // ignore any wait that may have been caused by Locker code, so
                // count the waiters before we release the lock
                long waiters = lockable.getRequested() - offset;
                lockable.addWaited((int) waiters);

                boolean dumped = lockable.isDumped();
                // release the lock
                lock.release();

                // count the time waiting and holding the lock
                int holdTime = (int) (System.currentTimeMillis() - acquiredAt);
                int waitTime = (int) (acquiredAt - waitStart);
                lockable.addTotalWaitTime(waitTime);
                lockable.addTotalHoldTime(holdTime);

                long averageHoldTime = lockable.getAverageHoldTime();
                if (waiters > 0 && holdTime > averageHoldTime * 2 || dumped)
                {
                    // some threads waited
                    String message = "Lock held for (" + holdTime + "MS), " + waiters
                            + " threads waited for some of that time! " + getCaller(lockable);
                    logger.warn(message);
                    if (holdTime > averageHoldTime * 10.0 || dumped)
                    {
                        Exception trace = new Exception(message);
                        logger.error(trace, trace);
                    }
                }

                if (holdTime > averageHoldTime * 5.0)
                {
                    // long hold!
                    String message = "Lock hold of lock (" + holdTime + "MS), average is " + lockable.getAverageHoldTime()
                            + " " + getCaller(lockable);

                    logger.warn(message);
                    if (holdTime > averageHoldTime * 10.0)
                    {
                        Exception trace = new Exception(message);
                        logger.error(trace, trace);
                    }
                }
            }
        };

    }

    private static void dumpBlocker(Lockable lockable, int ctr)
    {
        synchronized (lockable)
        {
            if (!lockable.isDumped())
            {

                Thread thread = lockable.threadHoldingLock.get();
                lockable.setDumped(true);
                if (thread != null)
                {

                    StackTraceElement[] trace = thread.getStackTrace();

                    String dump = "";

                    int i = 0;
                    for (; i < trace.length; i++)
                    {
                        StackTraceElement ste = trace[i];
                        dump += "\tat " + ste.toString();
                        dump += '\n';
                    }
                    logger.error("Waiting on lock... blocked by...");
                    logger.error(dump);
                }
                else
                {
                    logger.error("Thread hasn't been set");
                }

            }
            else
            {
                logger.warn("Still waiting " + ctr);
            }
        }
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

    private static volatile boolean first = true;

    private static void dumpStats()
    {
        List<Lockable> lockables = new LinkedList<>();

        synchronized (sync)
        {
            lockables.addAll(keepList.values());
            keepList.clear();
        }

        boolean activity = false;
        for (Lockable lockable : lockables)
        {
            if ((lockable.getWaited() > 0 && lockable.getTotalWaitTime() > lockable.getAverageHoldTime()) || first)
            {
                activity = true;
                logger.warn(lockable.asString());
            }
        }

        if (first || activity)
        {
            logger.warn("Dump Lock Stats finished. Will dump every minute when there is contention...");
            first = false;
        }
    }

}
