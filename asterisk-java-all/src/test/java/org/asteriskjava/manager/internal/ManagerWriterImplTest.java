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

import org.asteriskjava.core.socket.SocketConnectionAdapter;
import org.asteriskjava.manager.action.StatusAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

class ManagerWriterImplTest {
    private ManagerWriter managerWriter;

    @BeforeEach
    void setUp() {
        managerWriter = new ManagerWriterImpl();
    }

    @Test
    void testSendActionWithoutSocket() throws Exception {
        try {
            managerWriter.sendAction(new StatusAction(), null);
            fail("Must throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e instanceof IllegalStateException, "Exception must be of type IllegalStateException");
        }
    }

    @Test
    void testSendAction() throws Exception {
        SocketConnectionAdapter socketConnectionAdapter;

        socketConnectionAdapter = mock(SocketConnectionAdapter.class);
        socketConnectionAdapter.write("action: Status\r\n\r\n");
        socketConnectionAdapter.flush();

        managerWriter.setSocket(socketConnectionAdapter);
        managerWriter.sendAction(new StatusAction(), null);
    }
}
