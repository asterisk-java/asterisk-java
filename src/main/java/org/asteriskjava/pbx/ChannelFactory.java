package org.asteriskjava.pbx;

/**
 * Currently this factories only purposes is to serve up globally unique channel
 * id's which every new channel must be assigned.
 * 
 * @author bsutton
 */
public class ChannelFactory
{

    private static long nextChannelId = 0;

    public static synchronized long getNextChannelId()
    {
        return ++ChannelFactory.nextChannelId;
    }
}
