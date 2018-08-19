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
package org.asteriskjava.fastagi.internal;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.fastagi.AgiReader;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiSpeechException;
import org.asteriskjava.fastagi.AgiWriter;
import org.asteriskjava.fastagi.InvalidCommandSyntaxException;
import org.asteriskjava.fastagi.InvalidOrUnknownCommandException;
import org.asteriskjava.fastagi.SpeechRecognitionResult;
import org.asteriskjava.fastagi.command.AgiCommand;
import org.asteriskjava.fastagi.command.AnswerCommand;
import org.asteriskjava.fastagi.command.BridgeCommand;
import org.asteriskjava.fastagi.command.ChannelStatusCommand;
import org.asteriskjava.fastagi.command.ConfbridgeCommand;
import org.asteriskjava.fastagi.command.ControlStreamFileCommand;
import org.asteriskjava.fastagi.command.DatabaseDelCommand;
import org.asteriskjava.fastagi.command.DatabaseDelTreeCommand;
import org.asteriskjava.fastagi.command.DatabaseGetCommand;
import org.asteriskjava.fastagi.command.DatabasePutCommand;
import org.asteriskjava.fastagi.command.DialCommand;
import org.asteriskjava.fastagi.command.ExecCommand;
import org.asteriskjava.fastagi.command.GetDataCommand;
import org.asteriskjava.fastagi.command.GetFullVariableCommand;
import org.asteriskjava.fastagi.command.GetOptionCommand;
import org.asteriskjava.fastagi.command.GetVariableCommand;
import org.asteriskjava.fastagi.command.GosubCommand;
import org.asteriskjava.fastagi.command.HangupCommand;
import org.asteriskjava.fastagi.command.MeetmeCommand;
import org.asteriskjava.fastagi.command.QueueCommand;
import org.asteriskjava.fastagi.command.RecordFileCommand;
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
import org.asteriskjava.fastagi.command.SpeechActivateGrammarCommand;
import org.asteriskjava.fastagi.command.SpeechCreateCommand;
import org.asteriskjava.fastagi.command.SpeechDeactivateGrammarCommand;
import org.asteriskjava.fastagi.command.SpeechDestroyCommand;
import org.asteriskjava.fastagi.command.SpeechLoadGrammarCommand;
import org.asteriskjava.fastagi.command.SpeechRecognizeCommand;
import org.asteriskjava.fastagi.command.SpeechSetCommand;
import org.asteriskjava.fastagi.command.SpeechUnloadGrammarCommand;
import org.asteriskjava.fastagi.command.StreamFileCommand;
import org.asteriskjava.fastagi.command.VerboseCommand;
import org.asteriskjava.fastagi.command.WaitForDigitCommand;
import org.asteriskjava.fastagi.reply.AgiReply;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Default implementation of the AgiChannel interface.
 *
 * @author srt
 * @version $Id$
 */
public class AgiChannelImpl implements AgiChannel
{
    private final AgiRequest request;
    private final AgiWriter agiWriter;
    private final AgiReader agiReader;
    private AsteriskVersion asteriskVersion;

    private static Log logger = LogFactory.getLog(AgiChannelImpl.class);

    private AgiReply lastReply;

    protected AgiChannelImpl(AgiRequest request, AgiWriter agiWriter, AgiReader agiReader)
    {
        this.request = request;
        this.agiWriter = agiWriter;
        this.agiReader = agiReader;
        this.lastReply = null;
    }

    public String getName()
    {
        return request.getChannel();
    }

    public String getUniqueId()
    {
        return request.getUniqueId();
    }

    public AgiReply getLastReply()
    {
        return lastReply;
    }

    public synchronized AgiReply sendCommand(AgiCommand command) throws AgiException
    {
        // make the Asterisk Version available to the AgiCommand, with out
        // causing a major refactor
        command.setAsteriskVersion(getAsteriskVersion());
        agiWriter.sendCommand(command);
        lastReply = agiReader.readReply();

        if (lastReply.getStatus() == AgiReply.SC_INVALID_OR_UNKNOWN_COMMAND)
        {
            throw new InvalidOrUnknownCommandException(command.buildCommand());
        }
        if (lastReply.getStatus() == AgiReply.SC_DEAD_CHANNEL)
        {
            throw new AgiHangupException();
        }
        if (lastReply.getStatus() == AgiReply.SC_INVALID_COMMAND_SYNTAX)
        {
            throw new InvalidCommandSyntaxException(lastReply.getSynopsis(), lastReply.getUsage());
        }

        return lastReply;
    }

    public AsteriskVersion getAsteriskVersion()
    {
        if (request == null)
        {
            logger.warn("Request is null, I assume you're testing...");
            return AsteriskVersion.DEFAULT_VERSION;
        }
        if (asteriskVersion == null)
        {
            asteriskVersion = request.getAsteriskVersion();
        }
        return asteriskVersion;
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
        sendCommand(new ChannelStatusCommand());
        return lastReply.getResultCode();
    }

    public String getData(String file) throws AgiException
    {
        sendCommand(new GetDataCommand(file));
        return lastReply.getResult();
    }

    public String getData(String file, long timeout) throws AgiException
    {
        sendCommand(new GetDataCommand(file, timeout));
        return lastReply.getResult();
    }

    public String getData(String file, long timeout, int maxDigits) throws AgiException
    {
        sendCommand(new GetDataCommand(file, timeout, maxDigits));
        return lastReply.getResult();
    }

    public char getOption(String file, String escapeDigits) throws AgiException
    {
        sendCommand(new GetOptionCommand(file, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public char getOption(String file, String escapeDigits, long timeout) throws AgiException
    {
        sendCommand(new GetOptionCommand(file, escapeDigits, timeout));
        return lastReply.getResultCodeAsChar();
    }

    public int exec(String application) throws AgiException
    {
        sendCommand(new ExecCommand(application));
        return lastReply.getResultCode();
    }

    public int exec(String application, String... options) throws AgiException
    {
        sendCommand(new ExecCommand(application, options));
        return lastReply.getResultCode();
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

    public char streamFile(String file, String escapeDigits) throws AgiException
    {
        sendCommand(new StreamFileCommand(file, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public char streamFile(String file, String escapeDigits, int offset) throws AgiException
    {
        sendCommand(new StreamFileCommand(file, escapeDigits, offset));
        return lastReply.getResultCodeAsChar();
    }

    public void sayDigits(String digits) throws AgiException
    {
        sendCommand(new SayDigitsCommand(digits));
    }

    public char sayDigits(String digits, String escapeDigits) throws AgiException
    {
        sendCommand(new SayDigitsCommand(digits, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public void sayNumber(String number) throws AgiException
    {
        sendCommand(new SayNumberCommand(number));
    }

    public char sayNumber(String number, String escapeDigits) throws AgiException
    {
        sendCommand(new SayNumberCommand(number, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public void sayPhonetic(String text) throws AgiException
    {
        sendCommand(new SayPhoneticCommand(text));
    }

    public char sayPhonetic(String text, String escapeDigits) throws AgiException
    {
        sendCommand(new SayPhoneticCommand(text, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public void sayAlpha(String text) throws AgiException
    {
        sendCommand(new SayAlphaCommand(text));
    }

    public char sayAlpha(String text, String escapeDigits) throws AgiException
    {
        sendCommand(new SayAlphaCommand(text, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public void sayTime(long time) throws AgiException
    {
        sendCommand(new SayTimeCommand(time));
    }

    public char sayTime(long time, String escapeDigits) throws AgiException
    {
        sendCommand(new SayTimeCommand(time, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public String getVariable(String name) throws AgiException
    {
        sendCommand(new GetVariableCommand(name));
        if (lastReply.getResultCode() != 1)
        {
            return null;
        }
        return lastReply.getExtra();
    }

    public void setVariable(String name, String value) throws AgiException
    {
        sendCommand(new SetVariableCommand(name, value));
    }

    public char waitForDigit(int timeout) throws AgiException
    {
        sendCommand(new WaitForDigitCommand(timeout));
        return lastReply.getResultCodeAsChar();
    }

    public String getFullVariable(String name) throws AgiException
    {
        sendCommand(new GetFullVariableCommand(name));
        if (lastReply.getResultCode() != 1)
        {
            return null;
        }
        return lastReply.getExtra();
    }

    public String getFullVariable(String name, String channel) throws AgiException
    {
        sendCommand(new GetFullVariableCommand(name, channel));
        if (lastReply.getResultCode() != 1)
        {
            return null;
        }
        return lastReply.getExtra();
    }

    public char sayDateTime(long time, String escapeDigits, String format, String timezone) throws AgiException
    {
        sendCommand(new SayDateTimeCommand(time, escapeDigits, format, timezone));
        return lastReply.getResultCodeAsChar();
    }

    public char sayDateTime(long time, String escapeDigits, String format) throws AgiException
    {
        sendCommand(new SayDateTimeCommand(time, escapeDigits, format));
        return lastReply.getResultCodeAsChar();
    }

    public char sayDateTime(long time, String escapeDigits) throws AgiException
    {
        sendCommand(new SayDateTimeCommand(time, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public void sayDateTime(long time) throws AgiException
    {
        sendCommand(new SayDateTimeCommand(time));
    }

    public String databaseGet(String family, String key) throws AgiException
    {
        sendCommand(new DatabaseGetCommand(family, key));
        if (lastReply.getResultCode() != 1)
        {
            return null;
        }
        return lastReply.getExtra();
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

    public char recordFile(String file, String format, String escapeDigits, int timeout) throws AgiException
    {
        sendCommand(new RecordFileCommand(file, format, escapeDigits, timeout));
        return lastReply.getResultCodeAsChar();
    }

    public char recordFile(String file, String format, String escapeDigits, int timeout, int offset, boolean beep,
            int maxSilence) throws AgiException
    {
        sendCommand(new RecordFileCommand(file, format, escapeDigits, timeout, offset, beep, maxSilence));
        return lastReply.getResultCodeAsChar();
    }

    public void controlStreamFile(String file) throws AgiException
    {
        sendCommand(new ControlStreamFileCommand(file));
    }

    public char controlStreamFile(String file, String escapeDigits) throws AgiException
    {
        sendCommand(new ControlStreamFileCommand(file, escapeDigits));
        return lastReply.getResultCodeAsChar();
    }

    public char controlStreamFile(String file, String escapeDigits, int offset) throws AgiException
    {
        sendCommand(new ControlStreamFileCommand(file, escapeDigits, offset));
        return lastReply.getResultCodeAsChar();
    }

    public char controlStreamFile(String file, String escapeDigits, int offset, String forwardDigit, String rewindDigit,
            String pauseDigit) throws AgiException
    {
        sendCommand(new ControlStreamFileCommand(file, escapeDigits, offset, forwardDigit, rewindDigit, pauseDigit));
        return lastReply.getResultCodeAsChar();
    }

    public void speechCreate() throws AgiException
    {
        speechCreate("");
    }

    public void speechCreate(String engine) throws AgiException
    {
        sendCommand(new SpeechCreateCommand(engine));
        if (lastReply.getResultCode() != 1)
        {
            if (engine == null || "".equals(engine))
            {
                throw new AgiSpeechException("Speech object for default engine cannot be created");
            }
            throw new AgiSpeechException("Speech object for engine '" + engine + "' cannot be created");
        }
    }

    public void speechSet(String name, String value) throws AgiException
    {
        sendCommand(new SpeechSetCommand(name, value));
        if (lastReply.getResultCode() != 1)
        {
            throw new AgiSpeechException("Setting '" + name + "' cannot be set to '" + value + "'");
        }
    }

    public void speechDestroy() throws AgiException
    {
        sendCommand(new SpeechDestroyCommand());
        if (lastReply.getResultCode() != 1)
        {
            throw new AgiSpeechException("Speech object cannot be destroyed");
        }
    }

    public void speechLoadGrammar(String name, String path) throws AgiException
    {
        sendCommand(new SpeechLoadGrammarCommand(name, path));
        if (lastReply.getResultCode() != 1)
        {
            throw new AgiSpeechException("Grammar '" + name + "' cannot be loaded from '" + path + "'");
        }
    }

    public void speechUnloadGrammar(String name) throws AgiException
    {
        sendCommand(new SpeechUnloadGrammarCommand(name));
        if (lastReply.getResultCode() != 1)
        {
            throw new AgiSpeechException("Grammar '" + name + "' cannot be unloaded");
        }
    }

    public void speechActivateGrammar(String name) throws AgiException
    {
        sendCommand(new SpeechActivateGrammarCommand(name));
        if (lastReply.getResultCode() != 1)
        {
            throw new AgiSpeechException("Grammar '" + name + "' cannot be activated");
        }
    }

    public void speechDeactivateGrammar(String name) throws AgiException
    {
        sendCommand(new SpeechDeactivateGrammarCommand(name));
        if (lastReply.getResultCode() != 1)
        {
            throw new AgiSpeechException("Grammar '" + name + "' cannot be deactivated");
        }
    }

    public SpeechRecognitionResult speechRecognize(String prompt, int timeout) throws AgiException
    {
        return speechRecognize(new SpeechRecognizeCommand(prompt, timeout));
    }

    public SpeechRecognitionResult speechRecognize(String prompt, int timeout, int offset) throws AgiException
    {
        return speechRecognize(new SpeechRecognizeCommand(prompt, timeout, offset));
    }

    private SpeechRecognitionResult speechRecognize(SpeechRecognizeCommand command) throws AgiException
    {
        sendCommand(command);
        if (lastReply.getResultCode() != 1)
        {
            throw new AgiSpeechException("Cannot recognize speech");
        }

        if ("hangup".equals(lastReply.getExtra()))
        {
            throw new AgiHangupException();
        }
        final AgiReply speechRecognizeReply = lastReply;
        return new SpeechRecognitionResult(speechRecognizeReply);
    }

    public void continueAt(String context, String extension, String priority) throws AgiException
    {
        setContext(context);
        setExtension(extension);
        setPriority(priority);
    }

    public void gosub(String context, String extension, String priority) throws AgiException
    {
        sendCommand(new GosubCommand(context, extension, priority));
    }

    public void gosub(String context, String extension, String priority, String... arguments) throws AgiException
    {
        sendCommand(new GosubCommand(context, extension, priority, arguments));
    }

    public AgiRequest getInternalAgiRequest()
    {
        return request;
    }

    @Override
    public void confbridge(String room, String profile) throws AgiException
    {
        sendCommand(new ConfbridgeCommand(room, profile));

    }

    @Override
    public void meetme(String room, String options) throws AgiException
    {
        sendCommand(new MeetmeCommand(room, options));

    }

    @Override
    public void dial(String target, int timeout, String options) throws AgiException
    {
        sendCommand(new DialCommand(target, timeout, options));

    }

    @Override
    public void bridge(String channelName, String options) throws AgiException
    {
        sendCommand(new BridgeCommand(channelName, options));

    }

    @Override
    public void queue(String queue) throws AgiException
    {
        sendCommand(new QueueCommand(queue));

    }
}
