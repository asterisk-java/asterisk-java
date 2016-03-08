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

import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.asteriskjava.live.AgentState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Patrick Breucking
 * @since 0.1
 * @version $Id$
 */
public class AsteriskAgentImplTest
{
    private AsteriskAgentImpl agent;
    private int numberOfChanges;

    @Before
    public void setUp()
    {
        AsteriskServerImpl server = new AsteriskServerImpl();
        agent = new AsteriskAgentImpl(server, "Testagent", "Agent/999", AgentState.AGENT_IDLE);
        numberOfChanges = 0;
    }

    @Test
    public void testUpdateStatus()
    {
        assertEquals(AgentState.AGENT_IDLE, agent.getState());
        agent.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                assertEquals("wrong propertyName", "state", evt.getPropertyName());
                assertEquals("wrong oldValue", AgentState.AGENT_IDLE, evt.getOldValue());
                assertEquals("wrong newValue", AgentState.AGENT_RINGING, evt.getNewValue());
                assertEquals("wrong queue", agent, evt.getSource());
                numberOfChanges++;
            }

        });
        agent.updateState(AgentState.AGENT_RINGING);
        assertEquals("wrong number of propagated changes", 1, numberOfChanges);
    }
}
