/*
 * Copyright 2004-2023 Asterisk Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.ami.action.api;

import org.asteriskjava.core.databind.AsteriskEncoder;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.EventMask.command;
import static org.asteriskjava.ami.action.api.EventMask.security;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;

class EventsActionTest {
    private static final AsteriskEncoder asteriskEncoder = new AsteriskEncoder(CRLF);

    @Test
    void shouldCorrectlyEncodeEnumSet() {
        //given
        EventsAction eventsAction = new EventsAction();
        eventsAction.setEventMask(EnumSet.of(security, command));

        //when
        String encode = asteriskEncoder.encode(eventsAction);

        //then
        assertThat(encode).contains("EventMask: command,security");
    }
}
