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

/**
 * Abstract base class for all AGI specific exceptions.
 * 
 * @author srt
 * @version $Id: AgiException.java,v 1.2 2005/03/11 09:37:39 srt Exp $
 */
public abstract class AgiException extends Exception
{
    /**
     *  
     */
    public AgiException()
    {
        super();
    }

    /**
     * Creates a new AgiException with the given message.
     * 
     * @param message a message decribing the AgiException.
     */
    public AgiException(String message)
    {
        super(message);
    }


    /**
     * Creates a new AgiException with the given message and cause.
     * 
     * @param message a message decribing the AgiException.
     * @param cause the throwable that caused this exception to be thrown.
     */
    public AgiException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
