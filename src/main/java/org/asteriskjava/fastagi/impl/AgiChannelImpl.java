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
package org.asteriskjava.fastagi.impl;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiReader;
import org.asteriskjava.fastagi.AgiWriter;
import org.asteriskjava.fastagi.InvalidCommandSyntaxException;
import org.asteriskjava.fastagi.InvalidOrUnknownCommandException;
import org.asteriskjava.fastagi.command.AgiCommand;
import org.asteriskjava.fastagi.command.AnswerCommand;
import org.asteriskjava.fastagi.command.ChannelStatusCommand;
import org.asteriskjava.fastagi.command.DatabaseDelCommand;
import org.asteriskjava.fastagi.command.DatabaseDelTreeCommand;
import org.asteriskjava.fastagi.command.DatabaseGetCommand;
import org.asteriskjava.fastagi.command.DatabasePutCommand;
import org.asteriskjava.fastagi.command.ExecCommand;
import org.asteriskjava.fastagi.command.GetDataCommand;
import org.asteriskjava.fastagi.command.GetFullVariableCommand;
import org.asteriskjava.fastagi.command.GetOptionCommand;
import org.asteriskjava.fastagi.command.GetVariableCommand;
import org.asteriskjava.fastagi.command.HangupCommand;
import org.asteriskjava.fastagi.command.SayAlphaCommand;
import org.asteriskjava.fastagi.command.SayDateTimeCommand;
import org.asteriskjava.fastagi.command.SayDigitsCommand;
import org.asteriskjava.fastagi.command.SayNumberCommand;
import org.asteriskjava.fastagi.command.SayPhoneticCommand;
import org.asteriskjava.fastagi.command.SayTimeCommand;
import org.asteriskjava.fastagi.command.SetAutoHangupCommand;
import org.asteriskjava.fastagi.command.SetCallerIdCommand;
import org.asteriskjava.fastagi.command.SetContextCommand;
import org.asteriskjava.fastagi.command.SetExtensionCommand;
import org.asteriskjava.fastagi.command.SetMusicOffCommand;
import org.asteriskjava.fastagi.command.SetMusicOnCommand;
import org.asteriskjava.fastagi.command.SetPriorityCommand;
import org.asteriskjava.fastagi.command.SetVariableCommand;
import org.asteriskjava.fastagi.command.StreamFileCommand;
import org.asteriskjava.fastagi.command.VerboseCommand;
import org.asteriskjava.fastagi.command.WaitForDigitCommand;
import org.asteriskjava.fastagi.reply.AgiReply;
import org.asteriskjava.io.SocketConnectionFacade;


/**
 * Default implementation of the AgiChannel interface.
 * 
 * @author srt
 * @version $Id: AgiChannelImpl.java,v 1.9 2006/01/11 16:12:19 srt Exp $
 */
public class AgiChannelImpl implements AgiChannel
{
    private AgiWriter agiWriter;
    private AgiReader agiReader;

    public AgiChannelImpl(SocketConnectionFacade socket)
    {
        this.agiWriter = new AgiWriterImpl(socket);
        this.agiReader = new AgiReaderImpl(socket);
    }

    public AgiChannelImpl(AgiWriter agiWriter, AgiReader agiReader)
    {
        this.agiWriter = agiWriter;
        this.agiReader = agiReader;
    }

    public synchronized AgiReply sendCommand(AgiCommand command)
            throws AgiException
    {
        AgiReply reply;

        agiWriter.sendCommand(command);
        reply = agiReader.readReply();

        if (reply.getStatus() == AgiReply.SC_INVALID_OR_UNKNOWN_COMMAND)
        {
            throw new InvalidOrUnknownCommandException(command.buildCommand());
        }
        if (reply.getStatus() == AgiReply.SC_INVALID_COMMAND_SYNTAX)
        {
            throw new InvalidCommandSyntaxException(reply.getSynopsis(), reply
                    .getUsage());
        }

        return reply;
    }
    
    public void answer() throws AgiException
    {
        sendCommand(new AnswerCommand());
    }

    public void hangup() throws AgiException
    {
        sendCommand(new HangupCommand());
    }

    public void setAutoHangup(int time) throws AgiException
    {
        sendCommand(new SetAutoHangupCommand(time));
    }

    public void setCallerId(String callerId) throws AgiException
    {
        sendCommand(new SetCallerIdCommand(callerId));
    }

    public void playMusicOnHold() throws AgiException
    {
        sendCommand(new SetMusicOnCommand());
    }

    public void playMusicOnHold(String musicOnHoldClass) throws AgiException
    {
        sendCommand(new SetMusicOnCommand(musicOnHoldClass));
    }

    public void stopMusicOnHold() throws AgiException
    {
        sendCommand(new SetMusicOffCommand());
    }

    public int getChannelStatus() throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new ChannelStatusCommand());
        return reply.getResultCode();
    }

    public String getData(String file) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetDataCommand(file));
        return reply.getResult();
    }

    public String getData(String file, long timeout) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetDataCommand(file, timeout));
        return reply.getResult();
    }

    public String getData(String file, long timeout, int maxDigits)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetDataCommand(file, timeout, maxDigits));
        return reply.getResult();
    }

    public char getOption(String file, String escapeDigits)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetOptionCommand(file, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public char getOption(String file, String escapeDigits, int timeout)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetOptionCommand(file, escapeDigits, timeout));
        return reply.getResultCodeAsChar();
    }

    public int exec(String application) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new ExecCommand(application));
        return reply.getResultCode();
    }

    public int exec(String application, String options) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new ExecCommand(application, options));
        return reply.getResultCode();
    }

    public void setContext(String context) throws AgiException
    {
        sendCommand(new SetContextCommand(context));
    }

    public void setExtension(String extension) throws AgiException
    {
        sendCommand(new SetExtensionCommand(extension));
    }

    public void setPriority(String priority) throws AgiException
    {
        sendCommand(new SetPriorityCommand(priority));
    }

    public void streamFile(String file) throws AgiException
    {
        sendCommand(new StreamFileCommand(file));
    }

    public char streamFile(String file, String escapeDigits)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new StreamFileCommand(file, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public void sayDigits(String digits) throws AgiException
    {
        sendCommand(new SayDigitsCommand(digits));
    }

    public char sayDigits(String digits, String escapeDigits)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayDigitsCommand(digits, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public void sayNumber(String number) throws AgiException
    {
        sendCommand(new SayNumberCommand(number));
    }

    public char sayNumber(String number, String escapeDigits)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayNumberCommand(number, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public void sayPhonetic(String text) throws AgiException
    {
        sendCommand(new SayPhoneticCommand(text));
    }

    public char sayPhonetic(String text, String escapeDigits)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayPhoneticCommand(text, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public void sayAlpha(String text) throws AgiException
    {
        sendCommand(new SayAlphaCommand(text));
    }

    public char sayAlpha(String text, String escapeDigits)
            throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayAlphaCommand(text, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public void sayTime(long time) throws AgiException
    {
        sendCommand(new SayTimeCommand(time));
    }

    public char sayTime(long time, String escapeDigits) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayTimeCommand(time, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public String getVariable(String name) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetVariableCommand(name));
        if (reply.getResultCode() != 1)
        {
            return null;
        }
        return reply.getExtra();
    }

    public void setVariable(String name, String value) throws AgiException
    {
        sendCommand(new SetVariableCommand(name, value));
    }

    public char waitForDigit(int timeout) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new WaitForDigitCommand(timeout));
        return reply.getResultCodeAsChar();
    }

    public String getFullVariable(String name) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetFullVariableCommand(name));
        if (reply.getResultCode() != 1)
        {
            return null;
        }
        return reply.getExtra();
    }

    public String getFullVariable(String name, String channel) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new GetFullVariableCommand(name, channel));
        if (reply.getResultCode() != 1)
        {
            return null;
        }
        return reply.getExtra();
    }

    public char sayDateTime(long time, String escapeDigits, String format, String timezone) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayDateTimeCommand(time, escapeDigits, format, timezone));
        return reply.getResultCodeAsChar();
    }

    public char sayDateTime(long time, String escapeDigits, String format) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayDateTimeCommand(time, escapeDigits, format));
        return reply.getResultCodeAsChar();
    }

    public char sayDateTime(long time, String escapeDigits) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new SayDateTimeCommand(time, escapeDigits));
        return reply.getResultCodeAsChar();
    }

    public void sayDateTime(long time) throws AgiException
    {
        sendCommand(new SayDateTimeCommand(time));
    }
    
    public String databaseGet(String family, String key) throws AgiException
    {
        AgiReply reply;

        reply = sendCommand(new DatabaseGetCommand(family, key));
        if (reply.getResultCode() != 1)
        {
            return null;
        }
        return reply.getExtra();
    }

    public void databasePut(String family, String key, String value) throws AgiException
    {
        sendCommand(new DatabasePutCommand(family, key, value));
    }

    public void databaseDel(String family, String key) throws AgiException
    {
        sendCommand(new DatabaseDelCommand(family, key));
    }

    public void databaseDelTree(String family) throws AgiException
    {
        sendCommand(new DatabaseDelTreeCommand(family));
    }

    public void databaseDelTree(String family, String keytree) throws AgiException
    {
        sendCommand(new DatabaseDelTreeCommand(family, keytree));
    }

    public void verbose(String message, int level) throws AgiException
    {
        sendCommand(new VerboseCommand(message, level));
    }
}
