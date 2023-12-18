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

/**
 * Creates an empty file in the configuration directory.
 * <p>
 * This action will create an empty file in the configuration directory. This action is intended to be used before an
 * UpdateConfig action.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/CreateConfig/">CreateConfig</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/CreateConfig/">CreateConfig</a></li>
 * </ul>
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
@ExpectedResponse(EmptyActionResponse.class)
public class CreateConfigAction extends AbstractManagerAction {
    private String filename;

    @Override
    public String getAction() {
        return "CreateConfig";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
