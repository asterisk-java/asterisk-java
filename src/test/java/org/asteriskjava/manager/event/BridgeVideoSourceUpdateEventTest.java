/*
 *  Copyright 2023 Hector Espert
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.event;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.manager.util.EventAttributesHelper.setAttributes;

class BridgeVideoSourceUpdateEventTest {

    @Test
    void shouldCreateEvent() {
        BridgeVideoSourceUpdateEvent bridgeVideoSourceUpdateEvent = new BridgeVideoSourceUpdateEvent(new Object());

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("bridgeuniqueid", "85f11e74-b0d9-4354-bc91-80782ee7a6a7");
        attributes.put("bridgepreviousvideosource", "e39cdee4-61a1-49b5-9656-96cef742806f");
        setAttributes(bridgeVideoSourceUpdateEvent, attributes, new HashSet<>());

        assertThat(bridgeVideoSourceUpdateEvent.getBridgeUniqueId())
            .isEqualTo("85f11e74-b0d9-4354-bc91-80782ee7a6a7");
        assertThat(bridgeVideoSourceUpdateEvent.getBridgePreviousVideoSource())
            .isEqualTo("e39cdee4-61a1-49b5-9656-96cef742806f");
    }

}