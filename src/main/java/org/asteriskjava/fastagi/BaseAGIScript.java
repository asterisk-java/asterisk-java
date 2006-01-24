/*
 *  Copyright  2004-2006 Stefan Reuter
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

import org.asteriskjava.fastagi.command.AGICommand;
import org.asteriskjava.fastagi.command.AnswerCommand;
import org.asteriskjava.fastagi.reply.AGIReply;


/**
 * The BaseAGIScript provides some convinience methods to make it easier to
 * write custom AGIScripts.<br>
 * Just extend it by your own AGIScripts.
 * 
 * @since 0.2
 * @author srt
 * @version $Id: BaseAGIScript.java,v 1.9 2006/01/12 14:08:33 srt Exp $
 */
public abstract class BaseAGIScript implements AGIScript
{
    /**
     * Answers the channel.
     */
    protected void answer() throws AGIException
    {
        sendCommand(new AnswerCommand());
    }

    /**
     * Hangs the channel up.
     */
    protected void hangup() throws AGIException
    {
        getChannel().hangup();
    }

    /**
     * Cause the channel to automatically hangup at the given number of seconds
     * in the future.
     * 
     * @param time the number of seconds before this channel is automatically
     *            hung up.<br>
     *            0 disables the autohangup feature.
     */
    protected void setAutoHangup(int time) throws AGIException
    {
        getChannel().setAutoHangup(time);
    }

    /**
     * Sets the caller id on the current channel.
     * 
     * @param callerId the raw caller id to set, for example "John Doe<1234>".
     */
    protected void setCallerId(String callerId) throws AGIException
    {
        getChannel().setCallerId(callerId);
    }

    /**
     * Plays music on hold from the default music on hold class.
     */
    protected void playMusicOnHold() throws AGIException
    {
        getChannel().playMusicOnHold();
    }

    /**
     * Plays music on hold from the given music on hold class.
     * 
     * @param musicOnHoldClass the music on hold class to play music from as
     *            configures in Asterisk's <code><musiconhold.conf/code>.
     */
    protected void playMusicOnHold(String musicOnHoldClass) throws AGIException
    {
        getChannel().playMusicOnHold(musicOnHoldClass);
    }

    /**
     * Stops playing music on hold.
     */
    protected void stopMusicOnHold() throws AGIException
    {
        getChannel().stopMusicOnHold();
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
    protected int getChannelStatus() throws AGIException
    {
        return getChannel().getChannelStatus();
    }

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#'. The user may interrupt the streaming by starting to enter
     * digits.
     * 
     * @param file the name of the file to play
     * @return a String containing the DTMF the user entered
     */
    protected String getData(String file) throws AGIException
    {
        return getChannel().getData(file);
    }

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#' or the timeout occurs. The user may interrupt the streaming
     * by starting to enter digits.
     * 
     * @param file the name of the file to play
     * @param timeout the timeout in milliseconds to wait for user input.<br>
     *            0 means standard timeout value, -1 means "ludicrous time"
     *            (essentially never times out).
     * @return a String containing the DTMF the user entered
     */
    protected String getData(String file, int timeout) throws AGIException
    {
        return getChannel().getData(file, timeout);
    }

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#' or the timeout occurs or the maximum number of digits has
     * been entered. The user may interrupt the streaming by starting to enter
     * digits.
     * 
     * @param file the name of the file to play
     * @param timeout the timeout in milliseconds to wait for user input.<br>
     *            0 means standard timeout value, -1 means "ludicrous time"
     *            (essentially never times out).
     * @param maxDigits the maximum number of digits the user is allowed to
     *            enter
     * @return a String containing the DTMF the user entered
     */
    protected String getData(String file, int timeout, int maxDigits)
            throws AGIException
    {
        return getChannel().getData(file, timeout, maxDigits);
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
    protected char getOption(String file, String escapeDigits)
            throws AGIException
    {
        return getChannel().getOption(file, escapeDigits);
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
    protected char getOption(String file, String escapeDigits, int timeout)
            throws AGIException
    {
        return getChannel().getOption(file, escapeDigits, timeout);
    }

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @return the return code of the application of -2 if the application was
     *         not found.
     */
    protected int exec(String application) throws AGIException
    {
        return getChannel().exec(application);
    }

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @param options the parameters to pass to the application, for example
     *            "SIP/123". Multiple options are separated by the pipe character ('|').
     * @return the return code of the application of -2 if the application was
     *         not found.
     */
    protected int exec(String application, String options) throws AGIException
    {
        return getChannel().exec(application, options);
    }

    /**
     * Sets the context for continuation upon exiting the application.
     * 
     * @param context the context for continuation upon exiting the application.
     */
    protected void setContext(String context) throws AGIException
    {
        getChannel().setContext(context);
    }

    /**
     * Sets the extension for continuation upon exiting the application.
     * 
     * @param extension the extension for continuation upon exiting the
     *            application.
     */
    protected void setExtension(String extension) throws AGIException
    {
        getChannel().setExtension(extension);
    }

    /**
     * Sets the priority or label for continuation upon exiting the application.
     * 
     * @param priority the priority or label for continuation upon exiting the
     *            application.
     */
    protected void setPriority(String priority) throws AGIException
    {
        getChannel().setPriority(priority);
    }

    /**
     * Plays the given file.
     * 
     * @param file name of the file to play.
     */
    protected void streamFile(String file) throws AGIException
    {
        getChannel().streamFile(file);
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
    protected char streamFile(String file, String escapeDigits)
            throws AGIException
    {
        return getChannel().streamFile(file, escapeDigits);
    }

    /**
     * Says the given digit string.
     * 
     * @param digits the digit string to say.
     */
    protected void sayDigits(String digits) throws AGIException
    {
        getChannel().sayDigits(digits);
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
    protected char sayDigits(String digits, String escapeDigits)
            throws AGIException
    {
        return getChannel().sayDigits(digits, escapeDigits);
    }

    /**
     * Says the given number.
     * 
     * @param number the number to say.
     */
    protected void sayNumber(String number) throws AGIException
    {
        getChannel().sayNumber(number);
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
    protected char sayNumber(String number, String escapeDigits)
            throws AGIException
    {
        return getChannel().sayNumber(number, escapeDigits);
    }

    /**
     * Says the given character string with phonetics.
     * 
     * @param text the text to say.
     */
    protected void sayPhonetic(String text) throws AGIException
    {
        getChannel().sayPhonetic(text);
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
    protected char sayPhonetic(String text, String escapeDigits)
            throws AGIException
    {
        return getChannel().sayPhonetic(text, escapeDigits);
    }

    /**
     * Says the given character string.
     * 
     * @param text the text to say.
     */
    protected void sayAlpha(String text) throws AGIException
    {
        getChannel().sayAlpha(text);
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
    protected char sayAlpha(String text, String escapeDigits)
            throws AGIException
    {
        return getChannel().sayAlpha(text, escapeDigits);
    }

    /**
     * Says the given time.
     * 
     * @param time the time to say in seconds since 00:00:00 on January 1, 1970.
     */
    protected void sayTime(long time) throws AGIException
    {
        getChannel().sayTime(time);
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
    protected char sayTime(long time, String escapeDigits) throws AGIException
    {
        return getChannel().sayTime(time, escapeDigits);
    }

    /**
     * Returns the value of the given channel variable.
     * 
     * @param name the name of the variable to retrieve.
     * @return the value of the given variable or <code>null</code> if not
     *         set.
     */
    protected String getVariable(String name) throws AGIException
    {
        return getChannel().getVariable(name);
    }

    /**
     * Sets the value of the given channel variable to a new value.
     * 
     * @param name the name of the variable to retrieve.
     * @param value the new value to set.
     */
    protected void setVariable(String name, String value) throws AGIException
    {
        getChannel().setVariable(name, value);
    }

    /**
     * Waits up to 'timeout' milliseconds to receive a DTMF digit.
     * 
     * @param timeout timeout the milliseconds to wait for the channel to
     *            receive a DTMF digit, -1 will wait forever.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     */
    protected char waitForDigit(int timeout) throws AGIException
    {
        return getChannel().waitForDigit(timeout);
    }

    /**
     * Returns the value of the current channel variable, unlike getVariable()
     * this method understands complex variable names and builtin variables.<br>
     * Available since Asterisk 1.2.
     * 
     * @param name the name of the variable to retrieve.
     * @return the value of the given variable or <code>null</code> if not
     *         set.
     * @since 0.2
     */
    protected String getFullVariable(String name) throws AGIException
    {
        return getChannel().getFullVariable(name);
    }

    /**
     * Returns the value of the given channel variable.<br>
     * Available since Asterisk 1.2.
     * 
     * @param name the name of the variable to retrieve.
     * @param channel the name of the channel.
     * @return the value of the given variable or <code>null</code> if not
     *         set.
     * @since 0.2
     */
    protected String getFullVariable(String name, String channel)
            throws AGIException
    {
        return getChannel().getFullVariable(name, channel);
    }

    /**
     * Says the given time.<br>
     * Available since Asterisk 1.2.
     * 
     * @param time the time to say in seconds elapsed since 00:00:00 on January
     *            1, 1970, Coordinated Universal Time (UTC)
     * @since 0.2
     */
    protected void sayDateTime(long time) throws AGIException
    {
        getChannel().sayDateTime(time);
    }

    /**
     * Says the given time and allows interruption by one of the given escape
     * digits.<br>
     * Available since Asterisk 1.2.
     * 
     * @param time the time to say in seconds elapsed since 00:00:00 on January
     *            1, 1970, Coordinated Universal Time (UTC)
     * @param escapeDigits the digits that allow the user to interrupt this
     *            command or <code>null</code> for none.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    protected char sayDateTime(long time, String escapeDigits)
            throws AGIException
    {
        return getChannel().sayDateTime(time, escapeDigits);
    }

    /**
     * Says the given time in the given format and allows interruption by one of
     * the given escape digits.<br>
     * Available since Asterisk 1.2.
     * 
     * @param time the time to say in seconds elapsed since 00:00:00 on January
     *            1, 1970, Coordinated Universal Time (UTC)
     * @param escapeDigits the digits that allow the user to interrupt this
     *            command or <code>null</code> for none.
     * @param format the format the time should be said in
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    protected char sayDateTime(long time, String escapeDigits, String format)
            throws AGIException
    {
        return getChannel().sayDateTime(time, escapeDigits, format);
    }

    /**
     * Says the given time in the given format and timezone and allows
     * interruption by one of the given escape digits.<br>
     * Available since Asterisk 1.2.
     * 
     * @param time the time to say in seconds elapsed since 00:00:00 on January
     *            1, 1970, Coordinated Universal Time (UTC)
     * @param escapeDigits the digits that allow the user to interrupt this
     *            command or <code>null</code> for none.
     * @param format the format the time should be said in
     * @param timezone the timezone to use when saying the time, for example
     *            "UTC" or "Europe/Berlin".
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    protected char sayDateTime(long time, String escapeDigits, String format,
            String timezone) throws AGIException
    {
        return getChannel().sayDateTime(time, escapeDigits, format, timezone);
    }

    /**
     * Retrieves an entry in the Asterisk database for a given family and key.
     *  
     * @param family the family of the entry to retrieve.
     * @param key the key of the entry to retrieve.
     * @return the value of the given family and key or <code>null</code> if there
     *         is no such value.
     * @since 0.3
     */
    protected String databaseGet(String family, String key) throws AGIException
    {
        return getChannel().databaseGet(family, key);
    }

    /**
     * Adds or updates an entry in the Asterisk database for a given family, key,
     * and value.
     *  
     * @param family the family of the entry to add or update.
     * @param key the key of the entry to add or update.
     * @param value the new value of the entry.
     * @since 0.3
     */
    protected void databasePut(String family, String key, String value) throws AGIException
    {
        getChannel().databasePut(family, key, value);
    }

    /**
     * Deletes an entry in the Asterisk database for a given family and key.
     *  
     * @param family the family of the entry to delete.
     * @param key the key of the entry to delete.
     * @since 0.3
     */
    protected void databaseDel(String family, String key) throws AGIException
    {
        getChannel().databaseDel(family, key);
    }

    /**
     * Deletes a whole family of entries in the Asterisk database.
     *  
     * @param family the family to delete.
     * @since 0.3
     */
    protected void databaseDelTree(String family) throws AGIException
    {
        getChannel().databaseDelTree(family);
    }

    /**
     * Deletes all entries of a given family in the Asterisk database that have a key
     * that starts with a given prefix.
     *  
     * @param family the family of the entries to delete.
     * @param keytree the prefix of the keys of the entries to delete.
     * @since 0.3
     */
    protected void databaseDelTree(String family, String keytree) throws AGIException
    {
        getChannel().databaseDelTree(family, keytree);
    }

    /**
     * Sends a message to the Asterisk console via the verbose message system.
     * 
     * @param message the message to send.
     * @param level the verbosity level to use. Must be in [1..4].
     * @since 0.3
     */
    protected void verbose(String message, int level) throws AGIException
    {
        getChannel().verbose(message, level);
    }

    /**
     * Sends the given command to the channel attached to the current thread.
     * 
     * @param command the command to send to Asterisk
     * @return the reply received from Asterisk
     * @throws AGIException if the command could not be processed properly
     */
    private AGIReply sendCommand(AGICommand command) throws AGIException
    {
        return getChannel().sendCommand(command);
    }

    private AGIChannel getChannel()
    {
        AGIChannel channel;

        channel = AGIConnectionHandler.getChannel();
        if (channel == null)
        {
            throw new RuntimeException(
                    "Trying to send command from an invalid thread");
        }

        return channel;
    }
}
