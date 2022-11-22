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
package org.asteriskjava.manager.response;

import org.asteriskjava.manager.util.EventAttributesHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SipShowPeerResponseTest {
    private SipShowPeerResponse response;

    @BeforeEach
    void setUp() {
        response = new SipShowPeerResponse();
    }

    @Test
    void shouldCreateResponse() {
        //given
        SipShowPeerResponse sipShowPeerResponse = new SipShowPeerResponse();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("named pickupgroup", "");
        attributes.put("sip-rtcp-mux", "N");
        attributes.put("description", "");
        attributes.put("subscribecontext", "");
        attributes.put("named callgroup", "");

        //when
        EventAttributesHelper.setAttributes(sipShowPeerResponse, attributes, new HashSet<>());

        //then
        assertThat(sipShowPeerResponse.getNamedPickupgroup()).isBlank();
        assertThat(sipShowPeerResponse.getSipRtcpMux()).isEqualTo("N");
        assertThat(sipShowPeerResponse.getDescription()).isBlank();
        assertThat(sipShowPeerResponse.getSubscribecontext()).isBlank();
        assertThat(sipShowPeerResponse.getNamedCallgroup()).isBlank();
    }

    @Test
    void testSetQualifyFreq() {
        response.setQualifyFreq("6000 ms");
        assertEquals(6000, (int) response.getQualifyFreq(), "Incorrect qualifyFreq");
    }

    @Test
    void testSetQualifyFreqWithWorkaround() {
        response.setQualifyFreq(": 6000 ms\n");
        assertEquals(6000, (int) response.getQualifyFreq(), "Incorrect qualifyFreq");
    }

    @Test
    void testSetQualifyFreqWithWorkaroundAndChanVariable() {
        response.setQualifyFreq(": 60000 ms\nChanVariable:\n PHBX_ID,191");
        assertEquals(60000, (int) response.getQualifyFreq(), "Incorrect qualifyFreq");
    }

    @Test
    void testSetMohsuggest() {
        response.setMohsuggest("default");
        assertEquals("default", response.getMohsuggest(), "Incorrect mohsuggest");
    }


}
