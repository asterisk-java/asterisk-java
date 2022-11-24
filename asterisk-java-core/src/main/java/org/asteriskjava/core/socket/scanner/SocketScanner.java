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
package org.asteriskjava.core.socket.scanner;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.asteriskjava.core.socket.scanner.SocketScanner.NewlineDelimiter.CRLF;

/**
 * A socket scanner is used for read strings (lines) returned by Asterisk server.<p>
 * Depending on the implementation, different newline delimiters are used:
 * <ul>
 *     <li>
 *         AGI uses <b>Line Feed (LF)</b>.<br>
 *         Newline delimiter {@code \n}.
 *     </li>
 *     <li>
 *         AMI uses <b>Carriage Return + Line Feed (CRLF)</b>.<br>
 *         Newline delimiter {@code \r\n}.
 *     </li>
 * </ul>
 *
 * <p>Use static factory method {@link #lfSocketScanner(Readable)} or {@link #crLfSocketScanner(Readable)} or
 * {@link #create(Readable, NewlineDelimiter)} or {@link #legacy(Readable, NewlineDelimiter)} to prepare an
 * {@code SocketScanner} instance.
 *
 * @author Robert Sutton
 * @author Piotr Olaszewski
 */
public interface SocketScanner extends Closeable {
    /**
     * Finds and returns the next line from this scanner.
     *
     * @return the next token
     * @throws IOException if this scanner cannot read the stream
     */
    String next() throws IOException;

    /**
     * Create a {@link SocketScanner} which use {@code \n} (LF) as a newline delimiter.
     *
     * @param source the character source implementing the {@link Readable} interface
     */
    static SocketScanner lfSocketScanner(Readable source) {
        return new LfFastSocketScanner(source);
    }

    /**
     * Create a {@link SocketScanner} which use {@code \r\n} (CRLF) as a newline delimiter.
     *
     * @param source the character source implementing the {@link Readable} interface
     */
    static SocketScanner crLfSocketScanner(Readable source) {
        return new CrLfFastSocketScanner(source);
    }

    /**
     * Create a {@link SocketScanner} depending on {@link NewlineDelimiter}.
     *
     * @param source           the character source implementing the {@link Readable} interface
     * @param newLineDelimiter the type which determine a newline delimiter
     */
    static SocketScanner create(Readable source, NewlineDelimiter newLineDelimiter) {
        if (newLineDelimiter == CRLF) {
            return crLfSocketScanner(source);
        }
        return lfSocketScanner(source);
    }

    /**
     * Create a legacy implementation of {@link SocketScanner} which uses a {@link Scanner} Java class.
     *
     * @param source           the character source implementing the {@link Readable} interface
     * @param newLineDelimiter the type which determine a newline delimiter
     */
    static SocketScanner legacy(Readable source, NewlineDelimiter newLineDelimiter) {
        Scanner scanner = new Scanner(source)
            .useDelimiter(newLineDelimiter.getPattern());
        return new LegacySocketScanner(scanner);
    }

    /**
     * Type of {@link SocketScanner}.<p>
     */
    enum NewlineDelimiter {
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

        public static NewlineDelimiter forPattern(Pattern pattern) {
            return stream(values())
                .filter(type -> type.pattern.equals(pattern.pattern()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("Cannot find type for pattern '%s'.", pattern.toString())));
        }
    }
}
