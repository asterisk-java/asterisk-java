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

import org.asteriskjava.ami.action.response.EmptyActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.response.ResponseType.Goodbye;

class LogoffActionItTest extends BaseActionItTest {
    @Test
    void shouldLogoffSuccessfully() throws InterruptedException {
        //given
        Instant now = now();

        LogoffAction logoffAction = new LogoffAction();
        logoffAction.setActionId("id-1");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(LogoffAction.class, logoffAction);

        //when
        ActionsRunner.ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        EmptyActionResponse actual = responseRecorder.getRecorderResponse("id-1", EmptyActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Goodbye);
        assertThat(actual.getMessage()).isEqualTo("Thanks for all the fish.");
        assertThat(actual.getDateReceived()).isEqualTo(now);
    }

    @Test
    void shouldHandleWhenSendLogoffWithoutLogin() throws InterruptedException {
        //given
        Instant now = now();

        LogoffAction logoffAction = new LogoffAction();
        logoffAction.setActionId("id-1");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerAction(LogoffAction.class, logoffAction);

        //when
        ActionsRunner.ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        EmptyActionResponse actual = responseRecorder.getRecorderResponse("id-1", EmptyActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Goodbye);
        assertThat(actual.getMessage()).isEqualTo("Thanks for all the fish.");
        assertThat(actual.getDateReceived()).isEqualTo(now);
    }
}
