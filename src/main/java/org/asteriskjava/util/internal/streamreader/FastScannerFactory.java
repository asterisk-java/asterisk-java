package org.asteriskjava.util.internal.streamreader;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class FastScannerFactory
{
    private static final Log logger = LogFactory.getLog(FastScannerFactory.class);

    private static volatile boolean useLegacyScanner = false;

    public static FastScanner getReader(Readable reader, Pattern pattern)
    {
        if (!useLegacyScanner)
        {
            if (pattern.pattern().equals("\r\n"))
            {
                return new FastScannerCrNl(reader);
            }
            if (pattern.pattern().equals("\n"))
            {
                return new FastScannerNl(reader);
            }
        }

        // fall back to legacy Scanner

        logger.warn("Using legacy scanner");
        Scanner scanner = new Scanner(reader);
        scanner.useDelimiter(pattern);

        return getWrappedScanner(scanner);

    }

    public static void useLegacyScanner(boolean b)
    {
        useLegacyScanner = b;
    }

    private static FastScanner getWrappedScanner(final Scanner scanner)
    {
        return new FastScanner()
        {

            @Override
            public String next() throws IOException
            {
                try
                {
                    return scanner.next();
                }
                catch (NoSuchElementException e)
                {
                    return null;
                }
            }

            @Override
            public void close()
            {
                scanner.close();
            }

        };
    }
}
