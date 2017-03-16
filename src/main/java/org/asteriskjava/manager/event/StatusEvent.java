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

import java.util.Map;

/**
 * A StatusEvent is triggered for each active channel in response to a
 * StatusAction.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.action.StatusAction
 */
public class StatusEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -3619197512835308812L;
    private String channel;
    private String accountCode;
    private Integer seconds;
    private String bridgedChannel;
    private String bridgedUniqueId;
    private String uniqueId;
    private String linkedId;
    private String data;
    private String readFormat;
    private String writeFormat;
    private String type;
    private String effectiveConnectedLineName;
    private String effectiveConnectedLineNum;
    private String application;
    private String callGroup;
    private String nativeFormats;
    private String pickupGroup;
    private String timeToHangup;
    private Map<String, String> variables;
    private String dnid;
    private String writetrans;
    private String bridgeId;
    private String readtrans;
    private String connectedlinename;
    private String connectedlinenum;

    public StatusEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of this channel.
     *
     * @return the name of this channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of this channel.
     *
     * @param channel the name of this channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the Caller*ID Number of this channel.
     * <p>
     * This property is deprecated as of Asterisk 1.4, use
     * {@link #getCallerIdNum()} instead.
     *
     * @return the Caller*ID Number of this channel or <code>null</code> if none
     *         is available.
     * @deprecated
     */
    @Deprecated
    public String getCallerId()
    {
        return callerIdNum;
    }

    /**
     * Sets the Caller*ID Number of this channel.
     * <p>
     * This property is deprecated as of Asterisk 1.4.
     *
     * @param callerIdNum the Caller*ID Number to set.
     */
    public void setCallerId(String callerIdNum)
    {
        this.callerIdNum = callerIdNum;
    }

    /**
     * Returns the account code of this channel.
     *
     * @return the account code of this channel.
     * @since 1.0.0
     */
    public String getAccountCode()
    {
        return accountCode;
    }

    /**
     * Sets the account code of this channel.
     *
     * @param accountCode the account code of this channel.
     * @since 1.0.0
     */
    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    /**
     * Returns the account code of this channel.
     *
     * @return the account code of this channel.
     * @deprecated since 1.0.0, use {@link #getAccountCode()} instead.
     */
    @Deprecated
    public String getAccount()
    {
        return accountCode;
    }

    /**
     * Sets the account code of this channel.
     * <p>
     * Asterisk versions up to 1.4 use the "Account" property instead of
     * "AccountCode".
     *
     * @param account the account code of this channel.
     */
    public void setAccount(String account)
    {
        this.accountCode = account;
    }

    /**
     * Returns the state of the channel as a descriptive text.
     *
     * @return the state of the channel as a descriptive text.
     * @deprecated use {@link #getChannelStateDesc()} instead.
     */
    @Deprecated
    public String getState()
    {
        return getChannelStateDesc();
    }

    public void setState(String state)
    {
        setChannelStateDesc(state);
    }

    public String getExtension()
    {
        return getExten();
    }

    public void setExtension(String extension)
    {
        setExten(extension);
    }

    /**
     * Returns the number of elapsed seconds.
     *
     * @return the number of elapsed seconds.
     */
    public Integer getSeconds()
    {
        return seconds;
    }

    /**
     * Sets the number of elapsed seconds.
     *
     * @param seconds the number of elapsed seconds.
     */
    public void setSeconds(Integer seconds)
    {
        this.seconds = seconds;
    }

    /**
     * Returns the name of the linked channel if this channel is bridged.
     *
     * @return the name of the linked channel if this channel is bridged.
     * @since 1.0.0
     */
    public String getBridgedChannel()
    {
        return bridgedChannel;
    }

    /**
     * Sets the name of the linked channel.
     *
     * @param bridgedChannel the name of the linked channel if this channel is
     *            bridged.
     * @since 1.0.0
     */
    public void setBridgedChannel(String bridgedChannel)
    {
        this.bridgedChannel = bridgedChannel;
    }

    /**
     * Returns the name of the linked channel if this channel is bridged.
     *
     * @return the name of the linked channel if this channel is bridged.
     * @deprecated as of 1.0.0, use {@link #getBridgedChannel()} instead.
     */
    @Deprecated
    public String getLink()
    {
        return bridgedChannel;
    }

    /**
     * Sets the name of the linked channel.
     * <p>
     * Asterisk versions up to 1.4 use "Link" instead of "BridgedChannel".
     *
     * @param link the name of the linked channel if this channel is bridged.
     */
    public void setLink(String link)
    {
        this.bridgedChannel = link;
    }

    /**
     * Returns the unique id of the linked channel if this channel is bridged.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return the unique id of the linked channel if this channel is bridged.
     * @since 1.0.0
     */
    public String getBridgedUniqueId()
    {
        return bridgedUniqueId;
    }

    /**
     * Sets the unique id of the linked channel if this channel is bridged.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @param bridgedUniqueId the unique id of the linked channel if this
     *            channel is bridged.
     * @since 1.0.0
     */
    public void setBridgedUniqueId(String bridgedUniqueId)
    {
        this.bridgedUniqueId = bridgedUniqueId;
    }

    /**
     * Returns the unique id of this channel.
     *
     * @return the unique id of this channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of this channel.
     *
     * @param uniqueId the unique id of this channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /**
     * Returns the channel variables if the
     * {@link org.asteriskjava.manager.action.StatusAction#setVariables(String)}
     * property has been set.
     * <p>
     * Available since Asterisk 1.6
     *
     * @return the channel variables.
     * @since 1.0.0
     */
    public Map<String, String> getVariables()
    {
        return variables;
    }

    /**
     * Sets the channel variables.
     * <p>
     * Available since Asterisk 1.6
     *
     * @param variables the channel variables.
     * @since 1.0.0
     */
    public void setVariables(Map<String, String> variables)
    {
        this.variables = variables;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getReadFormat()
    {
        return readFormat;
    }

    public void setReadFormat(String readFormat)
    {
        this.readFormat = readFormat;
    }

    public String getWriteFormat()
    {
        return writeFormat;
    }

    public void setWriteFormat(String writeFormat)
    {
        this.writeFormat = writeFormat;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getEffectiveConnectedLineName()
    {
        return effectiveConnectedLineName;
    }

    public void setEffectiveConnectedLineName(String effectiveConnectedLineName)
    {
        this.effectiveConnectedLineName = effectiveConnectedLineName;
    }

    public String getEffectiveConnectedLineNum()
    {
        return effectiveConnectedLineNum;
    }

    public void setEffectiveConnectedLineNum(String effectiveConnectedLineNum)
    {
        this.effectiveConnectedLineNum = effectiveConnectedLineNum;
    }

    public String getApplication()
    {
        return application;
    }

    public void setApplication(String application)
    {
        this.application = application;
    }

    public String getCallGroup()
    {
        return callGroup;
    }

    public void setCallGroup(String callGroup)
    {
        this.callGroup = callGroup;
    }

    public String getNativeFormats()
    {
        return nativeFormats;
    }

    public void setNativeFormats(String nativeFormats)
    {
        this.nativeFormats = nativeFormats;
    }

    public String getPickupGroup()
    {
        return pickupGroup;
    }

    public void setPickupGroup(String pickupGroup)
    {
        this.pickupGroup = pickupGroup;
    }

    public String getTimeToHangup()
    {
        return timeToHangup;
    }

    public void setTimeToHangup(String timeToHangup)
    {
        this.timeToHangup = timeToHangup;
    }

    /**
     * @return the dnid
     */
    public String getDnid()
    {
        return dnid;
    }

    /**
     * @param dnid the dnid to set
     */
    public void setDnid(String dnid)
    {
        this.dnid = dnid;
    }

    /**
     * @return the writetrans
     */
    public String getWritetrans()
    {
        return writetrans;
    }

    /**
     * @param writetrans the writetrans to set
     */
    public void setWritetrans(String writetrans)
    {
        this.writetrans = writetrans;
    }

    /**
     * @return the bridgeid
     */
    public String getBridgeId()
    {
        return bridgeId;
    }

    /**
     * @param bridgeid the bridgeid to set
     */
    public void setBridgeId(String bridgeid)
    {
        this.bridgeId = bridgeid;
    }

    /**
     * @return the readtrans
     */
    public String getReadtrans()
    {
        return readtrans;
    }

    /**
     * @param readtrans the readtrans to set
     */
    public void setReadtrans(String readtrans)
    {
        this.readtrans = readtrans;
    }

	public String getConnectedlinename() {
		return connectedlinename;
	}

	public void setConnectedlinename(String connectedlinename) {
		this.connectedlinename = connectedlinename;
	}

	public String getConnectedlinenum() {
		return connectedlinenum;
	}

	public void setConnectedlinenum(String connectedlinenum) {
		this.connectedlinenum = connectedlinenum;
	}
}
