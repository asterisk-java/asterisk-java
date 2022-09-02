package org.asteriskjava.util.internal.streamreader;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import org.asteriskjava.util.internal.SocketConnectionFacadeImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

class FastScannerRandomTest {
    @Test
    void compareOutputOfNlFastScannerToScanner() throws Exception {
        setupTest(SocketConnectionFacadeImpl.NL_PATTERN, "NL");
    }

    @Test
    void compareOutputOfCrNlFastScannerToScanner() throws Exception {
        setupTest(SocketConnectionFacadeImpl.CRNL_PATTERN, "CR NL");
    }

    void setupTest(final Pattern pattern, String caption) {

        final AtomicInteger ctr = new AtomicInteger();

        int tests = 50;

        for (int i = 0; i < tests; i++) {

            try {
                String testData = generateTestData(1_000_000);
                compare(testData, pattern);
                System.out.println(caption + " Completed " + (ctr.incrementAndGet()) * 1 + "MB");
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    void compare(String testData, Pattern pattern) throws IOException {

        InputStream inputStream1 = new ByteArrayInputStream(testData.getBytes());
        InputStreamReader reader1 = new InputStreamReader(inputStream1, StandardCharsets.UTF_8);

        InputStream inputStream2 = new ByteArrayInputStream(testData.getBytes());
        InputStreamReader reader2 = new InputStreamReader(inputStream2, StandardCharsets.UTF_8);

        FastScanner fast = FastScannerFactory.getReader(reader2, pattern);

        String scannerResult = "";
        String fastResult = "";

        int ctr = 0;
        Scanner scanner = new Scanner(reader1);

        scanner.useDelimiter(pattern);

        try {
            while ((scannerResult = scanner.next()) != null) {
                ctr++;
                fastResult = fast.next();

                if (!fastResult.equals(scannerResult)) {

                    System.out.println("Expected " + ByteArrayUtil.toHexString(scannerResult.getBytes()));
                    System.out.println("Got      " + ByteArrayUtil.toHexString(fastResult.getBytes()));
                    System.out.println("Error " + ctr);
                    System.out.println("");

                    throw new RuntimeException("Mismatched output");
                }
            }
        } catch (NoSuchElementException e) {
            if (fast.next() != null) {
                throw new RuntimeException("Expected null, not something else");
            }
            // Scanner normally throws this exception at the end of the stream
        } finally {
            scanner.close();
            fast.close();
        }
    }

    public static String generateTestData(int byteSize) {
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
                builder.append(bodyData.substring(position, position + 1));
            }
        }

        return builder.toString();
    }
}
