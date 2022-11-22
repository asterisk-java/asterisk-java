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
 * AGI Command: <b>GET VARIABLE</b>
 * <p>
 * Gets a channel variable.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_get+variable">AGI Command GET VARIABLE (Asterisk 18)</a>
 *
 * @author srt
 */
public class GetVariableCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The name of the variable to retrieve.
     */
    private String variable;

    /**
     * Creates a new GetVariableCommand.
     *
     * @param variable the name of the variable to retrieve.
     */
    public GetVariableCommand(String variable) {
        super();
        this.variable = variable;
    }

    /**
     * Returns the name of the variable to retrieve.
     *
     * @return the name of the variable to retrieve.
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the name of the variable to retrieve.<p>
     * Since Asterisk 1.2 you can also use custom dialplan functions (like "func(args)") as variable.
     *
     * @param variable the name of the variable to retrieve.
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public String buildCommand() {
        return "GET VARIABLE " + escapeAndQuote(variable);
    }
}
