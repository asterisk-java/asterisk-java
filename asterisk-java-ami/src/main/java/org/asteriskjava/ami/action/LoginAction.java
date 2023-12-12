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
package org.asteriskjava.ami.action;

import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.response.LoginResponse;

import java.io.Serial;
import java.util.EnumSet;

/**
 * The {@link LoginAction} authenticates the connection.
 * <p>
 * A successful login is the precondition for sending any other action except for the {@link ChallengeAction}.
 * <p>
 * An unsuccessful login results in an ManagerError being received from the server with a message set to
 * "Authentication failed" and the socket being closed by Asterisk.
 * <p>
 * Username and secret must be configured in Asterisk's {@code manager.conf}
 * <p>
 * Set to {@link EventMask#on} if all events should be sent, {@link EventMask#off} if not events should be sent or
 * combination of other flags from {@link EventMask}.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/Login/">Login</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/Login/">Login</a></li>
 * </ul>
 * <p>
 * Examples:
 * <pre>
 * Action: Login
 * ActionID: 12345
 * Username: admin
 * Secret: 123qwe
 * </pre>
 *
 * <pre>
 * Action: Login
 * ActionID: 12345
 * Username: admin
 * AuthType: MD5
 * Key: hashed-secret-123qwe
 * </pre>
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 * @since 1.0.0
 */
@ExpectedResponse(LoginResponse.class)
public class LoginAction extends AbstractManagerAction {
    @Serial
    private static final long serialVersionUID = -2600694249339115032L;

    private String username;
    private String secret;
    private AuthType authType;
    private String key;
    private EnumSet<EventMask> events;

    public LoginAction() {
    }

    /**
     * Creates a {@link LoginAction} that performs a clear text login.
     *
     * @param username the username configured in Asterisk's {@code manager.conf}
     * @param secret   the secret configured in Asterisk's {@code manager.conf}
     * @deprecated This method is not secure. <b>You should <u>NOT</u> use clear text secret if you are concerned about
     * security.</b> Use {@link ChallengeAction} or {@link #LoginAction(String, AuthType, String)} instead.
     */
    public LoginAction(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    /**
     * Creates a {@link LoginAction} that performs a login via challenge/response mechanism.<p>
     * <b>You should use this way to login to Asterisk Manager.</b>
     *
     * @param username the username configured in Asterisk's {@code manager.conf}
     * @param authType the digest algorithm, must match the digest algorithm that was used by {@link ChallengeAction}
     * @param key      the hash of the user's password and the challenge
     */
    public LoginAction(String username, AuthType authType, String key) {
        this.username = username;
        this.authType = authType;
        this.key = key;
    }

    @Override
    public String getAction() {
        return "Login";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    /**
     * @deprecated The secret is passed to Asterisk in <b>clear text</b>.
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public EnumSet<EventMask> getEvents() {
        return events;
    }

    public void setEvents(EnumSet<EventMask> events) {
        this.events = events;
    }
}
