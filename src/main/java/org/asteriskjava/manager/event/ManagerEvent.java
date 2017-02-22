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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import org.asteriskjava.util.AstState;
import org.asteriskjava.util.ReflectionUtil;

/**
 * Abstract base class for all Events that can be received from the Asterisk
 * server. <br>
 * Events contain data pertaining to an event generated from within the Asterisk
 * core or an extension module. <br>
 * There is one conrete subclass of ManagerEvent per each supported Asterisk
 * Event.
 *
 * @author srt
 * @version $Id$
 */
public abstract class ManagerEvent extends EventObject
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 2L;
    protected String connectedLineNum;
    protected String connectedLineName;
    protected Integer priority;
    protected Integer channelState;
    protected String channelStateDesc;
    protected String exten;
    protected String callerIdNum;
    protected String callerIdName;

    /**
     * Returns the Caller ID name of the caller's channel.
     *
     * @return the Caller ID name of the caller's channel or "unknown" if none
     *         has been set.
     * @since 0.2
     */

    public String getCallerIdName()
    {
        return callerIdName;
    }

    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }

    public String getConnectedLineNum()
    {
        return connectedLineNum;
    }

    public void setConnectedLineNum(String connectedLineNum)
    {
        this.connectedLineNum = connectedLineNum;
    }

    public String getConnectedLineName()
    {
        return connectedLineName;
    }

    public void setConnectedLineName(String connectedLineName)
    {
        this.connectedLineName = connectedLineName;
    }

    public Integer getPriority()
    {
        return priority;
    }

    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    public Integer getChannelState()
    {
        return channelState == null ? AstState.str2state(channelStateDesc) : channelState;
    }

    public void setChannelState(Integer channelState)
    {
        this.channelState = channelState;
    }

    public String getChannelStateDesc()
    {
        return channelStateDesc;
    }

    public void setChannelStateDesc(String channelStateDesc)
    {
        this.channelStateDesc = channelStateDesc;
    }

    public String getExten()
    {
        return exten;
    }

    public void setExten(String exten)
    {
        this.exten = exten;
    }

    public String getCallerIdNum()
    {
        return callerIdNum;
    }

    public void setCallerIdNum(String callerIdNum)
    {
        this.callerIdNum = callerIdNum;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    protected String context;

    /**
     * AMI authorization class.
     */
    private String privilege;

    /**
     * The point in time this event has been received from the Asterisk server.
     */
    private Date dateReceived;

    private Double timestamp;

    /**
     * The server from which this event has been received (only used with
     * AstManProxy).
     */
    private String server;

    private String systemName;

    // AJ-213 only used when debugging is turned on
    private String file;
    private Integer line;
    private String func;
    private Integer sequenceNumber;

    public ManagerEvent(Object source)
    {
        super(source);

    }

    /**
     * Returns the point in time this event was received from the Asterisk
     * server. <br>
     * Pseudo events that are not directly received from the asterisk server
     * (for example ConnectEvent and DisconnectEvent) may return
     * <code>null</code>.
     */
    public Date getDateReceived()
    {
        return dateReceived;
    }

    /**
     * Sets the point in time this event was received from the asterisk server.
     */
    public void setDateReceived(Date dateReceived)
    {
        this.dateReceived = dateReceived;
    }

    /**
     * Returns the AMI authorization class of this event. <br>
     * This is one or more of system, call, log, verbose, command, agent or
     * user. Multiple privileges are separated by comma. <br>
     * Note: This property is not available from Asterisk 1.0 servers.
     *
     * @since 0.2
     */
    public String getPrivilege()
    {
        return privilege;
    }

    /**
     * Sets the AMI authorization class of this event.
     *
     * @since 0.2
     */
    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    /**
     * Returns the timestamp for this event. <br>
     * The timestamp property is available in Asterisk since 1.4 if enabled in
     * <code>manager.conf</code> by setting <code>timestampevents = yes</code>.
     * <br>
     * In contains the time the event was generated in seconds since the epoch.
     * <br>
     * Example: 1159310429.569108
     *
     * @return the timestamp for this event.
     * @since 0.3
     */
    public final Double getTimestamp()
    {
        return timestamp;
    }

    /**
     * Sets the timestamp for this event.
     *
     * @param timestamp the timestamp to set.
     * @since 0.3
     */
    public final void setTimestamp(Double timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * Returns the name of the Asterisk server from which this event has been
     * received. <br>
     * This property is only available when using to AstManProxy.
     *
     * @return the name of the Asterisk server from which this event has been
     *         received or <code>null</code> when directly connected to an
     *         Asterisk server instead of AstManProxy.
     * @since 1.0.0
     */
    public final String getServer()
    {
        return server;
    }

    /**
     * Sets the name of the Asterisk server from which this event has been
     * received.
     *
     * @param server the name of the Asterisk server from which this event has
     *            been received.
     * @since 1.0.0
     */
    public final void setServer(String server)
    {
        this.server = server;
    }

    public String getSystemName()
    {
        return systemName;
    }

    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    /**
     * Returns the name of the file in Asterisk's source code that triggered
     * this event. For example <code>pbx.c</code>.
     * <p>
     * This property is only available if debugging for the Manager API has been
     * turned on in Asterisk. This can be done by calling
     * <code>manager debug on</code> on Asterisk's command line interface or by
     * adding <code>debug=on</code> to Asterisk's <code>manager.conf</code>.
     * This feature is availble in Asterisk since 1.6.0.
     *
     * @return the name of the file in that triggered this event or
     *         <code>null</code> if debgging is turned off.
     * @see #getFunc()
     * @see #getLine()
     * @since 1.0.0
     */
    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
    }

    /**
     * Returns the line number in Asterisk's source code where this event was
     * triggered.
     * <p>
     * This property is only available if debugging for the Manager API has been
     * turned on in Asterisk. This can be done by calling
     * <code>manager debug on</code> on Asterisk's command line interface or by
     * adding <code>debug=on</code> to Asterisk's <code>manager.conf</code>.
     * This feature is availble in Asterisk since 1.6.0.
     *
     * @return the line number where this event was triggered or
     *         <code>null</code> if debgging is turned off.
     * @see #getFile()
     * @see #getFunc()
     * @since 1.0.0
     */
    public Integer getLine()
    {
        return line;
    }

    public void setLine(Integer line)
    {
        this.line = line;
    }

    /**
     * Returns the name of the C function in Asterisk's source code that
     * triggered this event. For example <code>pbx_builtin_setvar_helper</code>
     * <p>
     * This property is only available if debugging for the Manager API has been
     * turned on in Asterisk. This can be done by calling
     * <code>manager debug on</code> on Asterisk's command line interface or by
     * adding <code>debug=on</code> to Asterisk's <code>manager.conf</code>.
     * This feature is availble in Asterisk since 1.6.0.
     *
     * @return the name of the C function that triggered this event or
     *         <code>null</code> if debgging is turned off.
     * @see #getFile()
     * @see #getLine()
     * @since 1.0.0
     */
    public String getFunc()
    {
        return func;
    }

    public void setFunc(String func)
    {
        this.func = func;
    }

    /**
     * Returns the sequence numbers of this event. Sequence numbers are only
     * incremented while debugging is enabled.
     * <p>
     * This property is only available if debugging for the Manager API has been
     * turned on in Asterisk. This can be done by calling
     * <code>manager debug on</code> on Asterisk's command line interface or by
     * adding <code>debug=on</code> to Asterisk's <code>manager.conf</code>.
     * This feature is availble in Asterisk since 1.6.0.
     *
     * @return the sequence number of this event or <code>null</code> if
     *         debgging is turned off.
     * @see #getFile()
     * @see #getLine()
     * @since 1.0.0
     */
    public Integer getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString()
    {
        final List<String> ignoredProperties = Arrays.asList("file", "func", "line", "sequenceNumber", "datereceived",
                "privilege", "source", "class");
        final StringBuilder sb = new StringBuilder(getClass().getName() + "[");
        appendPropertyIfNotNull(sb, "file", getFile());
        appendPropertyIfNotNull(sb, "func", getFunc());
        appendPropertyIfNotNull(sb, "line", getLine());
        appendPropertyIfNotNull(sb, "sequenceNumber", getSequenceNumber());
        appendPropertyIfNotNull(sb, "dateReceived", getDateReceived());
        appendPropertyIfNotNull(sb, "privilege", getPrivilege());

        final Map<String, Method> getters = ReflectionUtil.getGetters(getClass());
        for (Map.Entry<String, Method> entry : getters.entrySet())
        {
            final String property = entry.getKey();
            if (ignoredProperties.contains(property))
            {
                continue;
            }

            try
            {
                final Object value = entry.getValue().invoke(this);
                appendProperty(sb, property, value);
            }
            catch (Exception e) // NOPMD
            {
                // swallow
            }
        }
        sb.append("systemHashcode=").append(System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }

    protected void appendPropertyIfNotNull(StringBuilder sb, String property, Object value)
    {
        if (value != null)
        {
            appendProperty(sb, property, value);
        }
    }

    private void appendProperty(StringBuilder sb, String property, Object value)
    {
        sb.append(property).append("=");
        if (value == null)
        {
            sb.append("null");
        }
        else
        {
            sb.append("'").append(value).append("'");
        }
        sb.append(",");

    }
}
