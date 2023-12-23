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
package org.asteriskjava.ami.action.api.response;

import org.asteriskjava.ami.action.api.CommandAction;

import java.io.Serial;
import java.util.List;

/**
 * Corresponds to a {@link CommandAction} and contains list of the output lines.
 *
 * @author Stefan Reuter
 * @since 1.0.0
 */
public class CommandActionResponse extends ManagerActionResponse {
    @Serial
    private static final long serialVersionUID = 1L;

    private String privilege;
    private List<String> output;

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }
}
