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
 * AGI Command: <b>SPEECH LOAD GRAMMAR</b>
 * <p>
 * Loads a grammar.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_speech+load+grammar">AGI Command SPEECH LOAD GRAMMAR (Asterisk 18)</a>
 *
 * @author srt
 * @see org.asteriskjava.fastagi.command.SpeechUnloadGrammarCommand
 * @see org.asteriskjava.fastagi.command.SpeechActivateGrammarCommand
 * @since 1.0.0
 */
public class SpeechLoadGrammarCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 1L;

    private String name;
    private String path;

    /**
     * Creates a new SpeechLoadGrammarCommand that loads the grammar from the given path and
     * makes it available under the given name.
     *
     * @param name the name of the grammar, used to activate the grammar later.
     * @param path the path to the grammar.
     */
    public SpeechLoadGrammarCommand(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Returns the name of the grammar.
     *
     * @return the name of the grammar.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the grammar, used to activate the grammar later.
     *
     * @param name the name of the grammar.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the path to the grammar.
     *
     * @return the path to the grammar.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path to the grammar.
     *
     * @param path the path to the grammar.
     */
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String buildCommand() {
        return "SPEECH LOAD GRAMMAR " + escapeAndQuote(name) + " " + escapeAndQuote(path);
    }
}
