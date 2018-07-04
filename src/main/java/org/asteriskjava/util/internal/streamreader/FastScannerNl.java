
package org.asteriskjava.util.internal.streamreader;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicReference;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class FastScannerNl implements FastScanner
{
    private static final Log logger = LogFactory.getLog(FastScannerNl.class);

    private static final int BUFFER_SIZE = 8192;

    private AtomicReference<Readable> readableReference = new AtomicReference<>();
    protected char nlChar = '\n';
    protected StringBuilder result = new StringBuilder(80);
    protected CharBuffer cbuf = CharBuffer.allocate(BUFFER_SIZE);
    protected int end = 0;
    protected int start = 0;
    private boolean closed = false;
    protected final Object sync = new Object();

    private boolean isFirst = true;

    File logfile;

    private BufferedWriter writer;

    public FastScannerNl(Readable reader)
    {
        this.readableReference.set(reader);

        // createFileWriter();

    }

    public String next() throws IOException
    {
        int bytes = 0;

        // check for a line in the buffer
        String line = getLine(false);
        if (line == null)
        {
            // doing this instead of a synchronized block to avoid a possible
            // deadlock between calling readable.read() and a call to close() on
            // another thread, which it appears that Asterisk-Java permits
            // and/or does
            Readable readable = readableReference.get();

            // read from the stream to the buffer
            while (readable != null && (bytes = readable.read(cbuf)) > -1)
            {
                // writeToFile(bytes);

                // set buffer position to 0
                cbuf.position(0);

                end = bytes;

                // try to get a line from the buffer
                line = getLine(bytes >= 0);

                if (line != null)
                {
                    if (isFirst && line.length() == 0)
                    {
                        // if the first character of the stream is the line
                        // terminator, try again
                        line = getLine(bytes >= 0);
                    }
                    break;
                }
            }

        }
        // clear the first line flag
        isFirst = false;
        if (line == null)

        {
            // the line is empty, get the contents from the StringBuilder if any
            String tmp = result.toString();
            result.setLength(0);

            // if the reader is closed and there is no output
            if (readableReference.get() == null && tmp.length() == 0)
            {
                return null;
            }

            // if at the end of the inputStream and there is no output
            if (bytes == -1 && tmp.length() == 0)
            {
                return null;
            }
            return tmp;
        }
        return line;

    }

    protected String getLine(boolean endOfLine)
    {
        // iterate the buffer, looking for the end character
        for (int i = start; i < end; i++)
        {
            if (cbuf.get(i) == nlChar)
            {
                if (i > start)
                {
                    // add the buffer contents up to i to the output buffer
                    result.append(cbuf.subSequence(start, start + (i - start)));
                }
                // skip the delimiter
                start = i + 1;

                // get the output
                String tmp = result.toString();

                // reset the StringBuilder to 0 to avoid re-allocating memory
                result.setLength(0);
                return tmp;

            }
        }
        if (end >= start)
        {
            // we've hit the end of the buffer, copy the contents to the
            // StringBuilder, then return null so we'll loop again and get the
            // next part
            result.append(cbuf.subSequence(start, start + (end - start))).toString();
            start = 0;
            end = 0;
        }
        return null;
    }

    public void close()
    {
        synchronized (sync)
        {
            if (closed)
                return;
            if (readableReference.get() instanceof Closeable)
            {
                try
                {
                    ((Closeable) readableReference.get()).close();
                    // closeFileWriter();
                }
                catch (IOException ioe)
                {
                    logger.error(ioe, ioe);
                }
            }
            readableReference.set(null);
            closed = true;
        }
    }

    @SuppressWarnings("unused")
    private void createFileWriter()
    {
        try
        {
            logfile = File.createTempFile(this.getClass().getSimpleName(), "txt");
            writer = Files.newBufferedWriter(logfile.toPath(), Charset.defaultCharset(), StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void writeToFile(int bytes) throws IOException
    {
        String lines = new StringBuffer().append(cbuf, 0, bytes).toString();
        writer.append(lines, 0, bytes);
    }

    @SuppressWarnings("unused")
    private void closeFileWriter() throws IOException
    {
        writer.flush();
        writer.close();
    }

}
