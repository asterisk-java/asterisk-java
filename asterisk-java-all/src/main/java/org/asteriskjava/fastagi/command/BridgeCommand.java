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

import static org.asteriskjava.AsteriskVersion.ASTERISK_13;

/**
 * This is not a real AGI command.
 * <p>
 * It uses <code>EXEC</code> command and add <code>bridge</code> application features.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_exec">AGI Command EXEC (Asterisk 18)</a><br>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+Application_Bridge">Application Bridge (Asterisk 18)</a>
 */
public class BridgeCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3762248656229053753L;

    private final String channel;
    private final String options;

    public BridgeCommand(String channel, String options) {
        super();
        this.channel = channel;
        this.options = options;
    }

    @Override
    public String buildCommand() {
        String command = "EXEC " + escapeAndQuote("bridge") + " " + escapeAndQuote(channel);
        if (options != null && options.length() > 0) {
            if (getAsteriskVersion().isAtLeast(ASTERISK_13)) {
                command += "," + escapeAndQuote(options);
            } else {
                command += "|" + escapeAndQuote(options);
            }
        }

        return command;
    }
}
