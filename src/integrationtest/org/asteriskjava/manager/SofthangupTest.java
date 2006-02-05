/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.manager;

import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;


/**
 * @author srt
 * @version $Id: SofthangupTest.java,v 1.1 2005/04/30 16:45:15 srt Exp $
 */
public class SofthangupTest extends AsteriskManagerTestCase
{
    public void testSofthangup() throws Exception
    {
        OriginateAction originate;
        
        originate = new OriginateAction();
        originate.setChannel("Local/1310");
        originate.setContext("default");
        originate.setExten("1340");
        originate.setPriority(new Integer(1));
        
        ManagerResponse response = managerConnection.sendAction(originate);
        
        System.out.println("Response: " + response);
        
        Thread.sleep(10000);
    }
}
