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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.manager.util.EventAttributesHelper.setAttributes;

class AgentConnectEventTest {
    @Test
    void shouldCreateEvent() {
        //given
        AgentConnectEvent agentConnectEvent = new AgentConnectEvent(new Object());

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("destexten", "600");
        attributes.put("destaccountcode", "441142993721");
        attributes.put("destchannelstatedesc", "Up");
        attributes.put("channelstate", "4");
        attributes.put("destuniqueid", "");
        attributes.put("calleridname", "");
        attributes.put("destconnectedlinename", "");
        attributes.put("destcalleridname", "202");
        attributes.put("priority", "4");
        attributes.put("destcalleridnum", "202");
        attributes.put("exten", "600");
        attributes.put("accountcode", "");
        attributes.put("connectedlinenum", "202");
        attributes.put("calleridnum", "07800656909");
        attributes.put("destpriority", "1");
        attributes.put("destcontext", "");
        attributes.put("channelstatedesc", "Ring");
        attributes.put("destchannel", "");
        attributes.put("connectedlinename", "202");
        attributes.put("destchannelstate", "6");
        attributes.put("destconnectedlinenum", "");
        attributes.put("interface", "");
        attributes.put("context", "");

        //when
        setAttributes(agentConnectEvent, attributes, new HashSet<>());

        //then
        assertThat(agentConnectEvent.getDestExten()).isEqualTo("600");
        assertThat(agentConnectEvent.getDestAccountCode()).isEqualTo("441142993721");
        assertThat(agentConnectEvent.getDestChannelStateDesc()).isEqualTo("Up");
        assertThat(agentConnectEvent.getChannelState()).isEqualTo(4);
        assertThat(agentConnectEvent.getDestUniqueId()).isEqualTo("");
        assertThat(agentConnectEvent.getCallerIdName()).isEqualTo("");
        assertThat(agentConnectEvent.getDestConnectedLineName()).isEqualTo("");
        assertThat(agentConnectEvent.getDestCallerIdName()).isEqualTo("202");
        assertThat(agentConnectEvent.getPriority()).isEqualTo(4);
        assertThat(agentConnectEvent.getDestCallerIdNum()).isEqualTo("202");
        assertThat(agentConnectEvent.getExten()).isEqualTo("600");
        assertThat(agentConnectEvent.getAccountcode()).isEqualTo("");
        assertThat(agentConnectEvent.getConnectedLineNum()).isEqualTo("202");
        assertThat(agentConnectEvent.getCallerIdNum()).isEqualTo("07800656909");
        assertThat(agentConnectEvent.getDestPriority()).isEqualTo("1");
        assertThat(agentConnectEvent.getDestContext()).isEqualTo("");
        assertThat(agentConnectEvent.getChannelStateDesc()).isEqualTo("Ring");
        assertThat(agentConnectEvent.getDestChannel()).isEqualTo("");
        assertThat(agentConnectEvent.getConnectedLineName()).isEqualTo("202");
        assertThat(agentConnectEvent.getDestChannelState()).isEqualTo("6");
        assertThat(agentConnectEvent.getDestConnectedLineNum()).isEqualTo("");
        assertThat(agentConnectEvent.getInterface()).isEqualTo("");
        assertThat(agentConnectEvent.getContext()).isEqualTo("");
    }
}
