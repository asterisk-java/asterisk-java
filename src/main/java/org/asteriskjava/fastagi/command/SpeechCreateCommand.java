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
 * AGI Command: <b>SPEECH CREATE</b>
 * <p>
 * Creates a speech object.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_speech+create">AGI Command SPEECH CREATE (Asterisk 18)</a>
 *
 * @author srt
 * @see org.asteriskjava.fastagi.command.SpeechDestroyCommand
 * @see org.asteriskjava.fastagi.command.SpeechLoadGrammarCommand
 * @since 1.0.0
 */
public class SpeechCreateCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 1L;

    private String engine;

    /**
     * Creates a new SpeechCreateCommand for the given engine.
     *
     * @param engine the name of the speech engine to use for subsequent Speech AGI commands.
     */
    public SpeechCreateCommand(String engine) {
        this.engine = engine;
    }

    /**
     * Returns the name of the speech engine to use for subsequent Speech AGI commands.
     *
     * @return the name of the speech engine to use for subsequent Speech AGI commands.
     */
    public String getEngine() {
        return engine;
    }

    /**
     * Sets the name of the speech engine to use for subsequent Speech AGI commands.
     *
     * @param engine the name of the speech engine to use for subsequent Speech AGI commands.
     */
    public void setEngine(String engine) {
        this.engine = engine;
    }

    @Override
    public String buildCommand() {
        return "SPEECH CREATE " + escapeAndQuote(engine);
    }
}
