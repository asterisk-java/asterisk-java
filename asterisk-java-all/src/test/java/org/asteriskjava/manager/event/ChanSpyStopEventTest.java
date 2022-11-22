/*
 * Copyright 2004-2022 Asterisk-Java contributors
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
package org.asteriskjava.manager.event;

import org.asteriskjava.manager.util.EventAttributesHelper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ChanSpyStopEventTest {
    @Test
    void shouldCreateEvent() {
        //given
        ChanSpyStopEvent chanSpyStopEvent = new ChanSpyStopEvent(new Object());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("spyeechannelstate", "");
        attributes.put("spyeeconnectedlinename", "<unknown>");
        attributes.put("spyeelinkedid", "out-87066661337-009277");
        attributes.put("spyeelanguage", "en");
        attributes.put("spyeecontext", "cce");
        attributes.put("spyeeuniqueid", "out-87066661337-009277");
        attributes.put("spyeeconnectedlinenum", "<unknown>");
        attributes.put("spyeecalleridnum", "87066661337");
        attributes.put("spyeraccountcode", "");
        attributes.put("spyeechannelstatedesc", "Up");
        attributes.put("spyeepriority", "1");
        attributes.put("spyeeaccountcode", "");
        attributes.put("spyeecalleridname", "<unknown>");
        attributes.put("spyeeexten", "87066661337");

        //when
        EventAttributesHelper.setAttributes(chanSpyStopEvent, attributes, new HashSet<>());

        //then
        assertThat(chanSpyStopEvent.getSpyerAccountCode()).isEqualTo("");

        assertThat(chanSpyStopEvent.getSpyeeChannelState()).isNull();
        assertThat(chanSpyStopEvent.getSpyeeConnectedLineName()).isNull();
        assertThat(chanSpyStopEvent.getSpyeeLinkedId()).isEqualTo("out-87066661337-009277");
        assertThat(chanSpyStopEvent.getSpyeeLanguage()).isEqualTo("en");
        assertThat(chanSpyStopEvent.getSpyeeUniqueId()).isEqualTo("out-87066661337-009277");
        assertThat(chanSpyStopEvent.getSpyeeConnectedLineNum()).isNull();
        assertThat(chanSpyStopEvent.getSpyeeCallerIdNum()).isEqualTo("87066661337");
        assertThat(chanSpyStopEvent.getSpyeeCallerIdName()).isNull();
        assertThat(chanSpyStopEvent.getSpyeeChannelStateDesc()).isEqualTo("Up");
        assertThat(chanSpyStopEvent.getSpyeePriority()).isEqualTo(1);
        assertThat(chanSpyStopEvent.getSpyeeAccountCode()).isEqualTo("");
        assertThat(chanSpyStopEvent.getSpyeeCallerIdName()).isNull();
        assertThat(chanSpyStopEvent.getSpyeeExten()).isEqualTo("87066661337");
    }
}
