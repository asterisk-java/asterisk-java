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
 * AGI Command: <b>SET VARIABLE</b>
 * <p>
 * Sets a channel variable.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_set+variable">AGI Command SET VARIABLE (Asterisk 18)</a>
 *
 * @author srt
 */
public class SetVariableCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The name of the variable to set.
     */
    private String variable;

    /**
     * The value to set.
     */
    private String value;

    /**
     * Creates a new GetVariableCommand.
     *
     * @param variable the name of the variable to set.
     * @param value    the value to set.
     */
    public SetVariableCommand(String variable, String value) {
        super();
        this.variable = variable;
        this.value = value;
    }

    /**
     * Returns the name of the variable to set.
     *
     * @return the name of the variable to set.
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the name of the variable to set.
     *
     * @param variable the name of the variable to set.
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * Returns the value to set.
     *
     * @return the value to set.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value to set.
     *
     * @param value the value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String buildCommand() {
        return "SET VARIABLE " + escapeAndQuote(variable) + " " + escapeAndQuote(value);
    }
}
