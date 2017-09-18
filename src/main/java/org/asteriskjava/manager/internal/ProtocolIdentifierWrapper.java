package org.asteriskjava.manager.internal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class ProtocolIdentifierWrapper
{
    private CountDownLatch latch = new CountDownLatch(1);
    private String value;

    void reset()
    {
        value = null;
        latch = new CountDownLatch(1);
    }

    void await(long timeout) throws InterruptedException
    {
        latch.await(timeout, TimeUnit.MILLISECONDS);
    }

    String getValue()
    {
        return value;
    }

    void countDown()
    {
        latch.countDown();
    }

    public void setValue(String identifier)
    {
        value = identifier;

    }
}
