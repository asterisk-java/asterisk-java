/*
 * Copyright 2004-2023 Asterisk Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.ami.action.api.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.asteriskjava.ami.action.api.ManagerAction;
import org.asteriskjava.core.databind.annotation.AsteriskName;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a response received from the Asterisk server as the result of a previously sent {@link ManagerAction}.
 * <p>
 * The response can be linked with the action that caused it by looking at the {@code ActionID} attribute, which will
 * match the action id of the corresponding action.
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 * @since 1.0.0
 */
public class ManagerActionResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private ResponseType response;
    private Instant dateReceived;
    @AsteriskName("ActionID")
    private String actionId;
    private String message;

    public ResponseType getResponse() {
        return response;
    }

    public void setResponse(ResponseType response) {
        this.response = response;
    }

    public Instant getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Instant dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ManagerActionResponse that = (ManagerActionResponse) o;

        return new EqualsBuilder()
                .append(response, that.response)
                .append(dateReceived, that.dateReceived)
                .append(actionId, that.actionId)
                .append(message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(response)
                .append(dateReceived)
                .append(actionId)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("response", response)
                .append("dateReceived", dateReceived)
                .append("actionId", actionId)
                .append("message", message)
                .toString();
    }

    /*------LEGACY BELOW---------*/

    /**
     * The server from which this response has been received (only used with
     * AstManProxy).
     */
    private String server;
    private String eventList;
    private String uniqueId;
    private Map<String, Object> attributes;
    private String output;

    public ManagerActionResponse() {
        this.attributes = null;
    }

    /**
     * Returns a Map with all attributes of this response.
     * <p>
     * The keys are all lower case!
     *
     * @see #getAttribute(String)
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Sets the Map with all attributes.
     *
     * @param attributes Map with containing the attributes with all lower case
     *                   keys.
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Returns the value of the attribute with the given key.
     * <p>
     * This is particulary important when a response contains special attributes
     * that are dependent on the action that has been sent.
     * <p>
     * An example of this is the response to the GetVarAction. It contains the
     * value of the channel variable as an attribute stored under the key of the
     * variable name.
     * <p>
     * Example:
     *
     * <pre>
     * GetVarAction action = new GetVarAction();
     * action.setChannel("SIP/1310-22c3");
     * action.setVariable("ALERT_INFO");
     * ManagerResponse response = connection.sendAction(action);
     * String alertInfo = response.getAttribute("ALERT_INFO");
     * </pre>
     * <p>
     * As all attributes are internally stored in lower case the key is
     * automatically converted to lower case before lookup.
     *
     * @param key the key to lookup.
     * @return the value of the attribute stored under this key or
     * <code>null</code> if there is no such attribute.
     */
    public String getAttribute(String key) {
        return (String) attributes.get(key.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Returns the name of the Asterisk server from which this response has been
     * received. <br>
     * This property is only available when using to AstManProxy.
     *
     * @return the name of the Asterisk server from which this response has been
     * received or <code>null</code> when directly connected to an
     * Asterisk server instead of AstManProxy.
     * @since 1.0.0
     */
    public final String getServer() {
        return server;
    }

    /**
     * Sets the name of the Asterisk server from which this response has been
     * received.
     *
     * @param server the name of the Asterisk server from which this response
     *               has been received.
     * @since 1.0.0
     */
    public final void setServer(String server) {
        this.server = server;
    }

    /**
     * Returns the unique id received with this response. The unique id is used
     * to keep track of channels created by the action sent, for example an
     * OriginateAction.
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * Sets the unique id received with this response.
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    protected Integer stringToInteger(String s, String suffix) throws NumberFormatException {
        if (s == null || s.length() == 0) {
            return null;
        }

        if (suffix != null && s.endsWith(suffix)) {
            return Integer.parseInt(s.substring(0, s.length() - suffix.length()).trim());
        }

        return Integer.parseInt(s.trim());
    }

    protected Long stringToLong(String s, String suffix) throws NumberFormatException {
        if (s == null || s.length() == 0) {
            return null;
        }

        if (suffix != null && s.endsWith(suffix)) {
            return Long.parseLong(s.substring(0, s.length() - suffix.length()).trim());
        }

        return Long.parseLong(s.trim());
    }
}
