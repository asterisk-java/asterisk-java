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
 * AGI Command: <b>HANGUP</b>
 * <p>
 * Hangs up the specified channel. If no channel name is given, hangs up the current channel.
 * <p>
 * See: <a hres="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_hangup">AGI Command HANGUP (Asterisk 18)</a>
 *
 * @author srt
 */
public class HangupCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3904959746380281145L;

    /**
     * The name of the channel to hangup or <code>null</code> for the current channel.
     */
    private String channel;

    /**
     * Creates a new HangupCommand that hangs up the current channel.
     */
    public HangupCommand() {
        super();
    }

    /**
     * Creates a new HangupCommand that hangs up the given channel.
     *
     * @param channel the name of the channel to hangup.
     */
    public HangupCommand(String channel) {
        super();
        this.channel = channel;
    }

    /**
     * Returns the name of the channel to hangup.
     *
     * @return the name of the channel to hangup or <code>null</code> for the current channel.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the name of the channel to hangup.
     *
     * @param channel the name of the channel to hangup or <code>null</code> for the current channel.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String buildCommand() {
        return "HANGUP" + (channel == null ? "" : " " + escapeAndQuote(channel));
    }
}
