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
package org.asteriskjava.ami.action;

import org.asteriskjava.ami.action.response.EventsActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.EnumSet;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.EventMask.*;
import static org.asteriskjava.ami.action.response.ResponseType.Success;

class EventsActionItTest extends BaseActionItTest {
    @Test
    void shouldHandleOffEvent() throws InterruptedException {
        //given
        Instant now = now();

        EventsAction eventsAction = new EventsAction();
        eventsAction.setActionId("id-1");
        eventsAction.setEventMask(EnumSet.of(off));

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(EventsAction.class, eventsAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        EventsActionResponse actual = responseRecorder.getRecorderResponse("id-1", EventsActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getEvents()).isEqualTo(off);
    }

    @Test
    void shouldHandleMultipleEvents() throws InterruptedException {
        //given
        Instant now = now();

        EventsAction eventsAction = new EventsAction();
        eventsAction.setActionId("id-1");
        eventsAction.setEventMask(EnumSet.of(security, command));

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(EventsAction.class, eventsAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        EventsActionResponse actual = responseRecorder.getRecorderResponse("id-1", EventsActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getEvents()).isEqualTo(on);
    }
}
