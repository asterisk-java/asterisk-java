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
 * A PeerEntryEvent is triggered in response to a SIPPeersAction or SIPShowPeerAction and contains
 * information about a peer.<p>
 * It is implemented in <code>channels/chan_sip.c</code>
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class PeerEntryEvent extends ResponseEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 1443711349135777437L;

    private String channelType;
    private String objectName;
    private String chanObjectType;
    private String ipAddress;
    private Integer ipPort;
    private Boolean dynamic;
    private Boolean natSupport;
    private Boolean videoSupport;
    private Boolean acl;
    private String status;
    private String realtimeDevice;

    /**
     * Creates a new instance.
     * 
     * @param source
     */
    public PeerEntryEvent(Object source)
    {
        super(source);
    }

    /**
     * For SIP peers this is "SIP".
     * 
     * @return
     */
    public String getChannelType()
    {
        return channelType;
    }

    public void setChannelType(String channelType)
    {
        this.channelType = channelType;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    /**
     * For SIP peers this is either "peer" or "user".
     * 
     * @return
     */
    public String getChanObjectType()
    {
        return chanObjectType;
    }

    public void setChanObjectType(String chanObjectType)
    {
        this.chanObjectType = chanObjectType;
    }

    /**
     * Returns the IP address of the peer.
     * 
     * @return the IP address of the peer or "-none-" if none is available.
     */
    public String getIpAddress()
    {
        return ipAddress;
    }

    /**
     * Sets the IP address of the peer.
     * 
     * @param ipAddress the IP address of the peer.
     */
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public Integer getIpPort()
    {
        return ipPort;
    }

    public void setIpPort(Integer ipPort)
    {
        this.ipPort = ipPort;
    }

    public Boolean getDynamic()
    {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic)
    {
        this.dynamic = dynamic;
    }

    public Boolean getNatSupport()
    {
        return natSupport;
    }

    public void setNatSupport(Boolean natSupport)
    {
        this.natSupport = natSupport;
    }

    /**
     * Available since Asterisk 1.4.
     * 
     * @since 0.3
     */
    public Boolean getVideoSupport()
    {
        return videoSupport;
    }

    /**
     * Available since Asterisk 1.4.
     * 
     * @since 0.3
     */
    public void setVideoSupport(Boolean videoSupport)
    {
        this.videoSupport = videoSupport;
    }

    public Boolean getAcl()
    {
        return acl;
    }

    public void setAcl(Boolean acl)
    {
        this.acl = acl;
    }

    /**
     * Returns the status of this peer.<p>
     * For SIP peers this is one of:
     * <dl>
     * <dt>"UNREACHABLE"</dt>
     * <dd></dd>
     * <dt>"LAGGED (%d ms)"</dt>
     * <dd></dd>
     * <dt>"OK (%d ms)"</dt>
     * <dd></dd>
     * <dt>"UNKNOWN"</dt>
     * <dd></dd>
     * <dt>"Unmonitored"</dt>
     * <dd></dd>
     * </dl>
     * 
     * @return the status of this peer.
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the status of this peer.
     * 
     * @param status the status of this peer.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * Available since Asterisk 1.4.
     * 
     * @since 0.3
     */
    public String getRealtimeDevice()
    {
        return realtimeDevice;
    }

    /**
     * Available since Asterisk 1.4.
     * 
     * @since 0.3
     */
    public void setRealtimeDevice(String realtimeDevice)
    {
        this.realtimeDevice = realtimeDevice;
    }
}
