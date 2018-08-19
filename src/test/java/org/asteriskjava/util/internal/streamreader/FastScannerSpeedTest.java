package org.asteriskjava.util.internal.streamreader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.asteriskjava.util.internal.SocketConnectionFacadeImpl;
import org.junit.Test;

public class FastScannerSpeedTest
{
    @Test
    public void testScannerSpeed() throws Exception
    {
        final byte[] bytes = FastScannerRandomTest.generateTestData(10_000_000).getBytes();

        for (int i = 10; i-- > 0;)
        {
            System.out.print("Scanner " + i + ":\t");

            InputStream inputStream = new ByteArrayInputStream(bytes);

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            long start = System.currentTimeMillis();
            int ctr = 0;
            try (Scanner scanner = new Scanner(reader);)
            {

                scanner.useDelimiter(Pattern.compile("\r\n"));

                @SuppressWarnings("unused")
                String t;
                while ((t = scanner.next()) != null)
                {
                    // System.out.println(t);
                    ctr++;
                }
                System.out.print(ctr + "\t");
            }
            catch (NoSuchElementException e)
            {
                System.out.print(ctr + "\t");
            }
            System.out.println((System.currentTimeMillis() - start) + " ms");
        }
    }

    @Test
    public void test1() throws Exception
    {
        System.out.println("\nTesting fast CrNlStream");
        fastScannerSpeedTest(SocketConnectionFacadeImpl.CRNL_PATTERN);
    }

    @Test
    public void test2() throws Exception
    {
        /*
         * the random test data generates quite a lot of /n which means we will
         * get a LOT more lines when checking for /n
         */
        System.out.println("\nTesting fast NlStream");
        fastScannerSpeedTest(SocketConnectionFacadeImpl.NL_PATTERN);
    }

    private void fastScannerSpeedTest(Pattern pattern) throws Exception
    {
        final byte[] bytes = FastScannerRandomTest.generateTestData(10_000_000).getBytes();

        for (int i = 10; i-- > 0;)
        {
            System.out.print("Fast " + i + ":\t");

            InputStream inputStream = new ByteArrayInputStream(bytes);

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            FastScanner scanner = FastScannerFactory.getReader(reader, pattern);

            long start = System.currentTimeMillis();
            try
            {
                int ctr = 0;
                @SuppressWarnings("unused")
                String t;
                while ((t = scanner.next()) != null)
                {
                    // System.out.println(t);
                    ctr++;
                }
                System.out.print(ctr + "\t");

            }
            catch (NoSuchElementException e)
            {
            }
            System.out.println((System.currentTimeMillis() - start) + " ms");
        }
    }

}
