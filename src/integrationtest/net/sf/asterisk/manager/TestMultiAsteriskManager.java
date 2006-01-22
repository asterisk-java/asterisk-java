/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package net.sf.asterisk.manager;

import junit.framework.TestCase;

/**
 * @author PY
 * @version $Id: TestMultiAsteriskManager.java,v 1.1 2005/03/13 11:39:06 srt Exp $
 */
public class TestMultiAsteriskManager extends TestCase
{
    protected MultiAsterisksManager getDefaultManager()
    {
        MultiAsterisksManager mam = new MultiAsterisksManager();

        DefaultManagerConnection dmc1 = new DefaultManagerConnection();
        dmc1.setHostname("asterisk1");
        dmc1.setUsername("username");
        dmc1.setPassword("password");
        mam.addManagerConnection(dmc1);

        DefaultManagerConnection dmc2 = new DefaultManagerConnection();
        dmc2.setHostname("asterisk2");
        dmc2.setUsername("username");
        dmc2.setPassword("password");
        mam.addManagerConnection(dmc2);

        return mam;
    }

    public void testInit() throws Exception
    {
        MultiAsterisksManager mam = getDefaultManager();

        mam.initialize();

        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
        }

        System.out.println(mam.getChannels());
    }

}
