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
package net.sf.asterisk.fastagi;

import net.sf.asterisk.fastagi.command.AGICommand;
import net.sf.asterisk.fastagi.reply.AGIReply;

/**
 * Provides the functionality to send AGICommands to Asterisk while handling an
 * AGIRequest.<br>
 * This interface is supposed to be used by AGIScripts for interaction with the
 * Asterisk server.
 * 
 * @author srt
 * @version $Id: AGIChannel.java,v 1.8 2006/01/12 14:08:33 srt Exp $
 */
public interface AGIChannel
{
    /**
     * Sends a command to asterisk and returns the corresponding reply.
     * 
     * @param command the command to send.
     * @return the reply of the asterisk server containing the return value.
     * @throws AGIException if the command can't be sent to Asterisk (for
     *             example because the channel has been hung up)
     */
    AGIReply sendCommand(AGICommand command) throws AGIException;

    /**
     * Answers the channel.
     * 
     * @since 0.2
     */
    void answer() throws AGIException;

    /**
     * Hangs the channel up.
     * 
     * @since 0.2
     */
    void hangup() throws AGIException;

    /**
     * Cause the channel to automatically hangup at the given number of seconds
     * in the future.
     * 
     * @param time the number of seconds before this channel is automatically
     *            hung up.<br>
     *            0 disables the autohangup feature.
     * @since 0.2
     */
    void setAutoHangup(int time) throws AGIException;

    /**
     * Sets the caller id on the current channel.
     * 
     * @param callerId the raw caller id to set, for example "John Doe<1234>".
     * @since 0.2
     */
    void setCallerId(String callerId) throws AGIException;

    /**
     * Plays music on hold from the default music on hold class.
     * 
     * @since 0.2
     */
    void playMusicOnHold() throws AGIException;

    /**
     * Plays music on hold from the given music on hold class.
     * 
     * @param musicOnHoldClass the music on hold class to play music from as
     *            configures in Asterisk's <code><musiconhold.conf/code>.
     * @since 0.2
     */
    void playMusicOnHold(String musicOnHoldClass) throws AGIException;

    /**
     * Stops playing music on hold.
     * 
     * @since 0.2
     */
    void stopMusicOnHold() throws AGIException;

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
     * @since 0.2
     */
    int getChannelStatus() throws AGIException;

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#'. The user may interrupt the streaming by starting to enter
     * digits.
     * 
     * @param file the name of the file to play
     * @return a String containing the DTMF the user entered
     * @since 0.2
     */
    String getData(String file) throws AGIException;

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
     * @since 0.2
     */
    String getData(String file, long timeout) throws AGIException;

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
     * @since 0.2
     */
    String getData(String file, long timeout, int maxDigits)
            throws AGIException;

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
     * @since 0.2
     */
    char getOption(String file, String escapeDigits) throws AGIException;

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
     * @since 0.2
     */
    char getOption(String file, String escapeDigits, int timeout)
            throws AGIException;

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @return the return code of the application of -2 if the application was
     *         not found.
     * @since 0.2
     */
    int exec(String application) throws AGIException;

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
    int exec(String application, String options) throws AGIException;

    /**
     * Sets the context for continuation upon exiting the application.
     * 
     * @param context the context for continuation upon exiting the application.
     * @since 0.2
     */
    void setContext(String context) throws AGIException;

    /**
     * Sets the extension for continuation upon exiting the application.
     * 
     * @param extension the extension for continuation upon exiting the
     *            application.
     * @since 0.2
     */
    void setExtension(String extension) throws AGIException;

    /**
     * Sets the priority or label for continuation upon exiting the application.
     * 
     * @param priority the priority or label for continuation upon exiting the
     *            application.
     * @since 0.2
     */
    void setPriority(String priority) throws AGIException;

    /**
     * Plays the given file.
     * 
     * @param file name of the file to play.
     * @since 0.2
     */
    void streamFile(String file) throws AGIException;

    /**
     * Plays the given file and allows the user to escape by pressing one of the
     * given digit.
     * 
     * @param file name of the file to play.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    char streamFile(String file, String escapeDigits) throws AGIException;

    /**
     * Says the given digit string.
     * 
     * @param digits the digit string to say.
     * @since 0.2
     */
    void sayDigits(String digits) throws AGIException;

    /**
     * Says the given number, returning early if any of the given DTMF number
     * are received on the channel.
     * 
     * @param digits the digit string to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    char sayDigits(String digits, String escapeDigits) throws AGIException;

    /**
     * Says the given number.
     * 
     * @param number the number to say.
     * @since 0.2
     */
    void sayNumber(String number) throws AGIException;

    /**
     * Says the given number, returning early if any of the given DTMF number
     * are received on the channel.
     * 
     * @param number the number to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    char sayNumber(String number, String escapeDigits) throws AGIException;

    /**
     * Says the given character string with phonetics.
     * 
     * @param text the text to say.
     * @since 0.2
     */
    void sayPhonetic(String text) throws AGIException;

    /**
     * Says the given character string with phonetics, returning early if any of
     * the given DTMF number are received on the channel.
     * 
     * @param text the text to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    char sayPhonetic(String text, String escapeDigits) throws AGIException;

    /**
     * Says the given character string.
     * 
     * @param text the text to say.
     * @since 0.2
     */
    void sayAlpha(String text) throws AGIException;

    /**
     * Says the given character string, returning early if any of the given DTMF
     * number are received on the channel.
     * 
     * @param text the text to say.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    char sayAlpha(String text, String escapeDigits) throws AGIException;

    /**
     * Says the given time.
     * 
     * @param time the time to say in seconds since 00:00:00 on January 1, 1970.
     * @since 0.2
     */
    void sayTime(long time) throws AGIException;

    /**
     * Says the given time, returning early if any of the given DTMF number are
     * received on the channel.
     * 
     * @param time the time to say in seconds since 00:00:00 on January 1, 1970.
     * @param escapeDigits a String containing the DTMF digits that allow the
     *            user to escape.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    char sayTime(long time, String escapeDigits) throws AGIException;

    /**
     * Returns the value of the current channel variable.
     * 
     * @param name the name of the variable to retrieve.
     * @return the value of the given variable or <code>null</code> if not
     *         set.
     * @since 0.2
     */
    String getVariable(String name) throws AGIException;

    /**
     * Sets the value of the current channel variable to a new value.
     * 
     * @param name the name of the variable to retrieve.
     * @param value the new value to set.
     * @since 0.2
     */
    void setVariable(String name, String value) throws AGIException;

    /**
     * Waits up to 'timeout' milliseconds to receive a DTMF digit.
     * 
     * @param timeout timeout the milliseconds to wait for the channel to
     *            receive a DTMF digit, -1 will wait forever.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    char waitForDigit(int timeout) throws AGIException;

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
    String getFullVariable(String name) throws AGIException;

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
    String getFullVariable(String name, String channel) throws AGIException;

    /**
     * Says the given time.<br>
     * Available since Asterisk 1.2.
     * 
     * @param time the time to say in seconds elapsed since 00:00:00 on January
     *            1, 1970, Coordinated Universal Time (UTC)
     * @since 0.2
     */
    void sayDateTime(long time) throws AGIException;

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
    char sayDateTime(long time, String escapeDigits) throws AGIException;

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
    char sayDateTime(long time, String escapeDigits, String format)
            throws AGIException;

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
    char sayDateTime(long time, String escapeDigits, String format,
            String timezone) throws AGIException;

    /**
     * Retrieves an entry in the Asterisk database for a given family and key.
     *  
     * @param family the family of the entry to retrieve.
     * @param key the key of the entry to retrieve.
     * @return the value of the given family and key or <code>null</code> if there
     *         is no such value.
     * @since 0.3
     */
    String databaseGet(String family, String key) throws AGIException;

    /**
     * Adds or updates an entry in the Asterisk database for a given family, key,
     * and value.
     *  
     * @param family the family of the entry to add or update.
     * @param key the key of the entry to add or update.
     * @param value the new value of the entry.
     * @since 0.3
     */
    void databasePut(String family, String key, String value) throws AGIException;

    /**
     * Deletes an entry in the Asterisk database for a given family and key.
     *  
     * @param family the family of the entry to delete.
     * @param key the key of the entry to delete.
     * @since 0.3
     */
    void databaseDel(String family, String key) throws AGIException;

    /**
     * Deletes a whole family of entries in the Asterisk database.
     *  
     * @param family the family to delete.
     * @since 0.3
     */
    void databaseDelTree(String family) throws AGIException;

    /**
     * Deletes all entries of a given family in the Asterisk database that have a key
     * that starts with a given prefix.
     *  
     * @param family the family of the entries to delete.
     * @param keytree the prefix of the keys of the entries to delete.
     * @since 0.3
     */
    void databaseDelTree(String family, String keytree) throws AGIException;

    /**
     * Sends a message to the Asterisk console via the verbose message system.
     * 
     * @param message the message to send.
     * @param level the verbosity level to use. Must be in [1..4].
     * @since 0.3
     */
    void verbose(String message, int level) throws AGIException;
}
