/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package net.sf.asterisk.manager;

import java.util.Arrays;
import java.util.Iterator;


/**
 * @author srt
 * @version $Id: TestDefaultAsteriskManager.java,v 1.4 2005/11/08 21:11:31 srt Exp $
 */
public class TestDefaultAsteriskManager extends AsteriskManagerTestCase
{
    public void XtestGetChannels() throws Exception
    {
        try
        {
            Thread.sleep(30000);
        }
        catch (InterruptedException e)
        {
        }

        System.out.println(manager.getChannels().size());
        Iterator i = manager.getChannels().keySet().iterator();
        while (i.hasNext())
        {
            String id = (String) i.next();
            System.out.println(id + ": " + manager.getChannels().get(id));
        }
    }

    public void testGetQueues() throws Exception
    {
        System.out.println("waiting...");
        
        try
        {
            Thread.sleep(20000);
        }
        catch (InterruptedException e)
        {
        }

        System.out.println(manager.getQueues().size());
        Iterator i = manager.getQueues().keySet().iterator();
        while (i.hasNext())
        {
            String id = (String) i.next();
            System.out.println(id + ": " + manager.getQueues().get(id));
        }
    }

    public void XtestGetVersion() throws Exception
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
