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
 * AGI Command: <b>SPEECH DESTROY</b>
 * <p>
 * Destroys a speech object.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_speech+destroy">AGI Command SPEECH DESTROY (Asterisk 18)</a>
 *
 * @author srt
 * @see org.asteriskjava.fastagi.command.SpeechCreateCommand
 * @since 1.0.0
 */
public class SpeechDestroyCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new empty SpeechDestroyCommand.
     */
    public SpeechDestroyCommand() {
        super();
    }

    @Override
    public String buildCommand() {
        return "SPEECH DESTROY";
    }
}
