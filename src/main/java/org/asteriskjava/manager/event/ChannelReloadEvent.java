/*
 *  Copyright 2004-2007 Stefan Reuter and others
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
 * A ChannelReloadEvent is triggered when the SIP configuration is reloaded from
 * sip.conf because the 'sip reload' command was issued at the Manager
 * interface.
 * <p>
 * It is implemented in <code>channels/chan_sip.c</code>
 * 
 * @author martins
 */
public class ChannelReloadEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 548974088194720544L;

    private String channel;
    private String reloadReason;
    private String registryCount;
    private String peerCount;
    private String userCount;
    
    /**
     * @param source
     */
    public ChannelReloadEvent(Object source)
    {
        super(source);
    }

    /**
     * @return the channel
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * @return the peerCount
     */
    public String getPeerCount()
    {
        return peerCount;
    }

    /**
     * @param peerCount the peerCount to set
     */
    public void setPeerCount(String peerCount)
    {
        this.peerCount = peerCount;
    }

    /**
     * @return the registryCount
     */
    public String getRegistryCount()
    {
        return registryCount;
    }

    /**
     * @param registryCount the registryCount to set
     */
    public void setRegistryCount(String registryCount)
    {
        this.registryCount = registryCount;
    }

    /**
     * @return the reloadReason
     */
    public String getReloadReason()
    {
        return reloadReason;
    }

    /**
     * @param reloadReason the reloadReason to set
     */
    public void setReloadReason(String reloadReason)
    {
        this.reloadReason = reloadReason;
    }

    /**
     * @return the userCount
     */
    public String getUserCount()
    {
        return userCount;
    }

    /**
     * @param userCount the userCount to set
     */
    public void setUserCount(String userCount)
    {
        this.userCount = userCount;
    }
}
