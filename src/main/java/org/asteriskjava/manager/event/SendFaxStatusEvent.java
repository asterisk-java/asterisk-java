/*
 *  Copyright 2004-2006 Stefan Reuter
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
 * A SendFaxStatusEvent is an event of Digium's Fax For Asterisk add-on.
 */
public class SendFaxStatusEvent extends AbstractFaxEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1L;
    private String status;
    private String callerId;
    private String localStationId;
    private String fileName;


    public SendFaxStatusEvent(Object source)
    {
        super(source);
    }


    /**
     * @return the status
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * @param status the status to set
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * @return the callerId
     */
    public String getCallerId()
    {
        return callerId;
    }


    /**
     * @param callerId the callerId to set
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }


    /**
     * @return the localStationId
     */
    public String getLocalStationId()
    {
        return localStationId;
    }


    /**
     * @param localStationId the localStationId to set
     */
    public void setLocalStationId(String localStationId)
    {
        this.localStationId = localStationId;
    }


    /**
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


}

