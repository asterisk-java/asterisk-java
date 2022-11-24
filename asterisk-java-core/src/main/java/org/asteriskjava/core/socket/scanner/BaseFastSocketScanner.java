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

import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.CharBuffer.allocate;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Convenient base class for supporting fast scanners.<p>
 * It's holds an information about read buffer.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
abstract class BaseFastSocketScanner implements SocketScanner {
    private static final Logger logger = getLogger(BaseFastSocketScanner.class);

    private static final int BUFFER_SIZE = 8192;

    protected static final CharBuffer lineCharBuffer = allocate(BUFFER_SIZE);

    protected final StringBuilder resultLine = new StringBuilder(80);
    private final AtomicReference<Readable> readableReference = new AtomicReference<>();

    protected int start = 0;
    protected int end = 0;

    private boolean firstLine = true;
    private boolean closed = false;

    BaseFastSocketScanner(Readable reader) {
        requireNonNull(reader, "reader cannot be null");

        this.readableReference.set(reader);
    }

    @Override
    public String next() throws IOException {
        String line = getLine();

        if (line != null) {
            return line;
        }

        // Doing this instead of a synchronized block to avoid a possible deadlock between calling
        // readable.read() and a call to close() on another thread, which it appears that
        // Asterisk Java permits and/or does.
        Readable readable = readableReference.get();

        int bytes = 0;
        // Read from the stream to the buffer.
        while (readable != null && (bytes = readable.read(lineCharBuffer)) > -1) {
            lineCharBuffer.position(0);

            end = bytes;

            line = getLine();

            if (line != null) {
                if (firstLine && line.length() == 0) {
                    // If the first character of the stream is the line delimiter - try again.
                    line = getLine();
                }
                break;
            }
        }

        firstLine = false;

        if (line == null) {
            // The line is empty, get the contents from the StringBuilder if any.
            line = resultLine.toString();
            resultLine.setLength(0);

            // If the reader is closed and there is no output.
            if (readableReference.get() == null && line.length() == 0) {
                return null;
            }

            // If at the end of the inputStream and there is no output.
            if (bytes == -1 && line.length() == 0) {
                return null;
            }
        }

        return line;
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        if (readableReference.get() instanceof Closeable) {
            try {
                ((Closeable) readableReference.get()).close();
            } catch (IOException e) {
                logger.error("Cannot close Readable source.", e);
            }
        }
        readableReference.set(null);
        closed = true;
    }

    protected abstract String getLine();

    protected void clearAtTheEndOfBuffer() {
        if (end >= start) {
            // We've hit the end of the buffer, copy the contents to the StringBuilder.
            // Then return null, so we'll loop again and get the next part.
            resultLine.append(lineCharBuffer.subSequence(start, start + (end - start)));
            start = 0;
            end = 0;
        }
    }
}
