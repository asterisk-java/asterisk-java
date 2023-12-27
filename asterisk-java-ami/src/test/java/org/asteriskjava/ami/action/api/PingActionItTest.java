/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.ami.action.api;

import org.asteriskjava.ami.action.api.response.PingActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.response.ResponseType.Success;

class PingActionItTest extends BaseActionItTest {
    @Test
    void shouldSendPingAction() throws InterruptedException {
        //given
        PingAction pingAction = new PingAction();
        pingAction.setActionId("id-1");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(pingAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        PingActionResponse actual = responseRecorder.getRecorderResponse("id-1", PingActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getTimestamp()).isInstanceOf(Instant.class);
    }
}
