/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.fastagi.command;

/**
 * Sets the extension for continuation upon exiting the application.
 * 
 * @author srt
 * @version $Id: SetExtensionCommand.java,v 1.3 2006/01/12 10:35:13 srt Exp $
 */
public class SetExtensionCommand extends AbstractAGICommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3256719598056387384L;

    /**
     * The extension for continuation upon exiting the application.
     */
    private String extension;

    /**
     * Creates a new SetPriorityCommand.
     * 
     * @param extension the extension for continuation upon exiting the
     *            application.
     */
    public SetExtensionCommand(String extension)
    {
        this.extension = extension;
    }

    /**
     * Returns the extension for continuation upon exiting the application.
     * 
     * @return the extension for continuation upon exiting the application.
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * Sets the extension for continuation upon exiting the application.
     * 
     * @param extension the extension for continuation upon exiting the
     *            application.
     */
    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public String buildCommand()
    {
        return "SET EXTENSION " + escapeAndQuote(extension);
    }
}
