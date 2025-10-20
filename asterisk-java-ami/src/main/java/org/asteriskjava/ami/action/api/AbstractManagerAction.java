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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.asteriskjava.core.databind.annotation.AsteriskName;

import java.io.Serial;

/**
 * This class implements the {@link ManagerAction} interface and can serve as the base class for your concrete action
 * implementations.
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 * @since 1.0.0
 */
public abstract class AbstractManagerAction implements ManagerAction {
    @Serial
    private static final long serialVersionUID = -7667827187378395689L;

    @AsteriskName("ActionID")
    private String actionId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("action", getAction())
                .append("actionId", actionId)
                .toString();
    }
}
