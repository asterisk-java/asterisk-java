package org.asteriskjava.live.internal;

import org.asteriskjava.live.ChannelState;
import org.asteriskjava.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsteriskChannelImplTest {
    private AsteriskChannelImpl channel;
    private int numberOfChanges;

    @BeforeEach
    void setUp() {
        AsteriskServerImpl server = new AsteriskServerImpl();
        channel = new AsteriskChannelImpl(server, "SIP/1234", "0123456789.123", DateUtil.getDate());
        channel.stateChanged(DateUtil.getDate(), ChannelState.DOWN);
        numberOfChanges = 0;
    }

    @Test
    void testStateChange() {
        channel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                assertEquals("state", evt.getPropertyName(), "wrong propertyName");
                assertEquals(ChannelState.DOWN, evt.getOldValue(), "wrong oldValue");
                assertEquals(ChannelState.DIALING, evt.getNewValue(), "wrong newValue");
                assertEquals(channel, evt.getSource(), "wrong source");
                numberOfChanges++;
            }
        });

        channel.stateChanged(DateUtil.getDate(), ChannelState.DIALING);
        assertEquals(1, numberOfChanges, "wrong number of propagated changes");
    }
}
