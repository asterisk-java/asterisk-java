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
 * A ChannelReloadEvent is when a channel driver is reloaded, either on startup
 * or by request.
 * <p>
 * For example, <code>channels/chan_sip.c</code> triggers the channel reload
 * event when the SIP configuration is reloaded from sip.conf because the 'sip
 * reload' command was issued at the Manager interface, the CLI, or for another
 * reason.
 * <p>
 * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_CLI_RELOAD
 * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_LOAD
 * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_RELOAD
 * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_MANAGER_RELOAD
 * 
 * @author martins
 */
public class ChannelReloadEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 548974088194720544L;

    /**
     * The channel that got reloaded (i.e. SIP)
     */
    private String channel;

    /**
     * The reason for the reload
     * 
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_CLI_RELOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_LOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_RELOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_MANAGER_RELOAD
     */
    private String reloadReason;

    /**
     * The number of registrations with other channels (e.g. registrations with
     * other sip proxies)
     */
    private Integer registryCount;

    /**
     * The number of peers defined during the configuration of this channel
     * (e.g. sip peer definitions)
     */
    private Integer peerCount;

    /**
     * The number of users defined during the configuration of this channel
     * (e.g. sip user definitions)
     */
    private Integer userCount;

    /*
     * Possible reasons for the channel being reloaded
     */
    public static final String REASON_CHANNEL_MODULE_LOAD = "LOAD (Channel module load)";
    public static final String REASON_CHANNEL_MODULE_RELOAD = "RELOAD (Channel module reload)";
    public static final String REASON_CHANNEL_CLI_RELOAD = "CLIRELOAD (Channel module reload by CLI command)";
    public static final String REASON_MANAGER_RELOAD = "MANAGERRELOAD (Channel module reload by manager)";

    /**
     * @param source
     */
    public ChannelReloadEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the channel that was reloaded. For
     * <code>channels/chan_sip.c</code>, this would be "SIP"
     * 
     * @return the channel that was reloaded (e.g. SIP)
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * @param channel the channel that was reloaded (e.g. SIP)
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * @return the number of peers defined during the configuration of this
     *         channel (e.g. sip peer definitions)
     */
    public Integer getPeerCount()
    {
        return peerCount;
    }

    /**
     * @param peerCount the number of peers defined during the configuration of
     *            this channel (e.g. sip peer definitions)
     */
    public void setPeerCount(Integer peerCount)
    {
        this.peerCount = peerCount;
    }

    /**
     * @return the number of registrations with other channels (e.g.
     *         registrations with other sip proxies)
     */
    public Integer getRegistryCount()
    {
        return registryCount;
    }

    /**
     * @param registryCount the number of registrations with other channels
     *            (e.g. registrations with other sip proxies)
     */
    public void setRegistryCount(Integer registryCount)
    {
        this.registryCount = registryCount;
    }

    /**
     * Returns the reason that this channel was reloaded
     * 
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_CLI_RELOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_LOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_RELOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_MANAGER_RELOAD
     * @return the reason for the reload
     */
    public String getReloadReason()
    {
        return reloadReason;
    }

    /**
     * Sets the reason that this channel was reloaded
     * 
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_CLI_RELOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_LOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_CHANNEL_MODULE_RELOAD
     * @see org.asteriskjava.manager.event.ChannelReloadEvent#REASON_MANAGER_RELOAD
     * @param reloadReason the reason that this channel was reloaded
     */
    public void setReloadReason(String reloadReason)
    {
        this.reloadReason = reloadReason;
    }

    /**
     * @return the number of users defined during the configuration of this
     *         channel (e.g. sip user definitions)
     */
    public Integer getUserCount()
    {
        return userCount;
    }

    /**
     * @param userCount the number of users defined during the configuration of
     *            this channel (e.g. sip user definitions)
     */
    public void setUserCount(Integer userCount)
    {
        this.userCount = userCount;
    }
}
