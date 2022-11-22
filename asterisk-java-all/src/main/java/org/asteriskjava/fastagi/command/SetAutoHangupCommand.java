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
 * AGI Command: <b>SET AUTOHANGUP</b>
 * <p>
 * Autohangup channel in some time.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_set+autohangup">AGI Command SET AUTOHANGUP (Asterisk 18)</a>
 *
 * @author srt
 */
public class SetAutoHangupCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3257562923458443314L;

    /**
     * The number of seconds before this channel is automatically hung up.
     */
    private int time;

    /**
     * Creates a new SetAutoHangupCommand.
     *
     * @param time the number of seconds before this channel is automatically hung up.<p>
     *             0 disables the autohangup feature.
     */
    public SetAutoHangupCommand(int time) {
        super();
        this.time = time;
    }

    /**
     * Returns the number of seconds before this channel is automatically hung up.
     *
     * @return the number of seconds before this channel is automatically hung up.
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the number of seconds before this channel is automatically hung up.
     *
     * @param time the number of seconds before this channel is automatically hung up.<p>
     *             0 disables the autohangup feature.
     */
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String buildCommand() {
        return "SET AUTOHANGUP " + time;
    }
}
