package org.asteriskjava.fastagi;

import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.AsyncAgiEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.fastagi.internal.AsyncAgiConnectionHandler;

import java.util.Map;
import java.util.HashMap;

/**
 * AGI server for AGI over the Manager API (AsyncAGI).<p>
 * AsyncAGI is available since Asterisk 1.6.
 *
 * @since 1.0.0
 */
public class AsyncAgiServer extends AbstractAgiServer implements ManagerEventListener
{
    private final Log logger = LogFactory.getLog(getClass());
    private final Map<Integer, AsyncAgiConnectionHandler> connectionHandlers;

    public AsyncAgiServer()
    {
        this.connectionHandlers = new HashMap<Integer, AsyncAgiConnectionHandler>();
    }

    public AsyncAgiServer(MappingStrategy mappingStrategy)
    {
        this();
        setMappingStrategy(mappingStrategy);
    }

    public AsyncAgiServer(AgiScript agiScript)
    {
        this();
        setMappingStrategy(new StaticMappingStrategy(agiScript));
    }

    public void shutdown()
    {
        super.shutdown();
    }

    public void onManagerEvent(ManagerEvent event)
    {
        if (event instanceof AsyncAgiEvent)
        {
            handleAsyncAgiEvent((AsyncAgiEvent) event);
        }
        else if (event instanceof RenameEvent)
        {
            handleRenameEvent((RenameEvent) event);
        }
    }

    private void handleAsyncAgiEvent(AsyncAgiEvent asyncAgiEvent)
    {
        final ManagerConnection connection;
        final String channelName;
        final AsyncAgiConnectionHandler connectionHandler;

        connection = (ManagerConnection) asyncAgiEvent.getSource();
        channelName = asyncAgiEvent.getChannel();

        if (asyncAgiEvent.isStart())
        {
            connectionHandler = new AsyncAgiConnectionHandler(getMappingStrategy(), asyncAgiEvent);
            setConnectionHandler(connection, channelName, connectionHandler);
            execute(connectionHandler);
        }
        else
        {
            connectionHandler = getConnectionHandler(connection, channelName);
            if (connectionHandler == null)
            {
                logger.info("No AsyncAgiConnectionHandler registered for channel " + channelName + ": Ignoring AsyncAgiEvent");
                return;
            }

            if (asyncAgiEvent.isExec())
            {
                connectionHandler.onAsyncAgiExecEvent(asyncAgiEvent);
            }
            else if (asyncAgiEvent.isEnd())
            {
                connectionHandler.onAsyncAgiEndEvent(asyncAgiEvent);
                removeConnectionHandler(connection, channelName);
            }
            else
            {
                logger.warn("Ignored unknown AsyncAgiEvent of sub type '" + asyncAgiEvent.getSubEvent() + "'");
            }
        }
    }

    private void handleRenameEvent(RenameEvent renameEvent)
    {
        final ManagerConnection connection = (ManagerConnection) renameEvent.getSource();
        final AsyncAgiConnectionHandler connectionHandler = getConnectionHandler(connection, renameEvent.getChannel());

        if (connectionHandler == null)
        {
            return;
        }

        removeConnectionHandler(connection, renameEvent.getChannel());
        setConnectionHandler(connection,  renameEvent.getNewname(), connectionHandler);

        connectionHandler.updateChannelName(renameEvent.getNewname());
    }

    private AsyncAgiConnectionHandler getConnectionHandler(ManagerConnection connection, String channelName)
    {
        synchronized (connectionHandlers)
        {
            return connectionHandlers.get(calculateHashKey(connection, channelName));
        }
    }

    private void setConnectionHandler(ManagerConnection connection, String channelName, AsyncAgiConnectionHandler connectionHandler)
    {
        synchronized (connectionHandlers)
        {
            connectionHandlers.put(calculateHashKey(connection, channelName), connectionHandler);
        }
    }

    private void removeConnectionHandler(ManagerConnection connection, String channelName)
    {
        synchronized (connectionHandlers)
        {
            connectionHandlers.remove(calculateHashKey(connection, channelName));
        }
    }

    private Integer calculateHashKey(ManagerConnection connection, String channelName)
    {
        return connection.hashCode() * 31 + channelName.hashCode();
    }
}
