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
package org.asteriskjava.manager.action;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.asteriskjava.manager.event.OriginateEvent;


/**
 * The OriginateAction generates an outgoing call to the extension in the given
 * context with the given priority or to a given application with optional
 * parameters.<br>
 * If you want to connect to an extension use the properties context, exten and
 * priority. If you want to connect to an application use the properties
 * application and data if needed. Note that no call detail record will be
 * written when directly connecting to an application, so it may be better to
 * connect to an extension that starts the application you wish to connect to.<br>
 * The response to this action is sent when the channel has been answered and
 * asterisk starts connecting it to the given extension. So be careful not to
 * choose a too short timeout when waiting for the response.<br>
 * If you set async to <code>true</code> Asterisk reports an OriginateSuccess-
 * and OriginateFailureEvents. The action id of these events equals the action
 * id of this OriginateAction.
 * 
 * @see org.asteriskjava.manager.event.OriginateSuccessEvent
 * @see org.asteriskjava.manager.event.OriginateFailureEvent
 * @author srt
 * @version $Id: OriginateAction.java,v 1.9 2005/10/29 12:00:11 srt Exp $
 */
public class OriginateAction extends AbstractManagerAction
        implements
            EventGeneratingAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 8194597741743334704L;

    private String channel;
    private String exten;
    private String context;
    private Integer priority;
    private Long timeout;
    private String callerId;
    private Boolean callingPres;
    private Map<String, String> variables;
    private String account;
    private String application;
    private String data;
    private Boolean async;

    /**
     * Returns the name of this action, i.e. "Originate".
     */
    public String getAction()
    {
        return "Originate";
    }

    /**
     * Returns the account code to use for the originated call.
     */
    public String getAccount()
    {
        return account;
    }

    /**
     * Sets the account code to use for the originated call.<br>
     * The account code is included in the call detail record generated for this
     * call and will be used for billing.
     */
    public void setAccount(String account)
    {
        this.account = account;
    }

    /**
     * Returns the caller id to set on the outgoing channel.
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the caller id to set on the outgoing channel.<br>
     * This includes both the Caller*Id Number and Caller*Id Name in the form
     * "Jon Doe &lt;1234&gt;".
     * 
     * @param callerId the caller id to set on the outgoing channel.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns <code>true</code> if Caller*ID presentation is set on the
     * outgoing channel.
     */
    public Boolean getCallingPres()
    {
        return callingPres;
    }

    /**
     * Set to <code>true</code> if you want Caller*ID presentation to be set
     * on the outgoing channel.
     */
    public void setCallingPres(Boolean callingPres)
    {
        this.callingPres = callingPres;
    }

    /**
     * Returns the name of the channel to connect to the outgoing call.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel to connect to the outgoing call.<br>
     * This property is required.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the name of the context of the extension to connect to.
     */
    public String getContext()
    {
        return context;
    }

    /**
     * Sets the name of the context of the extension to connect to.<br>
     * If you set the context you also have to set the exten and priority
     * properties.
     */
    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * Returns the extension to connect to.
     */
    public String getExten()
    {
        return exten;
    }

    /**
     * Sets the extension to connect to.<br>
     * If you set the extension you also have to set the context and priority
     * properties.
     */
    public void setExten(String exten)
    {
        this.exten = exten;
    }

    /**
     * Returns the priority of the extension to connect to.
     */
    public Integer getPriority()
    {
        return priority;
    }

    /**
     * Sets the priority of the extension to connect to. If you set the priority
     * you also have to set the context and exten properties.
     */
    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    /**
     * Returns the name of the application to connect to.
     */
    public String getApplication()
    {
        return application;
    }

    /**
     * Sets the name of the application to connect to.
     */
    public void setApplication(String application)
    {
        this.application = application;
    }

    /**
     * Returns the parameters to pass to the application.
     */
    public String getData()
    {
        return data;
    }

    /**
     * Sets the parameters to pass to the application.
     */
    public void setData(String data)
    {
        this.data = data;
    }

    /**
     * Returns the timeout for the origination.
     */
    public Long getTimeout()
    {
        return timeout;
    }

    /**
     * Sets the timeout (in milliseconds) for the origination.<br>
     * The channel must be answered within this time, otherwise the origination
     * is considered to have failed and an OriginateFailureEvent is generated.<br>
     * If not set, Asterisk assumes a default value of 30000 meaning 30 seconds.
     * 
     * @param timeout the timeout in milliseconds
     * @deprecated use {@see #setTimeout(Long)} instead.
     */
    public void setTimeout(Integer timeout)
    {
        this.timeout = new Long(timeout.longValue());
    }

    /**
     * Sets the timeout (in milliseconds) for the origination.<br>
     * The channel must be answered within this time, otherwise the origination
     * is considered to have failed and an OriginateFailureEvent is generated.<br>
     * If not set, Asterisk assumes a default value of 30000 meaning 30 seconds.
     * 
     * @param timeout the timeout in milliseconds
     */
    public void setTimeout(Long timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Sets the variables to set on the originated call.<br>
     * Variable assignments are of the form "VARNAME=VALUE". You can specify
     * multiple variable assignments separated by the '|' character.<br>
     * Example: "VAR1=abc|VAR2=def" sets the channel variables VAR1 to "abc" and
     * VAR2 to "def".
     * 
     * @deprecated use {@link #setVariables(Map)} instead.
     */
    public void setVariable(String variable)
    {
        StringTokenizer st;

        if (variable == null)
        {
            this.variables = null;
        }

        st = new StringTokenizer(variable, "|");
        variables = new LinkedHashMap<String, String>();
        while (st.hasMoreTokens())
        {
            String[] keyValue;

            keyValue = st.nextToken().split("=", 2);
            if (keyValue.length < 2)
            {
                variables.put(keyValue[0], null);
            }
            else
            {
                variables.put(keyValue[0], keyValue[1]);
            }
        }
    }

    /**
     * Returns the variables to set on the originated call.
     * 
     * @param variables a Map containing the variable names as key and their
     *            values as value.
     * @since 0.2
     */
    public Map getVariables()
    {
        return variables;
    }

    /**
     * Sets the variables to set on the originated call.
     * 
     * @param variables a Map containing the variable names as key and their
     *            values as value.
     * @since 0.2
     */
    public void setVariables(Map<String, String> variables)
    {
        this.variables = variables;
    }

    /**
     * Returns true if this is a fast origination.
     */
    public Boolean getAsync()
    {
        return async;
    }

    /**
     * Set to true for fast origination. Only with fast origination Asterisk
     * will send OriginateSuccess- and OriginateFailureEvents.
     */
    public void setAsync(Boolean async)
    {
        this.async = async;
    }

    public Class getActionCompleteEventClass()
    {
        return OriginateEvent.class;
    }
}
