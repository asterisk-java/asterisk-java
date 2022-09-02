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
 * AGI Command: <b>RECEIVE CHAR</b>
 * <p>
 * Receives one character from channels supporting it.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_receive+char">AGI CommandRECEIVE CHAR (Asterisk 18)</a>
 *
 * @author srt
 */
public class ReceiveCharCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The milliseconds to wait for the channel to receive a character.
     */
    private int timeout;

    /**
     * Creates a new ReceiveCharCommand with a default timeout of 0 meaning to wait forever.
     */
    public ReceiveCharCommand() {
        super();
        this.timeout = 0;
    }

    /**
     * Creates a new ReceiveCharCommand.
     *
     * @param timeout the milliseconds to wait for the channel to receive a character.
     */
    public ReceiveCharCommand(int timeout) {
        super();
        this.timeout = timeout;
    }

    /**
     * Returns the milliseconds to wait for the channel to receive a character.
     *
     * @return the milliseconds to wait for the channel to receive a character.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the milliseconds to wait for the channel to receive a character.
     *
     * @param timeout the milliseconds to wait for the channel to receive a character.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String buildCommand() {
        return "RECEIVE CHAR " + timeout;
    }
}
