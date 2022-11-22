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
 * AGI Command: <b>SET CALLERID</b>
 * <p>
 * Sets callerid for the current channel.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_set+callerid">AGI Command SET CALLERID (Asterisk 18)</a>
 *
 * @author srt
 */
public class SetCallerIdCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256721797012404276L;

    /**
     * The new callerId.
     */
    private String callerId;

    /**
     * Creates a new SetCallerIdCommand.
     *
     * @param callerId the new callerId.
     */
    public SetCallerIdCommand(String callerId) {
        super();
        this.callerId = callerId;
    }

    /**
     * Returns the new callerId.
     *
     * @return the new callerId.
     */
    public String getCallerId() {
        return callerId;
    }

    /**
     * Sets the new callerId.
     *
     * @param callerId the new callerId.
     */
    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    @Override
    public String buildCommand() {
        return "SET CALLERID " + escapeAndQuote(callerId);
    }
}
