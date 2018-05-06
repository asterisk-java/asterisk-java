/*
 *  Copyright 2018 Sean Bright
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
 * Raised when the state of a contact changes.
 *
 * Available since Asterisk 13
 */
public class ContactStatusEvent extends ManagerEvent
{
    private static final long serialVersionUID = 0L;

    public static final String STATUS_CREATED     = "Created";
    public static final String STATUS_REACHABLE   = "Reachable";
    public static final String STATUS_REMOVED     = "Removed";
    public static final String STATUS_UNKNOWN     = "Unknown";
    public static final String STATUS_UNREACHABLE = "Unreachable";
    public static final String STATUS_UPDATED     = "Updated";

    private String uri;
    private String contactStatus;
    private String aor;
    private String endpointName;
    private String roundtripUsec;
    private String userAgent;
    private String regExpire;
    private String viaAddress;
    private String callID;

    public ContactStatusEvent(Object source)
    {
        super(source);
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String value)
    {
        this.uri = value;
    }

    public String getContactStatus()
    {
        return contactStatus;
    }

    public void setContactStatus(String value)
    {
        this.contactStatus = value;
    }

    public String getAor()
    {
        return aor;
    }

    public void setAor(String value)
    {
        this.aor = value;
    }

    public String getEndpointName()
    {
        return endpointName;
    }

    public void setEndpointName(String value)
    {
        this.endpointName = value;
    }

    public String getRoundtripUsec()
    {
        return roundtripUsec;
    }

    public void setRoundtripUsec(String value)
    {
        this.roundtripUsec = value;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String value)
    {
        this.userAgent = value;
    }

    public String getRegExpire()
    {
        return regExpire;
    }

    public void setRegExpire(String value)
    {
        this.regExpire = value;
    }

    public String getViaAddress()
    {
        return viaAddress;
    }

    public void setViaAddress(String value)
    {
        this.viaAddress = value;
    }

    public String getCallID()
    {
        return callID;
    }

    public void setCallID(String callID)
    {
        this.callID = callID;
    }
}
