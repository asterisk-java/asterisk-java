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

import static org.asteriskjava.core.socket.scanner.SocketScanner.NewlineDelimiter.CRLF;

/**
 * This a fast scanner implementation which take care about {@code \r\n} (CRLF) newlines delimiter.<p>
 * This implementation is used for AMI.
 *
 * @author Robert Sutton
 * @author Piotr Olaszewski
 * @see LfFastSocketScanner
 * @see LegacySocketScanner
 * @since 2.0.6
 */
class CrLfFastSocketScanner extends BaseFastSocketScanner {
    private static final char crLineDelimiter = CRLF.getPattern().charAt(0);
    private static final char lfLineDelimiter = CRLF.getPattern().charAt(1);

    private boolean seenFirstDelimiter = false;

    CrLfFastSocketScanner(Readable reader) {
        super(reader);
    }

    @Override
    protected String getLine() {
        // Iterate the buffer, looking for the line delimiter characters.
        for (int i = start; i < end; i++) {
            if (isFirstLineDelimiter(i)) {
                seenFirstDelimiter = true;
                continue;
            }

            if (isNotBothLineDelimitersInRowSeen(i)) {
                seenFirstDelimiter = false;
                continue;
            }

            // We've seen both of the delimiter characters, package up the data and return it.
            if (i == start) {
                // Back track to remove a CR in the last packet.
                resultLine.setLength(resultLine.length() - 1);
            }

            seenFirstDelimiter = false;

            if (i > start) {
                resultLine.append(lineCharBuffer.subSequence(start, start + (i - start) - 1));
            }

            // Skip the delimiter.
            start = i + 1;

            String line = resultLine.toString();
            resultLine.setLength(0);
            return line;
        }

        clearAtTheEndOfBuffer();

        return null;
    }

    private static boolean isFirstLineDelimiter(int i) {
        return lineCharBuffer.get(i) == crLineDelimiter;
    }

    private static boolean isSecondLineDelimiter(int i) {
        return lineCharBuffer.get(i) == lfLineDelimiter;
    }

    private boolean isNotBothLineDelimitersInRowSeen(int i) {
        return !(seenFirstDelimiter && isSecondLineDelimiter(i));
    }
}
