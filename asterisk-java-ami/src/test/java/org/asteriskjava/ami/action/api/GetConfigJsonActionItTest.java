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

import org.asteriskjava.ami.action.api.UpdateConfigAction.Append;
import org.asteriskjava.ami.action.api.UpdateConfigAction.NewCat;
import org.asteriskjava.ami.action.api.response.GetConfigJsonActionResponse;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.api.response.ResponseType.Success;

class GetConfigJsonActionItTest extends BaseActionItTest {
    @Test
    void shouldReturnWholeConfig() throws InterruptedException, JSONException {
        //given
        String filename = "asterisk-java-sample-config.conf";

        CreateConfigAction createConfigAction = createConfigAction(filename);
        UpdateConfigAction updateConfigAction = updateConfigAction(filename);

        GetConfigJsonAction getConfigJsonAction = new GetConfigJsonAction();
        getConfigJsonAction.setActionId("id-1");
        getConfigJsonAction.setFilename(filename);

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction)
                .registerAction(getConfigJsonAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        GetConfigJsonActionResponse actual = responseRecorder.getRecorderResponse("id-1", GetConfigJsonActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        String expectedStr = """
                {
                    "brand-new-category":{
                        "some-variable":"some-value",
                        "another-variable":"another-value"
                    },
                    "again-new-category":{
                        "var":"value"
                    }
                }
                """;
        JSONAssert.assertEquals(expectedStr, actual.getJson(), false);
    }

    @Test
    void shouldReturnConfigForCategory() throws InterruptedException, JSONException {
        //given
        String filename = "asterisk-java-sample-config.conf";

        CreateConfigAction createConfigAction = createConfigAction(filename);
        UpdateConfigAction updateConfigAction = updateConfigAction(filename);

        GetConfigJsonAction getConfigJsonAction = new GetConfigJsonAction();
        getConfigJsonAction.setActionId("id-1");
        getConfigJsonAction.setFilename(filename);
        getConfigJsonAction.setCategory("brand-new-category");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction)
                .registerAction(getConfigJsonAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        GetConfigJsonActionResponse actual = responseRecorder.getRecorderResponse("id-1", GetConfigJsonActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        String expectedStr = """
                {
                    "brand-new-category":{
                        "some-variable":"some-value",
                        "another-variable":"another-value"
                    }
                }
                """;
        JSONAssert.assertEquals(expectedStr, actual.getJson(), false);
    }

    @Test
    void shouldReturnConfigWhereVariablesMatchingToFilter() throws InterruptedException, JSONException {
        //given
        String filename = "asterisk-java-sample-config.conf";

        CreateConfigAction createConfigAction = createConfigAction(filename);
        UpdateConfigAction updateConfigAction = updateConfigAction(filename);

        GetConfigJsonAction getConfigJsonAction = new GetConfigJsonAction();
        getConfigJsonAction.setActionId("id-1");
        getConfigJsonAction.setFilename(filename);
        getConfigJsonAction.setFilter("var=value");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(createConfigAction)
                .registerAction(updateConfigAction)
                .registerAction(getConfigJsonAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        GetConfigJsonActionResponse actual = responseRecorder.getRecorderResponse("id-1", GetConfigJsonActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(Success);
        String expectedStr = """
                {
                    "again-new-category":{
                        "var":"value"
                    }
                }
                """;
        JSONAssert.assertEquals(expectedStr, actual.getJson(), false);
    }

    private static CreateConfigAction createConfigAction(String filename) {
        CreateConfigAction createConfigAction = new CreateConfigAction();
        createConfigAction.setActionId("config-1");
        createConfigAction.setFilename(filename);
        return createConfigAction;
    }

    private static UpdateConfigAction updateConfigAction(String filename) {
        NewCat config1 = new NewCat();
        config1.setCat("brand-new-category");

        Append config2 = new Append();
        config2.setCat("brand-new-category");
        config2.setVar("some-variable");
        config2.setValue("some-value");

        Append config3 = new Append();
        config3.setCat("brand-new-category");
        config3.setVar("another-variable");
        config3.setValue("another-value");

        NewCat config4 = new NewCat();
        config4.setCat("again-new-category");

        Append config5 = new Append();
        config5.setCat("again-new-category");
        config5.setVar("var");
        config5.setValue("value");

        UpdateConfigAction updateConfigAction = new UpdateConfigAction();
        updateConfigAction.setActionId("config-2");
        updateConfigAction.setSrcFilename(filename);
        updateConfigAction.setDstFilename(filename);
        updateConfigAction.setConfigs(List.of(config1, config2, config3, config4, config5));
        return updateConfigAction;
    }
}
