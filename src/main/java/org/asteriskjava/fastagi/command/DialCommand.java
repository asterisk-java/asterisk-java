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

import org.asteriskjava.util.Log;

import static org.asteriskjava.AsteriskVersion.ASTERISK_10;
import static org.asteriskjava.util.LogFactory.getLog;

/**
 * This is not a real AGI command.
 * <p>
 * It uses <code>EXEC</code> command and add <code>dial</code> application features.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_exec">AGI Command EXEC (Asterisk 18)</a><br>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+Application_Dial">Application Dial (Asterisk 18)</a>
 */
public class DialCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3762248656229053753L;

    private final Log logger = getLog(getClass());

    private final String target;
    private final int timeout;
    private final String options;

    public DialCommand(String target, int timeout, String options) {
        super();
        this.target = target;
        this.timeout = timeout;
        this.options = options;
    }

    @Override
    public String buildCommand() {

        String separator = "|";
        if (getAsteriskVersion().isAtLeast(ASTERISK_10)) {
            separator = ",";
        }

        String command = "EXEC " + escapeAndQuote("dial") + " " + escapeAndQuote(target) + separator + escapeAndQuote("" + timeout);
        if (options != null && options.length() > 0) {
            command += separator + escapeAndQuote(options);
        }

        logger.info(command);

        return command;
    }
}
