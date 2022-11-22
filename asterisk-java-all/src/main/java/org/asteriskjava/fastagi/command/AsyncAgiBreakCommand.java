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
 * AGI Command <b>ASYNCAGI BREAK</b>
 * <p>
 * Interrupts expected flow of Async AGI commands and returns control to previous source (typically, the PBX dialplan).
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_asyncagi+break">AGI Command ASYNCAGI BREAK (Asterisk 18)</a>
 *
 * @author srt
 */
public class AsyncAgiBreakCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 1L;

    public AsyncAgiBreakCommand() {
        super();
    }

    @Override
    public String buildCommand() {
        return "ASYNCAGI BREAK";
    }
}
