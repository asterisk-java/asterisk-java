/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.fastagi.command;

/**
 * AGI Command: <b>STREAM FILE</b>
 * <p>
 * Sends audio file on channel.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_stream+file">AGI Command STREAM FILE (Asterisk 18)</a>
 *
 * @author srt
 */
public class StreamFileCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3978141041352128820L;

    /**
     * The name of the file to stream.
     */
    private String file;

    /**
     * When one of these digits is pressed while streaming the command returns.
     */
    private String escapeDigits;

    /**
     * The offset samples to skip before streaming.
     */
    private int offset;

    /**
     * Creates a new StreamFileCommand, streaming from the beginning.
     *
     * @param file the name of the file to stream, must not include extension.
     */
    public StreamFileCommand(String file) {
        super();
        this.file = file;
        this.offset = -1;
    }

    /**
     * Creates a new StreamFileCommand, streaming from the beginning.
     *
     * @param file         the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that allow the user to interrupt
     *                     this command.
     */
    public StreamFileCommand(String file, String escapeDigits) {
        super();
        this.file = file;
        this.escapeDigits = escapeDigits;
        this.offset = -1;
    }

    /**
     * Creates a new StreamFileCommand, streaming from the given offset.
     *
     * @param file         the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that allow the user to interrupt
     *                     this command. Maybe <code>null</code> if you don't want the
     *                     user to interrupt.
     * @param offset       the offset samples to skip before streaming.
     */
    public StreamFileCommand(String file, String escapeDigits, int offset) {
        super();
        this.file = file;
        this.escapeDigits = escapeDigits;
        this.offset = offset;
    }

    /**
     * Returns the name of the file to stream.
     *
     * @return the name of the file to stream.
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the name of the file to stream.
     *
     * @param file the name of the file to stream, must not include extension.
     */
    public void setFile(String file) {
        this.file = file;
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
     * @param escapeDigits the digits that allow the user to interrupt this
     *                     command or <code>null</code> for none.
     */
    public void setEscapeDigits(String escapeDigits) {
        this.escapeDigits = escapeDigits;
    }

    /**
     * Returns the offset samples to skip before streaming.
     *
     * @return the offset samples to skip before streaming.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the offset samples to skip before streaming.
     *
     * @param offset the offset samples to skip before streaming.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String buildCommand() {
        return "STREAM FILE " + escapeAndQuote(file) + " " + escapeAndQuote(escapeDigits) + (offset < 0 ? "" : " " + offset);
    }
}
