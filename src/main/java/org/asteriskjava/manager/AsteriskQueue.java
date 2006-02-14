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
package org.asteriskjava.manager;

import java.util.List;

/**
 * An Asterisk ACD queue.
 * 
 * @author srt
 * @version $Id: $
 */
public interface AsteriskQueue
{
    /**
     * Returns the name of this queue as defined in Asterisk's
     * <code>queues.conf</code>.
     * 
     * @return the name of this queue.
     */
    String getName();

    /**
     * Returns the maximum number of people waiting in this queue or 0 for
     * unlimited.<br>
     * Corresponds to the <code>maxlen</code> option in Asterisk's
     * <code>queues.conf</code>.
     * 
     * @return the maximum number of people waiting in this queue.
     */
    Integer getMax();

    /**
     * Returns the list of channels currently waiting in this queue.
     * 
     * @return the list of channels currently waiting in this queue.
     */
    List<AsteriskChannel> getEntries();
}
