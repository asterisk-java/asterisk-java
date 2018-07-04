package org.asteriskjava.util.internal.streamreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.asteriskjava.util.internal.SocketConnectionFacadeImpl;
import org.junit.Test;

public class FastScannerSpeedTestOnSocket
{

    @Test
    public void testScannerSpeed() throws Exception
    {
        try
        {
            for (int i = 10; i-- > 0;)
            {

                InputStreamReader reader = getReader();
                System.out.print("Scanner " + i + ":\t");

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
        catch (Exception e)
        {
            System.out.println(
                    "If you want to run FastScannerSpeedTestOnSocket, you'll need to run FastScannerTestSocketSource first");
        }
    }

    private InputStreamReader getReader() throws UnknownHostException, IOException, InterruptedException
    {

        Socket echoSocket = new Socket("127.0.0.1", FastScannerTestSocketSource.portNumber);

        InputStream inputStream = echoSocket.getInputStream();

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return reader;
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
        try
        {
            for (int i = 10; i-- > 0;)
            {

                InputStreamReader reader = getReader();
                System.out.print("Fast " + i + ":\t");
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
        catch (Exception e)
        {
            System.out.println(
                    "If you want to run FastScannerSpeedTestOnSocket, you'll need to run FastScannerTestSocketSource first");
        }
    }

}
