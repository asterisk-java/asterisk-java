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

import org.asteriskjava.ami.action.response.ChallengeActionResponse;
import org.asteriskjava.ami.action.response.EmptyActionResponse;
import org.asteriskjava.ami.action.response.ManagerActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.function.Function;

import static java.time.Instant.now;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.AuthType.MD5;
import static org.asteriskjava.ami.action.response.ResponseType.Error;
import static org.asteriskjava.ami.action.response.ResponseType.Success;

class LoginActionItTest extends BaseActionItTest {
    @Test
    void shouldReturnFailedWhenLoginIsNotSuccessful() throws InterruptedException {
        //given
        Instant now = now();

        ChallengeAction challengeAction = new ChallengeAction();
        challengeAction.setActionId("id-1");
        challengeAction.setAuthType(MD5);

        LoginAction loginAction = new LoginAction("asterisk-java-it-tests", MD5, "invalid");
        loginAction.setActionId("id-2");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerAction(ChallengeAction.class, challengeAction)
                .registerAction(LoginAction.class, loginAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        EmptyActionResponse actual = responseRecorder.getRecorderResponse("id-2", EmptyActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Error);
        assertThat(actual.getMessage()).isEqualTo("Authentication failed");
        assertThat(actual.getDateReceived()).isEqualTo(now);
    }

    @Test
    void shouldLoginSuccessfully() throws InterruptedException {
        //given
        Instant now = now();

        ChallengeAction challengeAction = new ChallengeAction();
        challengeAction.setActionId("id-1");
        challengeAction.setAuthType(MD5);

        Function<ManagerActionResponse, ManagerAction> loginActionProvider = prevResponse -> {
            ChallengeActionResponse challengeActionResponse = (ChallengeActionResponse) prevResponse;

            String challenge = challengeActionResponse.getChallenge();
            String bytes = md5Hex(challenge + "123qwe");

            LoginAction loginAction = new LoginAction("asterisk-java-it-tests", MD5, bytes);
            loginAction.setActionId("id-2");
            return loginAction;
        };

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerAction(ChallengeAction.class, challengeAction)
                .registerAction(LoginAction.class, loginActionProvider);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        EmptyActionResponse actual = responseRecorder.getRecorderResponse("id-2", EmptyActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getMessage()).isEqualTo("Authentication accepted");
        assertThat(actual.getDateReceived()).isEqualTo(now);
    }
}