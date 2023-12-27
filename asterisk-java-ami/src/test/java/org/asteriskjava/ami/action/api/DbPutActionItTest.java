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

import org.asteriskjava.ami.action.api.response.DefaultActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.response.ResponseType.Success;

class DbPutActionItTest extends BaseActionItTest {
    @Test
    void shouldPutValueInAsteriskDatabase() throws Exception {
        //given
        DbPutAction dbPutAction = new DbPutAction();
        dbPutAction.setActionId("id-1");
        dbPutAction.setFamily("family");
        dbPutAction.setKey("key");
        dbPutAction.setVal("database-value");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(dbPutAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getMessage()).isEqualTo("Updated database successfully");

        Container.ExecResult execResult = asteriskDocker.execInContainer("asterisk", "-rx", "database get family key");
        assertThat(execResult.getStdout().trim()).isEqualTo("Value: database-value");
    }
}
