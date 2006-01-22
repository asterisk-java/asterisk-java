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
package net.sf.asterisk.manager;

/**
 * An EventTimeoutException is thrown if a ManagerResponse or some
 * ResponseEvents are not completely received within the expected time period.<br>
 * This exception allows you to retrieve the partial result, that is the events
 * that have been successfully received before the timeout occured.
 * 
 * @author srt
 * @version $Id: EventTimeoutException.java,v 1.3 2005/10/25 23:25:12 srt Exp $
 * @since 0.2
 */
public class EventTimeoutException extends TimeoutException
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5461825583966922L;
    private final ResponseEvents partialResult;

    /**
     * Creates a new EventTimeoutException with the given message and partial
     * result.
     * 
     * @param message message with details about the timeout.
     * @param partialResult the ResponseEvents object filled with the parts that
     *            have been received before the timeout occured.
     */
    public EventTimeoutException(String message, ResponseEvents partialResult)
    {
        super(message);
        this.partialResult = partialResult;
    }

    /**
     * Returns the partial result that has been received before the timeout
     * occured.<br>
     * Note: Using the partial result in your application should be avoided
     * wherever possible. This is only a hack to handle those versions of
     * Asterisk that don't follow the Manager API conventions, for example by
     * not sending the correct ActionCompleteEvent.
     * 
     * @return the ResponseEvents object filled with the parts that have been
     *         received before the timeout occured. Note: The response attribute
     *         may be <code>null</code> when no response has been received.
     */
    public ResponseEvents getPartialResult()
    {
        return partialResult;
    }
}
