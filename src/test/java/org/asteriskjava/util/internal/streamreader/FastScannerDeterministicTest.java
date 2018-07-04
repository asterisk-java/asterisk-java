package org.asteriskjava.util.internal.streamreader;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.asteriskjava.util.internal.SocketConnectionFacadeImpl;
import org.junit.Test;

public class FastScannerDeterministicTest
{

    @Test
    public void testCrNlScanner() throws Exception
    {
        testScanner(10000, SocketConnectionFacadeImpl.NL_PATTERN);
    }

    @Test
    public void testNlScanner() throws Exception
    {
        testScanner(10000, SocketConnectionFacadeImpl.CRNL_PATTERN);
    }

    private void testScanner(int testLines, Pattern pattern) throws Exception
    {

        final byte[] bytes = buildTestData(testLines, pattern).getBytes();

        InputStream inputStream = new ByteArrayInputStream(bytes);

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        // factory will return the correct reader for the pattern
        try (FastScanner scanner = FastScannerFactory.getReader(reader, pattern))
        {
            System.out.println("\nTesting scanner class: " + scanner.getClass());
            int ctr = 0;
            @SuppressWarnings("unused")
            String t;
            while ((t = scanner.next()) != null)
            {
                // System.out.println("L: " + t + " " + t.length());
                ctr++;
            }
            assertTrue("Counter expected : " + (testLines) + " got " + ctr, (testLines) == ctr);

        }
        catch (NoSuchElementException e)
        {
        }

        System.out.println("Done\n");
    }

    @Test
    public void testBR2Accuraccy3() throws Exception
    {

        final byte[] bytes = Files.readAllBytes(new File("NlStreamReaderFast273316816601633219txt").toPath());

        InputStream inputStream = new ByteArrayInputStream(bytes);

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (FastScannerNl scanner = new FastScannerNl(reader);)
        {
            String t;
            while ((t = scanner.next()) != null)
            {
                System.out.println("L: " + t + " " + t.length());
            }

        }
        catch (NoSuchElementException e)
        {
        }
    }

    String buildTestData(int lines, Pattern terminator)
    {
        StringBuilder builder = new StringBuilder(lines * 30);
        int ctr = 0;
        while (ctr < lines)
        {
            ctr++;
            builder.append("Hallo hello: one line with text! " + ctr + terminator.pattern());
        }
        return builder.toString();
    }
}
