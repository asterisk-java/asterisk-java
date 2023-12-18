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
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Container.ExecResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.response.ResponseType.Success;

class CreateConfigActionItTest extends BaseActionItTest {
    @Test
    void shouldCreateFilename() throws Exception {
        //given
        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename("asterisk-java-test.conf");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        EmptyActionResponse actual = responseRecorder.getRecorderResponse("id-1", EmptyActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getMessage()).isEqualTo("New configuration file created successfully");

        ExecResult execResult = asteriskDocker.execInContainer("ls", "-la", "/etc/asterisk");
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout()).contains("asterisk-java-test.conf");
    }
}
