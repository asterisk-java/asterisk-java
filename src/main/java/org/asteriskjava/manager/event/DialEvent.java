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
 * A dial event is triggered whenever a phone attempts to dial someone.
 * <p>
 * This event is implemented in <code>apps/app_dial.c</code>.
 * <p>
 * Available since Asterisk 1.2.
 *
 * @author Asteria Solutions Group, Inc. http://www.asteriasgi.com/
 * @version $Id$
 * @since 0.2
 */
public class DialEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;

    public static final String SUBEVENT_BEGIN = "Begin";
    public static final String SUBEVENT_END = "End";

    public static final String DIALSTATUS_CHANUNAVAIL = "CHANUNAVAIL";
    public static final String DIALSTATUS_CONGESTION = "CONGESTION";
    public static final String DIALSTATUS_NOANSWER = "NOANSWER";
    public static final String DIALSTATUS_BUSY = "BUSY";
    public static final String DIALSTATUS_ANSWER = "ANSWER";
    public static final String DIALSTATUS_CANCEL = "CANCEL";
    public static final String DIALSTATUS_DONTCALL = "DONTCALL";
    public static final String DIALSTATUS_TORTURE = "TORTURE";
    public static final String DIALSTATUS_INVALIDARGS = "INVALIDARGS";

    private String subEvent = SUBEVENT_BEGIN;

    /**
     * The name of the source channel.
     */
    private String channel;

    /**
     * The name of the destination channel.
     */
    private String destination;

    /**
     * Destination channel state
     */
    private Integer destChannelState;
    private String destChannelStateDesc;

    private String destContext;

    private Integer destPriority;

    private String destExten;

    private String destConnectedLineName;
    private String destConnectedLineNum;

    private String destCallerIdName;
    private String destCallerIdNum;

    /**
     * The unique id of the source channel.
     */
    private String uniqueId;

    /**
     * The unique id of the destination channel.
     */
    private String destUniqueId;

    private String dialString;
    private String dialStatus;

    public DialEvent(Object source)
    {
        super(source);
    }

    /**
     * Enro 2015-03 Workaround to build legacy DialEvent (unsupported in
     * Asterisk 13) from new DialBeginEvent Asterisk 13.
     */
    public DialEvent(DialBeginEvent dialBeginEvent)
    {
        this(dialBeginEvent.getSource());
        setDateReceived(dialBeginEvent.getDateReceived());
        setTimestamp(dialBeginEvent.getTimestamp());
        setPrivilege(dialBeginEvent.getPrivilege());
        setCallerId(dialBeginEvent.getCallerIdNum());
        setCallerIdName(dialBeginEvent.getCallerIdName());
        setSrc(dialBeginEvent.getChannel());
        setUniqueId(dialBeginEvent.getSrcUniqueId());
        setDestUniqueId(dialBeginEvent.getDestUniqueId());
        setDestination(dialBeginEvent.getDestChannel());
        setDialStatus(dialBeginEvent.getDialStatus());
    }

    /**
     * Since Asterisk 1.6 the begin and the end of a dial command generate a
     * Dial event. The subEvent property returns whether the dial started
     * execution ("Begin") or completed ("End"). As Asterisk prior to 1.6 only
     * sends one event per Dial command this always returns "Begin" for Asterisk
     * prior to 1.6.<br>
     * For an "End" sub event only the properies channel, unqiue id and dial
     * status are available, for a "Begin" sub event all properties are
     * available except for the dial status.
     *
     * @return "Begin" or "End" for Asterisk since 1.6, "Begin" for Asterisk
     *         prior to 1.6.
     * @since 1.0.0
     */
    public String getSubEvent()
    {
        return subEvent;
    }

    public void setSubEvent(String subEvent)
    {
        this.subEvent = subEvent;
    }

    /**
     * Returns the name of the source channel.
     *
     * @return the name of the source channel.
     * @since 1.0.0
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Returns the name of the source channel.
     *
     * @param channel the name of the source channel.
     * @since 1.0.0
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the name of the source channel.
     *
     * @return the name of the source channel.
     * @deprecated as of 1.0.0, use {@link #getChannel()} instead.
     */
    @Deprecated
    public String getSrc()
    {
        return channel;
    }

    /**
     * Sets the name of the source channel.
     * <p>
     * Asterisk versions up to 1.4 use the "Source" property instead of
     * "Channel".
     *
     * @param src the name of the source channel.
     */
    public void setSrc(String src)
    {
        this.channel = src;
    }

    /**
     * Returns the name of the destination channel.
     *
     * @return the name of the destination channel.
     */
    public String getDestination()
    {
        return destination;
    }

    public String getDestChannel()
    {
        return getDestination();
    }

    /**
     * Sets the name of the destination channel.
     *
     * @param destination the name of the destination channel.
     */
    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public void setDestChannel(String destination)
    {
        setDestination(destination);
    }

    /**
     * Returns the Caller*ID.
     *
     * @return the Caller*ID or "&lt;unknown&gt;" if none has been set.
     * @deprecated as of 1.0.0, use {@link #getCallerIdNum()} instead.
     */
    @Deprecated
    public String getCallerId()
    {
        return getCallerIdNum();
    }

    /**
     * Sets the caller*ID.
     *
     * @param callerId the caller*ID.
     * @deprecated as of 1.0.0, use {@link #setCallerIdNum()} instead.
     */
    @Deprecated
    public void setCallerId(String callerId)
    {
        setCallerIdNum(callerId);
    }

    /**
     * Returns the unique ID of the source channel.
     *
     * @return the unique ID of the source channel.
     * @since 1.0.0
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique ID of the source channel.
     *
     * @param srcUniqueId the unique ID of the source channel.
     * @since 1.0.0
     */
    public void setUniqueId(String srcUniqueId)
    {
        this.uniqueId = srcUniqueId;
    }

    /**
     * Returns the unique ID of the source channel.
     *
     * @return the unique ID of the source channel.
     * @deprecated as of 1.0.0, use {@link #getUniqueId()} instead.
     */
    @Deprecated
    public String getSrcUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique ID of the source channel.
     * <p>
     * Asterisk versions up to 1.4 use the "SrcUniqueId" property instead of
     * "UniqueId".
     *
     * @param srcUniqueId the unique ID of the source channel.
     */
    public void setSrcUniqueId(String srcUniqueId)
    {
        this.uniqueId = srcUniqueId;
    }

    /**
     * Returns the unique ID of the destination channel.
     *
     * @return the unique ID of the destination channel.
     */
    public String getDestUniqueId()
    {
        return destUniqueId;
    }

    /**
     * Sets the unique ID of the destination channel.
     *
     * @param destUniqueId the unique ID of the destination channel.
     */
    public void setDestUniqueId(String destUniqueId)
    {
        this.destUniqueId = destUniqueId;
    }

    /**
     * Returns the dial string passed to the Dial application.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return the dial string passed to the Dial application.
     * @since 1.0.0
     */
    public String getDialString()
    {
        return dialString;
    }

    /**
     * Sets the dial string passed to the Dial application.
     *
     * @param dialString the dial string passed to the Dial application.
     * @since 1.0.0
     */
    public void setDialString(String dialString)
    {
        this.dialString = dialString;
    }

    /**
     * For end subevents this returns whether the completion status of the dial
     * application.<br>
     * Possible values are:
     * <ul>
     * <li>CHANUNAVAIL</li>
     * <li>CONGESTION</li>
     * <li>NOANSWER</li>
     * <li>BUSY</li>
     * <li>ANSWER</li>
     * <li>CANCEL</li>
     * <li>DONTCALL</li>
     * <li>TORTURE</li>
     * <li>INVALIDARGS</li>
     * </ul>
     * It corresponds the the DIALSTATUS variable used in the dialplan.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return the completion status of the dial application.
     * @since 1.0.0
     */
    public String getDialStatus()
    {
        return dialStatus;
    }

    public void setDialStatus(String dialStatus)
    {
        this.dialStatus = dialStatus;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("DialEvent [subEvent=");
        builder.append(subEvent);
        builder.append(", channel=");
        builder.append(channel);
        builder.append(", destination=");
        builder.append(destination);
        builder.append(", callerIdNum=");
        builder.append(callerIdNum);
        builder.append(", callerIdName=");
        builder.append(callerIdName);
        builder.append(", uniqueId=");
        builder.append(uniqueId);
        builder.append(", destUniqueId=");
        builder.append(destUniqueId);
        builder.append(", dialString=");
        builder.append(dialString);
        builder.append(", dialStatus=");
        builder.append(dialStatus);
        builder.append(", connectedLineNum=");
        builder.append(connectedLineNum);
        builder.append(", connectedLineName=");
        builder.append(connectedLineName);
        builder.append("]");
        return builder.toString();
    }

    public Integer getDestChannelState()
    {
        return destChannelState;
    }

    public void setDestChannelState(Integer destChannelState)
    {
        this.destChannelState = destChannelState;
    }

    public String getDestContext()
    {
        return destContext;
    }

    public void setDestContext(String destContext)
    {
        this.destContext = destContext;
    }

    public Integer getDestPriority()
    {
        return destPriority;
    }

    public void setDestPriority(Integer destPriority)
    {
        this.destPriority = destPriority;
    }

    public String getDestChannelStateDesc()
    {
        return destChannelStateDesc;
    }

    public void setDestChannelStateDesc(String destChannelStateDesc)
    {
        this.destChannelStateDesc = destChannelStateDesc;
    }

    public String getDestExten()
    {
        return destExten;
    }

    public void setDestExten(String destExten)
    {
        this.destExten = destExten;
    }

    public String getDestConnectedLineName()
    {
        return destConnectedLineName;
    }

    public void setDestConnectedLineName(String destConnectedLineName)
    {
        this.destConnectedLineName = destConnectedLineName;
    }

    public String getDestConnectedLineNum()
    {
        return destConnectedLineNum;
    }

    public void setDestConnectedLineNum(String destConnectedLineNum)
    {
        this.destConnectedLineNum = destConnectedLineNum;
    }

    public String getDestCallerIdName()
    {
        return destCallerIdName;
    }

    public void setDestCallerIdName(String destCallerIdName)
    {
        this.destCallerIdName = destCallerIdName;
    }

    public String getDestCallerIdNum()
    {
        return destCallerIdNum;
    }

    public void setDestCallerIdNum(String destCallerIdNum)
    {
        this.destCallerIdNum = destCallerIdNum;
    }
}
