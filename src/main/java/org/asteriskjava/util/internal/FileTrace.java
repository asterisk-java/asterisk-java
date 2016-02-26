package org.asteriskjava.util.internal;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Writes a trace file to the file system.
 */
public class FileTrace implements Trace
{
    public static final String TRACE_DIRECTORY_PROPERTY = "org.asteriskjava.trace.directory";
    protected static final String FILE_PREFIX = "aj-trace";
    protected static final String FILE_SUFFIX = ".txt";

    private final Log logger = LogFactory.getLog(FileTrace.class);

    // ok to share instance as access to this object is synchronized anyway
    private final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmsszzz");
    private Charset charset = Charset.forName("UTF-8");
    private FileChannel channel;
    private boolean exceptionLogged = false;
    private RandomAccessFile randomAccessFile;

    public FileTrace(Socket socket) throws IOException
    {
        randomAccessFile = new RandomAccessFile(getFile(socket), "rw");
        channel = randomAccessFile.getChannel();
        print(getHeader(socket));
    }

    private String getHeader(Socket socket)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("Local:  ");
        sb.append(socket.getLocalAddress());
        sb.append(":");
        sb.append(socket.getLocalPort());
        sb.append("\n");
        sb.append("Remote: ");
        sb.append(socket.getInetAddress());
        sb.append(":");
        sb.append(socket.getPort());
        sb.append("\n");
        sb.append("\n");

        return sb.toString();
    }

    private File getFile(Socket socket)
    {
        final String directory = System.getProperty(TRACE_DIRECTORY_PROPERTY, System.getProperty("java.io.tmpdir"));
        final String fileName = getFileName(socket);

        logger.info("Writing trace to " + directory + File.separator + fileName);
        return new File(directory, fileName);
    }

    private String getFileName(Socket socket)
    {
        final StringBuilder sb = new StringBuilder(FILE_PREFIX);
        sb.append("_");
        sb.append(df.format(new Date()));
        sb.append("_");
        sb.append(socket.getLocalAddress().getHostAddress());
        sb.append("_");
        sb.append(socket.getLocalPort());
        sb.append("_");
        sb.append(socket.getInetAddress().getHostAddress());
        sb.append("_");
        sb.append(socket.getPort());
        sb.append(FILE_SUFFIX);
        return sb.toString();
    }

    public synchronized void received(String s)
    {
        try
        {
            print(format("<<< ", s));
        }
        catch (IOException e)
        {
            logException(e);
        }
    }

    public synchronized void sent(String s)
    {
        try
        {
            print(format(">>> ", s));
        }
        catch (IOException e)
        {
            logException(e);
        }
    }

    private void logException(IOException e)
    {
        // avoid excessive failure logging
        if (exceptionLogged)
        {
            return;
        }
        logger.warn("Unable to write trace to disk", e);
        exceptionLogged = true;
    }

    protected String format(String prefix, String s)
    {
        final StringBuilder sb = new StringBuilder(df.format(new Date()));
        final String filler = String.format("%" + sb.length() + "s", "");
        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; i++)
        {
            if (i != 0)
            {
                sb.append(filler);
            }
            sb.append(" ");
            sb.append(prefix);
            sb.append(lines[i]);
            sb.append("\n");
        }
        return sb.toString();
    }

    protected void print(String s) throws IOException
    {
        final CharBuffer charBuffer = CharBuffer.allocate(s.length());
        charBuffer.put(s);
        charBuffer.flip();
        print(charset.encode(charBuffer));
    }

    private void print(ByteBuffer byteBuffer) throws IOException
    {
        int bytesWritten = 0;
        while (bytesWritten < byteBuffer.remaining())
        {
            // Loop if only part of the buffer contents get written.
            bytesWritten = channel.write(byteBuffer);
            if (bytesWritten == 0)
            {
                throw new IOException("Unable to write trace to channel. Media may be full.");
            }
        }
    }

    public void close()
    {
        try
        {
            randomAccessFile.close();
        }
        catch (IOException e)
        {
            logException(e);
        }
    }
}
