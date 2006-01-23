/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.manager.response;

/**
 * Corresponds to a ChallengeAction and contains the challenge needed to log in using
 * challenge/response.
 * 
 * @see net.sf.asterisk.manager.action.ChallengeAction
 * @see net.sf.asterisk.manager.action.LoginAction
 * 
 * @author srt
 * @version $Id: ChallengeResponse.java,v 1.2 2005/02/23 22:50:58 srt Exp $
 */
public class ChallengeResponse extends ManagerResponse
{
    /**
     * Serial version identifier
     */
    static final long serialVersionUID = -7253724086340850957L;

    private String challenge;

    /**
     * Returns the challenge to use when creating the key for log in.
     * 
     * @see net.sf.asterisk.manager.action.LoginAction#key
     */
    public String getChallenge()
    {
        return challenge;
    }

    /**
     * Sets the challenge to use when creating the key for log in.
     */
    public void setChallenge(String challenge)
    {
        this.challenge = challenge;
    }
}
