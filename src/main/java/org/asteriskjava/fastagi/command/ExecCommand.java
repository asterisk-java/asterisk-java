/*
 *  Copyright  2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.fastagi.command;

/**
 * Executes an application with the given options.<br>
 * Returns whatever the application returns, or -2 if the application was not
 * found.
 * 
 * @author srt
 * @version $Id: ExecCommand.java,v 1.6 2006/01/12 10:35:13 srt Exp $
 */
public class ExecCommand extends AbstractAGICommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3904959746380281145L;

    /**
     * The name of the application to execute.
     */
    private String application;

    /**
     * The options to pass to the application.
     */
    private String options;

    /**
     * Creates a new ExecCommand.
     * 
     * @param application the name of the application to execute.
     */
    public ExecCommand(String application)
    {
        this.application = application;
    }

    /**
     * Creates a new ExecCommand.
     * 
     * @param application the name of the application to execute.
     * @param options the options to pass to the application.
     */
    public ExecCommand(String application, String options)
    {
        this.application = application;
        this.options = options;
    }

    /**
     * Returns the name of the application to execute.
     * 
     * @return the name of the application to execute.
     */
    public String getApplication()
    {
        return application;
    }

    /**
     * Sets the name of the application to execute.
     * 
     * @param application the name of the application to execute.
     */
    public void setApplication(String application)
    {
        this.application = application;
    }

    /**
     * Returns the options to pass to the application.
     * 
     * @return the options to pass to the application.
     */
    public String getOptions()
    {
        return options;
    }

    /**
     * Sets the options to pass to the application. Multiple options are
     * separated by the pipe character ('|').
     * 
     * @param options the options to pass to the application.
     */
    public void setOptions(String options)
    {
        this.options = options;
    }

    public String buildCommand()
    {
        return "EXEC " + escapeAndQuote(application) + " "
                + escapeAndQuote(options);
    }
}
