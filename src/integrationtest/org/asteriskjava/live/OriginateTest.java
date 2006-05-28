/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.live;

/**
 * @author srt
 * @version $Id: TestExtensionHistory.java 417 2006-05-27 10:53:26Z srt $
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
