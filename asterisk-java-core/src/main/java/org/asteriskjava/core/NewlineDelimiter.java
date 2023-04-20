/*
 * Copyright 2004-2022 Asterisk Java contributors
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
package org.asteriskjava.core;

/**
 * Newline delimiters used for determine how lines was sent/received to/from Asterisk.
 *
 * @author Piotr Olaszewski
 */
public enum NewlineDelimiter {
    /**
     * AGI uses <b>LF</b> (Line Feed) as a newline delimiter.
     */
    LF("\n"),

    /**
     * AMI uses <b>CRLF</b> (Carriage Return + Line Feed) as a newline delimiter.
     */
    CRLF("\r\n"),
    ;

    private final String pattern;

    NewlineDelimiter(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
