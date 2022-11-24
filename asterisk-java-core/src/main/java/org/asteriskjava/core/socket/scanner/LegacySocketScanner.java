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

import java.io.IOException;
import java.util.Scanner;

/**
 * Adapter for the legacy Java {@link Scanner}.
 *
 * @author Piotr Olaszewski
 * @see CrLfFastSocketScanner
 * @see LfFastSocketScanner
 * @since 4.0.0
 */
class LegacySocketScanner implements SocketScanner {
    private final Scanner scanner;

    LegacySocketScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String next() throws IOException {
        return scanner.next();
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
