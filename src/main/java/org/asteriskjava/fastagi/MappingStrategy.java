/*
 *  Copyright  2004-2006 Stefan Reuter
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
 * A MappingStrategy determines which AGIScript is called to service a
 * given AGIRequest. A MappingStrategy can use any of the properties
 * of an AGIRequest to do this but will most likely use the script
 * property, that is the name of the invoked AGI script as passed
 * from the dialplan.
 * 
 * @author srt
 * @version $Id: MappingStrategy.java,v 1.2 2006/01/12 11:50:24 srt Exp $
 */
public interface MappingStrategy
{
    /**
     * Returns an instance of an AGIScript that is responsible to handle 
     * the given request.
     * 
     * @param request the request to lookup.
     * @return the AGIScript instance that should handle this request 
     *         or <code>null</code> if none could be determined by this strategy.
     */
    AGIScript determineScript(AGIRequest request);
}
