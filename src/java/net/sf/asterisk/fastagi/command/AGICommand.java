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
 * AGICommand that can be sent to Asterisk via the Asterisk Gateway Interface.<br>
 * This interface contains only one method that transforms the command to a
 * String representation understood by Asterisk.
 * 
 * @author srt
 * @version $Id: AGICommand.java,v 1.5 2006/01/12 10:35:12 srt Exp $
 */
public interface AGICommand
{

    /**
     * Returns a string suitable to be sent to asterisk.<br>
     * 
     * @return a string suitable to be sent to asterisk.
     */
    public abstract String buildCommand();

}
