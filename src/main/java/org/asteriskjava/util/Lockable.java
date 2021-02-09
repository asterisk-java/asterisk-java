package org.asteriskjava.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import org.asteriskjava.util.Locker.LockCloser;

public class Lockable
{
    private final ReentrantLock internalLock = new ReentrantLock(true);
    final private String lockName;
    final AtomicReference<Thread> threadHoldingLock = new AtomicReference<>();
    private final AtomicInteger totalWaitTime = new AtomicInteger();
    private final AtomicInteger totalHoldTime = new AtomicInteger();
    private final AtomicInteger requested = new AtomicInteger();
    private final AtomicInteger waited = new AtomicInteger();
    private final AtomicInteger acquired = new AtomicInteger();
    private volatile boolean dumped = false;

    private final static AtomicLong seed = new AtomicLong();
    private final long lockableId = seed.incrementAndGet();

    public Lockable()
    {
        this.lockName = getCaller();
    }

    String getCaller()
    {
        Exception ex = new Exception();
        StackTraceElement[] trace = ex.getStackTrace();
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

    public String asString()
    {
        return "Lockable [totalWaitTime=" + totalWaitTime + ", totalHoldTime=" + totalHoldTime + ", waited=" + waited
                + ", acquired=" + acquired + ", object=" + lockName + ", id=" + lockableId + "]";
    }

    long getAverageHoldTime()
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

    void addTotalWaitTime(int time)
    {
        totalWaitTime.addAndGet(time);
    }

    void addTotalHoldTime(int time)
    {
        totalHoldTime.addAndGet(time);
    }

    int addRequested()
    {
        return requested.incrementAndGet();
    }

    void addWaited(int time)
    {
        waited.addAndGet(time);
    }

    void addAcquired()
    {
        acquired.incrementAndGet();
    }

    boolean isDumped()
    {
        return dumped;
    }

    void setDumped(boolean dumped)
    {
        this.dumped = dumped;
    }

    public long getTotalWaitTime()
    {
        return totalWaitTime.longValue();
    }

    public int getWaited()
    {
        return waited.intValue();
    }

    public int getRequested()
    {
        return requested.intValue();
    }

    public Long getLockableId()
    {
        return lockableId;
    }
}
