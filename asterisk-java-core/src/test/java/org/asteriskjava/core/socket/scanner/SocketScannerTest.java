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

import org.asteriskjava.core.socket.scanner.SocketScanner.NewlineDelimiter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.socket.scanner.SocketScanner.NewlineDelimiter.CRLF;
import static org.asteriskjava.core.socket.scanner.SocketScanner.NewlineDelimiter.LF;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for {@link SocketScanner}.
 *
 * @author Robert Sutton
 * @author Piotr Olaszewski
 */
class SocketScannerTest {
    @Nested
    class Deterministic {
        private static final int NUMBER_OF_TOTAL_LINES = 10_000;

        @Test
        void shouldTestLfFastScanner() throws Exception {
            testFastScanner(SocketScanner::lfSocketScanner, LF);
        }

        @Test
        void shouldTestCrLfFastScanner() throws Exception {
            testFastScanner(SocketScanner::crLfSocketScanner, CRLF);
        }

        @Test
        void shouldScanLinesInFileDelimitedByLf() throws Exception {
            //given
            URL resource = getClass().getClassLoader().getResource("LfStreamReaderFastScanner.txt");
            InputStreamReader inputStreamReader = getInputStreamReader(readAllBytes(Paths.get(resource.toURI())));

            //when
            try (SocketScanner fastScanner = SocketScanner.lfSocketScanner(inputStreamReader)) {
                List<String> readLines = new ArrayList<>();
                String t;
                while ((t = fastScanner.next()) != null) {
                    readLines.add(t);
                }

                //then
                assertThat(readLines).isEqualTo(readAllLines(Paths.get(resource.toURI())));
            }
        }

        private void testFastScanner(Function<InputStreamReader, SocketScanner> fastScannerProvider, NewlineDelimiter newLineDelimiter) throws Exception {
            //given
            byte[] bytes = buildTestData(newLineDelimiter);
            InputStreamReader inputStreamReader = getInputStreamReader(bytes);

            //when
            try (SocketScanner fastScanner = fastScannerProvider.apply(inputStreamReader)) {
                int numberOfProcessedLines = 0;
                while (fastScanner.next() != null) {
                    numberOfProcessedLines++;
                }

                //then
                assertThat(numberOfProcessedLines).isEqualTo(NUMBER_OF_TOTAL_LINES);
            }
        }

        private InputStreamReader getInputStreamReader(byte[] bytes) {
            return new InputStreamReader(new ByteArrayInputStream(bytes));
        }

        private byte[] buildTestData(NewlineDelimiter newLineDelimiter) {
            StringBuilder builder = new StringBuilder(NUMBER_OF_TOTAL_LINES);
            int counter = 0;
            while (counter < NUMBER_OF_TOTAL_LINES) {
                counter++;
                builder
                    .append("Hello hello: one line with text! ")
                    .append(counter)
                    .append(newLineDelimiter.getPattern());
            }
            return builder.toString().getBytes();
        }
    }

    @Nested
    @Disabled("This test should be done as some benchmarking tool")
    class Speed {
        @Test
        void shouldTestLegacyScannerSpeed() {
            //given
            final byte[] bytes = generateTestData(10_000_000);

            List<Long> totalTimes = new ArrayList<>();

            //when
            for (int i = 10; i-- > 0; ) {
                InputStream inputStream = new ByteArrayInputStream(bytes);
                InputStreamReader reader = new InputStreamReader(inputStream);

                long totalTime;
                long start = System.currentTimeMillis();
                try (Scanner scanner = new Scanner(reader)) {
                    scanner.useDelimiter(Pattern.compile("\r\n"));
                    while (scanner.next() != null) {
                        // Ignore, we only need to consume lines, and calculate time.
                    }
                } catch (NoSuchElementException ignore) {
                } finally {
                    totalTime = System.currentTimeMillis() - start;
                    totalTimes.add(totalTime);
                }
            }

            //then
            double avgTotalTimes = totalTimes.stream()
                .mapToDouble(Long::doubleValue)
                .average()
                .orElseThrow(() -> new ArithmeticException("Cannot calculate avg od total times"));
            assertThat(avgTotalTimes).isBetween(60.0, 80.0);
        }

        @Test
        void shouldTestCrLfFastScanner() throws Exception {
            double avgTotalTimes = fastScannerSpeedTest(SocketScanner::crLfSocketScanner);

            //then
            assertThat(avgTotalTimes).isBetween(10.0, 30.0);
        }

        @Test
        void shouldTestLfFastScanner() throws Exception {
            // The random test data generates quite a lot of LF chars
            // which means we will get a LOT more lines when checking for LF.
            double avgTotalTimes = fastScannerSpeedTest(SocketScanner::lfSocketScanner);

            //then
            assertThat(avgTotalTimes).isBetween(10.0, 30.0);
        }

        private double fastScannerSpeedTest(Function<InputStreamReader, SocketScanner> fastScannerProvider) throws Exception {
            //given
            final byte[] bytes = generateTestData(10_000_000);

            List<Long> totalTimes = new ArrayList<>();

            //when
            for (int i = 10; i-- > 0; ) {
                InputStream inputStream = new ByteArrayInputStream(bytes);
                InputStreamReader reader = new InputStreamReader(inputStream);

                long totalTime;
                long start = System.currentTimeMillis();
                try (SocketScanner scanner = fastScannerProvider.apply(reader);) {
                    while (scanner.next() != null) {
                        // Ignore, we only need to consume lines, and calculate time.
                    }
                } catch (NoSuchElementException ignore) {
                } finally {
                    totalTime = System.currentTimeMillis() - start;
                    totalTimes.add(totalTime);
                }
            }

            return totalTimes.stream()
                .mapToDouble(Long::doubleValue)
                .average()
                .orElseThrow(() -> new ArithmeticException("Cannot calculate avg od total times"));
        }
    }

    @Nested
    class Comparing {
        @Test
        void lfAndLegacyScannersShouldReturnTheSameData() {
            setupTest(SocketScanner::lfSocketScanner, LF);
        }

        @Test
        void crLfAndLegacyScannersShouldReturnTheSameData() {
            setupTest(SocketScanner::crLfSocketScanner, CRLF);
        }

        private void setupTest(Function<InputStreamReader, SocketScanner> fastScannerProvider, NewlineDelimiter newLineDelimiter) {
            for (int i = 0; i < 50; i++) {
                try {
                    assertResults(fastScannerProvider, newLineDelimiter);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }
        }

        private void assertResults(Function<InputStreamReader, SocketScanner> fastScannerProvider, NewlineDelimiter newLineDelimiter) throws IOException {
            byte[] testData = generateTestData(1_000_000);

            InputStream inputStream1 = new ByteArrayInputStream(testData);
            InputStreamReader reader1 = new InputStreamReader(inputStream1);

            InputStream inputStream2 = new ByteArrayInputStream(testData);
            InputStreamReader reader2 = new InputStreamReader(inputStream2);

            Scanner scanner = new Scanner(reader1);
            scanner.useDelimiter(newLineDelimiter.getPattern());

            SocketScanner fastScanner = fastScannerProvider.apply(reader2);

            try {
                String scannerResult;
                String fastScannerResult;
                while ((scannerResult = scanner.next()) != null) {
                    fastScannerResult = fastScanner.next();

                    assertThat(scannerResult).isEqualTo(fastScannerResult);
                }
            } catch (NoSuchElementException e) {
                // Scanner normally throws this exception at the end of the stream.
                assertThat(fastScanner.next()).isNull();
            } finally {
                scanner.close();
                fastScanner.close();
            }
        }
    }

    private static byte[] generateTestData(int byteSize) {
        Random rand = new Random();
        String bodyData = "abcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*(),.<>[] {};':\"";
        bodyData += bodyData.toUpperCase();

        String[] terminators = new String[]{"\n", "\r", "\r\n", "\n\r"};

        StringBuilder builder = new StringBuilder(byteSize * 2);
        for (int i = 0; i < byteSize; i++) {
            if (Math.random() * 100 > 98) {
                // 2 % chance of a line ending
                builder.append(terminators[rand.nextInt(terminators.length)]);
            } else {
                int position = rand.nextInt(bodyData.length());
                builder.append(bodyData.charAt(position));
            }
        }

        return builder.toString().getBytes();
    }
}
