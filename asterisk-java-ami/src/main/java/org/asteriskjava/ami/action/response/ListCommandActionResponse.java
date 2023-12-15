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
package org.asteriskjava.ami.action.response;

import org.asteriskjava.ami.action.ListCommandsAction;
import org.asteriskjava.core.databind.annotation.AsteriskAttributesBucket;

import java.util.Map;

/**
 * Corresponds to a {@link ListCommandsAction} and contains all available commands.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class ListCommandActionResponse extends ManagerActionResponse {
    private Map<String, String> commands;

    public Map<String, String> getCommands() {
        return commands;
    }

    @AsteriskAttributesBucket
    public void setCommands(Map<String, String> commands) {
        this.commands = commands;
    }
}
