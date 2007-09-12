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
 * @author <a href="mailto:patrick.breucking{@nospam}gonicus.de">Patrick
 *         Breucking</a>
 * @since 0.1
 * @version $Id$
 * 
 */
public class AsteriskAgentImplTest
{

    private AsteriskAgentImpl agent;
    private int numberOfChanges;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
	AsteriskServerImpl server = new AsteriskServerImpl();
	agent = new AsteriskAgentImpl(server, "Testagent", "Agent/999",
		AgentState.AGENT_IDLE);
	numberOfChanges = 0;
    }

    /**
     * Test method for
     * {@link org.asteriskjava.live.internal.AsteriskAgentImpl#updateStatus(org.asteriskjava.live.AgentState)}.
     */
    @Test
    public void testUpdateStatus()
    {
	assertEquals(AgentState.AGENT_IDLE, agent.getStatus());
	agent.addPropertyChangeListener(new PropertyChangeListener()
	{

	    @Override
	    public void propertyChange(PropertyChangeEvent evt)
	    {
		assertEquals("wrong propertyName", "status", evt
			.getPropertyName());
		assertEquals("wrong oldValue", AgentState.AGENT_IDLE, evt
			.getOldValue());
		assertEquals("wrong newValue", AgentState.AGENT_RINGING, evt
			.getNewValue());
		assertEquals("wrong queue", agent, evt.getSource());
		numberOfChanges++;
	    }

	});
	agent.updateStatus(AgentState.AGENT_RINGING);
	assertEquals("wrong number of propagated changes", 1, numberOfChanges);
    }

}
