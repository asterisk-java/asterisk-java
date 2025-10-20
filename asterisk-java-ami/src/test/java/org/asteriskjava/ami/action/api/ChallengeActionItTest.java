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

import org.asteriskjava.ami.action.api.response.ChallengeActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.AuthType.MD5;
import static org.asteriskjava.ami.action.api.response.ResponseType.success;

class ChallengeActionItTest extends BaseActionItTest {
    @Test
    void shouldSendChallengeActionAndReceiveChallengeResponse() throws InterruptedException {
        //given
        Instant now = now();

        ChallengeAction challengeAction = new ChallengeAction();
        challengeAction.setActionId("id-1");
        challengeAction.setAuthType(MD5);

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerAction(ChallengeAction.class, challengeAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        ChallengeActionResponse actual = responseRecorder.getRecorderResponse("id-1", ChallengeActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(success);
        assertThat(actual.getActionId()).isEqualTo("id-1");
        assertThat(actual.getDateReceived()).isEqualTo(now);
        assertThat(actual.getChallenge()).isNotBlank();
    }
}
