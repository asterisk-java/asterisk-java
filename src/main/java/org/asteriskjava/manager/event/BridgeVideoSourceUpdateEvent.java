/*
 *  Copyright 2023 Hector Espert
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.event;

/**
 * Raised when the channel that is the source of video in a bridge changes.
 */
public class BridgeVideoSourceUpdateEvent extends AbstractBridgeEvent {
    private String bridgePreviousVideoSource;

    public BridgeVideoSourceUpdateEvent(Object source) {
        super(source);
    }

    /**
     * Gets the unique ID of the channel that was the video source.
     *
     * @return the unique ID of the channel that was the video source.
     */
    public String getBridgePreviousVideoSource() {
        return bridgePreviousVideoSource;
    }

    public void setBridgePreviousVideoSource(String bridgePreviousVideoSource) {
        this.bridgePreviousVideoSource = bridgePreviousVideoSource;
    }
}
