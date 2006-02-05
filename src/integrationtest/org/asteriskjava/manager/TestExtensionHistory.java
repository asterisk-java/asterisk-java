/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author srt
 * @version $Id: TestDefaultAsteriskManager.java,v 1.3 2005/07/16 13:19:34 srt
 *          Exp $
 */
public class TestExtensionHistory extends AsteriskManagerTestCase
{
    public void testGetHistory() throws Exception
    {
        List<Channel> channels;
        
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
        }

        channels = new ArrayList<Channel>(manager.getChannels().values());
        System.out.println("# of active channels: " + channels.size());
        Iterator channelsIterator = channels.iterator();
        while (channelsIterator.hasNext())
        {
            Channel channel = (Channel) channelsIterator.next();

            System.out.println(channel);
            System.out.println("  first extension: " + channel.getFirstExtension());
            System.out.println("  current extension: " + channel.getCurrentExtension());

            Iterator historyIterator = channel.getExtensions().iterator();
            while (historyIterator.hasNext())
            {
                Extension extension = (Extension) historyIterator.next();
                System.out.println("  " + extension);
            }
        }
    }
}
