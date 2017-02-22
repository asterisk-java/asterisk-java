/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.fastagi;

import java.io.IOException;
import java.net.InetAddress;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.RejectedExecutionException;

import org.asteriskjava.fastagi.internal.AgiConnectionHandler;
import org.asteriskjava.fastagi.internal.DefaultAgiChannelFactory;
import org.asteriskjava.fastagi.internal.FastAgiConnectionHandler;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.ReflectionUtil;
import org.asteriskjava.util.ServerSocketFacade;
import org.asteriskjava.util.SocketConnectionFacade;
import org.asteriskjava.util.internal.ServerSocketFacadeImpl;

/**
 * Default implementation of the {@link org.asteriskjava.fastagi.AgiServer}
 * interface for FastAGI.
 *
 * @author srt
 * @version $Id$
 */
public class DefaultAgiServer extends AbstractAgiServer implements AgiServer
{
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * The default name of the resource bundle that contains the config.
     */
    private static final String DEFAULT_CONFIG_RESOURCE_BUNDLE_NAME = "fastagi";

    /**
     * The default bind port.
     */
    private static final int DEFAULT_BIND_PORT = 4573;

    /** Default 50? Windows server max 200? */
    private static final int BACKLOG = 200;

    private ServerSocketFacade serverSocket;

    private String configResourceBundleName = DEFAULT_CONFIG_RESOURCE_BUNDLE_NAME;
    private int port = DEFAULT_BIND_PORT;
    private InetAddress address = null;

    /**
     * Creates a new DefaultAgiServer.
     */
    public DefaultAgiServer()
    {
        this(null, null);

    }

    /**
     * Creates a new DefaultAgiServer and set a custom factory for creating
     * AgiChannels
     *
     * @param agiChannelFactory The factory to use for creating new AgiChannel
     *            instances.
     */
    public DefaultAgiServer(AgiChannelFactory agiChannelFactory)
    {
        this(null, null, agiChannelFactory);
    }

    /**
     * Creates a new DefaultAgiServer and loads its configuration from an
     * alternative resource bundle.
     *
     * @param configResourceBundleName the name of the conifiguration resource
     *            bundle (default is "fastagi").
     */
    public DefaultAgiServer(String configResourceBundleName)
    {
        this(configResourceBundleName, null);
    }

    /**
     * Creates a new DefaultAgiServer that uses the given
     * {@link MappingStrategy}.
     *
     * @param mappingStrategy the MappingStrategy to use to determine the
     *            AgiScript to run.
     * @since 1.0.0
     */
    public DefaultAgiServer(MappingStrategy mappingStrategy)
    {
        this(null, mappingStrategy);
    }

    /**
     * Creates a new DefaultAgiServer that runs the given {@link AgiScript} for
     * all requests.
     *
     * @param agiScript the AgiScript to run.
     * @since 1.0.0
     */
    public DefaultAgiServer(AgiScript agiScript)
    {
        this(null, new StaticMappingStrategy(agiScript));
    }

    /**
     * Creates a new DefaultAgiServer and loads its configuration from an
     * alternative resource bundle and uses the given {@link MappingStrategy}.
     *
     * @param configResourceBundleName the name of the conifiguration resource
     *            bundle (default is "fastagi").
     * @param mappingStrategy the MappingStrategy to use to determine the
     *            AgiScript to run.
     * @since 1.0.0
     */
    public DefaultAgiServer(String configResourceBundleName, MappingStrategy mappingStrategy)
    {
        this(configResourceBundleName, mappingStrategy, new DefaultAgiChannelFactory());
    }

    /**
     * Creates a new DefaultAgiServer and loads its configuration from an
     * alternative resource bundle and uses the given {@link MappingStrategy}.
     *
     * @param configResourceBundleName the name of the conifiguration resource
     *            bundle (default is "fastagi").
     * @param mappingStrategy the MappingStrategy to use to determine the
     *            AgiScript to run.
     * @param agiChannelFactory The factory to use for creating new AgiChannel
     *            instances.
     * @since 1.0.0
     */
    public DefaultAgiServer(String configResourceBundleName, MappingStrategy mappingStrategy,
            AgiChannelFactory agiChannelFactory)
    {
        super(agiChannelFactory);

        if (mappingStrategy == null)
        {
            final CompositeMappingStrategy compositeMappingStrategy = new CompositeMappingStrategy();

            compositeMappingStrategy.addStrategy(new ResourceBundleMappingStrategy());
            compositeMappingStrategy.addStrategy(new ClassNameMappingStrategy());
            if (ReflectionUtil.isClassAvailable("javax.script.ScriptEngineManager"))
            {
                MappingStrategy scriptEngineMappingStrategy = (MappingStrategy) ReflectionUtil
                        .newInstance("org.asteriskjava.fastagi.ScriptEngineMappingStrategy");
                if (scriptEngineMappingStrategy != null)
                {
                    compositeMappingStrategy.addStrategy(scriptEngineMappingStrategy);
                }
            }
            else
            {
                logger.warn("ScriptEngine support disabled: It is only availble when running at least Java 6");
            }

            setMappingStrategy(compositeMappingStrategy);
        }
        else
        {
            setMappingStrategy(mappingStrategy);
        }

        if (configResourceBundleName != null)
        {
            this.configResourceBundleName = configResourceBundleName;
        }

        loadConfig();
    }

    /**
     * Sets the TCP port to listen on for new connections. <br>
     * The default port is 4573.
     *
     * @param bindPort the port to bind to.
     * @deprecated use {@link #setPort(int)} instead
     */
    @Deprecated
    public void setBindPort(int bindPort)
    {
        this.port = bindPort;
    }

    /**
     * Sets the TCP port to listen on for new connections. <br>
     * The default port is 4573.
     *
     * @param port the port to bind to.
     * @since 0.2
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Returns the TCP port this server is configured to bind to.
     *
     * @return the TCP port this server is configured to bind to.
     * @since 1.0.0
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Returns the address this server is configured to bind to.
     * 
     * @return the address this server is configured to bind to.
     */
    public InetAddress getAddress()
    {
        return address;
    }

    /**
     * Sets the address to bind server.
     * 
     * @param address the address to bind to.
     */
    public void setAddress(InetAddress address)
    {
        this.address = address;
    }

    private void loadConfig()
    {
        final ResourceBundle resourceBundle;

        try
        {
            resourceBundle = ResourceBundle.getBundle(configResourceBundleName);
        }
        catch (MissingResourceException e)
        {
            return;
        }

        try
        {
            String portString;

            try
            {
                portString = resourceBundle.getString("port");
            }
            catch (MissingResourceException e)
            {
                // for backward compatibility only
                portString = resourceBundle.getString("bindPort");
            }
            port = Integer.parseInt(portString);
        }
        catch (Exception e) // NOPMD
        {
            // swallow
        }

        try
        {
            setPoolSize(Integer.parseInt(resourceBundle.getString("poolSize")));
        }
        catch (Exception e) // NOPMD
        {
            // swallow
        }

        try
        {
            setMaximumPoolSize(Integer.parseInt(resourceBundle.getString("maximumPoolSize")));
        }
        catch (Exception e) // NOPMD
        {
            // swallow
        }
    }

    protected ServerSocketFacade createServerSocket() throws IOException
    {
        return new ServerSocketFacadeImpl(port, BACKLOG, address);
    }

    public void startup() throws IOException, IllegalStateException
    {
        try
        {
            serverSocket = createServerSocket();
        }
        catch (IOException e)
        {
            logger.error("Unable start AgiServer: cannot to bind to *:" + port + ".", e);
            throw e;
        }

        logger.info("Listening on *:" + port + ".");

        // loop will be terminated by accept() throwing an IOException when the
        // ServerSocket is closed.
        while (true)
        {
            final SocketConnectionFacade socket;

            // accept connection
            try
            {
                socket = serverSocket.accept();
            }
            catch (IOException e)
            {
                // swallow only if shutdown
                if (isDie())
                {
                    break;
                }
                // handle exception but continue to run
                handleException("IOException while waiting for connections.", e);
                continue;
            }

            logger.debug("Received connection from " + socket.getRemoteAddress());

            // execute connection handler
            final AgiConnectionHandler connectionHandler = new FastAgiConnectionHandler(getMappingStrategy(), socket,
                    this.getAgiChannelFactory());
            try
            {
                execute(connectionHandler);
            }
            catch (RejectedExecutionException e)
            {
                logger.warn("Execution was rejected by pool. Try to increase the pool size.");
                // release resources like closing the socket if execution was
                // rejected due to the pool size
                connectionHandler.release();
            }
        }
        logger.info("AgiServer shut down.");
    }

    /**
     * @deprecated use {@link #startup()} instead.
     */
    @Deprecated
    public void run()
    {
        try
        {
            startup();
        }
        catch (IOException e) // NOPMD
        {
            // nothing we can do about that and exceptions have already been
            // logged
            // by startup().
        }
    }

    @Override
    public synchronized void shutdown() throws IllegalStateException
    {
        // setting the death flag causes the accept() loop to exit when a
        // SocketException occurs.
        super.shutdown();

        if (serverSocket != null)
        {
            try
            {
                // closes the server socket and throws a SocketException on
                // Threads waiting in accept()
                serverSocket.close();
            }
            catch (IOException e)
            {
                logger.warn("IOException while closing server socket.", e);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable
    {

        if (serverSocket != null)
        {
            try
            {
                serverSocket.close();
            }
            catch (IOException e) // NOPMD
            {
                // swallow
            }
        }

        super.finalize();
    }

    /**
     * Creates a new DefaultAgiServer and starts it.
     *
     * @param args not used
     * @throws Exception if the server can't be started
     * @deprecated since 1.0.0 use {@link org.asteriskjava.Cli} instead.
     */
    @Deprecated
    public static void main(String[] args) throws Exception
    {
        final AgiServer server;

        server = new DefaultAgiServer();
        server.startup();
    }
}
