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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.asteriskjava.core.databind.annotation.AsteriskDeserialize;
import org.asteriskjava.core.databind.annotation.AsteriskName;
import org.asteriskjava.core.databind.annotation.AsteriskSerialize;
import org.asteriskjava.core.databind.deserializer.custom.EqualsSignDeserializer;
import org.asteriskjava.core.databind.serializer.custom.ComaJoiningSerializer;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;
import static org.asteriskjava.core.databind.AsteriskObjectMapper.builder;
import static org.asteriskjava.core.databind.AsteriskObjectMapperTest.Base.ResponseType.Goodbye;
import static org.asteriskjava.core.databind.AsteriskObjectMapperTest.SimpleBean.AuthType.MD5;

class AsteriskObjectMapperTest {
    private final AsteriskObjectMapper asteriskObjectMapper = builder()
            .crlfNewlineDelimiter()
            .build();

    @Test
    void shouldWriteAsStringForSimpleTypes() {
        //given
        SimpleBean bean = new SimpleBean();
        bean.setActionId("id-1");
        bean.setAuthType(MD5);
        bean.setCodecs(List.of("codec1", "codec2"));

        //when
        String string = asteriskObjectMapper.writeValueAsString(bean);

        //then
        String expected = "Action: SimpleBean" + CRLF.getPattern();
        expected += "ActionID: id-1" + CRLF.getPattern();
        expected += "AuthType: MD5" + CRLF.getPattern();
        expected += "Codecs: codec1,codec2" + CRLF.getPattern() + CRLF.getPattern();
        assertThat(string).isEqualTo(expected);
    }

    public static class SimpleBean {
        public enum AuthType {
            MD5,
        }

        private String actionId;

        private AuthType authType;

        private List<String> codecs;

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

        @AsteriskSerialize(ComaJoiningSerializer.class)
        public List<String> getCodecs() {
            return codecs;
        }

        public void setCodecs(List<String> codecs) {
            this.codecs = codecs;
        }
    }

    @Test
    void shouldReadForSimpleTypes() {
        //given
        Instant date = Instant.parse("2023-11-20T20:33:30.002Z");

        Map<String, Object> content = Map.of(
                "ActionID", "id-1",
                "DateReceived", date.toString(),
                "Challenge", "123456",
                "Response", "Goodbye"
        );

        //when
        SomeClass someClass = asteriskObjectMapper.readValue(content, SomeClass.class);

        //then
        SomeClass expected = new SomeClass();
        expected.setChallenge("123456");
        expected.setActionId("id-1");
        expected.setDateReceived(date);
        expected.setResponse(Goodbye);
        assertThat(someClass).isEqualTo(expected);
    }

    @Test
    void shouldReadContentWithList() {
        //given
        Map<String, Object> content = Map.of(
                "Number", List.of("1", "2", "3")
        );

        //when
        SomeClass2 someClass = asteriskObjectMapper.readValue(content, SomeClass2.class);

        //then
        SomeClass2 expected = new SomeClass2();
        expected.setNumbers(List.of(1, 2, 3));
        assertThat(someClass).isEqualTo(expected);
    }

    @Test
    void shouldReadContentWithListWhichShouldBeAMap() {
        //given
        Map<String, Object> content = Map.of(
                "Header", List.of("1=name1", "2=name2", "3=name3")
        );

        //when
        SomeClass3 someClass = asteriskObjectMapper.readValue(content, SomeClass3.class);

        //then
        Map<Integer, String> map = Map.of(
                1, "name1",
                2, "name2",
                3, "name3"
        );
        SomeClass3 expected = new SomeClass3();
        expected.setHeaders(map);
        assertThat(someClass).isEqualTo(expected);
    }

    @Test
    void shouldReadContentWithMap() {
        //given
        Map<String, Object> content = Map.of("Header", "1=name1");

        //when
        SomeClass3 someClass = asteriskObjectMapper.readValue(content, SomeClass3.class);

        //then
        Map<Integer, String> map = Map.of(1, "name1");
        SomeClass3 expected = new SomeClass3();
        expected.setHeaders(map);
        assertThat(someClass).isEqualTo(expected);
    }

    public static class Base {
        public enum ResponseType {
            Success,
            Error,
            Goodbye,
        }

        private ResponseType response;

        private Instant dateReceived;

        private String actionId;

        public ResponseType getResponse() {
            return response;
        }

        public void setResponse(ResponseType response) {
            this.response = response;
        }

        public Instant getDateReceived() {
            return dateReceived;
        }

        public void setDateReceived(Instant dateReceived) {
            this.dateReceived = dateReceived;
        }

        public String getActionId() {
            return actionId;
        }

        @AsteriskName("ActionID")
        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            Base base = (Base) object;

            return new EqualsBuilder()
                    .append(response, base.response)
                    .append(dateReceived, base.dateReceived)
                    .append(actionId, base.actionId)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(response)
                    .append(dateReceived)
                    .append(actionId)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("response", response)
                    .append("dateReceived", dateReceived)
                    .append("actionId", actionId)
                    .toString();
        }
    }

    public static class SomeClass extends Base {
        private String challenge;

        public String getChallenge() {
            return challenge;
        }

        public void setChallenge(String challenge) {
            this.challenge = challenge;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            SomeClass someClass = (SomeClass) object;

            return new EqualsBuilder()
                    .appendSuper(super.equals(object))
                    .append(challenge, someClass.challenge)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .appendSuper(super.hashCode())
                    .append(challenge)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("challenge", challenge)
                    .toString();
        }
    }

    public static class SomeClass2 {
        private List<Integer> numbers;

        public List<Integer> getNumbers() {
            return numbers;
        }

        @AsteriskName("Number")
        public void setNumbers(List<Integer> numbers) {
            this.numbers = numbers;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            SomeClass2 someClass = (SomeClass2) object;

            return new EqualsBuilder()
                    .append(numbers, someClass.numbers)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(numbers)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("numbers", numbers)
                    .toString();
        }
    }

    public static class SomeClass3 {
        private Map<Integer, String> headers;

        public Map<Integer, String> getHeaders() {
            return headers;
        }

        @AsteriskName("Header")
        @AsteriskDeserialize(EqualsSignDeserializer.class)
        public void setHeaders(Map<Integer, String> headers) {
            this.headers = headers;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            SomeClass3 someClass = (SomeClass3) object;

            return new EqualsBuilder()
                    .append(headers, someClass.headers)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(headers)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("headers", headers)
                    .toString();
        }
    }
}
