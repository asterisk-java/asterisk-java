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
package org.asteriskjava.fastagi;

import org.asteriskjava.fastagi.command.AgiCommand;
import org.asteriskjava.fastagi.command.AnswerCommand;
import org.asteriskjava.fastagi.internal.AgiConnectionHandler;
import org.asteriskjava.fastagi.reply.AgiReply;

/**
 * AgiOperations provides some convinience methods that wrap the various
 * {@link AgiCommand AgiCommands}.
 * 
 * @since 0.3
 * @author srt
 * @version $Id$
 */
public class AgiOperations
{
    private final AgiChannel channel;

    /**
     * Creates a new instance that operates on the channel attached to the
     * current thread.
     */
    public AgiOperations()
    {
        this.channel = null;
    }

    /**
     * Creates a new instance that operates on the given channel.
     * 
     * @param channel the channel to operate on.
     */
    public AgiOperations(AgiChannel channel)
    {
        this.channel = channel;
    }

    /**
     * Answers the channel.
     */
    public void answer() throws AgiException
    {
        sendCommand(new AnswerCommand());
    }

    /**
     * Hangs the channel up.
     */
    public void hangup() throws AgiException
    {
        getChannel().hangup();
    }

    /**
     * Cause the channel to automatically hangup at the given number of seconds
     * in the future.
     * 
     * @param time the number of seconds before this channel is automatically
     *            hung up.
     *            <p>
     *            0 disables the autohangup feature.
     */
    public void setAutoHangup(int time) throws AgiException
    {
        getChannel().setAutoHangup(time);
    }

    /**
     * Sets the caller id on the current channel.
     * 
     * @param callerId the raw caller id to set, for example "John Doe<1234>".
     */
    public void setCallerId(String callerId) throws AgiException
    {
        getChannel().setCallerId(callerId);
    }

    /**
     * Plays music on hold from the default music on hold class.
     */
    public void playMusicOnHold() throws AgiException
    {
        getChannel().playMusicOnHold();
    }

    /**
     * Plays music on hold from the given music on hold class.
     * 
     * @param musicOnHoldClass the music on hold class to play music from as
     *            configures in Asterisk's <code><musiconhold.conf</code>.
     */
    public void playMusicOnHold(String musicOnHoldClass) throws AgiException
    {
        getChannel().playMusicOnHold(musicOnHoldClass);
    }

    /**
     * Stops playing music on hold.
     */
    public void stopMusicOnHold() throws AgiException
    {
        getChannel().stopMusicOnHold();
    }

    /**
     * Returns the status of the channel.
     * <p>
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
    public int getChannelStatus() throws AgiException
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
    public String getData(String file) throws AgiException
    {
        return getChannel().getData(file);
    }

    /**
     * Plays the given file and waits for the user to enter DTMF digits until he
     * presses '#' or the timeout occurs. The user may interrupt the streaming
     * by starting to enter digits.
     * 
     * @param file the name of the file to play
     * @param timeout the timeout in milliseconds to wait for user input.
     *            <p>
     *            0 means standard timeout value, -1 means "ludicrous time"
     *            (essentially never times out).
     * @return a String containing the DTMF the user entered
     */
    public String getData(String file, int timeout) throws AgiException
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
     * @param timeout the timeout in milliseconds to wait for user input.
     *            <p>
     *            0 means standard timeout value, -1 means "ludicrous time"
     *            (essentially never times out).
     * @param maxDigits the maximum number of digits the user is allowed to
     *            enter
     * @return a String containing the DTMF the user entered
     */
    public String getData(String file, int timeout, int maxDigits) throws AgiException
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
    public char getOption(String file, String escapeDigits) throws AgiException
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
    public char getOption(String file, String escapeDigits, int timeout) throws AgiException
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
    public int exec(String application) throws AgiException
    {
        return getChannel().exec(application);
    }

    /**
     * Executes the given command.
     * 
     * @param application the name of the application to execute, for example
     *            "Dial".
     * @param options the parameters to pass to the application, for example
     *            "SIP/123". Multiple options are separated by the pipe
     *            character ('|').
     * @return the return code of the application of -2 if the application was
     *         not found.
     */
    public int exec(String application, String options) throws AgiException
    {
        return getChannel().exec(application, options);
    }

    /**
     * Sets the context for continuation upon exiting the application.
     * 
     * @param context the context for continuation upon exiting the application.
     */
    public void setContext(String context) throws AgiException
    {
        getChannel().setContext(context);
    }

    /**
     * Sets the extension for continuation upon exiting the application.
     * 
     * @param extension the extension for continuation upon exiting the
     *            application.
     */
    public void setExtension(String extension) throws AgiException
    {
        getChannel().setExtension(extension);
    }

    /**
     * Sets the priority or label for continuation upon exiting the application.
     * 
     * @param priority the priority or label for continuation upon exiting the
     *            application.
     */
    public void setPriority(String priority) throws AgiException
    {
        getChannel().setPriority(priority);
    }

    /**
     * Plays the given file.
     * 
     * @param file name of the file to play.
     */
    public void streamFile(String file) throws AgiException
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
    public char streamFile(String file, String escapeDigits) throws AgiException
    {
        return getChannel().streamFile(file, escapeDigits);
    }

    /**
     * Says the given digit string.
     * 
     * @param digits the digit string to say.
     */
    public void sayDigits(String digits) throws AgiException
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
    public char sayDigits(String digits, String escapeDigits) throws AgiException
    {
        return getChannel().sayDigits(digits, escapeDigits);
    }

    /**
     * Says the given number.
     * 
     * @param number the number to say.
     */
    public void sayNumber(String number) throws AgiException
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
    public char sayNumber(String number, String escapeDigits) throws AgiException
    {
        return getChannel().sayNumber(number, escapeDigits);
    }

    /**
     * Says the given character string with phonetics.
     * 
     * @param text the text to say.
     */
    public void sayPhonetic(String text) throws AgiException
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
    public char sayPhonetic(String text, String escapeDigits) throws AgiException
    {
        return getChannel().sayPhonetic(text, escapeDigits);
    }

    /**
     * Says the given character string.
     * 
     * @param text the text to say.
     */
    public void sayAlpha(String text) throws AgiException
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
    public char sayAlpha(String text, String escapeDigits) throws AgiException
    {
        return getChannel().sayAlpha(text, escapeDigits);
    }

    /**
     * Says the given time.
     * 
     * @param time the time to say in seconds since 00:00:00 on January 1, 1970.
     */
    public void sayTime(long time) throws AgiException
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
    public char sayTime(long time, String escapeDigits) throws AgiException
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
    public String getVariable(String name) throws AgiException
    {
        return getChannel().getVariable(name);
    }

    /**
     * Sets the value of the given channel variable to a new value.
     * 
     * @param name the name of the variable to retrieve.
     * @param value the new value to set.
     */
    public void setVariable(String name, String value) throws AgiException
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
    public char waitForDigit(int timeout) throws AgiException
    {
        return getChannel().waitForDigit(timeout);
    }

    /**
     * Returns the value of the current channel variable, unlike getVariable()
     * this method understands complex variable names and builtin variables.
     * <p>
     * You can also use this method to use custom Asterisk functions. Syntax is
     * "func(args)".
     * <p>
     * Available since Asterisk 1.2.
     * 
     * @param name the name of the variable to retrieve.
     * @return the value of the given variable or <code>null</code> if not
     *         set.
     * @since 0.2
     */
    public String getFullVariable(String name) throws AgiException
    {
        return getChannel().getFullVariable(name);
    }

    /**
     * Returns the value of the given channel variable.
     * <p>
     * Available since Asterisk 1.2.
     * 
     * @param name the name of the variable to retrieve.
     * @param channel the name of the channel.
     * @return the value of the given variable or <code>null</code> if not
     *         set.
     * @since 0.2
     */
    public String getFullVariable(String name, String channel) throws AgiException
    {
        return getChannel().getFullVariable(name, channel);
    }

    /**
     * Says the given time.
     * <p>
     * Available since Asterisk 1.2.
     * 
     * @param time the time to say in seconds elapsed since 00:00:00 on January
     *            1, 1970, Coordinated Universal Time (UTC)
     * @since 0.2
     */
    public void sayDateTime(long time) throws AgiException
    {
        getChannel().sayDateTime(time);
    }

    /**
     * Says the given time and allows interruption by one of the given escape
     * digits.
     * <p>
     * Available since Asterisk 1.2.
     * 
     * @param time the time to say in seconds elapsed since 00:00:00 on January
     *            1, 1970, Coordinated Universal Time (UTC)
     * @param escapeDigits the digits that allow the user to interrupt this
     *            command or <code>null</code> for none.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.2
     */
    public char sayDateTime(long time, String escapeDigits) throws AgiException
    {
        return getChannel().sayDateTime(time, escapeDigits);
    }

    /**
     * Says the given time in the given format and allows interruption by one of
     * the given escape digits.
     * <p>
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
    public char sayDateTime(long time, String escapeDigits, String format) throws AgiException
    {
        return getChannel().sayDateTime(time, escapeDigits, format);
    }

    /**
     * Says the given time in the given format and timezone and allows
     * interruption by one of the given escape digits.
     * <p>
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
    public char sayDateTime(long time, String escapeDigits, String format, String timezone) throws AgiException
    {
        return getChannel().sayDateTime(time, escapeDigits, format, timezone);
    }

    /**
     * Retrieves an entry in the Asterisk database for a given family and key.
     * 
     * @param family the family of the entry to retrieve.
     * @param key the key of the entry to retrieve.
     * @return the value of the given family and key or <code>null</code> if
     *         there is no such value.
     * @since 0.3
     */
    public String databaseGet(String family, String key) throws AgiException
    {
        return getChannel().databaseGet(family, key);
    }

    /**
     * Adds or updates an entry in the Asterisk database for a given family,
     * key, and value.
     * 
     * @param family the family of the entry to add or update.
     * @param key the key of the entry to add or update.
     * @param value the new value of the entry.
     * @since 0.3
     */
    public void databasePut(String family, String key, String value) throws AgiException
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
    public void databaseDel(String family, String key) throws AgiException
    {
        getChannel().databaseDel(family, key);
    }

    /**
     * Deletes a whole family of entries in the Asterisk database.
     * 
     * @param family the family to delete.
     * @since 0.3
     */
    public void databaseDelTree(String family) throws AgiException
    {
        getChannel().databaseDelTree(family);
    }

    /**
     * Deletes all entries of a given family in the Asterisk database that have
     * a key that starts with a given prefix.
     * 
     * @param family the family of the entries to delete.
     * @param keytree the prefix of the keys of the entries to delete.
     * @since 0.3
     */
    public void databaseDelTree(String family, String keytree) throws AgiException
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
    public void verbose(String message, int level) throws AgiException
    {
        getChannel().verbose(message, level);
    }

    /**
     * Record to a file until a given dtmf digit in the sequence is received.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param format the format of the file to be recorded, for example "wav".
     * @param escapeDigits contains the digits that allow the user to end
     *            recording.
     * @param timeout the maximum record time in milliseconds, or -1 for no
     *            timeout.
     * @since 0.3
     */
    public void recordFile(String file, String format, String escapeDigits, int timeout) throws AgiException
    {
        getChannel().recordFile(file, format, escapeDigits, timeout);
    }

    /**
     * Record to a file until a given dtmf digit in the sequence is received.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param format the format of the file to be recorded, for example "wav".
     * @param escapeDigits contains the digits that allow the user to end
     *            recording.
     * @param timeout the maximum record time in milliseconds, or -1 for no
     *            timeout.
     * @param offset the offset samples to skip.
     * @param beep <code>true</code> if a beep should be played before
     *            recording.
     * @param maxSilence The amount of silence (in seconds) to allow before
     *            returning despite the lack of dtmf digits or reaching timeout.
     * @since 0.3
     */
    public void recordFile(String file, String format, String escapeDigits, int timeout, int offset, boolean beep,
            int maxSilence) throws AgiException
    {
        getChannel().recordFile(file, format, escapeDigits, timeout, offset, beep, maxSilence);
    }

    /**
     * Plays the given file allowing the user to control the streaming by using
     * "#" for forward and "*" for rewind.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @since 0.3
     */
    public void controlStreamFile(String file) throws AgiException
    {
        getChannel().controlStreamFile(file);
    }

    /**
     * Plays the given file allowing the user to control the streaming by using
     * "#" for forward and "*" for rewind. Pressing one of the escape digits
     * stops streaming.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that allow the user to interrupt
     *            this command.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.3
     */
    public char controlStreamFile(String file, String escapeDigits) throws AgiException
    {
        return getChannel().controlStreamFile(file, escapeDigits);
    }

    /**
     * Plays the given file allowing the user to control the streaming by using
     * "#" for forward and "*" for rewind. Pressing one of the escape digits
     * stops streaming. The file is played starting at the indicated offset.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that allow the user to interrupt
     *            this command. May be <code>null</code> if you don't want the
     *            user to interrupt.
     * @param offset the offset samples to skip before streaming.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.3
     */
    public char controlStreamFile(String file, String escapeDigits, int offset) throws AgiException
    {
        return getChannel().controlStreamFile(file, escapeDigits, offset);
    }

    /**
     * Plays the given file allowing the user to control the streaming by using
     * forwardDigit for forward, rewindDigit for rewind and pauseDigit for
     * pause. Pressing one of the escape digits stops streaming. The file is
     * played starting at the indicated offset, use 0 to start at the beginning.
     * 
     * @param file the name of the file to stream, must not include extension.
     * @param escapeDigits contains the digits that allow the user to interrupt
     *            this command. May be <code>null</code> if you don't want the
     *            user to interrupt.
     * @param offset the offset samples to skip before streaming, use 0 to start
     *            at the beginning.
     * @param forwardDigit the digit for fast forward.
     * @param rewindDigit the digit for rewind.
     * @param pauseDigit the digit for pause and unpause.
     * @return the DTMF digit pressed or 0x0 if none was pressed.
     * @since 0.3
     */
    public char controlStreamFile(String file, String escapeDigits, int offset, String forwardDigit, String rewindDigit,
            String pauseDigit) throws AgiException
    {
        return getChannel().controlStreamFile(file, escapeDigits, offset, forwardDigit, rewindDigit, pauseDigit);
    }

    /**
     * Sends the given command to the channel attached to the current thread.
     * 
     * @param command the command to send to Asterisk
     * @return the reply received from Asterisk
     * @throws AgiException if the command could not be processed properly
     */
    public AgiReply sendCommand(AgiCommand command) throws AgiException
    {
        return getChannel().sendCommand(command);
    }

    /**
     * Returns the channel to operate on.
     * 
     * @return the channel to operate on.
     * @throws IllegalStateException if no {@link AgiChannel} is bound to the
     *             current channel and no channel has been passed to the
     *             constructor.
     */
    protected AgiChannel getChannel()
    {
        AgiChannel threadBoundChannel;

        if (channel != null)
        {
            return channel;
        }

        threadBoundChannel = AgiConnectionHandler.getChannel();
        if (threadBoundChannel == null)
        {
            throw new IllegalStateException("Trying to send command from an invalid thread");
        }

        return threadBoundChannel;
    }
}
