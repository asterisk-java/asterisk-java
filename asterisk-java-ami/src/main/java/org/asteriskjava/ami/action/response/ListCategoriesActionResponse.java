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

import org.asteriskjava.ami.action.ListCategoriesAction;
import org.asteriskjava.core.databind.annotation.AsteriskName;

import java.util.List;

/**
 * Corresponds to a {@link ListCategoriesAction} and contains all available categories in passed configuration file.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class ListCategoriesActionResponse extends ManagerActionResponse {
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    @AsteriskName("Category")
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
