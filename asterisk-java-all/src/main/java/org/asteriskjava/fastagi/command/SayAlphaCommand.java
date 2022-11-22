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
 * AGI Command: <b>SAY ALPHA</b>
 * <p>
 * Say a given character string, returning early if any of the
 * given DTMF digits are received on the channel.<br>
 * Returns 0 if playback completes without a digit being pressed, or the ASCII
 * numerical value of the digit if one was pressed or -1 on error/hangup.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_say+alpha">AGI Command SAY ALPHA</a>
 *
 * @author srt
 */
public class SayAlphaCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256721797012404276L;

    /**
     * The text to say.
     */
    private String text;

    /**
     * When one of these digits is pressed the command returns.
     */
    private String escapeDigits;

    /**
     * Creates a new SayAlphaCommand.
     *
     * @param text the text to say.
     */
    public SayAlphaCommand(String text) {
        super();
        this.text = text;
    }

    /**
     * Creates a new SayAlphaCommand.
     *
     * @param text         the text to say.
     * @param escapeDigits contains the digits that allow the user to interrupt this command.
     */
    public SayAlphaCommand(String text, String escapeDigits) {
        super();
        this.text = text;
        this.escapeDigits = escapeDigits;
    }

    /**
     * Returns the text to say.
     *
     * @return the text to say.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text to say.
     *
     * @param text the text to say.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the digits that allow the user to interrupt this command.
     *
     * @return the digits that allow the user to interrupt this command.
     */
    public String getEscapeDigits() {
        return escapeDigits;
    }

    /**
     * Sets the digits that allow the user to interrupt this command.
     *
     * @param escapeDigits the text that allow the user to interrupt this command or <code>null</code> for none.
     */
    public void setEscapeDigits(String escapeDigits) {
        this.escapeDigits = escapeDigits;
    }

    @Override
    public String buildCommand() {
        return "SAY ALPHA " + escapeAndQuote(text) + " " + escapeAndQuote(escapeDigits);
    }
}
