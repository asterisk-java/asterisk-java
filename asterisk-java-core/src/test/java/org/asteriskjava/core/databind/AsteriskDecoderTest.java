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
import org.asteriskjava.core.databind.annotation.AsteriskName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.NewlineDelimiter.LF;
import static org.asteriskjava.core.databind.AsteriskDecoderTest.BaseBean.ResponseType.Goodbye;

class AsteriskDecoderTest {
    private final AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

    @Test
    void shouldDecodeForSimpleBeanWhichExtendsFormBaseBean() {
        //given
        Instant date = Instant.parse("2023-11-20T20:33:30.002Z");

        Map<String, Object> content = Map.of(
                "ActionID", "id-1",
                "DateReceived", date.toString(),
                "Challenge", "123456",
                "Response", "Goodbye"
        );

        //when
        SimpleBean simpleBean = asteriskDecoder.decode(content, SimpleBean.class);

        //then
        SimpleBean expected = new SimpleBean();
        expected.setChallenge("123456");
        expected.setActionId("id-1");
        expected.setDateReceived(date);
        expected.setResponse(Goodbye);
        assertThat(simpleBean).isEqualTo(expected);
    }

    @Test
    void shouldDecodeListValues() {
        //given
        AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        Map<String, Object> content = Map.of(
                "Number", List.of("1", "2", "3")
        );

        //when
        ListBean listBean = asteriskDecoder.decode(content, ListBean.class);

        //then
        ListBean expected = new ListBean();
        expected.setNumbers(List.of(1, 2, 3));
        assertThat(listBean).isEqualTo(expected);
    }

    @Test
    void shouldDecodeMap() {
        //given
        AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        Map<String, Object> content = Map.of("Header", "1=name1");

        //when
        MapBean someClass = asteriskDecoder.decode(content, MapBean.class);

        //then
        Map<Integer, String> map = Map.of(1, "name1");
        MapBean expected = new MapBean();
        expected.setHeaders(map);
        assertThat(someClass).isEqualTo(expected);
    }

    @Test
    void shouldDecodeMapWhenIsListOfEntries() {
        //given
        AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        Map<String, Object> content = Map.of(
                "Header", List.of("1=name1", "2=name2", "3=name3")
        );

        //when
        MapBean mapBean = asteriskDecoder.decode(content, MapBean.class);

        //then
        Map<Integer, String> map = Map.of(
                1, "name1",
                2, "name2",
                3, "name3"
        );
        MapBean expected = new MapBean();
        expected.setHeaders(map);
        assertThat(mapBean).isEqualTo(expected);
    }

    @Test
    void shouldDecodeSimpleBeanFromString() {
        //given
        AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        Instant date = Instant.parse("2023-11-20T20:33:30.002Z");
        String string = """
                ActionID: id-1
                DateReceived: %s
                Challenge: 123456
                Response: Goodbye
                """.formatted(date);
        String[] content = string.split(LF.getPattern());

        //when
        SimpleBean simpleBean = asteriskDecoder.decode(content, SimpleBean.class);

        //then
        SimpleBean expected = new SimpleBean();
        expected.setChallenge("123456");
        expected.setActionId("id-1");
        expected.setDateReceived(date);
        expected.setResponse(Goodbye);
        assertThat(simpleBean).isEqualTo(expected);
    }

    @Test
    void shouldDecoderListFromString() {
        //given
        AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        String string = """
                Number: 1
                Number: 2
                Number: 3
                """;
        String[] content = string.split(LF.getPattern());

        //when
        ListBean listBean = asteriskDecoder.decode(content, ListBean.class);

        //then
        ListBean expected = new ListBean();
        expected.setNumbers(List.of(1, 2, 3));
        assertThat(listBean).isEqualTo(expected);
    }

    @Test
    void shouldDecodeMapFromString() {
        //given
        AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        String string = """
                Header: 1=name1
                """;
        String[] content = string.split(LF.getPattern());

        //when
        MapBean someClass = asteriskDecoder.decode(content, MapBean.class);

        //then
        Map<Integer, String> map = Map.of(1, "name1");
        MapBean expected = new MapBean();
        expected.setHeaders(map);
        assertThat(someClass).isEqualTo(expected);
    }

    @Test
    void shouldDecodeMapWhenIsListOfEntriesFromString() {
        //given
        AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        String string = """
                Header: 1=name1
                Header: 2=name2
                Header: 3=name3
                """;
        String[] content = string.split(LF.getPattern());

        //when
        MapBean mapBean = asteriskDecoder.decode(content, MapBean.class);

        //then
        Map<Integer, String> map = Map.of(
                1, "name1",
                2, "name2",
                3, "name3"
        );
        MapBean expected = new MapBean();
        expected.setHeaders(map);
        assertThat(mapBean).isEqualTo(expected);
    }

    public static class BaseBean {
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

            BaseBean base = (BaseBean) object;

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

    public static class SimpleBean extends BaseBean {
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

            SimpleBean someClass = (SimpleBean) object;

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

    public static class ListBean {
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

            ListBean someClass = (ListBean) object;

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

    public static class MapBean {
        private Map<Integer, String> headers;

        public Map<Integer, String> getHeaders() {
            return headers;
        }

        @AsteriskName("Header")
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

            MapBean someClass = (MapBean) object;

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
