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
import org.asteriskjava.ami.action.response.ChallengeActionResponse;

import java.io.Serial;

/**
 * The {@link ChallengeAction} requests a challenge from the server to use when logging in using challenge/response.
 * Sending this action to the Asterisk server results in a {@link ChallengeActionResponse} being received from the server.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/Challenge/">Challenge</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/Challenge/">Challenge</a></li>
 * </ul>
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 * @see ChallengeActionResponse
 * @since 1.0.0
 */
@ExpectedResponse(ChallengeActionResponse.class)
public class ChallengeAction extends AbstractManagerAction {
    @Serial
    private static final long serialVersionUID = 7240516124871953971L;

    private AuthType authType;

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
