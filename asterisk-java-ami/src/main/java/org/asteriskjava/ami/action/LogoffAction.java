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
import org.asteriskjava.ami.action.response.LogoffResponse;

/**
 * The {@link LogoffAction} causes the server to close the connection.<p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>16 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+16+ManagerAction_Logoff">Logoff</a></li>
 *     <li>18 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+18+ManagerAction_Logoff">Logoff</a></li>
 *     <li>20 - <a href="https://wiki.asterisk.org/wiki/display/AST/Asterisk+20+ManagerAction_Logoff">Logoff</a></li>
 * </ul>
 *
 * @author Stefan Reuter
 * @see LogoffResponse
 */
@ExpectedResponse(LogoffResponse.class)
public class LogoffAction extends AbstractManagerAction {
    private static final long serialVersionUID = -7576797478570238525L;

    public LogoffAction() {
    }

    @Override
    public String getAction() {
        return "Logoff";
    }
}
