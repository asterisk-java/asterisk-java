/*
 *  This code is property of GONICUS GmbH
 *
 *  (c) 2007
 *
 *  SVN-Information
 *       Author: $LastChangedBy$
 *     Revision: $LastChangedRevision$
 *  Last change: $LastChangedDate$
 *
 *         File: AsteriskAgentImplTest.java
 *      Package: org.asteriskjava.live.internal
 *
 *   Change History:
 *
 *             0001 breucking Sep 12, 2007 File created
 */
package org.asteriskjava.live.internal;

import org.asteriskjava.live.AgentState;
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
class AsteriskAgentImplTest {
    private AsteriskAgentImpl agent;
    private int numberOfChanges;

    @BeforeEach
    void setUp() {
        AsteriskServerImpl server = new AsteriskServerImpl();
        agent = new AsteriskAgentImpl(server, "Testagent", "Agent/999", AgentState.AGENT_IDLE);
        numberOfChanges = 0;
    }

    @Test
    void testUpdateStatus() {
        assertEquals(AgentState.AGENT_IDLE, agent.getState());
        agent.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                assertEquals("state", evt.getPropertyName(), "wrong propertyName");
                assertEquals(AgentState.AGENT_IDLE, evt.getOldValue(), "wrong oldValue");
                assertEquals(AgentState.AGENT_RINGING, evt.getNewValue(), "wrong newValue");
                assertEquals(agent, evt.getSource(), "wrong queue");
                numberOfChanges++;
            }

        });
        agent.updateState(AgentState.AGENT_RINGING);
        assertEquals(1, numberOfChanges, "wrong number of propagated changes");
    }
}
