package org.asteriskjava.live.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.TestCase;

import org.asteriskjava.live.ChannelState;
import org.asteriskjava.util.DateUtil;

public class AsteriskChannelImplTest extends TestCase
{
    private AsteriskChannelImpl channel;
    private int numberOfChanges;

    public void setUp()
    {
        AsteriskServerImpl server = new AsteriskServerImpl();
        channel = new AsteriskChannelImpl(server, "SIP/1234", "0123456789.123", DateUtil.getDate());
        channel.setState(ChannelState.DOWN);
        numberOfChanges = 0;
    }

    public void testStateChange()
    {
        channel.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                assertEquals("wrong propertyName", "state", evt.getPropertyName());
                assertEquals("wrong oldValue", ChannelState.DOWN, evt.getOldValue());
                assertEquals("wrong newValue", ChannelState.DIALING, evt.getNewValue());
                assertEquals("wrong source", channel, evt.getSource());
                numberOfChanges++;
            }
        });

        channel.setState(ChannelState.DIALING);
        assertEquals("wrong number of propagated changes", 1, numberOfChanges);
    }
}
