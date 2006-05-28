/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.live;

/**
 * @author srt
 * @version $Id$
 */
public class OriginateTest extends AsteriskServerTestCase
{
    public void testOriginate() throws Exception
    {
        AsteriskChannel channel;
        channel = server.originateToExtension("Local/1310@default", "from-local", "1330", 1, 10000L);

        System.err.println(channel);
        Thread.sleep(20000L);
        System.err.println(channel);
    }
}
