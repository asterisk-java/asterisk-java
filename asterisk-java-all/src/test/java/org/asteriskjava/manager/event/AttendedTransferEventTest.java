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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.manager.util.EventAttributesHelper.setAttributes;

class AttendedTransferEventTest {
    @Test
    void shouldCreateEvent() {
        //given
        AttendedTransferEvent attendedTransferEvent = new AttendedTransferEvent(new Object());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("transfertargetcalleridname", "<unknown>");
        attributes.put("secondbridgevideosourcemode", "none");
        attributes.put("transfertargetlinkedid", "1668752056.12");
        attributes.put("transfertargetpriority", "1");
        attributes.put("transfertargetcalleridnum", "102");
        attributes.put("origbridgevideosourcemode", "none");
        attributes.put("transfertargetconnectedlinenum", "103");
        attributes.put("transfertargetchannel", "SIP/102-0000000d");
        attributes.put("transfertargetcontext", "phones");
        attributes.put("transfertargetconnectedlinename", "J.Doe");
        attributes.put("transfertargetexten", "");
        attributes.put("transfertargetchannelstate", "6");
        attributes.put("transfertargetlanguage", "en");
        attributes.put("transfertargetaccountcode", "");
        attributes.put("transfertargetchannelstatedesc", "Up");

        //when
        setAttributes(attendedTransferEvent, attributes, new HashSet<>());

        //then
        assertThat(attendedTransferEvent.getTransferTargetCallerIDName()).isNull();
        assertThat(attendedTransferEvent.getSecondBridgeVideoSourceMode()).isNull();
        assertThat(attendedTransferEvent.getTransferTargetLinkedID()).isEqualTo("1668752056.12");
        assertThat(attendedTransferEvent.getTransferTargetPriority()).isEqualTo("1");
        assertThat(attendedTransferEvent.getTransferTargetCallerIDNum()).isEqualTo("102");
        assertThat(attendedTransferEvent.getOrigBridgeVideoSourceMode()).isNull();
        assertThat(attendedTransferEvent.getTransferTargetConnectedLineNum()).isEqualTo("103");
        assertThat(attendedTransferEvent.getTransferTargetChannel()).isEqualTo("SIP/102-0000000d");
        assertThat(attendedTransferEvent.getTransferTargetContext()).isEqualTo("phones");
        assertThat(attendedTransferEvent.getTransferTargetConnectedLineName()).isEqualTo("J.Doe");
        assertThat(attendedTransferEvent.getTransferTargetExten()).isBlank();
        assertThat(attendedTransferEvent.getTransferTargetChannelState()).isEqualTo("6");
        assertThat(attendedTransferEvent.getTransferTargetLanguage()).isEqualTo("en");
        assertThat(attendedTransferEvent.getTransferTargetAccountCode()).isBlank();
        assertThat(attendedTransferEvent.getTransferTargetChannelStateDesc()).isEqualTo("Up");
    }
}
