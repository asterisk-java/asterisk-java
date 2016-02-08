/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.internal;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.util.SocketConnectionFacade;
import org.junit.Before;
import org.junit.Test;

public class ManagerWriterImplTest
{
    private ManagerWriter managerWriter;

    @Before
    public void setUp()
    {
        managerWriter = new ManagerWriterImpl();
    }

    @SuppressWarnings("cast")
    @Test
    public void testSendActionWithoutSocket() throws Exception
    {
        try
        {
            managerWriter.sendAction(new StatusAction(), null);
            fail("Must throw IllegalStateException");
        }
        catch (IllegalStateException e)
        {
            assertTrue("Exception must be of type IllegalStateException", e instanceof IllegalStateException);
        }
    }

    @Test
    public void testSendAction() throws Exception
    {
        SocketConnectionFacade socketConnectionFacade;

        socketConnectionFacade = createMock(SocketConnectionFacade.class);
        socketConnectionFacade.write("action: Status\r\n\r\n");
        socketConnectionFacade.flush();
        replay(socketConnectionFacade);

        managerWriter.setSocket(socketConnectionFacade);
        managerWriter.sendAction(new StatusAction(), null);
        verify(socketConnectionFacade);
    }
}
