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
 * AGI Command: <b>WAIT FOR DIGIT</b>
 * <p>
 * Waits for a digit to be pressed.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_wait+for+digit">AGI Command WAIT FOR DIGIT (Asterisk 18)</a>
 *
 * @author srt
 */
public class WaitForDigitCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3257562923458443314L;

    /**
     * The milliseconds to wait for the channel to receive a DTMF digit.
     */
    private long timeout;

    /**
     * Creates a new WaitForDigitCommand with a default timeout of -1 which blocks the channel indefinitely.
     */
    public WaitForDigitCommand() {
        super();
        this.timeout = -1;
    }

    /**
     * Creates a new WaitForDigitCommand.
     *
     * @param timeout the milliseconds to wait for the channel to receive a DTMF digit.
     */
    public WaitForDigitCommand(long timeout) {
        super();
        this.timeout = timeout;
    }

    /**
     * Returns the milliseconds to wait for the channel to receive a DTMF digit.
     *
     * @return the milliseconds to wait for the channel to receive a DTMF digit.
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets the milliseconds to wait for the channel to receive a DTMF digit.
     *
     * @param timeout the milliseconds to wait for the channel to receive a DTMF digit.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String buildCommand() {
        return "WAIT FOR DIGIT " + timeout;
    }
}
