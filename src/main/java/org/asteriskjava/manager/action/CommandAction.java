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

/**
 * The CommandAction sends a command line interface (CLI) command to the
 * asterisk server.<br>
 * For a list of supported commands type <code>help</code> on asterisk's
 * command line.
 * 
 * @author srt
 * @version $Id: CommandAction.java,v 1.5 2005/08/07 00:09:42 srt Exp $
 */
public class CommandAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 4753117770471622025L;

    protected String command;

    /**
     * Creates a new CommandAction.
     */
    public CommandAction()
    {

    }

    /**
     * Creates a new CommandAction with the given command.
     * 
     * @param command the CLI command to execute.
     * @since 0.2
     */
    public CommandAction(String command)
    {
        this.command = command;
    }

    /**
     * Returns the name of this action, i.e. "Command".
     */
    public String getAction()
    {
        return "Command";
    }

    /**
     * Returns the command.
     */
    public String getCommand()
    {
        return command;
    }

    /**
     * Sets the CLI command to send to the asterisk server.
     */
    public void setCommand(String command)
    {
        this.command = command;
    }
}
