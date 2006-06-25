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
package org.asteriskjava.live;

/**
 * Callback interface for asynchronous originates.
 *
 * @see org.asteriskjava.live.AsteriskServer#ori
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public interface OriginateCallback
{
    /**
     * Called if the originate was successful and the called party answered the call.
     * 
     * @param channel the channel created.
     */
    void onSuccess(AsteriskChannel channel);

    /**
     * Called if the originate was unsuccessful because the called party did not answer the call.
     * 
     * @param channel  the channel created.
     */
    void onNoAnswer(AsteriskChannel channel);

    /**
     * Called if the originate was unsuccessful because the called party was busy.
     * 
     * @param channel  the channel created.
     */
    void onBusy(AsteriskChannel channel);

    /**
     * Called if the originate failed for example due to an invalid channel name or an
     * originate to an unregistered SIP or IAX peer.
     * 
     * @param cause the exception that caused the failure.
     */
    void onFailure(LiveException cause);
}
