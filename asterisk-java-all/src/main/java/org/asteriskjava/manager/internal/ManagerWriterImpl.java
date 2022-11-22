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
package org.asteriskjava.manager.internal;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.lock.Lockable;
import org.asteriskjava.lock.Locker.LockCloser;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.util.SocketConnectionFacade;

import java.io.IOException;

/**
 * Default implementation of ManagerWriter interface.
 *
 * @author srt
 * @version $Id$
 */
public class ManagerWriterImpl extends Lockable implements ManagerWriter {
    /**
     * Instance logger.
     */
    // private final Log logger = LogFactory.getLog(getClass());

    /**
     * The action builder utility to convert ManagerAction to a String suitable
     * to be sent to the asterisk server.
     */
    private final ActionBuilder actionBuilder;

    private SocketConnectionFacade socket;

    /**
     * Creates a new ManagerWriter.
     */
    public ManagerWriterImpl() {
        this.actionBuilder = new ActionBuilderImpl();
    }

    public void setTargetVersion(AsteriskVersion version) {
        actionBuilder.setTargetVersion(version);
    }

    public void setSocket(final SocketConnectionFacade socket) {
        try (LockCloser closer = this.withLock()) {
            this.socket = socket;
        }
    }

    public void sendAction(final ManagerAction action, final String internalActionId) throws IOException {
        try (LockCloser closer = this.withLock()) {
            final String actionString;

            if (socket == null) {
                throw new IllegalStateException("Unable to send action: socket is null");
            }

            actionString = actionBuilder.buildAction(action, internalActionId);

            socket.write(actionString);
            socket.flush();

            // TODO tracing
            // logger.debug("Sent " + action.getAction() + " action with
            // actionId '"
            // + action.getActionId() + "':\n" + actionString);
        }
    }
}
