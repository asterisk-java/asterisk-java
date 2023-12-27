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

class DbDelTreeActionItTest extends BaseActionItTest {
    @Test
    void shouldDeleteSpecifiedKey() throws Exception {
        //given
        asteriskDocker.execInContainer("asterisk", "-rx", "database put family1 key1 value1");
        asteriskDocker.execInContainer("asterisk", "-rx", "database put family1 key2 value2");

        DbDelTreeAction dbDelTreeAction = new DbDelTreeAction();
        dbDelTreeAction.setActionId("id-1");
        dbDelTreeAction.setFamily("family1");
        dbDelTreeAction.setKey("key1");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(dbDelTreeAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getMessage()).isEqualTo("Key tree deleted successfully");

        Container.ExecResult execResult1 = asteriskDocker.execInContainer("asterisk", "-rx", "database get family1 key1");
        assertThat(execResult1.getStdout().trim()).isEqualTo("Database entry not found.");

        Container.ExecResult execResult2 = asteriskDocker.execInContainer("asterisk", "-rx", "database get family1 key2");
        assertThat(execResult2.getStdout().trim()).isEqualTo("Value: value2");
    }

    @Test
    void shouldDeleteKeysForFamily() throws Exception {
        //given
        asteriskDocker.execInContainer("asterisk", "-rx", "database put family1 key1 value1");
        asteriskDocker.execInContainer("asterisk", "-rx", "database put family1 key2 value2");
        asteriskDocker.execInContainer("asterisk", "-rx", "database put family2 key1 another-family-value1");

        DbDelTreeAction dbDelTreeAction = new DbDelTreeAction();
        dbDelTreeAction.setActionId("id-1");
        dbDelTreeAction.setFamily("family1");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(dbDelTreeAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getMessage()).isEqualTo("Key tree deleted successfully");

        Container.ExecResult execResult1 = asteriskDocker.execInContainer("asterisk", "-rx", "database get family1 key1");
        assertThat(execResult1.getStdout().trim()).isEqualTo("Database entry not found.");

        Container.ExecResult execResult2 = asteriskDocker.execInContainer("asterisk", "-rx", "database get family1 key2");
        assertThat(execResult2.getStdout().trim()).isEqualTo("Database entry not found.");

        Container.ExecResult execResult3 = asteriskDocker.execInContainer("asterisk", "-rx", "database get family2 key1");
        assertThat(execResult3.getStdout().trim()).isEqualTo("Value: another-family-value1");
    }
}
