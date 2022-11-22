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

class ConfbridgeJoinEventTest {
    @Test
    void shouldCreateEvent() {
        //given
        ConfbridgeJoinEvent confbridgeJoinEvent = new ConfbridgeJoinEvent(new Object());

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("language", "en");
        attributes.put("linkedid", "1474881412.1");

        //when
        setAttributes(confbridgeJoinEvent, attributes, new HashSet<>());

        //then
        assertThat(confbridgeJoinEvent.getLanguage()).isEqualTo("en");
        assertThat(confbridgeJoinEvent.getLinkedId()).isEqualTo("1474881412.1");
    }
}
