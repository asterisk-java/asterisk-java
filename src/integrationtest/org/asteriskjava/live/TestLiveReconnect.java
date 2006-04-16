/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.live;

/**
 * @author srt
 * @version $Id: TestDefaultAsteriskManager.java,v 1.4 2005/11/08 21:11:31 srt Exp $
 */
public class TestLiveReconnect extends AsteriskManagerTestCase
{
    public void testLiveReconnect() throws Exception
    {
        System.out.println("Please stop and restart the Asterisk server...");
        while (true)
        {
            Thread.sleep(3000);
        }
    }
}
