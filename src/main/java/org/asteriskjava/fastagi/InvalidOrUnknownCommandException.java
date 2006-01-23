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
package org.asteriskjava.fastagi;

/**
 * An InvalidOrUnknownCommandException is thrown when the reader receives a reply
 * with status code 510.
 * 
 * @author srt
 * @version $Id: InvalidOrUnknownCommandException.java,v 1.2 2005/03/11 19:43:48 srt Exp $
 */
public class InvalidOrUnknownCommandException extends AGIException
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3257002168165807929L;

    /**
     * Creates a new InvalidOrUnknownCommandException.
     * 
     * @param command the invalid or unknown command.
     */
    public InvalidOrUnknownCommandException(String command)
    {
        super("Invalid or unknown command: " + command);
    }
}
