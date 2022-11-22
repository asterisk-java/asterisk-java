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
 * AGI Command: <b>RECEIVE TEXT</b>
 * <p>
 * Receives text from channels supporting it.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_receive+text">AGI Command RECEIVE TEXT (Asterisk 18)</a>
 *
 * @author srt
 */
public class ReceiveTextCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The milliseconds to wait for the channel to receive a character.
     */
    private int timeout;

    /**
     * Creates a new ReceiveTextCommand with a default timeout of 0 meaning to wait forever.
     */
    public ReceiveTextCommand() {
        super();
        this.timeout = 0;
    }

    /**
     * Creates a new ReceiveTextCommand.
     *
     * @param timeout the milliseconds to wait for the channel to receive the text.
     */
    public ReceiveTextCommand(int timeout) {
        super();
        this.timeout = timeout;
    }

    /**
     * Returns the milliseconds to wait for the channel to receive the text.
     *
     * @return the milliseconds to wait for the channel to receive the text.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the milliseconds to wait for the channel to receive the text.
     *
     * @param timeout the milliseconds to wait for the channel to receive the text.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String buildCommand() {
        return "RECEIVE TEXT " + timeout;
    }
}
