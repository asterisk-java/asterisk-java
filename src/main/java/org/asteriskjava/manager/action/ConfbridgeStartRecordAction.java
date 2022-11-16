/*
 * Copyright 2004-2022 Asterisk-Java contributors
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
package org.asteriskjava.manager.action;

/**
 * The ConfbridgeStartAction starts an audio recording of a conference.
 *
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeStartRecordAction extends AbstractManagerAction {
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 3059632508521358701L;

    private String conference;
    private String recordFile;

    /**
     * Creates a new empty ConfbridgeStartRecordAction.
     */
    public ConfbridgeStartRecordAction() {
        super();
    }

    /**
     * Creates a new ConfbridgeStartRecordAction for a specific conference.
     */
    public ConfbridgeStartRecordAction(String conference) {
        this.setConference(conference);
    }

    /**
     * Returns the name of this action, i.e. "ConfbridgeStartRecord".
     */
    @Override
    public String getAction() {
        return "ConfbridgeStartRecord";
    }

    /**
     * Sets the id of the conference for which to start an audio recording.
     */
    public void setConference(String conference) {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference for which to start an audio recording.
     */
    public String getConference() {
        return conference;
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile;
    }
}
