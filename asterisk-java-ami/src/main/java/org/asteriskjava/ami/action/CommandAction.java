/*
 * Copyright 2004-2022 Asterisk Java contributors
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
package org.asteriskjava.ami.action;

import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.response.CommandResponse;

/**
 * The {@link CommandAction} sends a command line interface (CLI) command to the Asterisk server.<p>
 * For a list of supported commands type {@code help} on Asterisk's command line.<p>
 * In response to a {@link CommandAction} you will receive a {@link CommandResponse} that contains the CLI output.<p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>16 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+16+ManagerAction_Command">Command</a></li>
 *     <li>18 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+ManagerAction_Command">Command</a></li>
 *     <li>20 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+20+ManagerAction_Command">Command</a></li>
 * </ul>
 * <p>
 * Example:
 * <pre>
 * CommandAction commandAction = new CommandAction("iax2 show peers");
 * CommandResponse response = (CommandResponse) c.sendAction(commandAction);
 * for (String line : response.getResult()) {
 *     System.out.println(line);
 * }
 * </pre>
 *
 * @author Stefan Reuter
 * @see CommandResponse
 */
@ExpectedResponse(CommandResponse.class)
public class CommandAction extends AbstractManagerAction {
    private static final long serialVersionUID = 4753117770471622025L;

    private String command;

    public CommandAction() {
    }

    /**
     * Creates a new {@link CommandAction} with the given command.
     *
     * @param command the CLI command to execute
     */
    public CommandAction(String command) {
        this.command = command;
    }

    @Override
    public String getAction() {
        return "Command";
    }

    /**
     * Asterisk argument: {@code Command}.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets Asterisk argument: {@code Command}.
     */
    public void setCommand(String command) {
        this.command = command;
    }
}
