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
 * Plays the given file, and waits for the user to press one of the given
 * digits. If none of the esacpe digits is pressed while streaming the file this
 * command waits for the specified timeout still waiting for the user to press a
 * digit. Streaming always begins at the beginning.<p>
 * Returns 0 if no digit being pressed, or the ASCII numerical value of the
 * digit if one was pressed, or -1 on error or if the channel was disconnected.
 * <p>
 * Remember, the file extension must not be included in the filename.
 * 
 * @see org.asteriskjava.fastagi.command.StreamFileCommand
 * @author srt
 * @version $Id$
 */
public class GetOptionCommand extends AbstractAgiCommand
{
    /**
     * Serial version identifier.
     */
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
     * The timeout in seconds.
     */
    private int timeout;

    /**
     * Creates a new GetOptionCommand with a default timeout of 5 seconds.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that the user is expected to
     *            press.
     */
    public GetOptionCommand(String file, String escapeDigits)
    {
        this.file = file;
        this.escapeDigits = escapeDigits;
        this.timeout = -1;
    }

    /**
     * Creates a new GetOptionCommand with the given timeout.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that the user is expected to
     *            press.
     * @param timeout the timeout in seconds to wait if none of the defined
     *            esacpe digits was presses while streaming.
     */
    public GetOptionCommand(String file, String escapeDigits, int timeout)
    {
        this.file = file;
        this.escapeDigits = escapeDigits;
        this.timeout = timeout;
    }

    /**
     * Returns the name of the file to stream.
     * 
     * @return the name of the file to stream.
     */
    public String getFile()
    {
        return file;
    }

    /**
     * Sets the name of the file to stream.
     * 
     * @param file the name of the file to stream, must not include extension.
     */
    public void setFile(String file)
    {
        this.file = file;
    }

    /**
     * Returns the digits that the user is expected to press.
     * 
     * @return the digits that the user is expected to press.
     */
    public String getEscapeDigits()
    {
        return escapeDigits;
    }

    /**
     * Sets the digits that the user is expected to press.
     * 
     * @param escapeDigits the digits that the user is expected to press.
     */
    public void setEscapeDigits(String escapeDigits)
    {
        this.escapeDigits = escapeDigits;
    }

    /**
     * Returns the timeout to wait if none of the defined esacpe digits was
     * presses while streaming.
     * 
     * @return the timeout in seconds.
     */
    public int getTimeout()
    {
        return timeout;
    }

    /**
     * Sets the timeout to wait if none of the defined esacpe digits was presses
     * while streaming.
     * 
     * @param timeout the timeout in seconds.
     */
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public String buildCommand()
    {
        return "GET OPTION " + escapeAndQuote(file) + " "
                + escapeAndQuote(escapeDigits)
                + (timeout < 0 ? "" : " " + timeout);
    }
}
