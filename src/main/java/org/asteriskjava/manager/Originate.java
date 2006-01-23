/*
 * Copyright  2004-2005 Stefan Reuter
 * 
 * Copyright 2005 Ben Hencke
 * 
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.asteriskjava.manager;

import java.util.*;

/**
 * @author Ben Hencke
 */
public class Originate
{
    private String channel;
    private String exten;
    private String context;
    private Integer priority;
    private Long timeout;
    private String callerId;
    private Map<String, String> variables = new HashMap<String, String>();
    private String account;
    private String application;
    private String data;

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
     * Sets the caller id to set on the outgoing channel.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
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
     * If not set, a default value of 30000 is asumed meaning 30 seconds.
     */
    public void setTimeout(Long timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Returns the variables to set on the originated call.
     */
    public Map<String, String> getVariables()
    {
        return variables;
    }

    /**
     * Sets the variables to set on the originated call.
     */
    public void setVariables(Map<String, String> variables)
    {
        this.variables = variables;
    }

    /**
     * Sets a variable on the originated call. Replaces any existing variable
     * with the same name.
     */
    public void setVariable(String name, String value)
    {
        variables.put(name, value);
    }

    /**
     * Returns the variables to set on the originated call in native asterisk
     * format.<br>
     * Example: "VAR1=abc|VAR2=def".
     * 
     * @deprecated This method is no longer needed and will be removed in the
     *             next version of Asterisk-Java.
     */
    public String getVariableString()
    {
        StringBuffer varstring = new StringBuffer();
        for (Map.Entry var : variables.entrySet())
        {
            varstring.append(var.getKey());
            varstring.append("|");
            varstring.append(var.getValue());
        }

        return varstring.toString();
    }
}
