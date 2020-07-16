package org.asteriskjava.pbx;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Currently this factories only purposes is to serve up globally unique channel
 * id's which every new channel must be assigned.
 * 
 * @author bsutton
 */
public class ChannelFactory
{

    private final static AtomicLong nextChannelId = new AtomicLong();

    public static long getNextChannelId()
    {
        return nextChannelId.incrementAndGet();
    }
}
