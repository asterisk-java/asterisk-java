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

import org.asteriskjava.ami.utils.ActionsRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.containers.BindMode.READ_ONLY;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;

@Testcontainers
public abstract class BaseActionItTest {
    @SuppressWarnings("rawtypes")
    @Container
    protected static final GenericContainer<?> asteriskDocker = new GenericContainer("andrius/asterisk:20.16.0_debian-trixie-dev")
            .withClasspathResourceMapping("/manager.conf", "/etc/asterisk/manager.conf", READ_ONLY)
            .withClasspathResourceMapping("/queues.conf", "/etc/asterisk/queues.conf", READ_ONLY)
            .withAccessToHost(true)
            .withExposedPorts(5038)
            .waitingFor(forLogMessage(".*Asterisk Ready.*", 1));

    protected static ActionsRunner actionRunner() {
        return new ActionsRunner(asteriskDocker);
    }
}
