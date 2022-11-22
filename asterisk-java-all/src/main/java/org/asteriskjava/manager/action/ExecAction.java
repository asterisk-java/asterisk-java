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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.ExpectedResponse;
import org.asteriskjava.manager.response.CommandResponse;

/**
 * The CommandAction sends a command line interface (CLI) command to the
 * asterisk server.
 * <p>
 * For a list of supported commands type <code>help</code> on Asterisk's command
 * line.
 * <p>
 * In response to a CommandAction you will receive a CommandResponse that
 * contains the CLI output.
 * <p>
 * Example:
 *
 * <pre>
 * CommandAction commandAction = new CommandAction("iax2 show peers");
 * CommandResponse response = (CommandResponse) c.sendAction(commandAction);
 * for (String line : response.getResult())
 * {
 *     System.out.println(line);
 * }
 * </pre>
 * <p>
 * Where <code>c</code> is an instance of
 * {@link org.asteriskjava.manager.ManagerConnection}.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.response.CommandResponse
 */
@ExpectedResponse(CommandResponse.class)
public class ExecAction extends AbstractManagerAction {
    static final long serialVersionUID = 4753117770471622025L;
    protected String command;

    /**
     * Creates a new CommandAction.
     */
    public ExecAction() {

    }

    /**
     * Creates a new CommandAction with the given command.
     *
     * @param command the CLI command to execute.
     * @since 0.2
     */
    public ExecAction(String command) {
        this.command = command;
    }

    /**
     * Returns the name of this action, i.e. "Command".
     */
    @Override
    public String getAction() {
        return "exec";
    }

    /**
     * Returns the command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the CLI command to send to the Asterisk server.
     */
    public void setCommand(String command) {
        this.command = command;
    }
}
