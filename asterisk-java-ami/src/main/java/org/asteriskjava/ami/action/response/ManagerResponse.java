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
package org.asteriskjava.ami.action.response;

import org.asteriskjava.ami.action.ManagerAction;

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
 * @see ManagerAction
 * @since 1.0.0
 */
public class ManagerResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private ResponseType response;

    private Instant dateReceived;

    private String actionId;

    /**
     * Asterisk argument: {@code Response}.
     */
    public ResponseType getResponse() {
        return response;
    }

    public void setResponse(ResponseType response) {
        this.response = response;
    }

    /**
     * Returns the point in time when this response was received from the Asterisk server.
     */
    public Instant getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Instant dateReceived) {
        this.dateReceived = dateReceived;
    }

    /**
     * Asterisk argument: {@code ActionID}.
     * <p>
     * Returns the user provided action id of the {@link ManagerAction} that caused this response. If the application
     * did not set an {@code ActionID} this method returns {@code null}.
     */
    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /*---------------*/

    /**
     * The server from which this response has been received (only used with
     * AstManProxy).
     */
    private String server;
    private String eventList;
    private String message;
    private String uniqueId;
    private String events;
    private Map<String, Object> attributes;
    private String output;

    public ManagerResponse() {
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
     * Sent for manager events that reply with a list of events.
     *
     * @return "start", "stop" or "complete"
     * @since 1.0.0
     */
    public String getEventList() {
        return eventList;
    }

    /**
     * Sets the eventList.
     *
     * @param eventList the eventList.
     * @since 1.0.0
     */
    public void setEventList(String eventList) {
        this.eventList = eventList;
    }

    /**
     * Returns the message received with this response. The content depends on
     * the action that generated this response.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     */
    public void setMessage(String message) {
        this.message = message;
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

    @Override
    public String toString() {
        StringBuilder sb;

        sb = new StringBuilder(100);
        sb.append(getClass().getName()).append(": ");
        sb.append("actionId='").append(getActionId()).append("'; ");
        sb.append("message='").append(getMessage()).append("'; ");
        sb.append("response='").append(getResponse()).append("'; ");
        sb.append("uniqueId='").append(getUniqueId()).append("'; ");
        sb.append("systemHashcode=").append(System.identityHashCode(this));

        return sb.toString();
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
