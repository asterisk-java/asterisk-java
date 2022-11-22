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
 * This is not a real AGI command.
 * <p>
 * It uses <code>EXEC</code> command and add <code>queue</code> application features.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_exec">AGI Command EXEC (Asterisk 18)</a><br>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+Application_Queue">Application Queue (Asterisk 18)</a>
 */
public class QueueCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3762248656229053753L;

    private final String queue;

    public QueueCommand(String queue) {
        super();
        this.queue = queue;

    }

    @Override
    public String buildCommand() {
        return "EXEC " + escapeAndQuote("queue") + " " + escapeAndQuote(queue);
    }
}
