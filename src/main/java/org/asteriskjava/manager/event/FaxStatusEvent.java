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
public class FaxStatusEvent extends AbstractFaxEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1L;
    private String operatingMode;
    private String result;
    private String error;
    private Double callDuration;
    private String ecmMode;
    private Integer dataRate;
    private String imageResolution;
    private String imageEncoding;
    private String pageSize;
    private Integer documentNumber;
    private Integer pageNumber;
    private String fileName;
    private Integer txPages;
    private Integer txBytes;
    private Integer totalTxLines;
    private Integer rxPages;
    private Integer rxBytes;
    private Integer totalRxLines;
    private Integer totalBadLines;
    private Integer disDcsDtcCtcCount;
    private Integer cfrCount;
    private Integer fttCount;
    private Integer mcfCount;
    private Integer pprCount;
    private Integer rtnCount;
    private Integer dcnCount;
    private String remoteStationId;
    private String localStationId;
    private String callerId;
    private String status;
    private String operation;

    public FaxStatusEvent(Object source)
    {
        super(source);
    }


    /**
     * @return the operatingMode
     */
    public String getOperatingMode()
    {
        return operatingMode;
    }

    /**
     * @param operatingMode the operatingMode to set
     */
    public void setOperatingMode(String operatingMode)
    {
        this.operatingMode = operatingMode;
    }

    /**
     * @return the result
     */
    public String getResult()
    {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * @return the error
     */
    public String getError()
    {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error)
    {
        this.error = error;
    }

    /**
     * @return the callDuration
     */
    public Double getCallDuration()
    {
        return callDuration;
    }

    /**
     * @param callDuration the callDuration to set
     */
    public void setCallDuration(Double callDuration)
    {
        this.callDuration = callDuration;
    }

    /**
     * @return the ecmMode
     */
    public String getEcmMode()
    {
        return ecmMode;
    }

    /**
     * @param ecmMode the ecmMode to set
     */
    public void setEcmMode(String ecmMode)
    {
        this.ecmMode = ecmMode;
    }

    /**
     * @return the dataRate
     */
    public Integer getDataRate()
    {
        return dataRate;
    }

    /**
     * @param dataRate the dataRate to set
     */
    public void setDataRate(Integer dataRate)
    {
        this.dataRate = dataRate;
    }

    /**
     * @return the imageResolution
     */
    public String getImageResolution()
    {
        return imageResolution;
    }

    /**
     * @param imageResolution the imageResolution to set
     */
    public void setImageResolution(String imageResolution)
    {
        this.imageResolution = imageResolution;
    }

    /**
     * @return the imageEncoding
     */
    public String getImageEncoding()
    {
        return imageEncoding;
    }

    /**
     * @param imageEncoding the imageEncoding to set
     */
    public void setImageEncoding(String imageEncoding)
    {
        this.imageEncoding = imageEncoding;
    }

    /**
     * @return the pageSize
     */
    public String getPageSize()
    {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(String pageSize)
    {
        this.pageSize = pageSize;
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
     * @return the pageNumber
     */
    public Integer getPageNumber()
    {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(Integer pageNumber)
    {
        this.pageNumber = pageNumber;
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

    /**
     * @return the txPages
     */
    public Integer getTxPages()
    {
        return txPages;
    }

    /**
     * @param txPages the txPages to set
     */
    public void setTxPages(Integer txPages)
    {
        this.txPages = txPages;
    }

    /**
     * @return the txBytes
     */
    public Integer getTxBytes()
    {
        return txBytes;
    }

    /**
     * @param txBytes the txBytes to set
     */
    public void setTxBytes(Integer txBytes)
    {
        this.txBytes = txBytes;
    }

    /**
     * @return the totalTxLines
     */
    public Integer getTotalTxLines()
    {
        return totalTxLines;
    }

    /**
     * @param totalTxLines the totalTxLines to set
     */
    public void setTotalTxLines(Integer totalTxLines)
    {
        this.totalTxLines = totalTxLines;
    }

    /**
     * @return the rxPages
     */
    public Integer getRxPages()
    {
        return rxPages;
    }

    /**
     * @param rxPages the rxPages to set
     */
    public void setRxPages(Integer rxPages)
    {
        this.rxPages = rxPages;
    }

    /**
     * @return the rxBytes
     */
    public Integer getRxBytes()
    {
        return rxBytes;
    }

    /**
     * @param rxBytes the rxBytes to set
     */
    public void setRxBytes(Integer rxBytes)
    {
        this.rxBytes = rxBytes;
    }

    /**
     * @return the totalRxLines
     */
    public Integer getTotalRxLines()
    {
        return totalRxLines;
    }

    /**
     * @param totalRxLines the totalRxLines to set
     */
    public void setTotalRxLines(Integer totalRxLines)
    {
        this.totalRxLines = totalRxLines;
    }

    /**
     * @return the totalBadLines
     */
    public Integer getTotalBadLines()
    {
        return totalBadLines;
    }

    /**
     * @param totalBadLines the totalBadLines to set
     */
    public void setTotalBadLines(Integer totalBadLines)
    {
        this.totalBadLines = totalBadLines;
    }

    /**
     * @return the disDcsDtcCtcCount
     */
    public Integer getDisDcsDtcCtcCount()
    {
        return disDcsDtcCtcCount;
    }

    /**
     * @param disDcsDtcCtcCount the disDcsDtcCtcCount to set
     */
    public void setDisDcsDtcCtcCount(Integer disDcsDtcCtcCount)
    {
        this.disDcsDtcCtcCount = disDcsDtcCtcCount;
    }

    /**
     * @return the cfrCount
     */
    public Integer getCfrCount()
    {
        return cfrCount;
    }

    /**
     * @param cfrCount the cfrCount to set
     */
    public void setCfrCount(Integer cfrCount)
    {
        this.cfrCount = cfrCount;
    }

    /**
     * @return the fttCount
     */
    public Integer getFttCount()
    {
        return fttCount;
    }

    /**
     * @param fttCount the fttCount to set
     */
    public void setFttCount(Integer fttCount)
    {
        this.fttCount = fttCount;
    }

    /**
     * @return the mcfCount
     */
    public Integer getMcfCount()
    {
        return mcfCount;
    }

    /**
     * @param mcfCount the mcfCount to set
     */
    public void setMcfCount(Integer mcfCount)
    {
        this.mcfCount = mcfCount;
    }

    /**
     * @return the pprCount
     */
    public Integer getPprCount()
    {
        return pprCount;
    }

    /**
     * @param pprCount the pprCount to set
     */
    public void setPprCount(Integer pprCount)
    {
        this.pprCount = pprCount;
    }

    /**
     * @return the rtnCount
     */
    public Integer getRtnCount()
    {
        return rtnCount;
    }

    /**
     * @param rtnCount the rtnCount to set
     */
    public void setRtnCount(Integer rtnCount)
    {
        this.rtnCount = rtnCount;
    }

    /**
     * @return the dcnCount
     */
    public Integer getDcnCount()
    {
        return dcnCount;
    }

    /**
     * @param dcnCount the dcnCount to set
     */
    public void setDcnCount(Integer dcnCount)
    {
        this.dcnCount = dcnCount;
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
     * @return the callerId
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * @param callerId the callerId to set
     */
    public void setCallerId(String callerid)
    {
        this.callerId = callerid;
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
     * @return the operation
     */
    public String getOperation()
    {
        return operation;
    }

    /**
     * @param context the context to set
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
}

