/*
 * Copyright  2004-2005 Stefan Reuter
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
package org.asteriskjava.fastagi;

import org.asteriskjava.fastagi.command.AnswerCommand;

/**
 * The AbstractAGIScript provides some convinience methods to make it easier to
 * write custom AGIScripts.<br>
 * Just extend it by your own AGIScripts.<br>
 * 
 * @deprecated use {@see net.sf.asterisk.fastagi.BaseAGIScript} instead
 * @author srt
 * @version $Id: AbstractAGIScript.java,v 1.19 2005/11/27 15:22:40 srt Exp $
 */
public abstract class AbstractAGIScript implements AGIScript
{
    /**
     * Answers the channel.
     */
    protected void answer(AGIChannel channel) throws AGIException
    {
        channel.sendCommand(new AnswerCommand());
    }

    /**
     * Hangs the channel up.
     */
    protected void hangup(AGIChannel channel) throws AGIException
    {
        channel.hangup();
    }

    /**
     * Cause the channel to automatically hangup at the given number of seconds
     * in the future.
     * 
     * @param time the number of seconds before this channel is automatically
     *            hung up.<br>
     *            0 disables the autohangup feature.
     */
    protected void setAutoHangup(AGIChannel channel, int time)
            throws AGIException
    {
        channel.setAutoHangup(time);
    }

    /**
     * Sets the caller id on the current channel.
     * 
     * @param callerId the raw caller id to set, for example "John Doe<1234>".
     */
    protected void setCallerId(AGIChannel channel, String callerId)
            throws AGIException
    {
        channel.setCallerId(callerId);
    }

    /**
     * Plays music on hold from the default music on hold class.
     */
    protected void playMusicOnHold(AGIChannel channel) throws AGIException
    {
        channel.playMusicOnHold();
    }

    /**
     * Plays music on hold from the given music on hold class.
     * 
     * @param musicOnHoldClass the music on hold class to play music from as
     *            configures in Asterisk's <code><musiconhold.conf/code>.
     */
    protected void playMusicOnHold(AGIChannel channel, String musicOnHoldClass)
            throws AGIException
    {
        channel.playMusicOnHold(musicOnHoldClass);
    }

    /**
     * Stops playing music on hold.
     */
    protected void stopMusicOnHold(AGIChannel channel) throws AGIException
    {
        channel.stopMusicOnHold();
    }

    /**
     * Returns the status of the channel.<br>
     * Return values:
     * <ul>
     * <li>0 Channel is down and available
     * <li>1 Channel is down, but reserved
     * <li>2 Channel is off hook
     * <li>3 Digits (or equivalent) have been dialed
     * <li>4 Line is ringing
     * <li>5 Remote end is ringing
     * <li>6 Line is up
     * <li>7 Line is busy
     * </ul>
     * 
     * @return the status of the channel.
     */
    protected int getChannelStatus(AGIChannel channel) throws AGIException
    {
        return channel.getChannelStatus();
    }

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#'. The user may interrupt the streaming by starting to enter
     * digits.
     * 
     * @param file the name of the file to play
     * @return a String containing the DTMF the user entered
     * @since 0.2
     */
    protected String getData(AGIChannel channel, String file)
            throws AGIException
    {
        return channel.getData(file);
    }

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#' or the timeout occurs. The user may interrupt the streaming
     * by starting to enter digits.
     * 
     * @param file the name of the file to play
     * @param timeout the timeout to wait for user input.<br>
     *            0 means standard timeout value, -1 means "ludicrous time"
     *            (essentially never times out).
     * @return a String containing the DTMF the user entered
     * @since 0.2
     */
    protected String getData(AGIChannel channel, String file, int timeout)
            throws AGIException
    {
        return channel.getData(file, timeout);
    }

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#' or the timeout occurs or the maximum number of digits has
     * been entered. The user may interrupt the streaming by starting to enter
     * digits.
     * 
     * @param file the name of the file to play
     * @param timeout the timeout to wait for user input.<br>
     *            0 means standard timeout value, -1 means "ludicrous time"
     *            (essentially never times out).
     * @param maxDigits the maximum number of digits the user is allowed to
     *            enter
     * @return a String containing the DTMF the user entered
     * @since 0.2
     */
    protected String getData(AGIChannel channel, String file, int timeout,
            int maxDigits) throws AGIException
    {
        return channel.getData(file, timeout, maxDigits);
    }

    /**
     * Plays the given file, and waits for the user to press one of the given
     * digits. If none of the esacpe digits is pressed while streaming the file
     * it waits for the default timeout of 5 seconds still waiting for the user
     * to press a digit.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that the user is expected to
     *            press.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char getOption(AGIChannel channel, String file,
            String escapeDigits) throws AGIException
    {
        return channel.getOption(file, escapeDigits);
    }

    /**
     * Plays the given file, and waits for the user to press one of the given
     * digits. If none of the esacpe digits is pressed while streaming the file
     * it waits for the specified timeout still waiting for the user to press a
     * digit.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that the user is expected to
     *            press.
     * @param timeout the timeout in seconds to wait if none of the defined
     *            esacpe digits was presses while streaming.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char getOption(AGIChannel channel, String file,
            String escapeDigits, int timeout) throws AGIException
    {
        return channel.getOption(file, escapeDigits, timeout);
    }

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @return the return code of the application of -2 if the application was
     *         not found.
     * @since 0.2
     */
    protected int exec(AGIChannel channel, String application)
            throws AGIException
    {
        return channel.exec(application);
    }

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @param options the parameters to pass to the application, for example
     *            "SIP/123".
     * @return the return code of the application of -2 if the application was
     *         not found.
     * @since 0.2
     */
    protected int exec(AGIChannel channel, String application, String options)
            throws AGIException
    {
        return channel.exec(application, options);
    }

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @return the return code of the application of -2 if the application was
     *         not found.
     * @deprecated use {@see #exec(AGIChannel, String)} instead
     */
    protected int execCommand(AGIChannel channel, String application)
            throws AGIException
    {
        return channel.exec(application);
    }

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @param options the parameters to pass to the application, for example
     *            "SIP/123".
     * @return the return code of the application of -2 if the application was
     *         not found.
     * @deprecated use {@see #exec(AGIChannel, String, String)} instead
     */
    protected int execCommand(AGIChannel channel, String application,
            String options) throws AGIException
    {
        return channel.exec(application, options);
    }

    /**
     * Sets the context for continuation upon exiting the application.
     * 
     * @param context the context for continuation upon exiting the application.
     */
    protected void setContext(AGIChannel channel, String context)
            throws AGIException
    {
        channel.setContext(context);
    }

    /**
     * Sets the extension for continuation upon exiting the application.
     * 
     * @param extension the extension for continuation upon exiting the
     *            application.
     */
    protected void setExtension(AGIChannel channel, String extension)
            throws AGIException
    {
        channel.setExtension(extension);
    }

    /**
     * Sets the priority or label for continuation upon exiting the application.
     * 
     * @param priority the priority or label for continuation upon exiting the
     *            application.
     */
    protected void setPriority(AGIChannel channel, String priority)
            throws AGIException
    {
        channel.setPriority(priority);
    }

    /**
     * Plays the given file.
     * 
     * @param file name of the file to play.
     */
    protected void streamFile(AGIChannel channel, String file)
            throws AGIException
    {
        channel.streamFile(file);
    }

    /**
     * Plays the given file and allows the user to escape by pressing one of the
     * given digit.
     * 
     * @param file name of the file to play.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char streamFile(AGIChannel channel, String file,
            String escapeDigits) throws AGIException
    {
        return channel.streamFile(file, escapeDigits);
    }

    /**
     * Says the given digit string.
     * 
     * @param digits the digit string to say.
     */
    protected void sayDigits(AGIChannel channel, String digits)
            throws AGIException
    {
        channel.sayDigits(digits);
    }

    /**
     * Says the given number, returning early if any of the given DTMF number
     * are received on the channel.
     * 
     * @param digits the digit string to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char sayDigits(AGIChannel channel, String digits,
            String escapeDigits) throws AGIException
    {
        return channel.sayDigits(digits, escapeDigits);
    }

    /**
     * Says the given number.
     * 
     * @param number the number to say.
     */
    protected void sayNumber(AGIChannel channel, String number)
            throws AGIException
    {
        channel.sayNumber(number);
    }

    /**
     * Says the given number, returning early if any of the given DTMF number
     * are received on the channel.
     * 
     * @param number the number to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char sayNumber(AGIChannel channel, String number,
            String escapeDigits) throws AGIException
    {
        return channel.sayNumber(number, escapeDigits);
    }

    /**
     * Says the given character string with phonetics.
     * 
     * @param text the text to say.
     */
    protected void sayPhonetic(AGIChannel channel, String text)
            throws AGIException
    {
        channel.sayPhonetic(text);
    }

    /**
     * Says the given character string with phonetics, returning early if any of
     * the given DTMF number are received on the channel.
     * 
     * @param text the text to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char sayPhonetic(AGIChannel channel, String text,
            String escapeDigits) throws AGIException
    {
        return channel.sayPhonetic(text, escapeDigits);
    }

    /**
     * Says the given character string.
     * 
     * @param text the text to say.
     */
    protected void sayAlpha(AGIChannel channel, String text)
            throws AGIException
    {
        channel.sayAlpha(text);
    }

    /**
     * Says the given character string, returning early if any of the given DTMF
     * number are received on the channel.
     * 
     * @param text the text to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char sayAlpha(AGIChannel channel, String text, String escapeDigits)
            throws AGIException
    {
        return channel.sayAlpha(text, escapeDigits);
    }

    /**
     * Says the given time.
     * 
     * @param time the time to say in seconds since 00:00:00 on January 1, 1970.
     */
    protected void sayTime(AGIChannel channel, long time) throws AGIException
    {
        channel.sayTime(time);
    }

    /**
     * Says the given time, returning early if any of the given DTMF number are
     * received on the channel.
     * 
     * @param time the time to say in seconds since 00:00:00 on January 1, 1970.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char sayTime(AGIChannel channel, long time, String escapeDigits)
            throws AGIException
    {
        return channel.sayTime(time, escapeDigits);
    }

    /**
     * Returns the value of the given channel variable.
     * 
     * @param name the name of the variable to retrieve.
     * @return the value of the given variable or <code>null</code> if not
     *         set.
     */
    protected String getVariable(AGIChannel channel, String name)
            throws AGIException
    {
        return channel.getVariable(name);
    }

    /**
     * Sets the value of the given channel variable to a new value.
     * 
     * @param name the name of the variable to retrieve.
     * @param value the new value to set.
     */
    protected void setVariable(AGIChannel channel, String name, String value)
            throws AGIException
    {
        channel.setVariable(name, value);
    }

    /**
     * Waits up to 'timeout' milliseconds to receive a DTMF digit.
     * 
     * @param timeout timeout the milliseconds to wait for the channel to
     *            receive a DTMF digit, -1 will wait forever.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char waitForDigit(AGIChannel channel, int timeout)
            throws AGIException
    {
        return channel.waitForDigit(timeout);
    }
}
