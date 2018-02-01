package org.asteriskjava.pbx.internal.managerAPI;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/*
 * this class is a very thin wrapper around the manager connection class
 */
public class Connector
{
    private ManagerConnection managerConnection;
    private static final Log logger = LogFactory.getLog(Connector.class);

    /**
     * Establishes a Asterisk ManagerConnection as well as performing the
     * 'login' required by Asterisk.
     * 
     * @param asteriskSettings
     * @return
     * @throws IOException
     * @throws AuthenticationFailedException
     * @throws TimeoutException
     * @throws IllegalStateException
     */
    public ManagerConnection connect(final AsteriskSettings asteriskSettings)
            throws IOException, AuthenticationFailedException, TimeoutException, IllegalStateException
    {
        checkIfAsteriskRunning(asteriskSettings);
        this.makeConnection(asteriskSettings);
        return this.managerConnection;
    }

    /**
     * This method will try to make a simple tcp connection to the asterisk
     * manager to establish it is up. We do this as the default makeConnection
     * doesn't have a timeout and will sit trying to connect for a minute or so.
     * By using method when the user realises they have a problem on start up
     * they can go to the asterisk panel. Fix the problem and then we can retry
     * with the new connection settings within a couple of seconds rather than
     * waiting a minute for a timeout.
     * 
     * @param asteriskSettings
     * @throws UnknownHostException
     * @throws IOException
     */
    private void checkIfAsteriskRunning(AsteriskSettings asteriskSettings) throws UnknownHostException, IOException
    {
        try (Socket socket = new Socket())
        {
            socket.setSoTimeout(2000);

            InetSocketAddress asteriskHost = new InetSocketAddress(asteriskSettings.getAsteriskIP(),
                    asteriskSettings.getManagerPortNo());
            socket.connect(asteriskHost, 2000);
        }

    }

    /**
     * @param socketReadTimeout
     * @throws IOException
     * @throws AuthenticationFailedException
     * @throws TimeoutException
     * @throws InsufficientPermissionsException
     * @throws IllegalStateException
     */
    private void makeConnection(final AsteriskSettings asteriskSettings)
            throws IOException, AuthenticationFailedException, TimeoutException, IllegalStateException
    {
        ManagerConnectionFactory factory = null;
        if (asteriskSettings.getManagerPortNo() == -1)
        {
            Connector.logger.debug("Using default port 5038."); //$NON-NLS-1$
            factory = new ManagerConnectionFactory(asteriskSettings.getAsteriskIP(), asteriskSettings.getManagerUsername(),
                    asteriskSettings.getManagerPassword());
        }
        else
        {
            factory = new ManagerConnectionFactory(asteriskSettings.getAsteriskIP(), asteriskSettings.getManagerPortNo(),
                    asteriskSettings.getManagerUsername(), asteriskSettings.getManagerPassword());

        }

        this.managerConnection = factory.createManagerConnection();
        // this.managerConnection.setSocketReadTimeout(socketReadTimeout);
        // this.managerConnection.setSocketTimeout(socketReadTimeout);
        this.managerConnection.login();
    }
}
