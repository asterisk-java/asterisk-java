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
package org.asteriskjava.ami.action.annotation;

import org.asteriskjava.ami.action.api.ManagerAction;
import org.asteriskjava.ami.action.api.response.ManagerActionResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that an annotated {@link ManagerAction} expects a specific subclass of {@link ManagerActionResponse} when
 * executed successfully.
 *
 * @author Stefan Reuter
 * @since 1.0.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ExpectedResponse {
    /**
     * {@link ManagerActionResponse} subclass, to handle {@link ManagerAction} response.
     */
    Class<? extends ManagerActionResponse> value();
}
