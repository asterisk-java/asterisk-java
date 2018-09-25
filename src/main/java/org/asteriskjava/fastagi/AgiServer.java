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

import java.io.IOException;

/**
 * Listens for incoming FastAGI connections, reads the inital data and builds an
 * {@link AgiRequest} that is then handed over to the appropriate 
 * {@link org.asteriskjava.fastagi.AgiScript} for processing.<p>
 * To pass a call from Asterisk to the AGI server add an extension to your
 * dialplan that makes use of the AGI() application. For example:
 * <pre>
 * exten =&gt; 5551212,1,AGI(agi://192.168.0.2/GetCallerRecord)
 * </pre>
 * Before Asterisk-Java returns control to the dialplan it sets the
 * channel variable <code>AJ_AGISTATUS</code> to one of the following
 * values:
 * <ul>
 * <li><code>NOT_FOUND</code> if no AGI script had been configured to
 * handle the request.
 * <li><code>SUCCESS</code> if the AGI script was executed successfully. 
 * <li><code>FAILED</code> if the AGI script terminated abnormally by
 * throwing an exception or there was an internal error processing it.
 * </ul>
 * If Asterisk-Java was not able to process the request because its thread
 * pool was exhausted the <code>AJ_AGISTATUS</code> variable is not set. 
 * <p>
 * The <code>AJ_AGISTATUS</code> variable complements the <code>AGISTATUS</code>
 * variable that is set by Asterisk to <code>SUCCESS</code>, <code>FAILURE</code>
 * or <code>HANGUP</code> and is available since Asterisk 1.4. 
 * 
 * @see org.asteriskjava.fastagi.AgiServerThread
 * @author srt
 * @version $Id$
 */
public interface AgiServer
{
    /**
     * Starts this AgiServer.<p>
     * After calling startup() this AgiServer is ready to receive requests from
     * Asterisk servers and process them.<p>
     * Note that this method will not return until the AgiServer has been shut down.
     * If you want to run the AgiServer in the background use wrap it with an 
     * {@link AgiServerThread}.
     * 
     * @throws IOException if the server socket cannot be bound.
     * @throws IllegalStateException if this AgiServer is already running.
     */
    void startup() throws IOException, IllegalStateException;

    /**
     * Stops this AgiServer.<p>
     * The server socket is closed, new connections are refused and resources 
     * are freed. Any running {@link AgiScript}s are finish before shutdown
     * completes.
     * 
     * @throws IllegalStateException if this AgiServer is already shut down or
     *             has not yet been started.
     */
    void shutdown() throws IllegalStateException;

    /**
     * Connection is dropped if it stales on read longer than the timeout.
     *
     * @param socketReadTimeout the read timeout value to be used in
     *            milliseconds.
     * @see java.net.Socket#setSoTimeout(int)
     * @since 3.0.0
     */
    public void setSocketReadTimeout(int socketReadTimeout);
}
