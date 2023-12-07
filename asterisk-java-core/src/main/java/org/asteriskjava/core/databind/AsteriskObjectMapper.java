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

import org.asteriskjava.core.NewlineDelimiter;
import org.asteriskjava.core.databind.reader.AsteriskObjectReader;
import org.asteriskjava.core.databind.writer.AsteriskObjectWriter;

import java.util.Comparator;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;
import static org.asteriskjava.core.NewlineDelimiter.LF;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskObjectMapper {
    private final NewlineDelimiter newlineDelimiter;
    private final Comparator<String> fieldNamesComparator;

    private AsteriskObjectMapper(NewlineDelimiter newlineDelimiter, Comparator<String> fieldNamesComparator) {
        this.newlineDelimiter = newlineDelimiter;
        this.fieldNamesComparator = fieldNamesComparator;
    }

    public String writeValueAsString(Object value) {
        AsteriskObjectWriter asteriskObjectWriter = new AsteriskObjectWriter(newlineDelimiter, fieldNamesComparator);
        return asteriskObjectWriter.write(value);
    }

    public <T> T readValue(Map<String, Object> content, Class<T> clazz) {
        AsteriskObjectReader asteriskObjectReader = new AsteriskObjectReader();
        return asteriskObjectReader.read(content, clazz);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private NewlineDelimiter newlineDelimiter = CRLF;
        private Comparator<String> fieldNamesComparator;

        public Builder newlineDelimiter(NewlineDelimiter newlineDelimiter) {
            this.newlineDelimiter = requireNonNull(newlineDelimiter, "newlineDelimiter cannot be null");
            return this;
        }

        public Builder crlfNewlineDelimiter() {
            return newlineDelimiter(CRLF);
        }

        public Builder lfNewlineDelimiter() {
            return newlineDelimiter(LF);
        }

        public Builder fieldNamesComparator(Comparator<String> fieldNamesComparator) {
            this.fieldNamesComparator = requireNonNull(fieldNamesComparator, "fieldNamesComparator cannot be null");
            return this;
        }

        public AsteriskObjectMapper build() {
            return new AsteriskObjectMapper(newlineDelimiter, fieldNamesComparator);
        }
    }
}
