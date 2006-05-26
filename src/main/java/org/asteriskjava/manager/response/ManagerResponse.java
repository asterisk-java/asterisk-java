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
package org.asteriskjava.manager.response;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Represents a response received from the Asterisk server as the result of a
 * previously sent ManagerAction.<p>
 * The response can be linked with the action that caused it by looking the
 * action id attribute that will match the action id of the corresponding
 * action.
 * 
 * @see org.asteriskjava.manager.action.ManagerAction
 * @author srt
 * @version $Id$
 */
public class ManagerResponse implements Serializable
{
    /**
     * Serial version identifier
     */
    static final long serialVersionUID = -935845815108584292L;

    private Date dateReceived;
    private String actionId;
    private String response;
    private String message;
    private String uniqueId;
    private Map<String, String> attributes;

    /**
     * Returns a Map with all attributes of this response.<p>
     * The keys are all lower case!
     * @see #getAttribute(String)
     */
    public Map<String, String> getAttributes()
    {
        return attributes;
    }

    /**
     * Sets the Map with all attributes.
     * @param attributes Map with containing the attributes with all lower 
     * case keys. 
     */
    public void setAttributes(Map<String, String> attributes)
    {
        this.attributes = attributes;
    }

    /**
     * Returns the value of the attribute with the given key.<p>
     * This is particulary important when a response contains special 
     * attributes that are dependent on the action that has been sent.<p>
     * An example of this is the response to the GetVarAction.
     * It contains the value of the channel variable as an attribute
     * stored under the key of the variable name.<p>
     * Example:
     * <pre>
     * GetVarAction action = new GetVarAction();
     * action.setChannel("SIP/1310-22c3");
     * action.setVariable("ALERT_INFO");
     * ManagerResponse response = connection.sendAction(action);
     * String alertInfo = response.getAttribute("ALERT_INFO");
     * </pre>
     * As all attributes are internally stored in lower case the key is
     * automatically converted to lower case before lookup.
     * 
     * @param key the key to lookup.
     * @return the value of the attribute stored under this key or
     *         <code>null</code> if there is no such attribute.
     */
    public String getAttribute(String key)
    {
        return attributes.get(key.toLowerCase());
    }

    /**
     * Returns the point in time this response was received from the asterisk
     * server.
     */
    public Date getDateReceived()
    {
        return dateReceived;
    }

    /**
     * Sets the point in time this response was received from the asterisk
     * server.
     */
    public void setDateReceived(Date dateReceived)
    {
        this.dateReceived = dateReceived;
    }

    /**
     * Returns the user provided action id of the ManagerAction that caused 
     * this response. If the application did not set an action id this method
     * returns <code>null</code>.
     * 
     * @return the action id of the ManagerAction that caused this response or
     *         <code>null</code> if none was set.
     * @see org.asteriskjava.manager.action.ManagerAction#setActionId()
     */
    public String getActionId()
    {
        return actionId;
    }

    /**
     * Sets the action id of the ManagerAction that caused this response.
     * 
     * @param actionId the action id of the ManagerAction that caused this
     *            response.
     */
    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }

    /**
     * Returns the message received with this response. The content depends on
     * the action that generated this response.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Sets the message.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Returns the value of the "Response:" line. This typically a String like
     * "Success" or "Error" but depends on the action that generated this
     * response.
     */
    public String getResponse()
    {
        return response;
    }

    /**
     * Sets the response.
     */
    public void setResponse(String response)
    {
        this.response = response;
    }

    /**
     * Returns the unique id received with this response. The unique id is used
     * to keep track of channels created by the action sent, for example an
     * OriginateAction.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id received with this response.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("actionId='" + getActionId() + "'; ");
        sb.append("message='" + getMessage() + "'; ");
        sb.append("response='" + getResponse() + "'; ");
        sb.append("uniqueId='" + getUniqueId() + "'; ");
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}
