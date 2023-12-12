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

/**
 * Manager event flags.
 * <p>
 * This flags allows filter what kind of events will be received for this connection.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public enum EventMask {
    /**
     * System events such as module load/unload.
     */
    system,
    /**
     * Call event, such as state change, etc.
     */
    call,
    /**
     * Log events.
     */
    log,
    /**
     * Verbose messages.
     */
    verbose,
    /**
     * Ability to read/set commands.
     */
    command,
    /**
     * Ability to read/set agent info.
     */
    agent,
    /**
     * Ability to read/set user info.
     */
    user,
    /**
     * Ability to modify configurations.
     */
    config,
    /**
     * Ability to read DTMF events.
     */
    dtmf,
    /**
     * Reporting events such as rtcp sent.
     */
    reporting,
    /**
     * CDR events.
     */
    cdr,
    /**
     * Dialplan events ({@code VarSet}, {@code NewExten}).
     */
    dialplan,
    /**
     * Originate a call to an extension.
     */
    originate,
    /**
     * AGI events.
     */
    agi,
    /**
     * Call Completion events.
     */
    cc,
    /**
     * Advice Of Charge events.
     */
    aoc,
    /**
     * Test event used to signal the Asterisk Test Suite.
     */
    test,
    /**
     * Security Message as AMI Event.
     */
    security,
    /**
     * MESSAGE events.
     */
    message,
    /**
     * Allows to receive all events.
     */
    on,
    /**
     * Disable receiving events.
     */
    off,
}
