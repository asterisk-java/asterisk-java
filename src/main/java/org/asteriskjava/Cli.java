package org.asteriskjava;

import org.asteriskjava.fastagi.DefaultAgiServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Simple command line interface for Asterisk-Java.
 */
public class Cli
{


    private void parseOptions(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            startAgiServer();
        }

        final String arg = args[0];
        if ("-h".equals(arg) || "-help".equals(arg))
        {
            showHelp();
        }
        if ("-v".equals(arg) || "-version".equals(arg))
        {
            showVersion();
        }
        if ("-a".equals(arg) || "-agi".equals(arg))
        {
            if (args.length >= 2)
            {
                Integer port = null;
                try
                {
                    port = new Integer(args[1]);
                }
                catch (NumberFormatException e)
                {
                    System.err.println("Invalid port '" + args[1] + "'.");
                    exit(1);
                }
                startAgiServer(port);
            }
        }
    }

    private void showHelp()
    {
        showVersion();
        System.err.println();
        System.err.println("-v, -version\n\tDisplays the version of Asterisk-Java\n");
        System.err.println("-a, -agi [port]\n\tStarts an AGI server");
    }

    private void showVersion()
    {
        System.out.println("Asterisk-Java " + getVersion());
    }

    private String getVersion()
    {
        String version = "unknown";
        final InputStream is;
        final Properties properties;

        is = getClass().getResourceAsStream("/META-INF/maven/org.asteriskjava/asterisk-java/pom.properties");
        if (is == null)
        {
            return version;
        }

        properties = new Properties();
        try
        {
            properties.load(is); // contains version, groupId and artifactId
        }
        catch (IOException e)
        {
            return version;
        }

        version = properties.getProperty("version", "unknown");
        return version;
    }

    private void startAgiServer() throws IOException
    {
        startAgiServer(null);
    }

    private void startAgiServer(Integer port) throws IOException
    {
        final DefaultAgiServer server;

        server = new DefaultAgiServer();
        if (port != null)
        {
            server.setPort(port);
        }
        server.startup();
    }

    private void exit(int code)
    {
        System.exit(code);
    }

    public static void main(String[] args) throws Exception
    {
        new Cli().parseOptions(args);
    }
}
