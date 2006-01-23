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
package org.asteriskjava.manager.action;

import java.io.Serializable;

/**
 * Interface that all Actions that can be sent to the Asterisk server must
 * impement.<br>
 * Instances of this class represent a command sent to Asterisk via Manager API,
 * requesting a particular Action be performed. The number of actions available
 * to the client are determined by the modules presently loaded in the Asterisk
 * engine.<br>
 * There is one conrete subclass of ManagerAction per each supported Asterisk
 * Action.
 * 
 * @author srt
 * @version $Id: ManagerAction.java,v 1.4 2005/07/16 13:19:33 srt Exp $
 */
public interface ManagerAction extends Serializable
{
    /**
     * Returns the name of the action.
     */
    String getAction();

    /**
     * Returns the action id.
     */
    String getActionId();

    /**
     * Sets the action id.<br>
     * If the action id is set and sent to the asterisk server any response
     * returned by the asterisk server will include the same action id. This way
     * the action id can be used to track actions and their corresponding
     * responses.
     */
    void setActionId(String actionId);

}
