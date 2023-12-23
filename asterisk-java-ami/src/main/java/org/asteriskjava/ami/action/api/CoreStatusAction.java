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
package org.asteriskjava.ami.action.api;

import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.api.response.CoreStatusActionResponse;

import java.io.Serial;

/**
 * The {@link CoreStatusActionResponse} requests a status summary from the server.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/CoreStatus/">CoreStatus</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/CoreStatus/">CoreStatus</a></li>
 * </ul>
 *
 * @author Stefan Reuter
 * @since 1.0.0
 */
@ExpectedResponse(CoreStatusActionResponse.class)
public class CoreStatusAction extends AbstractManagerAction {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String getAction() {
        return "CoreStatus";
    }
}
