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
 * AGIScripts are used by the AsteriskServer to handle AGIRequests received from
 * the Asterisk server.<br>
 * To implement functionality using this framework you have to implement this
 * interface.<br>
 * Note: The implementation of AGIScript must be threadsafe as only one instance
 * is used by AsteriskServer to handle all requests to a resource.
 * 
 * @author srt
 * @version $Id: AGIScript.java,v 1.6 2005/03/11 09:37:39 srt Exp $
 */
public interface AGIScript
{
    /**
     * The service method is called by the AsteriskServer whenever this
     * AGIScript should handle an incoming AGIRequest.
     * 
     * @param request the initial data received from Asterisk when requesting
     *            this script.
     * @param channel a handle to communicate with Asterisk such as sending
     *            commands to the channel sending the request.
     * 
     * @throws AGIException any exception thrown by your script will be logged.
     */
    void service(final AGIRequest request, final AGIChannel channel)
            throws AGIException;
}
