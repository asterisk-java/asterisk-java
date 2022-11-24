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
package org.asteriskjava.fastagi.internal;

import org.asteriskjava.core.socket.SocketConnectionAdapter;
import org.asteriskjava.fastagi.AgiWriter;
import org.asteriskjava.fastagi.command.StreamFileCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AgiWriterImplTest {
    private AgiWriter agiWriter;
    private SocketConnectionAdapter socket;

    @BeforeEach
    void setUp() {
        this.socket = mock(SocketConnectionAdapter.class);
        this.agiWriter = new FastAgiWriter(socket);
    }

    @Test
    void testSendCommand() throws Exception {
        StreamFileCommand command;

        command = new StreamFileCommand("welcome");

        agiWriter.sendCommand(command);

        verify(socket).write("STREAM FILE \"welcome\" \"\"\n");
        verify(socket).flush();
    }
}
