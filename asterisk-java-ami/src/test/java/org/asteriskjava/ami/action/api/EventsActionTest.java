package org.asteriskjava.ami.action.api;

import org.assertj.core.api.Assertions;
import org.asteriskjava.ami.ActionFieldsComparator;
import org.asteriskjava.core.databind.AsteriskEncoder;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.asteriskjava.ami.action.api.EventMask.command;
import static org.asteriskjava.ami.action.api.EventMask.security;

class EventsActionTest {
    private static final AsteriskEncoder asteriskEncoder = AsteriskEncoder
            .builder()
            .crlfNewlineDelimiter()
            .fieldNamesComparator(new ActionFieldsComparator())
            .build();

    @Test
    void shouldCorrectlyEncodeEnumSet() {
        //given
        EventsAction eventsAction = new EventsAction();
        eventsAction.setEventMask(EnumSet.of(security, command));

        //when
        String encode = asteriskEncoder.encode(eventsAction);

        //then
        Assertions.assertThat(encode).contains("EventMask: command,security");
    }
}
