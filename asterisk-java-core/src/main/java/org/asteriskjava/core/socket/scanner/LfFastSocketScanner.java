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

import static org.asteriskjava.core.NewlineDelimiter.LF;

/**
 * This a fast scanner implementation which take care about {@code \n} (LF) newlines delimiter.<p>
 * This implementation is used for AGI.
 *
 * @author Robert Sutton
 * @author Piotr Olaszewski
 * @see CrLfFastSocketScanner
 * @see LegacySocketScanner
 * @since 2.0.6
 */
class LfFastSocketScanner extends BaseFastSocketScanner {
    private static final char lineDelimiter = LF.getPattern().charAt(0);

    LfFastSocketScanner(Readable reader) {
        super(reader);
    }

    @Override
    protected String getLine() {
        // Iterate the buffer, looking for the line delimiter character.
        for (int i = start; i < end; i++) {
            if (isLineDelimiter(i)) {
                if (i > start) {
                    // Add the buffer contents up to i to the output buffer.
                    resultLine.append(lineCharBuffer.subSequence(start, start + (i - start)));
                }

                // Skip the delimiter.
                start = i + 1;

                String line = resultLine.toString();
                // Reset the StringBuilder to 0 to avoid re-allocating memory.
                resultLine.setLength(0);
                return line;
            }
        }

        clearAtTheEndOfBuffer();

        return null;
    }

    private static boolean isLineDelimiter(int i) {
        return lineCharBuffer.get(i) == lineDelimiter;
    }
}
