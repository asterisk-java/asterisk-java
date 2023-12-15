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
import org.asteriskjava.ami.action.response.EmptyActionResponse;

import java.io.Serial;

/**
 * The action causes the server to close the connection.
 *
 * @author Stefan Reuter
 * @since 1.0.0
 */
@ExpectedResponse(EmptyActionResponse.class)
public class LogoffAction extends AbstractManagerAction {
    @Serial
    private static final long serialVersionUID = -7576797478570238525L;

    @Override
    public String getAction() {
        return "Logoff";
    }
}
