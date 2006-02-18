/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.Extension;

/**
 * @author srt
 * @version $Id: TestDefaultAsteriskManager.java,v 1.3 2005/07/16 13:19:34 srt
 *          Exp $
 */
public class TestExtensionHistory extends AsteriskManagerTestCase
{
    public void testGetHistory() throws Exception
    {
        List<AsteriskChannel> channels;
        
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
        }

        channels = new ArrayList<AsteriskChannel>(manager.getChannels().values());
        System.out.println("# of active channels: " + channels.size());
        Iterator channelsIterator = channels.iterator();
        while (channelsIterator.hasNext())
        {
            AsteriskChannel channel = (AsteriskChannel) channelsIterator.next();

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
