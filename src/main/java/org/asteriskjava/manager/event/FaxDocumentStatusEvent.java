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
 * A FaxDocumentStatusEvent is an event of Digium's Fax For Asterisk add-on.
 */
public class FaxDocumentStatusEvent extends AbstractFaxEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1L;
    private Integer documentNumber;
    private Integer lastError;
    private Integer pageCount;
    private Integer startPage;
    private Integer lastPageProcessed;
    private Integer retransmitCount;
    private Integer transferPels;
    private Integer transferRate;
    private String transferDuration;
    private Integer badLineCount;
    private String processedStatus;
    private String documentTime;
    private String localSid;
    private String localDis;
    private String remoteSid;
    private String remoteDis;


    public FaxDocumentStatusEvent(Object source)
    {
        super(source);
    }


    /**
     * @return the documentNumber
     */
    public Integer getDocumentNumber()
    {
        return documentNumber;
    }


    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(Integer documentNumber)
    {
        this.documentNumber = documentNumber;
    }


    /**
     * @return the lastError
     */
    public Integer getLastError()
    {
        return lastError;
    }


    /**
     * @param lastError the lastError to set
     */
    public void setLastError(Integer lastError)
    {
        this.lastError = lastError;
    }


    /**
     * @return the pageCount
     */
    public Integer getPageCount()
    {
        return pageCount;
    }


    /**
     * @param pageCount the pageCount to set
     */
    public void setPageCount(Integer pageCount)
    {
        this.pageCount = pageCount;
    }


    /**
     * @return the startPage
     */
    public Integer getStartPage()
    {
        return startPage;
    }


    /**
     * @param startPage the startPage to set
     */
    public void setStartPage(Integer startPage)
    {
        this.startPage = startPage;
    }


    /**
     * @return the lastPageProcessed
     */
    public Integer getLastPageProcessed()
    {
        return lastPageProcessed;
    }


    /**
     * @param lastPageProcessed the lastPageProcessed to set
     */
    public void setLastPageProcessed(Integer lastPageProcessed)
    {
        this.lastPageProcessed = lastPageProcessed;
    }


    /**
     * @return the retransmitCount
     */
    public Integer getRetransmitCount()
    {
        return retransmitCount;
    }


    /**
     * @param retransmitCount the retransmitCount to set
     */
    public void setRetransmitCount(Integer retransmitCount)
    {
        this.retransmitCount = retransmitCount;
    }


    /**
     * @return the transferPels
     */
    public Integer getTransferPels()
    {
        return transferPels;
    }


    /**
     * @param transferPels the transferPels to set
     */
    public void setTransferPels(Integer transferPels)
    {
        this.transferPels = transferPels;
    }


    /**
     * @return the transferRate
     */
    public Integer getTransferRate()
    {
        return transferRate;
    }


    /**
     * @param transferRate the transferRate to set
     */
    public void setTransferRate(Integer transferRate)
    {
        this.transferRate = transferRate;
    }


    /**
     * @return the transferDuration
     */
    public String getTransferDuration()
    {
        return transferDuration;
    }


    /**
     * @param transferDuration the transferDuration to set
     */
    public void setTransferDuration(String transferDuration)
    {
        this.transferDuration = transferDuration;
    }


    /**
     * @return the badLineCount
     */
    public Integer getBadLineCount()
    {
        return badLineCount;
    }


    /**
     * @param badLineCount the badLineCount to set
     */
    public void setBadLineCount(Integer badLineCount)
    {
        this.badLineCount = badLineCount;
    }


    /**
     * @return the processedStatus
     */
    public String getProcessedStatus()
    {
        return processedStatus;
    }


    /**
     * @param processedStatus the processedStatus to set
     */
    public void setProcessedStatus(String processedStatus)
    {
        this.processedStatus = processedStatus;
    }


    /**
     * @return the documentTime
     */
    public String getDocumentTime()
    {
        return documentTime;
    }


    /**
     * @param documentTime the documentTime to set
     */
    public void setDocumentTime(String documentTime)
    {
        this.documentTime = documentTime;
    }


    /**
     * @return the localSid
     */
    public String getLocalSid()
    {
        return localSid;
    }


    /**
     * @param localSid the localSid to set
     */
    public void setLocalSid(String localSid)
    {
        this.localSid = localSid;
    }


    /**
     * @return the localDis
     */
    public String getLocalDis()
    {
        return localDis;
    }


    /**
     * @param localDis the localDis to set
     */
    public void setLocalDis(String localDis)
    {
        this.localDis = localDis;
    }


    /**
     * @return the remoteSid
     */
    public String getRemoteSid()
    {
        return remoteSid;
    }


    /**
     * @param remoteSid the remoteSid to set
     */
    public void setRemoteSid(String remoteSid)
    {
        this.remoteSid = remoteSid;
    }


    /**
     * @return the remoteDis
     */
    public String getRemoteDis()
    {
        return remoteDis;
    }


    /**
     * @param remoteDis the remoteDis to set
     */
    public void setRemoteDis(String remoteDis)
    {
        this.remoteDis = remoteDis;
    }


}

