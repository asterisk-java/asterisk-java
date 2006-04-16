/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.live;

import java.util.Arrays;
import java.util.Collection;

import org.asteriskjava.manager.Originate;


/**
 * @author srt
 * @version $Id$
 */
public class TestDefaultAsteriskManager extends AsteriskManagerTestCase
{
    public void testGetChannels() throws Exception
    {
        System.out.println("waiting for channels...");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
        }

        Collection<AsteriskChannel> channels = manager.getChannels();

        System.out.println("got channels. waiting to hangup...");

        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
        }

        for (AsteriskChannel channel : channels)
        {
            System.out.println(channel);
            try
            {
                System.out.println("MY_VAR=" + channel.getVariable("MY_VAR"));
                //channel.redirect("default", "1330", 1);
                channel.hangup();
            }
            catch (NoSuchChannelException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public void XtestGetQueues() throws Exception
    {
        System.out.println("waiting for queues...");
        
        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
        }

        Collection<AsteriskQueue> queues = manager.getQueues();
        for (AsteriskQueue queue : queues)
        {
            System.out.println(queue);
        }
    }

    public void testGetVersion() throws Exception
    {
        System.out.println(Arrays.toString(manager.getVersion("cdr_manager.c")));
        System.out.println(manager.getVersion());
    }
    
    public void XtestOriginate() throws Exception
    {
        Originate originate;
        Call call;

        originate = new Originate();
        originate.setChannel("SIP/1310");
        originate.setContext("default");
        originate.setExten("1330");
        originate.setPriority(new Integer(1));

        call = manager.originateCall(originate);

        System.out.println(call);
    }
}
