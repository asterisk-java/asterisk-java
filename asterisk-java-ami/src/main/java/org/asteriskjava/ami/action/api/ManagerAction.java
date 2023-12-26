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
package org.asteriskjava.ami.action.api;

import org.asteriskjava.core.databind.annotation.AsteriskFieldOrder;

import java.io.Serializable;

/**
 * Interface that all actions that can be sent to the Asterisk server must implement.
 * <p>
 * Instances of this class represent a command sent to Asterisk via AMI, requesting a particular action be performed.
 * The number of actions available to the client are determined by the modules presently loaded in the Asterisk engine.
 * <p>
 * There is one concrete subclass of ManagerAction per each supported Asterisk action.
 *
 * @author Stefan Reuter
 * @since 1.0.0
 */
@AsteriskFieldOrder({"Action", "ActionID"})
public interface ManagerAction extends Serializable {
    String getAction();

    String getActionId();

    /**
     * Sets the Asterisk argument: {@code ActionID}.
     * <p>
     * If the {@code ActionID} is set and sent to Asterisk, any response returned by Asterisk will include the same ID.
     * This way, the {@code ActionID} can be used to track actions and their corresponding responses and response events.
     * <p>
     * Note that Asterisk Java uses its own internal {@code ActionID} to match actions with the corresponding responses
     * and events. Although the internal action is never exposed to the application code, if you want to handle responses
     * or response events on your own, your application must set a unique {@code ActionID} using this method.
     * Otherwise, the {@code ActionID} of the responses and response event objects passed to your application will be null.
     */
    void setActionId(String actionId);
}
