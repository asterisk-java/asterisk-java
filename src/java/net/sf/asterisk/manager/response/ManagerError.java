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
package net.sf.asterisk.manager.response;

/**
 * Represents an "Response: Error" response received from the asterisk server.
 * The cause for the error is given in the message attribute.
 * 
 * @author srt
 * @version $Id: ManagerError.java,v 1.3 2005/03/13 11:26:49 srt Exp $
 */
public class ManagerError extends ManagerResponse
{
    /**
     * Serial version identifier
     */
    static final long serialVersionUID = -8753149536715547476L;

    /**
     * Creates a new ManagerError.
     */
    public ManagerError()
    {

    }
}
