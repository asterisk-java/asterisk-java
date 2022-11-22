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
 * AGI Command: <b>SET EXTENSION</b>
 * <p>
 * Changes channel extension.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_set+extension">AGI Command SET EXTENSION (Asterisk 18)</a>
 *
 * @author srt
 */
public class SetExtensionCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The extension for continuation upon exiting the application.
     */
    private String extension;

    /**
     * Creates a new SetPriorityCommand.
     *
     * @param extension the extension for continuation upon exiting the application.
     */
    public SetExtensionCommand(String extension) {
        super();
        this.extension = extension;
    }

    /**
     * Returns the extension for continuation upon exiting the application.
     *
     * @return the extension for continuation upon exiting the application.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets the extension for continuation upon exiting the application.
     *
     * @param extension the extension for continuation upon exiting the application.
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String buildCommand() {
        return "SET EXTENSION " + escapeAndQuote(extension);
    }
}
