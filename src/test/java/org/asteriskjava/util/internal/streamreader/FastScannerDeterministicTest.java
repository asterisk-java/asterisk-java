package org.asteriskjava.util.internal.streamreader;

import org.asteriskjava.util.internal.SocketConnectionFacadeImpl;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class FastScannerDeterministicTest {
    @Test
    public void testCrNlScanner() throws Exception {
        testScanner(10000, SocketConnectionFacadeImpl.NL_PATTERN);
    }

    @Test
    public void testNlScanner() throws Exception {
        testScanner(10000, SocketConnectionFacadeImpl.CRNL_PATTERN);
    }

    private void testScanner(int testLines, Pattern pattern) throws Exception {

        final byte[] bytes = buildTestData(testLines, pattern).getBytes();

        InputStream inputStream = new ByteArrayInputStream(bytes);

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        // factory will return the correct reader for the pattern
        try (FastScanner scanner = FastScannerFactory.getReader(reader, pattern)) {
            System.out.println("\nTesting scanner class: " + scanner.getClass());
            int ctr = 0;
            @SuppressWarnings("unused")
            String t;
            while ((t = scanner.next()) != null) {
                // System.out.println("L: " + t + " " + t.length());
                ctr++;
            }
            assertTrue("Counter expected : " + (testLines) + " got " + ctr, (testLines) == ctr);

        } catch (NoSuchElementException e) {
        }

        System.out.println("Done\n");
    }

    @Test
    public void testBR2Accuraccy3() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("NlStreamReaderFast273316816601633219.txt");
        final byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));

        InputStream inputStream = new ByteArrayInputStream(bytes);

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (FastScannerNl scanner = new FastScannerNl(reader);) {
            String t;
            while ((t = scanner.next()) != null) {
                System.out.println("L: " + t + " " + t.length());
            }

        } catch (NoSuchElementException e) {
        }
    }

    String buildTestData(int lines, Pattern terminator) {
        StringBuilder builder = new StringBuilder(lines * 30);
        int ctr = 0;
        while (ctr < lines) {
            ctr++;
            builder.append("Hallo hello: one line with text! " + ctr + terminator.pattern());
        }
        return builder.toString();
    }
}
