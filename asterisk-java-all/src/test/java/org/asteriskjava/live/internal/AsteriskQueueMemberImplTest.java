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
package org.asteriskjava.live.internal;

import org.asteriskjava.live.QueueMemberState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Patrick Breucking
 * @version $Id$
 * @since 0.1
 */
class AsteriskQueueMemberImplTest {

    private AsteriskQueueMemberImpl queueMember;
    private int numberOfChanges;
    private AsteriskServerImpl server;
    // private QueueManager qManager;
    private AsteriskQueueImpl queue;

    @BeforeEach
    void setUp() {
        server = new AsteriskServerImpl();
        // ChannelManager channelManager = new ChannelManager(server);
        // qManager = new QueueManager(server, channelManager);
        queue = new AsteriskQueueImpl(server, "test", 25, "RoundRobin", 15, 5,
                0, 0, 1, 1, 1, 1.0);
        queueMember = new AsteriskQueueMemberImpl(server, queue, "Agent/777", QueueMemberState.DEVICE_UNKNOWN, false, 10,
                "dynamic", 3, 6000l);

        numberOfChanges = 0;
    }

    @Test
    void testQueueMemberEvents() {
        /*
         * Test should generate a new member like queueMember = new
         */
        // queueMember = new AsteriskQueueMemberImpl(server, queue, "Agent/777",
        // QueueMemberState.DEVICE_UNKNOWN);
        // QueueParamsEvent qpe = new QueueParamsEvent(new Object());
        // qpe.setQueue("test");
        // server.onManagerEvent(qpe);
        // qManager.handleQueueParamsEvent(qpe);
        // QueueMemberEvent qme = new QueueMemberEvent(new Object());
        // qme.setLocation("Agent/777");
        // qme.setQueue("test");
        // qme.setStatus(0);
        // server.onManagerEvent(qme);
        // qManager.handleQueueMemberEvent(qme);
        // Collection<AsteriskQueue> queues = server.getQueues();
        // assertEquals(1, queues.size());
        // AsteriskQueue queue = queues.iterator().next();
        //
        // Collection<AsteriskQueueMember> members = queue.getMembers();
        // assertEquals(1, members.size());
        // AsteriskQueueMember member = members.iterator().next();
        // assertEquals("Agent/777", member.getLocation());
        assertEquals(QueueMemberState.DEVICE_UNKNOWN, queueMember.getState());
        queueMember.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                assertEquals("state", evt.getPropertyName(), "wrong propertyName");
                assertEquals(QueueMemberState.DEVICE_UNKNOWN, evt.getOldValue(), "wrong oldValue");
                assertEquals(QueueMemberState.DEVICE_BUSY, evt.getNewValue(), "wrong newValue");
                assertEquals(queueMember, evt.getSource(), "wrong queue member");
                numberOfChanges++;
            }

        });

        // queue.addAsteriskQueueListener(new AsteriskQueueListener()
        // {
        //
        // void onEntryLeave(AsteriskChannel channel)
        // {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // void onMemberStateChange(AsteriskQueueMember member)
        // {
        // assertEquals("wrong newValue", QueueMemberState.DEVICE_BUSY,
        // member.getState());
        // assertEquals("wrong queue member", queueMember, member);
        // System.err.println("foo");
        // numberOfChanges++;
        //
        // }
        //
        // void onNewEntry(AsteriskChannel channel)
        // {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // });
        // queueMember.stateChanged(QueueMemberState.DEVICE_BUSY);
        // QueueMemberStatusEvent qmse = new QueueMemberStatusEvent(new
        // Object());
        // qmse.setLocation(queueMember.getLocation());
        // qmse.setStatus(3);
        // server.onManagerEvent(qmse);
        queueMember.stateChanged(QueueMemberState.DEVICE_BUSY);
        assertEquals(1, numberOfChanges, "wrong number of propagated changes");
    }
}
