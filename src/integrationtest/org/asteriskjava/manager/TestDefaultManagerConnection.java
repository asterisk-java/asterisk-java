/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Sep 24, 2004
 */
package org.asteriskjava.manager;

import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.CommandResponse;

import junit.framework.TestCase;

/**
 * @author srt
 * @version $Id$
 */
public class TestDefaultManagerConnection extends TestCase
{
    private DefaultManagerConnection getDefaultManagerConnection()
    {
        DefaultManagerConnection dmc;

        dmc = new DefaultManagerConnection();
        dmc.setUsername("manager");
        dmc.setPassword("obelisk");
        dmc.setHostname("pbx0");

        return dmc;
    }

    public void testLogin() throws Exception
    {
        DefaultManagerConnection dmc;

        dmc = getDefaultManagerConnection();
        dmc.login();
        dmc.addEventHandler(new ManagerEventHandler()
        {
            public void handleEvent(ManagerEvent event)
            {
                System.out.println("Got event: " + event);
            }
        });
        dmc.sendAction(new StatusAction());
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            
        }

        while (true)
        {
            // wait for 3 seconds to receive events
            Thread.sleep(3000);
        }
        //dmc.logoff();
    }

    public void testLoginAuthenticationFailure() throws Exception
    {
        DefaultManagerConnection dmc;

        dmc = getDefaultManagerConnection();
        dmc.setPassword("");
        
        try
        {
            dmc.login();
            fail("No AuthenticationFailedException received.");
        }
        catch (AuthenticationFailedException e)
        {
        }
        dmc.logoff();
    }


    public void testCommandAction() throws Exception
    {
        DefaultManagerConnection dmc;
        CommandResponse response;

        dmc = getDefaultManagerConnection();
        dmc.login();
        
        response = (CommandResponse) dmc.sendAction(new CommandAction("show voicemail users"));
        System.out.println("Got response: " + response.getResult());
        
        dmc.logoff();
    }
}
