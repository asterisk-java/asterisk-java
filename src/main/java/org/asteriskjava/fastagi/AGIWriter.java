/*
 *  Copyright 2004-2006 Stefan Reuter
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
package org.asteriskjava.fastagi;

import org.asteriskjava.fastagi.command.AGICommand;

/**
 * The AGIWriter sends commands to Asterisk.
 * 
 * @author srt
 * @version $Id: AGIWriter.java,v 1.2 2005/03/11 09:37:39 srt Exp $
 */
public interface AGIWriter
{
    /**
     * Sends the given command to the Asterisk server.
     * 
     * @param command the command to send.
     * @throws AGIException if the command can't be sent.
     */
    void sendCommand(AGICommand command) throws AGIException;
}
