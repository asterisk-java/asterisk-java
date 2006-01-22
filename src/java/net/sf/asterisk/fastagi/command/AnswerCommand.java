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
 * Answers channel if not already in answer state.<br>
 * Returns -1 on channel failure, or 0 if successful.
 * 
 * @author srt
 * @version $Id: AnswerCommand.java,v 1.2 2006/01/12 10:35:13 srt Exp $
 */
public class AnswerCommand extends AbstractAGICommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3762248656229053753L;

    /**
     * Creates a new AnswerCommand.
     */
    public AnswerCommand()
    {
    }

    public String buildCommand()
    {
        return "ANSWER";
    }
}
