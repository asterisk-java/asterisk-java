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
package org.asteriskjava.core.databind;

import org.asteriskjava.core.databind.annotation.AsteriskName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.naturalOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.NewlineDelimiter.LF;
import static org.asteriskjava.core.databind.AsteriskEncoderTest.SimpleBean.AuthType.MD5;

class AsteriskEncoderTest {
    @Test
    void shouldEncodeWithoutSortingFields() {
        //given
        AsteriskEncoder asteriskEncoder = new AsteriskEncoder(LF);

        Map<String, Object> source = new LinkedHashMap<>();
        source.put("Z", "value z");
        source.put("A", "value a");
        source.put("M", "value m");
        source.put("F", "value f");

        //when
        String encode = asteriskEncoder.encode(source);

        //then
        String expected = "Z: value z" + LF.getPattern();
        expected += "A: value a" + LF.getPattern();
        expected += "M: value m" + LF.getPattern();
        expected += "F: value f" + LF.getPattern() + LF.getPattern();
        assertThat(encode).isEqualTo(expected);
    }

    @Test
    void shouldEncodeAndSortFields() {
        //given
        AsteriskEncoder asteriskEncoder = new AsteriskEncoder(LF, naturalOrder());

        Map<String, Object> source = Map.of(
                "Z", "value z",
                "A", "value a",
                "M", "value m",
                "F", "value f"
        );

        //when
        String encode = asteriskEncoder.encode(source);

        //then
        String expected = "A: value a" + LF.getPattern();
        expected += "F: value f" + LF.getPattern();
        expected += "M: value m" + LF.getPattern();
        expected += "Z: value z" + LF.getPattern() + LF.getPattern();
        assertThat(encode).isEqualTo(expected);
    }

    @Test
    void shouldEncodeObject() {
        //given
        AsteriskEncoder asteriskEncoder = new AsteriskEncoder(LF);

        SimpleBean bean = new SimpleBean();
        bean.setActionId("id-1");
        bean.setAuthType(MD5);
        bean.setCodecs(List.of("codec1", "codec2"));
        Map<String, String> variable = new LinkedHashMap<>();
        variable.put("key1", "value1");
        variable.put("key2", "value2");
        variable.put("key3", "value3");
        bean.setVariable(variable);

        //when
        String string = asteriskEncoder.encode(bean);

        //then
        assertThat(string).contains(
                "Action: SimpleBean", "ActionID: id-1", "AuthType: MD5", "Codecs: codec1,codec2",
                "Variable: key1=value1", "Variable: key2=value2", "Variable: key3=value3"
        );
    }

    @Test
    void shouldNotEncodeNulls() {
        //given
        AsteriskEncoder asteriskEncoder = new AsteriskEncoder(LF);

        SimpleBean bean = new SimpleBean();
        bean.setActionId("id-1");
        bean.setAuthType(MD5);
        bean.setCodecs(null);
        Map<String, String> variable = new LinkedHashMap<>();
        variable.put("key1", "value1");
        variable.put("key2", "value2");
        variable.put("key3", "value3");
        bean.setVariable(variable);

        //when
        String string = asteriskEncoder.encode(bean);

        //then
        assertThat(string).contains(
                "Action: SimpleBean", "ActionID: id-1", "AuthType: MD5",
                "Variable: key1=value1", "Variable: key2=value2", "Variable: key3=value3"
        ).doesNotContain(
                "Codecs: null"
        );
    }

    public static class SimpleBean {
        public enum AuthType {
            MD5,
        }

        private String actionId;

        private AuthType authType;

        private List<String> codecs;

        private Map<String, String> variable;

        public String getAction() {
            return "SimpleBean";
        }

        @AsteriskName("ActionID")
        public String getActionId() {
            return actionId;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        public AuthType getAuthType() {
            return authType;
        }

        public void setAuthType(AuthType authType) {
            this.authType = authType;
        }

        public List<String> getCodecs() {
            return codecs;
        }

        public void setCodecs(List<String> codecs) {
            this.codecs = codecs;
        }

        public Map<String, String> getVariable() {
            return variable;
        }

        public void setVariable(Map<String, String> variable) {
            this.variable = variable;
        }
    }
}
