package org.asteriskjava.lock;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.asteriskjava.pbx.agi.RateLimiter;
import org.asteriskjava.pbx.util.LogTime;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class Locker
{

    private static final Log logger = LogFactory.getLog(Locker.class);

    private static volatile boolean diags = false;

    private static ScheduledFuture< ? > future;
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Object sync = new Object();

    // keep references to LockStats so the WeakHashMap won't remove them between
    // reporting intervals
    private static final Map<Long, Lockable> keepList = new HashMap<>();

    private static final RateLimiter waitRateLimiter = new RateLimiter(4);

    public static LockCloser doWithLock(final Lockable lockable)
    {
        try
        {
            if (diags)
            {
                synchronized (sync)
                {
                    keepList.put(lockable.getLockableId(), lockable);
                }
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
     * @param lockable
     * @return
     */
    static String getCaller(Lockable lockable)
    {
        StackTraceElement[] trace = new Exception().getStackTrace();
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

    // Once every ten seconds max
    private static RateLimiter warnRateLimiter = new RateLimiter(0.1);

    private static LockCloser simpleLock(Lockable lockable) throws InterruptedException
    {
        LogTime acquireTimer = new LogTime();
        ReentrantLock lock = lockable.getInternalLock();
        lock.lock();
        // lock acquired!
        if (acquireTimer.timeTaken() > 1_000 && warnRateLimiter.tryAcquire())
        {
            logger.warn("Locks are being held for to long, you can enable lock diagnostics by calling Locker.enable()");
        }

        LogTime holdTimer = new LogTime();
        return () -> {
            lock.unlock();
            if (holdTimer.timeTaken() > 500 && warnRateLimiter.tryAcquire())
            {
                logger.warn("Locks are being held for to long, you can enable lock diagnostics by calling Locker.enable()");
            }
        };
    }

    private static LockCloser lockWithDiags(Lockable lockable) throws InterruptedException
    {

        int offset = lockable.addLockRequested();
        long waitStart = System.currentTimeMillis();

        ReentrantLock lock = lockable.getInternalLock();
        while (!lock.tryLock(100, TimeUnit.MILLISECONDS))
        {
            if (!lockable.isLockDumped() && lockable.getDumpRateLimit().tryAcquire())
            {
                lockable.setLockDumped(true);
                dumpThread(lockable.threadHoldingLock.get(),
                        "Waiting on lock... blocked by... id:" + lockable.getLockableId());
            }
            else
            {
                if (waitRateLimiter.tryAcquire())
                {
                    long elapsed = System.currentTimeMillis() - waitStart;
                    logger.warn("waiting " + elapsed + "(MS) id:" + lockable.getLockableId());
                }
            }
            lockable.setLockBlocked(true);
        }
        lockable.setLockDumped(false);
        lockable.addLockAcquired(1);

        long acquiredAt = System.currentTimeMillis();
        lockable.threadHoldingLock.set(Thread.currentThread());
        return new LockCloser()
        {

            @Override
            public void close()
            {
                // ignore any wait that may have been caused by Locker code, so
                // count the waiters before we release the lock
                long waiters = lockable.getLockRequested() - offset;
                lockable.addLockWaited((int) waiters);

                boolean dumped = lockable.isLockDumped();
                // release the lock
                lock.unlock();

                // count the time waiting and holding the lock
                int holdTime = (int) (System.currentTimeMillis() - acquiredAt);
                int waitTime = (int) (acquiredAt - waitStart);
                lockable.addLockTotalWaitTime(waitTime);
                lockable.addLockTotalHoldTime(holdTime);

                long averageHoldTime = lockable.getLockAverageHoldTime();
                if ((waiters > 0 && holdTime > averageHoldTime * 2) || dumped)
                {
                    // some threads waited
                    String message = "Lock held for (" + holdTime + "MS), " + waiters
                            + " threads waited for some of that time! " + getCaller(lockable) + " id:"
                            + lockable.getLockableId();
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
                    String message = "Lock hold of lock (" + holdTime + "MS), average is "
                            + lockable.getLockAverageHoldTime() + " " + getCaller(lockable) + " id:"
                            + lockable.getLockableId();

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

    public static void dumpThread(Thread thread, String message)
    {

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
            logger.error(message);
            logger.error(dump);
        }
        else
        {
            logger.error("Thread hasn't been set: " + message);
        }

    }

    /**
     * start dumping lock stats once per minute, can't be stopped once started.
     */
    public static void enable()
    {
        synchronized (sync)
        {
            if (!diags)
            {
                diags = true;
                future = executor.scheduleWithFixedDelay(() -> {
                    dumpStats();
                }, 1, 1, TimeUnit.MINUTES);
                logger.warn("Lock checking enabled");
            }
            else
            {
                logger.warn("Already enabled");
            }
        }
    }

    public static void disable()
    {
        synchronized (sync)
        {
            if (diags)
            {
                diags = false;
                future.cancel(false);
                dumpStats();
                logger.warn("Lock checking disabled");
            }
            else
            {
                logger.warn("Lock checking is already disabled");
            }
        }
    }

    private static volatile boolean first = true;

    static void dumpStats()
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
            if (lockable.wasLockBlocked())
            {
                int waited = lockable.getLockWaited();
                int waitTime = lockable.getLockTotalWaitTime();
                int acquired = lockable.getLockAcquired();
                int holdTime = lockable.getLockTotalHoldTime();
                lockable.setLockBlocked(false);
                activity = true;
                logger.warn(lockable.asLockString());
                lockable.addLockWaited(-waited);
                lockable.addLockTotalWaitTime(-waitTime);

                lockable.addLockAcquired(-acquired);
                lockable.addLockTotalHoldTime(-holdTime);

            }
        }

        if (first || activity)

        {
            logger.warn("Will dump Lock stats each minute when there is contention...");
            first = false;
        }
    }

}
