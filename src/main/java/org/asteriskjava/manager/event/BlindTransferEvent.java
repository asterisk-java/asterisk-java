package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BlindTransferEvent extends AbstractBridgeEvent {
    private String transfererUniqueId;
    private String transfererConnectedLineNum;
    private String transfererConnectedLineName;
    private String transfererCallerIdName;
    private String transfererCallerIdNum;
    private String transfererChannel;
    private String transfererChannelState;
    private String transfererChannelStateDesc;
    private Integer transfererPriority;
    private Integer transfererContext;


    private String transfereeUniqueId;
    private String transfereeConnectedLineNum;
    private String transfereeConnectedLineName;
    private String transfereeCallerIdName;
    private String transfereeCallerIdNum;
    private String transfereeChannel;
    private String transfereeChannelState;
    private String transfereeChannelStateDesc;
    private Integer transfereePriority;
    private Integer transfereeContext;

    private String extension;
    private String isexternal;
    private String result;

    public BlindTransferEvent(Object source) { super(source); }

    public String getTransfererUniqueId() {
        return transfererUniqueId;
    }

    public void setTransfererUniqueId(String transfererUniqueId) {
        this.transfererUniqueId = transfererUniqueId;
    }

    public String getTransfererConnectedLineNum() {
        return transfererConnectedLineNum;
    }

    public void setTransfererConnectedLineNum(String transfererConnectedLineNum) {
        this.transfererConnectedLineNum = transfererConnectedLineNum;
    }

    public String getTransfererConnectedLineName() {
        return transfererConnectedLineName;
    }

    public void setTransfererConnectedLineName(String transfererConnectedLineName) {
        this.transfererConnectedLineName = transfererConnectedLineName;
    }

    public String getTransfererCallerIdName() {
        return transfererCallerIdName;
    }

    public void setTransfererCallerIdName(String transfererCallerIdName) {
        this.transfererCallerIdName = transfererCallerIdName;
    }

    public String getTransfererCallerIdNum() {
        return transfererCallerIdNum;
    }

    public void setTransfererCallerIdNum(String transfererCallerIdNum) {
        this.transfererCallerIdNum = transfererCallerIdNum;
    }

    public String getTransfererChannel() {
        return transfererChannel;
    }

    public void setTransfererChannel(String transfererChannel) {
        this.transfererChannel = transfererChannel;
    }

    public String getTransfererChannelState() {
        return transfererChannelState;
    }

    public void setTransfererChannelState(String transfererChannelState) {
        this.transfererChannelState = transfererChannelState;
    }

    public String getTransfererChannelStateDesc() {
        return transfererChannelStateDesc;
    }

    public void setTransfererChannelStateDesc(String transfererChannelStateDesc) {
        this.transfererChannelStateDesc = transfererChannelStateDesc;
    }

    public Integer getTransfererPriority() {
        return transfererPriority;
    }

    public void setTransfererPriority(Integer transfererPriority) {
        this.transfererPriority = transfererPriority;
    }

    public Integer getTransfererContext() {
        return transfererContext;
    }

    public void setTransfererContext(Integer transfererContext) {
        this.transfererContext = transfererContext;
    }

    public String getTransfereeUniqueId() {
        return transfereeUniqueId;
    }

    public void setTransfereeUniqueId(String transfereeUniqueId) {
        this.transfereeUniqueId = transfereeUniqueId;
    }

    public String getTransfereeConnectedLineNum() {
        return transfereeConnectedLineNum;
    }

    public void setTransfereeConnectedLineNum(String transfereeConnectedLineNum) {
        this.transfereeConnectedLineNum = transfereeConnectedLineNum;
    }

    public String getTransfereeConnectedLineName() {
        return transfereeConnectedLineName;
    }

    public void setTransfereeConnectedLineName(String transfereeConnectedLineName) {
        this.transfereeConnectedLineName = transfereeConnectedLineName;
    }

    public String getTransfereeCallerIdName() {
        return transfereeCallerIdName;
    }

    public void setTransfereeCallerIdName(String transfereeCallerIdName) {
        this.transfereeCallerIdName = transfereeCallerIdName;
    }

    public String getTransfereeCallerIdNum() {
        return transfereeCallerIdNum;
    }

    public void setTransfereeCallerIdNum(String transfereeCallerIdNum) {
        this.transfereeCallerIdNum = transfereeCallerIdNum;
    }

    public String getTransfereeChannel() {
        return transfereeChannel;
    }

    public void setTransfereeChannel(String transfereeChannel) {
        this.transfereeChannel = transfereeChannel;
    }

    public String getTransfereeChannelState() {
        return transfereeChannelState;
    }

    public void setTransfereeChannelState(String transfereeChannelState) {
        this.transfereeChannelState = transfereeChannelState;
    }

    public String getTransfereeChannelStateDesc() {
        return transfereeChannelStateDesc;
    }

    public void setTransfereeChannelStateDesc(String transfereeChannelStateDesc) {
        this.transfereeChannelStateDesc = transfereeChannelStateDesc;
    }

    public Integer getTransfereePriority() {
        return transfereePriority;
    }

    public void setTransfereePriority(Integer transfereePriority) {
        this.transfereePriority = transfereePriority;
    }

    public Integer getTransfereeContext() {
        return transfereeContext;
    }

    public void setTransfereeContext(Integer transfereeContext) {
        this.transfereeContext = transfereeContext;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getIsexternal() {
        return isexternal;
    }

    public void setIsexternal(String isexternal) {
        this.isexternal = isexternal;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
