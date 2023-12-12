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

import org.asteriskjava.ami.action.response.CommandActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.response.ResponseType.Error;
import static org.asteriskjava.ami.action.response.ResponseType.Success;

class CommandActionItTest extends BaseActionItTest {
    @Test
    void shouldHandleInvalidCommand() throws InterruptedException {
        //given
        Instant now = now();

        CommandAction commandAction = new CommandAction();
        commandAction.setActionId("id-1");
        commandAction.setCommand("invalid command");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(CommandAction.class, commandAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        CommandActionResponse actual = responseRecorder.getRecorderResponse("id-1", CommandActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Error);
        assertThat(actual.getMessage()).isEqualTo("Command output follows");
        assertThat(actual.getOutput()).containsExactly("No such command 'invalid command' (type 'core show help invalid command' for other possible commands)");
    }

    @Test
    void shouldReturnCommandWithOutput() throws InterruptedException {
        //given
        Instant now = now();

        CommandAction commandAction = new CommandAction();
        commandAction.setActionId("id-1");
        commandAction.setCommand("core show applications");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(CommandAction.class, commandAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        CommandActionResponse actual = responseRecorder.getRecorderResponse("id-1", CommandActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getMessage()).isEqualTo("Command output follows");
        assertThat(actual.getOutput()).contains("-= 183 Applications Registered =-", "VoiceMailPlayMsg");
    }
}
