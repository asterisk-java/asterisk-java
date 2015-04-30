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
package org.asteriskjava.manager.response;

/**
 * Corresponds to a PingAction and contains an additional (yet useless) ping property.
 *
 * @author srt
 * @see org.asteriskjava.manager.action.PingAction
 */
public class PingResponse extends ManagerResponse
{
    private static final long serialVersionUID = 0L;

    private String ping;
    private String timestamp;
    
    /**
     * Returns always "Pong".
     *
     * @return always "Pong".
     */
    public String getPing()
    {
        return ping;
    }

    public void setPing(String ping)
    {
        this.ping = ping;
    }

    /**
     * Timestamp for the response.
     * @return Timestamp as a String, e.g 1353747825.795863
     * @since 1.0.0
     */
    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
}
