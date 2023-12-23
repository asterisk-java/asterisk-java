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
import org.asteriskjava.ami.action.api.response.GetConfigJsonActionResponse;

/**
 * Retrieve configuration (JSON format).
 * <p>
 * This action will dump the contents of a configuration file by category and contents in JSON format or optionally by
 * specified category only. This only makes sense to be used rawman over the HTTP interface. In the case where a
 * category name is non-unique, a filter may be specified to match only categories with matching variable values.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/GetConfigJSON/">GetConfigJSON</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/GetConfigJSON/">GetConfigJSON</a></li>
 * </ul>
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
@ExpectedResponse(GetConfigJsonActionResponse.class)
public class GetConfigJsonAction extends AbstractManagerAction {
    private String filename;
    private String category;
    private String filter;

    @Override
    public String getAction() {
        return "GetConfigJSON";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
