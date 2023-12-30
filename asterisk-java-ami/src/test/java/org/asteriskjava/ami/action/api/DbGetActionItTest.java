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

import org.asteriskjava.ami.action.api.response.DbGetActionResponse;
import org.asteriskjava.ami.action.api.response.event.DbGetCompleteEvent;
import org.asteriskjava.ami.action.api.response.event.DbGetResponseEvent;
import org.asteriskjava.ami.utils.ActionsRunner;
import org.asteriskjava.ami.utils.ActionsRunner.ResponseRecorder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.asteriskjava.ami.action.api.response.EventList.complete;
import static org.asteriskjava.ami.action.api.response.EventList.start;
import static org.asteriskjava.ami.action.api.response.ResponseType.success;

class DbGetActionItTest extends BaseActionItTest {
    @Test
    void shouldReturnEventsWithResponse() throws Exception {
        //given
        asteriskDocker.execInContainer("asterisk", "-rx", "database put family key1 value1");
        asteriskDocker.execInContainer("asterisk", "-rx", "database put family key2 value2");

        DbGetAction dbGetAction = new DbGetAction();
        dbGetAction.setActionId("id-1");
        dbGetAction.setFamily("family");
        dbGetAction.setKey("key1");

        ActionsRunner actionsRunner = actionRunner()
                .registerLoginSequence()
                .registerAction(dbGetAction);

        //when
        ResponseRecorder responseRecorder = actionsRunner.run();

        //then
        ResponseRecorder.Provider response = responseRecorder.getForAction("id-1");

        DbGetActionResponse actual = response.getResponse(DbGetActionResponse.class);
        assertThat(actual.getResponse()).isEqualTo(success);
        assertThat(actual.getEventList()).isEqualTo(start);
        assertThat(actual.getMessage()).isEqualTo("Result will follow");

        List<DbGetResponseEvent> actualResponseEvents1 = response.getResponseEvents(DbGetResponseEvent.class);
        assertThat(actualResponseEvents1)
                .extracting(DbGetResponseEvent::getEvent, DbGetResponseEvent::getFamily, DbGetResponseEvent::getKey, DbGetResponseEvent::getVal)
                .containsExactly(
                        tuple("DBGetResponse", "family", "key1", "value1")
                );

        List<DbGetCompleteEvent> actualResponseEvents2 = response.getResponseEvents(DbGetCompleteEvent.class);
        assertThat(actualResponseEvents2)
                .extracting(DbGetCompleteEvent::getEvent, DbGetCompleteEvent::getListItems, DbGetCompleteEvent::getEventList)
                .containsExactly(
                        tuple("DBGetComplete", 1, complete)
                );
    }
}
