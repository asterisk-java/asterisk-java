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
package org.asteriskjava.ami.action.response;

import org.asteriskjava.ami.action.CommandAction;

import java.util.List;

/**
 * Response that is received when sending a {@link CommandAction}.<p>
 * todo check END COMMAND
 * Asterisk's handling of the command action is generally quite hairy. It sends a "Response: Follows" line followed by
 * the raw output of the command including empty lines. At the end of the command output a line containing
 * "--END COMMAND--" is sent. The reader parses this response into a CommandResponse object to hide these details.
 *
 * @author Stefan Reuter
 * @see CommandAction
 */
public class CommandResponse extends ManagerResponse {
    private static final long serialVersionUID = 1L;

    //todo this is still in use?
    private String privilege;
    private List<String> outputs;

    /**
     * Returns the AMI authorization class of this response.
     */
    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    /**
     * Returns a List of strings representing the lines returned by the CLI command.
     */
    public List<String> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<String> outputs) {
        this.outputs = outputs;
    }
}
