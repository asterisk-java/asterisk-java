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
package org.asteriskjava.manager.event;

/**
 * A ParkedCallTimeOutEvent is triggered when call parking times out for a given
 * channel.
 * <p>
 * It is implemented in <code>res/res_features.c</code>
 * <p>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class ParkedCallTimeOutEvent extends AbstractUnParkedEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -7437833328723536814L;

    /**
     * @param source
     */
    public ParkedCallTimeOutEvent(Object source)
    {
        super(source);
    }
}
