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
package org.asteriskjava.core.databind.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Collection;

import static java.util.stream.Collectors.joining;
import static org.asteriskjava.core.databind.CodersConsts.listSeparator;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class CommaConverter extends StdConverter<Collection<Object>, String> {
    @Override
    public String convert(Collection<Object> value) {
        return value.stream()
                .map(String::valueOf)
                .collect(joining(listSeparator));
    }
}
