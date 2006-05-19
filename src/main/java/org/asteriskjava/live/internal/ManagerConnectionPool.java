/*
 *  Copyright 2005-2006 Stefan Reuter
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
package org.asteriskjava.live.internal;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class ManagerConnectionPool
{
    private BlockingQueue<ManagerConnection> connections;
    
    public ManagerConnectionPool(int size)
    {
        this.connections = new ArrayBlockingQueue<ManagerConnection>(size);
    }
    
    public void clear()
    {
        connections.clear();
    }
    
    public ManagerResponse sendAction(ManagerAction action) throws ManagerCommunicationException
    {
        ManagerConnection connection;
        ManagerResponse response;
        
        connection = get();
        try
        {
            response = connection.sendAction(action);
        }
        catch (IllegalStateException e)
        {
            throw new ManagerCommunicationException("Not connected to Asterisk Server", e);
        }
        catch (IOException e)
        {
            throw new ManagerCommunicationException("Unable to send " + action.getAction() + "Action", e);
        }
        catch (TimeoutException e)
        {
            throw new ManagerCommunicationException("Timeout while sending " + action.getAction() + "Action", e);
        }
        finally
        {
            put(connection);
        }
        
        return response;
    }

    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action) throws ManagerCommunicationException
    {
        return sendEventGeneratingAction(action, -1);
    }
    
    public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, long timeout) throws ManagerCommunicationException
    {
        ManagerConnection connection;
        ResponseEvents responseEvents;
        
        connection = get();
        try
        {
            if (timeout > 0)
            {
                responseEvents = connection.sendEventGeneratingAction(action, timeout);
            }
            else
            {
                responseEvents = connection.sendEventGeneratingAction(action);
            }
        }
        catch (IllegalStateException e)
        {
            throw new ManagerCommunicationException("Not connected to Asterisk Server", e);
        }
        catch (IOException e)
        {
            throw new ManagerCommunicationException("Unable to send " + action.getAction() + "Action", e);
        }
        catch (EventTimeoutException e)
        {
            throw new ManagerCommunicationException("Timeout waiting for events from " + action.getAction() + "Action", e);
        }
        finally
        {
            put(connection);
        }
        
        return responseEvents;
    }
    
    ManagerConnection get()
    {
        return connections.poll();
    }
    
    void put(ManagerConnection connection)
    {
        try
        {
            connections.put(connection);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted while trying to add connection to pool");
        }
    }
}
