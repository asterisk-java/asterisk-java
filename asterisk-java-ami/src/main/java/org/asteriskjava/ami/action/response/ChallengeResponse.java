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

import org.asteriskjava.ami.action.ChallengeAction;

import java.io.Serial;

/**
 * Corresponds to a {@link ChallengeAction} and contains the challenge needed to log in using challenge/response.
 *
 * @author Stefan Reuter
 * @see ChallengeAction
 * @since 1.0.0
 */
public class ChallengeResponse extends ManagerResponse {
    @Serial
    private static final long serialVersionUID = -7253724086340850957L;

    private String challenge;

    /**
     * Returns the challenge to use when creating the key for log in.
     */
    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
