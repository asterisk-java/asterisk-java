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
 * A PeerlistCompleteEvent is triggered after the details of all peers has been
 * reported in response to an SIPPeersAction or SIPShowPeerAction.
 * <p>
 * Available since Asterisk 1.2
 * 
 * @see org.asteriskjava.manager.event.PeerEntryEvent
 * @see org.asteriskjava.manager.action.SipPeersAction
 * @see org.asteriskjava.manager.action.SipShowPeerAction
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class EndpointList extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1177773673509373296L;
    private Integer listItems;
    private String eventList;

    private String aor;
    private String auths;
    private String objectName;
    private String activeChannels;
    private String transport;
    private String outboundAuths;
    private String devicestate;
    private String event;
    private String objectType;
    private String contacts;

    /**
     * Creates a new instance.
     * 
     * @param source
     */
    public EndpointList(Object source)
    {
        super(source);
    }

    /**
     * Returns the number of PeerEvents that have been reported.
     * 
     * @return the number of PeerEvents that have been reported.
     */
    public Integer getListItems()
    {
        return listItems;
    }

    /**
     * Sets the number of PeerEvents that have been reported.
     * 
     * @param listItems the number of PeerEvents that have been reported.
     */
    public void setListItems(Integer listItems)
    {
        this.listItems = listItems;
    }

    /**
     * Returns always "Complete".
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return always returns "Complete" confirming that all PeerEntry events have
     *         been sent.
     * @since 1.0.0
     */
    public String getEventList()
    {
        return eventList;
    }

    public void setEventList(String eventList)
    {
        this.eventList = eventList;
    }

    public String getAor()
    {
        return aor;
    }

    public void setAor(String aor)
    {
        this.aor = aor;
    }

    public String getAuths()
    {
        return auths;
    }

    public void setAuths(String auths)
    {
        this.auths = auths;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    public String getTransport()
    {
        return transport;
    }

    public void setTransport(String transport)
    {
        this.transport = transport;
    }

    public String getOutboundAuths()
    {
        return outboundAuths;
    }

    public void setOutboundAuths(String outboundAuths)
    {
        this.outboundAuths = outboundAuths;
    }

    public String getDevicestate()
    {
        return devicestate;
    }

    public void setDevicestate(String devicestate)
    {
        this.devicestate = devicestate;
    }

    public String getEvent()
    {
        return event;
    }

    public void setEvent(String event)
    {
        this.event = event;
    }

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType(String objectType)
    {
        this.objectType = objectType;
    }

    public String getContacts()
    {
        return contacts;
    }

    public void setContacts(String contacts)
    {
        this.contacts = contacts;
    }

    public String getActiveChannels()
    {
        return activeChannels;
    }

    public void setActiveChannels(String activeChannels)
    {
        this.activeChannels = activeChannels;
    }
}
