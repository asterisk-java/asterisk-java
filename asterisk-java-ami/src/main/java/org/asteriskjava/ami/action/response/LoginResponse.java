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

import org.asteriskjava.ami.action.LoginAction;

/**
 * Corresponds to a {@link LoginAction} and contains information about login action.
 *
 * @author Piotr Olaszewski
 * @see LoginAction
 * @since 4.0.0
 */
public class LoginResponse extends ManagerActionResponse {
    private String message;

    /**
     * Returns the message received with this response. The content depends on the action that generated this response.
     */
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
