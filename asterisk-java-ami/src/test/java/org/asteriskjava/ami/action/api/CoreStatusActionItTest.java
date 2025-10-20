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

import org.asteriskjava.ami.action.api.response.CoreStatusActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.response.ResponseType.success;

class CoreStatusActionItTest extends BaseActionItTest {
    @Test
    void shouldReturnWithParsedTimeAndDate() throws InterruptedException {
        //given
        Instant now = now();

        CoreStatusAction coreStatusAction = new CoreStatusAction();
        coreStatusAction.setActionId("id-1");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(CoreStatusAction.class, coreStatusAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        CoreStatusActionResponse actual = responseRecorder.getRecorderResponse("id-1", CoreStatusActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(success);
        assertThat(actual.getCoreStartupDate()).isInstanceOf(LocalDate.class).isNotNull();
        assertThat(actual.getCoreStartupTime()).isInstanceOf(LocalTime.class).isNotNull();
        assertThat(actual.getCoreReloadDate()).isInstanceOf(LocalDate.class).isNotNull();
        assertThat(actual.getCoreReloadTime()).isInstanceOf(LocalTime.class).isNotNull();
        assertThat(actual.getCoreCurrentCalls()).isEqualTo(0);
    }
}
