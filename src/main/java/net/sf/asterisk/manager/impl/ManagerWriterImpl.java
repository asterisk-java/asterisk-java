/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.manager.impl;

import java.io.IOException;

import net.sf.asterisk.AsteriskVersion;
import net.sf.asterisk.io.SocketConnectionFacade;
import net.sf.asterisk.manager.ActionBuilder;
import net.sf.asterisk.manager.ManagerWriter;
import net.sf.asterisk.manager.action.ManagerAction;
import net.sf.asterisk.util.Log;
import net.sf.asterisk.util.LogFactory;

/**
 * Default implementation of ManagerWriter interface.
 * 
 * @author srt
 * @version $Id: ManagerWriterImpl.java,v 1.4 2005/11/08 15:25:18 srt Exp $
 */
public class ManagerWriterImpl implements ManagerWriter
{
    /**
     * Instance logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * The action builder utility to convert ManagerAction to a String suitable to be sent to the
     * asterisk server.
     */
    private final ActionBuilder actionBuilder;

    private SocketConnectionFacade socket;

    /**
     * Creates a new ManagerWriter.
     */
    public ManagerWriterImpl()
    {
        this.actionBuilder = new ActionBuilderImpl();
    }

    public void setTargetVersion(AsteriskVersion version)
    {
        actionBuilder.setTargetVersion(version);
    }

    public synchronized void setSocket(final SocketConnectionFacade socket)
    {
        this.socket = socket;
    }

    public synchronized void sendAction(final ManagerAction action, final String internalActionId) throws IOException
    {
        final String actionString;

        if (socket == null)
        {
            throw new IllegalStateException("Unable to send action: socket is null");
        }

        actionString = actionBuilder.buildAction(action, internalActionId);

        socket.write(actionString);
        socket.flush();

        logger.debug("Sent " + action.getAction() + " action with actionId '" + action.getActionId() + "':\n"
                + actionString);
    }
}
