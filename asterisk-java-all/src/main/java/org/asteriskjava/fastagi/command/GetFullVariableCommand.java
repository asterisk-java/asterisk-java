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
 * AGI Command: <b>GET FULL VARIABLE</b>
 * <p>
 * Evaluates the given expression against the channel specified by channelname, or the current channel if channelname is not provided.<br>
 * Unlike GET VARIABLE, the expression is processed in a manner similar to dialplan evaluation,
 * allowing complex and built-in variables to be accessed, e.g. The time is ${EPOCH}<br>
 * Returns 0 if no channel matching channelname exists, 1 otherwise.<br>
 * Example return code: 200 result=1 (testvariable)
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_get+full+variable">AGI Command GET FULL VARIABLE (Asterisk 18)</a>
 *
 * @author srt
 */
public class GetFullVariableCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The name of the variable to retrieve.
     */
    private String variable;

    private String channel;

    /**
     * Creates a new GetFullVariableCommand.
     *
     * @param variable the name of the variable to retrieve.
     */
    public GetFullVariableCommand(String variable) {
        super();
        this.variable = variable;
    }

    /**
     * Creates a new GetFullVariableCommand.
     *
     * @param variable the name of the variable to retrieve.
     * @param channel  the name of the channel.
     */
    public GetFullVariableCommand(String variable, String channel) {
        super();
        this.variable = variable;
        this.channel = channel;
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
     * You can also use custom dialplan functions (like "func(args)") as variable.
     *
     * @param variable the name of the variable to retrieve.
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * Returns the name of the channel.
     *
     * @return the name of the channel.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the name of the channel.
     *
     * @param channel the name of the channel.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String buildCommand() {
        StringBuilder sb;

        sb = new StringBuilder("GET FULL VARIABLE ");
        sb.append(escapeAndQuote(variable));

        if (channel != null) {
            sb.append(" ");
            sb.append(escapeAndQuote(channel));
        }

        return sb.toString();
    }
}
