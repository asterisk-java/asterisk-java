/*
 * Copyright 2004-2023 Asterisk Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.ami.action.api.response.event;

import org.asteriskjava.ami.action.api.ManagerAction;
import org.asteriskjava.ami.event.api.ManagerEvent;

/**
 * Abstract base class for events triggered in response to a ManagerAction.
 * <p>
 * All ResponseEvents contain an additional action id property that links the
 * event to the action that caused it.
 *
 * @author srt
 * @version $Id$
 * @see ManagerAction
 */
public abstract class ResponseEvent extends ManagerEvent {
    private String actionId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }


    /*----------LEGACY BELOW-----*/

    private static final long serialVersionUID = 1L;
    private String internalActionId;

    public ResponseEvent(Object source) {
        super(source);
    }

    /**
     * Returns the internal action id of the ManagerAction that caused this
     * event.
     * <p>
     * Warning: This method is internal to Asterisk-Java and should never be
     * used in application code.
     *
     * @return the internal action id of the ManagerAction that caused this
     * event.
     * @since 0.2
     */
    public final String getInternalActionId() {
        return internalActionId;
    }

    /**
     * Sets the internal action id of the ManagerAction that caused this event.
     *
     * @param internalActionId the internal action id of the ManagerAction that
     *                         caused this event.
     * @since 0.2
     */
    public final void setInternalActionId(String internalActionId) {
        this.internalActionId = internalActionId;
    }
}
