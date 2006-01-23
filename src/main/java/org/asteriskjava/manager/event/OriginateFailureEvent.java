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
package org.asteriskjava.manager.event;

/**
 * An OriginateFailureEvent is triggered when the execution of an
 * OriginateAction failed.
 * 
 * @see org.asteriskjava.manager.action.OriginateAction
 * @author srt
 * @version $Id: OriginateFailureEvent.java,v 1.3 2005/06/08 02:21:31 srt Exp $
 */
public class OriginateFailureEvent extends OriginateEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -6812199688948480631L;

    /**
     * @param source
     */
    public OriginateFailureEvent(Object source)
    {
        super(source);
    }
}
