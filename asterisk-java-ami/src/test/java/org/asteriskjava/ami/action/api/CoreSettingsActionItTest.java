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

import org.asteriskjava.ami.action.api.response.CoreSettingsActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.response.ResponseType.success;

class CoreSettingsActionItTest extends BaseActionItTest {
    @Test
    void shouldGetCoreSettings() throws InterruptedException {
        //given
        Instant now = now();

        CoreSettingsAction coreSettingsAction = new CoreSettingsAction();
        coreSettingsAction.setActionId("id-1");

        ActionsRunner actionsRunner = actionRunner()
                .withFixedTime(now)
                .registerLoginSequence()
                .registerAction(CoreSettingsAction.class, coreSettingsAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        CoreSettingsActionResponse actual = responseRecorder.getRecorderResponse("id-1", CoreSettingsActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(success);
        assertThat(actual.getAmiVersion()).isEqualTo("7.0.3");
        assertThat(actual.getAsteriskVersion()).isEqualTo("18.15.1");
        assertThat(actual.getSystemName()).isNull();
        assertThat(actual.getCoreMaxCalls()).isEqualTo(0);
        assertThat(actual.getCoreRunUser()).isNull();
        assertThat(actual.getCoreRunGroup()).isNull();
        assertThat(actual.getCoreMaxFilehandles()).isEqualTo(0);
        assertThat(actual.isCoreRealtimeEnabled()).isFalse();
        assertThat(actual.isCoreCdrEnabled()).isTrue();
        assertThat(actual.isCoreHttpEnabled()).isFalse();
    }
}
