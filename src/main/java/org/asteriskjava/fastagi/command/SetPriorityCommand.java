/*
 * Copyright 2004-2022 Asterisk-Java contributors
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
package org.asteriskjava.fastagi.command;

/**
 * AGI Command: <b>SET PRIORITY</b>
 * <p>
 * Set channel dialplan priority.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_set+priority">AGI Command SET PRIORITY (Asterisk 18)</a>
 *
 * @author srt
 */
public class SetPriorityCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The priority or label for continuation upon exiting the application.
     */
    private String priority;

    /**
     * Creates a new SetPriorityCommand.
     *
     * @param priority the priority or label for continuation upon exiting the application.
     */
    public SetPriorityCommand(String priority) {
        super();
        this.priority = priority;
    }

    /**
     * Returns the priority or label for continuation upon exiting the application.
     *
     * @return the priority or label for continuation upon exiting the application.
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority or label for continuation upon exiting the application.
     *
     * @param priority the priority or label for continuation upon exiting the application.
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String buildCommand() {
        return "SET PRIORITY " + escapeAndQuote(priority);
    }
}
