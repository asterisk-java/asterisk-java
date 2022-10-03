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
 * AGI Command: <b>TDD MODE</b>
 * <p>
 * Toggles TDD mode (for the deaf).
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_tdd+mode">AGI Command TDD MODE (Asterisk 18)</a>
 *
 * @author srt
 */
public class TddModeCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3258411746401268532L;

    /**
     * The mode to set.
     */
    private String mode;

    /**
     * Creates a new TDDModeCommand.
     *
     * @param mode the mode to set, this can be one of "on", "off", "mate" or "tdd".
     */
    public TddModeCommand(String mode) {
        super();
        this.mode = mode;
    }

    /**
     * Returns the mode to set.
     *
     * @return the mode to set.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the mode to set.
     *
     * @param mode the mode to set, this can be one of "on", "off", "mate" or "tdd".
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String buildCommand() {
        return "TDD MODE " + escapeAndQuote(mode);
    }
}
