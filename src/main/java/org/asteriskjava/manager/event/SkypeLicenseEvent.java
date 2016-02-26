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
 * A SkypeLicenseEvent is triggered in response to a SkypeLicenseListAction for each
 * license installed on the system.<p>
 * It is implemented in <code>chan_skype.c</code>.<p>
 * Available with Skype for Asterisk.
 *
 * @see org.asteriskjava.manager.action.SkypeLicenseListAction
 * @since 1.0.0
 */
public class SkypeLicenseEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 1L;
    private String key;
    private String expires;
    private String hostId;
    private Integer channels;
    private String status;

    public SkypeLicenseEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the license key.
     *
     * @return the license key.
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Sets the license key.
     *
     * @param key the license key.
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * Returns the date the license expires in the format "YYYY-MM-DD".
     *
     * @return the date the license expires in the format "YYYY-MM-DD".
     */
    public String getExpires()
    {
        return expires;
    }

    /**
     * Sets the date the license expires in the format "YYYY-MM-DD".
     *
     * @param expires the date the license expires in the format "YYYY-MM-DD".
     */
    public void setExpires(String expires)
    {
        this.expires = expires;
    }

    public String getHostId()
    {
        return hostId;
    }

    public void setHostId(String hostId)
    {
        this.hostId = hostId;
    }

    /**
     * Returns the number of licensed channels.
     *
     * @return the number of licensed channels.
     */
    public Integer getChannels()
    {
        return channels;
    }

    /**
     * Sets the number of licensed channels.
     *
     * @param channels the number of licensed channels.
     */
    public void setChannels(Integer channels)
    {
        this.channels = channels;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
