/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Sep 24, 2004
 */
package org.asteriskjava.manager;

import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.action.VoicemailUsersListAction;
import org.asteriskjava.manager.action.AgiAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;

import junit.framework.TestCase;

/**
 * @author srt
 * @version $Id$
 */
public class TestAsyncAgi extends AbstractManagerTestCase
{
    public void testAgiAction() throws Exception
    {
        DefaultManagerConnection dmc;

        dmc = getDefaultManagerConnection();
        dmc.addEventListener(new ManagerEventListener()
        {
            public void onManagerEvent(ManagerEvent event)
            {
                System.out.println("Got event: " + event);
            }
        });
        dmc.login();
        ManagerResponse response = dmc.sendAction(new AgiAction(
                "IAX2/iax0-cgn_reucon_net-2",
                "EXEC Playback tt-monkeysintro",
                "myCommandId"));

        System.out.println(response);

        // wait for 3 seconds to receive events
        Thread.sleep(15000);
        dmc.logoff();
    }
}