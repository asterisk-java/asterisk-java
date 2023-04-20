/*
 * Copyright 2004-2022 Asterisk Java contributors
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
import org.asteriskjava.ami.databind.annotation.AsteriskSerialize;
import org.asteriskjava.ami.databind.serializer.ComaJoiningSerializer;

import java.util.EnumSet;

/**
 * The {@link LoginAction} authenticates the connection.<p>
 * A successful login is the precondition for sending any other action except for the {@link ChallengeAction}.<p>
 * An unsuccessful login results in an ManagerError being received from the server with a message set to
 * "Authentication failed" and the socket being closed by Asterisk.<p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>16 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+16+ManagerAction_Login">Login</a></li>
 *     <li>18 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+ManagerAction_Login">Login</a></li>
 *     <li>20 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+20+ManagerAction_Login">Login</a></li>
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
 * @see ChallengeAction
 * @see AuthType
 * @see LoginResponse
 */
@ExpectedResponse(LoginResponse.class)
public class LoginAction extends AbstractManagerAction {
    private static final long serialVersionUID = -2600694249339115032L;

    private String username;
    private String secret;
    private AuthType authType;
    private String key;
    private EnumSet<EventMask> events;

    public LoginAction() {
    }

    /**
     * Creates a {@link LoginAction} that performs a clear text login.<p>
     * <b>You should <u>NOT</u> use clear text secret if you are concerned about security.</b><p>
     * Use {@link ChallengeAction} or {@link #LoginAction(String, AuthType, String)} instead.
     *
     * @param username the username configured in Asterisk's {@code manager.conf}
     * @param secret   the secret configured in Asterisk's {@code manager.conf}
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
     * @see AuthType
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

    /**
     * Asterisk argument: {@code Username}.<p>
     * Username must be configured in Asterisk's {@code manager.conf}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets Asterisk argument: {@code Username}.<p>
     * Username must be configured in Asterisk's {@code manager.conf}.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Asterisk argument: {@code Secret}.<p>
     * Secret must be configured in Asterisk's {@code manager.conf}.
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets Asterisk argument: {@code Secret}.<p>
     * Secret must be configured in Asterisk's {@code manager.conf}.<p>
     * <b>The secret is passed to Asterisk in clear text.</b>
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Asterisk argument: {@code AuthType}.<p>
     */
    public AuthType getAuthType() {
        return authType;
    }

    /**
     * Sets Asterisk argument: {@code AuthType}.<p>
     * The digest algorithm is used to create the key based on the challenge and the user's password.<p>
     * Currently, Asterisk supports only {@code MD5}.
     */
    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    /**
     * Asterisk argument: {@code Key}.<p>
     * Hash of the user's password and the challenge.
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets Asterisk argument: {@code Key}.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Asterisk argument: {@code Events}.<p>
     */
    @AsteriskSerialize(using = ComaJoiningSerializer.class)
    public EnumSet<EventMask> getEvents() {
        return events;
    }

    /**
     * Sets Asterisk argument: {@code Events}.<p>
     * Specify what kind of events should be sent.<p>
     * Set to {@link EventMask#on} if all events should be sent, {@link EventMask#off} if not events should be sent or
     * combination of other flags from {@link EventMask}.
     */
    public void setEvents(EnumSet<EventMask> events) {
        this.events = events;
    }
}
