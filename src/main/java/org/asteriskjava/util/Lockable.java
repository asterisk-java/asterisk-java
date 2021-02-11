package org.asteriskjava.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import org.asteriskjava.util.Locker.LockCloser;

public class Lockable
{
    private final ReentrantLock internalLock = new ReentrantLock(false);
    final private String lockName;
    final AtomicReference<Thread> threadHoldingLock = new AtomicReference<>();
    private final AtomicInteger totalWaitTime = new AtomicInteger();
    private final AtomicInteger totalHoldTime = new AtomicInteger();
    private final AtomicInteger requested = new AtomicInteger();
    private final AtomicInteger waited = new AtomicInteger();
    private final AtomicInteger acquired = new AtomicInteger();
    private volatile boolean dumped = false;
    private volatile boolean blocked = false;

    private final static AtomicLong seed = new AtomicLong();
    private final long lockableId = seed.incrementAndGet();

    public Lockable()
    {
        this.lockName = getLockCaller();
    }

    private String getLockCaller()
    {
        StackTraceElement[] trace = new Exception().getStackTrace();
        String name = this.getClass().getCanonicalName();
        for (StackTraceElement element : trace)
        {
            if (element.getFileName() != null && !element.getFileName().contains(Lockable.class.getSimpleName()))
            {
                name = element.getFileName() + " " + element.getMethodName() + " " + element.getLineNumber() + " "
                        + element.getClassName();
                break;
            }
        }

        return name;
    }

    String asLockString()
    {
        return "Lockable [waited=" + waited + ", waitTime=" + totalWaitTime + ", totalHoldTime=" + totalHoldTime
                + ", acquired=" + acquired + ", object=" + lockName + ", id=" + lockableId + "]";
    }

    long getLockAverageHoldTime()
    {
        long hold = totalHoldTime.get();
        long count = acquired.get();
        if (count < 10)
        {
            return 250;
        }
        return Math.max(hold / count, 50);
    }

    public LockCloser withLock()
    {
        return Locker.doWithLock(this);
    }

    ReentrantLock getInternalLock()
    {
        return internalLock;
    }

    void addLockTotalWaitTime(int time)
    {
        totalWaitTime.addAndGet(time);
    }

    void addLockTotalHoldTime(int time)
    {
        totalHoldTime.addAndGet(time);
    }

    int addLockRequested()
    {
        return requested.incrementAndGet();
    }

    void addLockWaited(int time)
    {
        waited.addAndGet(time);
    }

    void addLockAcquired(int count)
    {
        acquired.addAndGet(count);
    }

    boolean isLockDumped()
    {
        return dumped;
    }

    void setLockDumped(boolean dumped)
    {
        this.dumped = dumped;
    }

    int getLockTotalWaitTime()
    {
        return totalWaitTime.intValue();
    }

    int getLockWaited()
    {
        return waited.intValue();
    }

    int getLockRequested()
    {
        return requested.intValue();
    }

    Long getLockableId()
    {
        return lockableId;
    }

    int getLockTotalHoldTime()
    {
        return totalHoldTime.intValue();
    }

    int getLockAcquired()
    {
        return acquired.intValue();
    }

    boolean wasLockBlocked()
    {
        return blocked;
    }

    void setLockBlocked(boolean blocked)
    {
        this.blocked = blocked;
    }

}
