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
 * AGI Command: <b>SET CONTEXT</b>
 * <p>
 * Sets channel context.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_set+context">AGI Command SET CONTEXT (Asterisk 18)</a>
 *
 * @author srt
 */
public class SetContextCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The context for continuation upon exiting the application.
     */
    private String context;

    /**
     * Creates a new SetPriorityCommand.
     *
     * @param context the context for continuation upon exiting the application.
     */
    public SetContextCommand(String context) {
        super();
        this.context = context;
    }

    /**
     * Returns the context for continuation upon exiting the application.
     *
     * @return the context for continuation upon exiting the application.
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets the context for continuation upon exiting the application.
     *
     * @param context the context for continuation upon exiting the application.
     */
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String buildCommand() {
        return "SET CONTEXT " + escapeAndQuote(context);
    }
}
