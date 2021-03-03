package org.asteriskjava.manager.internal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class ProtocolIdentifierWrapper
{
    private volatile CountDownLatch latch = new CountDownLatch(1);
    private String value;

    void reset()
    {
        value = null;
        latch = new CountDownLatch(1);
    }

    boolean await(long timeout) throws InterruptedException
    {
        return latch.await(timeout, TimeUnit.MILLISECONDS);
    }

    String getValue()
    {
        return value;
    }

    public void setValue(String identifier)
    {
        value = identifier;
        latch.countDown();

    }
}
