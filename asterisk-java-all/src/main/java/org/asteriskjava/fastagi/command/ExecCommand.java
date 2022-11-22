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

import static java.lang.String.format;

/**
 * AGI Command: <b>EXEC</b>
 * <p>
 * Executes an <code>application</code> with the given <code>options</code>.<br>
 * Returns whatever the <code>application</code> returns, or -2 if the <code>application</code> was not found.
 * <p>
 * See: <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+AGICommand_exec">EXEC (Asterisk 18)</a>
 *
 * @author srt
 */
public class ExecCommand extends AbstractAgiCommand {
    private static final long serialVersionUID = 3904959746380281145L;

    /**
     * The name of the application to execute.
     */
    private String application;

    /**
     * The options to pass to the application.
     */
    private String[] options;

    public ExecCommand(String application) {
        super();
        this.application = application;
    }

    public ExecCommand(String application, String... options) {
        super();
        this.application = application;
        this.options = options;
    }

    /**
     * Returns the name of the application to execute.
     *
     * @return the name of the application to execute.
     */
    public String getApplication() {
        return application;
    }

    /**
     * Sets the name of the application to execute.
     *
     * @param application the name of the application to execute.
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * Returns the options to pass to the application.
     *
     * @return the options to pass to the application.
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * Sets the options to pass to the application.
     *
     * @param options the options to pass to the application.
     */
    public void setOptions(String... options) {
        this.options = options;
    }

    @Override
    public String buildCommand() {
        return format("EXEC %s %s", escapeAndQuote(application), escapeAndQuote(options));
    }
}
