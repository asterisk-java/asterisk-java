/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.live;

import java.util.Collection;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.Extension;

/**
 * @author srt
 * @version $Id: TestDefaultAsteriskManager.java,v 1.3 2005/07/16 13:19:34 srt
 *          Exp $
 */
public class TestExtensionHistory extends AsteriskServerTestCase
{
    public void testGetHistory() throws Exception
    {
        Collection<AsteriskChannel> channels;
        
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
        }

        channels = server.getChannels();
        System.out.println("# of active channels: " + channels.size());
        for (AsteriskChannel channel : channels)
        {
            System.out.println(channel);
            System.out.println("  first extension: " + channel.getFirstExtension());
            System.out.println("  current extension: " + channel.getCurrentExtension());

            for (Extension extension : channel.getExtensions())
            {
                System.out.println("  " + extension);
            }
        }
    }
}
