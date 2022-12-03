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
import org.asteriskjava.ami.action.response.ChallengeResponse;

/**
 * The {@link ChallengeAction} requests a challenge from the server to use when logging in using challenge/response.
 * Sending this action to the Asterisk server results in a {@link ChallengeResponse} being received from the server.<p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>16 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+16+ManagerAction_Challenge">Challenge</a></li>
 *     <li>18 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+ManagerAction_Challenge">Challenge</a></li>
 *     <li>20 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+20+ManagerAction_Challenge">Challenge</a></li>
 * </ul>
 *
 * @author Stefan Reuter
 * @see ChallengeResponse
 */
@ExpectedResponse(ChallengeResponse.class)
public class ChallengeAction extends AbstractManagerAction {
    private static final long serialVersionUID = 7240516124871953971L;

    private AuthType authType;

    public ChallengeAction() {
    }

    /**
     * Creates a {@link ChallengeAction} that requests a new login challenge for use with the given digest algorithm.
     *
     * @param authType the digest algorithm, must match the digest algorithm that was used by {@link ChallengeAction}
     */
    public ChallengeAction(AuthType authType) {
        this.authType = authType;
    }

    @Override
    public String getAction() {
        return "Challenge";
    }

    /**
     * Asterisk argument: {@code AuthType}.
     */
    public AuthType getAuthType() {
        return authType;
    }

    /**
     * Sets Asterisk argument: {@code AuthType}.
     */
    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }
}
