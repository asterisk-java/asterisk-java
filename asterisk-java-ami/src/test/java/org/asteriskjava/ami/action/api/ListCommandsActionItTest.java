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

import org.asteriskjava.ami.action.api.response.ListCommandActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.asteriskjava.ami.action.api.response.ResponseType.success;

class ListCommandsActionItTest extends BaseActionItTest {
    @Test
    void shouldReturnListOfCommands() throws InterruptedException {
        //given
        Instant now = now();

        ListCommandsAction listCommandsAction = new ListCommandsAction();
        listCommandsAction.setActionId("id-1");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(ListCommandsAction.class, listCommandsAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        ListCommandActionResponse actual = responseRecorder.getRecorderResponse("id-1", ListCommandActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(success);
        assertThat(actual.getCommands()).contains(
                entry("ConfbridgeList", "List participants in a conference.  (Priv: reporting,all)"),
                entry("ConfbridgeMute", "Mute a Confbridge user.  (Priv: call,all)"),
                entry("QueuePause", "Makes a queue member temporarily unavailable.  (Priv: agent,all)")
        );
    }
}
