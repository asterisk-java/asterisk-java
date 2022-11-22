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
 * The AgiNetworkException usally wraps an IOException denoting a network
 * problem while talking to the Asterisk server.
 *
 * @author srt
 * @version $Id$
 */
public class AgiNetworkException extends AgiException {
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3256445789629723703L;

    /**
     * Creates a new AgiNetworkException with the given message and cause.
     *
     * @param message a message describing the AgiException.
     * @param cause   the throwable that caused this exception.
     */
    public AgiNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
