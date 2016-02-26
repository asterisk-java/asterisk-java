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
 * A SendFaxEvent is an event of Digium's Fax For Asterisk add-on.
 */
public class SendFaxEvent extends AbstractFaxEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1L;
    private String callerId;
    private String localStationId;
    private String remoteStationId;
    private String pagesTransferred;
    private String resolution;
    private String transferRate;
    private String fileName;


    public SendFaxEvent(Object source)
    {
        super(source);
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
     * @return the remoteStationId
     */
    public String getRemoteStationId()
    {
        return remoteStationId;
    }


    /**
     * @param remoteStationId the remoteStationId to set
     */
    public void setRemoteStationId(String remoteStationId)
    {
        this.remoteStationId = remoteStationId;
    }


    /**
     * @return the pagesTransferred
     */
    public String getPagesTransferred()
    {
        return pagesTransferred;
    }


    /**
     * @param pagesTransferred the pagesTransferred to set
     */
    public void setPagesTransferred(String pagesTransferred)
    {
        this.pagesTransferred = pagesTransferred;
    }


    /**
     * @return the resolution
     */
    public String getResolution()
    {
        return resolution;
    }


    /**
     * @param resolution the resolution to set
     */
    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }


    /**
     * @return the transferRate
     */
    public String getTransferRate()
    {
        return transferRate;
    }


    /**
     * @param transferRate the transferRate to set
     */
    public void setTransferRate(String transferRate)
    {
        this.transferRate = transferRate;
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

