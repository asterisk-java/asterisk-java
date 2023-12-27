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

import org.asteriskjava.ami.action.api.UpdateConfigAction.*;
import org.asteriskjava.ami.action.api.response.DefaultActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Container;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.response.ResponseType.Error;
import static org.asteriskjava.ami.action.api.response.ResponseType.Success;

class UpdateConfigActionItTest extends BaseActionItTest {
    @Test
    void shouldCreateCategory() throws Exception {
        //given
        String filename = "asterisk-java-test-create-category.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config = new NewCat();
        config.setCat("brand-new-category");

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout()).contains("[brand-new-category]");
    }

    @Test
    void shouldRenameCategory() throws Exception {
        //given
        String filename = "asterisk-java-test-rename-category.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        RenameCat config2 = new RenameCat();
        config2.setCat("brand-new-category");
        config2.setValue("change-category-name");

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config1, config2);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout())
                .contains("[change-category-name]")
                .doesNotContain("[brand-new-category]");
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        //given
        String filename = "asterisk-java-test-delete-category.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        DelCat config2 = new DelCat();
        config2.setCat("brand-new-category");

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config1, config2);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-2", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout()).doesNotContain("[brand-new-category]");
    }

    @Test
    void shouldAppendValue() throws Exception {
        //given
        String filename = "asterisk-java-test-append.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Append config2 = new Append();
        config2.setCat("brand-new-category");
        config2.setVar("some-variable");
        config2.setValue("some-value");

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config1, config2);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-2", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/asterisk-java-test-append.conf");
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout()).contains("[brand-new-category]", "some-variable = some-value");
    }

    @Test
    void shouldInsertValue() throws Exception {
        //given
        String filename = "asterisk-java-test-insert.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-variable");
        config2.setLine(0);
        config2.setValue("some-value");

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config1, config2);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout()).contains("[brand-new-category]", "some-variable = some-value");
    }

    @Test
    void shouldInsertMultipleValues() throws Exception {
        //given
        String filename = "asterisk-java-test-insert-multiple-values.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-variable");
        config2.setLine(0);
        config2.setValue("some-value");

        Insert config3 = new Insert();
        config3.setCat("brand-new-category");
        config3.setVar("new-variable");
        config3.setLine(1);
        config3.setValue("new-value");

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config1, config2, config3);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-2", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout()).contains("[brand-new-category]", "some-variable = some-value", "new-variable = new-value");
    }

    @Test
    void shouldDeleteValueUsingLineSelector() throws Exception {
        //given
        String filename = "asterisk-java-test-delete-by-line.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-variable");
        config2.setLine(0);
        config2.setValue("some-value");

        Insert config3 = new Insert();
        config3.setCat("brand-new-category");
        config3.setVar("new-variable");
        config3.setLine(1);
        config3.setValue("new-value");

        Delete config4 = new Delete();
        config4.setCat("brand-new-category");
        config4.setLine(0);

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config1, config2, config3, config4);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout())
                .contains("[brand-new-category]", "new-variable = new-value")
                .doesNotContain("some-variable = some-value");
    }

    @Test
    void shouldDeleteValueUsingVarSelector() throws Exception {
        //given
        String filename = "asterisk-java-test-delete-by-var.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-variable");
        config2.setLine(0);
        config2.setValue("some-value");

        Insert config3 = new Insert();
        config3.setCat("brand-new-category");
        config3.setVar("new-variable");
        config3.setLine(1);
        config3.setValue("new-value");

        Delete config4 = new Delete();
        config4.setCat("brand-new-category");
        config4.setVar("some-variable");

        UpdateConfigAction updateConfigAction = createUpdateConfigAction("id-2", filename, config1, config2, config3, config4);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-1", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout())
                .contains("[brand-new-category]", "new-variable = new-value")
                .doesNotContain("some-variable = some-value");
    }

    @Test
    void shouldDeleteValueUsingVarAndValueMatchSelector() throws Exception {
        //given
        String filename = "asterisk-java-test-delete-by-var-and-match.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-var");
        config2.setLine(0);
        config2.setValue("some-value");

        Insert config3 = new Insert();
        config3.setCat("brand-new-category");
        config3.setVar("var-to-delete");
        config3.setLine(1);
        config3.setValue("value-to-delete");

        UpdateConfigAction updateConfigAction1 = createUpdateConfigAction("id-2", filename, config1, config2, config3);

        Delete config4 = new Delete();
        config4.setCat("brand-new-category");
        config4.setVar("var-to-delete");
        config4.setMatch("value-to-delete");

        UpdateConfigAction updateConfigAction2 = createUpdateConfigAction("id-3", filename, config4);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction1)
                .registerAction(updateConfigAction2);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-3", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout())
                .contains("[brand-new-category]", "some-var = some-value")
                .doesNotContain("var-to-delete", "value-to-delete");
    }

    @Test
    void shouldUpdateValueUsingVarSelector() throws Exception {
        //given
        String filename = "asterisk-java-test-update-by-var.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-var");
        config2.setLine(0);
        config2.setValue("some-value");

        Insert config3 = new Insert();
        config3.setCat("brand-new-category");
        config3.setVar("var-to-update");
        config3.setLine(1);
        config3.setValue("value-to-update");

        UpdateConfigAction updateConfigAction1 = createUpdateConfigAction("id-2", filename, config1, config2, config3);

        Update config4 = new Update();
        config4.setCat("brand-new-category");
        config4.setVar("var-to-update");
        config4.setValue("new-value");

        UpdateConfigAction updateConfigAction2 = createUpdateConfigAction("id-3", filename, config4);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction1)
                .registerAction(updateConfigAction2);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-3", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout())
                .contains("[brand-new-category]", "some-var = some-value", "var-to-update = new-value");
    }

    @Test
    void shouldNotUpdateWhenVarIsMatchingButMatchNot() throws Exception {
        //given
        String filename = "asterisk-java-test-not-update-when-var-matching-match-not.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-var");
        config2.setLine(0);
        config2.setValue("some-value");

        Insert config3 = new Insert();
        config3.setCat("brand-new-category");
        config3.setVar("var-to-update");
        config3.setLine(1);
        config3.setValue("value-to-update");

        UpdateConfigAction updateConfigAction1 = createUpdateConfigAction("id-2", filename, config1, config2, config3);

        Update config4 = new Update();
        config4.setCat("brand-new-category");
        config4.setVar("var-to-update");
        config4.setValue("new-value");
        config4.setMatch("not-matching");

        UpdateConfigAction updateConfigAction2 = createUpdateConfigAction("id-3", filename, config4);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction1)
                .registerAction(updateConfigAction2);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-3", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Error);
        assertThat(actual.getMessage()).isEqualTo("Update did not complete successfully");

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout())
                .contains("[brand-new-category]", "some-var = some-value", "var-to-update = value-to-update");
    }

    @Test
    void shouldCleanupCategory() throws Exception {
        //given
        String filename = "asterisk-java-test-cleanup-category.conf";

        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("id-1");
        createConfigAction.setFilename(filename);

        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Insert config2 = new Insert();
        config2.setCat("brand-new-category");
        config2.setVar("some-var");
        config2.setLine(0);
        config2.setValue("some-value");

        Insert config3 = new Insert();
        config3.setCat("brand-new-category");
        config3.setVar("another-var");
        config3.setLine(1);
        config3.setValue("another-value");

        UpdateConfigAction updateConfigAction1 = createUpdateConfigAction("id-2", filename, config1, config2, config3);

        EmptyCat config4 = new EmptyCat();
        config4.setCat("brand-new-category");

        UpdateConfigAction updateConfigAction2 = createUpdateConfigAction("id-3", filename, config4);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction1)
                .registerAction(updateConfigAction2);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        DefaultActionResponse actual = responseRecorder.getRecorderResponse("id-3", DefaultActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);

        Container.ExecResult execResult = asteriskDocker.execInContainer("cat", "/etc/asterisk/%s".formatted(filename));
        assertThat(execResult.getExitCode()).isZero();
        assertThat(execResult.getStdout())
                .contains("[brand-new-category]")
                .doesNotContain("some-var = some-value", "another-var = another-value");
    }

    private static UpdateConfigAction createUpdateConfigAction(String actionId, String filename, Config... configs) {
        UpdateConfigAction updateConfigAction1 = new UpdateConfigAction();
        updateConfigAction1.setActionId(actionId);
        updateConfigAction1.setSrcFilename(filename);
        updateConfigAction1.setDstFilename(filename);
        updateConfigAction1.setConfigs(asList(configs));
        return updateConfigAction1;
    }
}
