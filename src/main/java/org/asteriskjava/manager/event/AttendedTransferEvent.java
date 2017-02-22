package org.asteriskjava.manager.event;

/**
 * Raised when attended transfer is complete
 *
 * https://wiki.asterisk.org/wiki/display/AST/Asterisk+13+ManagerEvent_AttendedTransfer
 */
public class AttendedTransferEvent extends AbstractBridgeEvent {
    private static final long serialVersionUID = 1L;

    private String    origTransfererChannel;
    private String    origTransfererChannelState;
    private String    origTransfererChannelStateDesc;
    private String    origTransfererCallerIDNum;
    private String    origTransfererCallerIDName;
    private String    origTransfererConnectedLineNum;
    private String    origTransfererConnectedLineName;
    private String    origTransfererAccountCode;
    private String    origTransfererContext;
    private String    origTransfererExten;
    private String    origTransfererPriority;
    private String    origTransfererUniqueid;
    private String    origBridgeUniqueid;
    private String    origBridgeType;
    private String    origBridgeTechnology;
    private String    origBridgeCreator;
    private String    origBridgeName;
    private String    origBridgeNumChannels;
    private String    secondTransfererChannel;
    private String    secondTransfererChannelState;
    private String    secondTransfererChannelStateDesc;
    private String    secondTransfererCallerIDNum;
    private String    secondTransfererCallerIDName;
    private String    secondTransfererConnectedLineNum;
    private String    secondTransfererConnectedLineName;
    private String    secondTransfererAccountCode;
    private String    secondTransfererContext;
    private String    secondTransfererExten;
    private String    secondTransfererPriority;
    private String    secondTransfererUniqueid;
    private String    secondBridgeUniqueid;
    private String    secondBridgeType;
    private String    secondBridgeTechnology;
    private String    secondBridgeCreator;
    private String    secondBridgeName;
    private String    secondBridgeNumChannels;
    private String    destType;
    private String    destBridgeUniqueid;
    private String    destApp;
    private String    localOneChannel;
    private String    localOneChannelState;
    private String    localOneChannelStateDesc;
    private String    localOneCallerIDNum;
    private String    localOneCallerIDName;
    private String    localOneConnectedLineNum;
    private String    localOneConnectedLineName;
    private String    localOneAccountCode;
    private String    localOneContext;
    private String    localOneExten;
    private String    localOnePriority;
    private String    localOneUniqueid;
    private String    localTwoChannel;
    private String    localTwoChannelState;
    private String    localTwoChannelStateDesc;
    private String    localTwoCallerIDNum;
    private String    localTwoCallerIDName;
    private String    localTwoConnectedLineNum;
    private String    localTwoConnectedLineName;
    private String    localTwoAccountCode;
    private String    localTwoContext;
    private String    localTwoExten;
    private String    localTwoPriority;
    private String    localTwoUniqueid;
    private String    destTransfererChannel;
    private String    transfereeChannel;
    private String    transfereeChannelState;
    private String    transfereeChannelStateDesc;
    private String    transfereeCallerIDNum;
    private String    transfereeCallerIDName;
    private String    transfereeConnectedLineNum;
    private String    transfereeConnectedLineName;
    private String    transfereeAccountCode;
    private String    transfereeContext;
    private String    transfereeExten;
    private String    transfereePriority;
    private String    transfereeUniqueid;

    private String  transfereeLinkedId;
    private String  transfereeLanguage;
    private String  origTransfererLinkedId;
    private String  secondTransfererLanguage;
    private String  isexternal;
    private String  result;
    private String  secondTransfererLinkedId;
    private String  origTransfererLanguage;
    
    public AttendedTransferEvent(Object source)
    {
        super(source);
    }

    public String getOrigTransfererChannel()
    {
        return origTransfererChannel;
    }

    public void setOrigTransfererChannel(String origTransfererChannel)
    {
        this.origTransfererChannel = origTransfererChannel;
    }

    public String getOrigTransfererChannelState()
    {
        return origTransfererChannelState;
    }

    public void setOrigTransfererChannelState(String origTransfererChannelState)
    {
        this.origTransfererChannelState = origTransfererChannelState;
    }

    public String getOrigTransfererChannelStateDesc()
    {
        return origTransfererChannelStateDesc;
    }

    public void setOrigTransfererChannelStateDesc(String origTransfererChannelStateDesc)
    {
        this.origTransfererChannelStateDesc = origTransfererChannelStateDesc;
    }

    public String getOrigTransfererCallerIDNum()
    {
        return origTransfererCallerIDNum;
    }

    public void setOrigTransfererCallerIDNum(String origTransfererCallerIDNum)
    {
        this.origTransfererCallerIDNum = origTransfererCallerIDNum;
    }

    public String getOrigTransfererCallerIDName()
    {
        return origTransfererCallerIDName;
    }

    public void setOrigTransfererCallerIDName(String origTransfererCallerIDName)
    {
        this.origTransfererCallerIDName = origTransfererCallerIDName;
    }

    public String getOrigTransfererConnectedLineNum()
    {
        return origTransfererConnectedLineNum;
    }

    public void setOrigTransfererConnectedLineNum(String origTransfererConnectedLineNum)
    {
        this.origTransfererConnectedLineNum = origTransfererConnectedLineNum;
    }

    public String getOrigTransfererConnectedLineName()
    {
        return origTransfererConnectedLineName;
    }

    public void setOrigTransfererConnectedLineName(String origTransfererConnectedLineName)
    {
        this.origTransfererConnectedLineName = origTransfererConnectedLineName;
    }

    public String getOrigTransfererAccountCode()
    {
        return origTransfererAccountCode;
    }

    public void setOrigTransfererAccountCode(String origTransfererAccountCode) {
        this.origTransfererAccountCode = origTransfererAccountCode;
    }

    public String getOrigTransfererContext()
    {
        return origTransfererContext;
    }

    public void setOrigTransfererContext(String origTransfererContext)
    {
        this.origTransfererContext = origTransfererContext;
    }

    public String getOrigTransfererExten()
    {
        return origTransfererExten;
    }

    public void setOrigTransfererExten(String origTransfererExten)
    {
        this.origTransfererExten = origTransfererExten;
    }

    public String getOrigTransfererPriority()
    {
        return origTransfererPriority;
    }

    public void setOrigTransfererPriority(String origTransfererPriority)
    {
        this.origTransfererPriority = origTransfererPriority;
    }

    public String getOrigTransfererUniqueid()
    {
        return origTransfererUniqueid;
    }

    public void setOrigTransfererUniqueid(String origTransfererUniqueid)
    {
        this.origTransfererUniqueid = origTransfererUniqueid;
    }

    public String getOrigBridgeUniqueid()
    {
        return origBridgeUniqueid;
    }

    public void setOrigBridgeUniqueid(String origBridgeUniqueid)
    {
        this.origBridgeUniqueid = origBridgeUniqueid;
    }

    public String getOrigBridgeType()
    {
        return origBridgeType;
    }

    public void setOrigBridgeType(String origBridgeType)
    {
        this.origBridgeType = origBridgeType;
    }

    public String getOrigBridgeTechnology()
    {
        return origBridgeTechnology;
    }

    public void setOrigBridgeTechnology(String origBridgeTechnology)
    {
        this.origBridgeTechnology = origBridgeTechnology;
    }

    public String getOrigBridgeCreator()
    {
        return origBridgeCreator;
    }

    public void setOrigBridgeCreator(String origBridgeCreator)
    {
        this.origBridgeCreator = origBridgeCreator;
    }

    public String getOrigBridgeName()
    {
        return origBridgeName;
    }

    public void setOrigBridgeName(String origBridgeName)
    {
        this.origBridgeName = origBridgeName;
    }

    public String getOrigBridgeNumChannels()
    {
        return origBridgeNumChannels;
    }

    public void setOrigBridgeNumChannels(String origBridgeNumChannels)
    {
        this.origBridgeNumChannels = origBridgeNumChannels;
    }

    public String getSecondTransfererChannel()
    {
        return secondTransfererChannel;
    }

    public void setSecondTransfererChannel(String secondTransfererChannel)
    {
        this.secondTransfererChannel = secondTransfererChannel;
    }

    public String getSecondTransfererChannelState()
    {
        return secondTransfererChannelState;
    }

    public void setSecondTransfererChannelState(String secondTransfererChannelState)
    {
        this.secondTransfererChannelState = secondTransfererChannelState;
    }

    public String getSecondTransfererChannelStateDesc()
    {
        return secondTransfererChannelStateDesc;
    }

    public void setSecondTransfererChannelStateDesc(String secondTransfererChannelStateDesc)
    {
        this.secondTransfererChannelStateDesc = secondTransfererChannelStateDesc;
    }

    public String getSecondTransfererCallerIDNum()
    {
        return secondTransfererCallerIDNum;
    }

    public void setSecondTransfererCallerIDNum(String secondTransfererCallerIDNum)
    {
        this.secondTransfererCallerIDNum = secondTransfererCallerIDNum;
    }

    public String getSecondTransfererCallerIDName()
    {
        return secondTransfererCallerIDName;
    }

    public void setSecondTransfererCallerIDName(String secondTransfererCallerIDName)
    {
        this.secondTransfererCallerIDName = secondTransfererCallerIDName;
    }

    public String getSecondTransfererConnectedLineNum() {
        return secondTransfererConnectedLineNum;
    }

    public void setSecondTransfererConnectedLineNum(String secondTransfererConnectedLineNum)
    {
        this.secondTransfererConnectedLineNum = secondTransfererConnectedLineNum;
    }

    public String getSecondTransfererConnectedLineName()
    {
        return secondTransfererConnectedLineName;
    }

    public void setSecondTransfererConnectedLineName(String secondTransfererConnectedLineName)
    {
        this.secondTransfererConnectedLineName = secondTransfererConnectedLineName;
    }

    public String getSecondTransfererAccountCode()
    {
        return secondTransfererAccountCode;
    }

    public void setSecondTransfererAccountCode(String secondTransfererAccountCode)
    {
        this.secondTransfererAccountCode = secondTransfererAccountCode;
    }

    public String getSecondTransfererContext()
    {
        return secondTransfererContext;
    }

    public void setSecondTransfererContext(String secondTransfererContext)
    {
        this.secondTransfererContext = secondTransfererContext;
    }

    public String getSecondTransfererExten()
    {
        return secondTransfererExten;
    }

    public void setSecondTransfererExten(String secondTransfererExten)
    {
        this.secondTransfererExten = secondTransfererExten;
    }

    public String getSecondTransfererPriority()
    {
        return secondTransfererPriority;
    }

    public void setSecondTransfererPriority(String secondTransfererPriority)
    {
        this.secondTransfererPriority = secondTransfererPriority;
    }

    public String getSecondTransfererUniqueid()
    {
        return secondTransfererUniqueid;
    }

    public void setSecondTransfererUniqueid(String secondTransfererUniqueid)
    {
        this.secondTransfererUniqueid = secondTransfererUniqueid;
    }

    public String getSecondBridgeUniqueid()
    {
        return secondBridgeUniqueid;
    }

    public void setSecondBridgeUniqueid(String secondBridgeUniqueid)
    {
        this.secondBridgeUniqueid = secondBridgeUniqueid;
    }

    public String getSecondBridgeType()
    {
        return secondBridgeType;
    }

    public void setSecondBridgeType(String secondBridgeType)
    {
        this.secondBridgeType = secondBridgeType;
    }

    public String getSecondBridgeTechnology()
    {
        return secondBridgeTechnology;
    }

    public void setSecondBridgeTechnology(String secondBridgeTechnology)
    {
        this.secondBridgeTechnology = secondBridgeTechnology;
    }

    public String getSecondBridgeCreator()
    {
        return secondBridgeCreator;
    }

    public void setSecondBridgeCreator(String secondBridgeCreator)
    {
        this.secondBridgeCreator = secondBridgeCreator;
    }

    public String getSecondBridgeName()
    {
        return secondBridgeName;
    }

    public void setSecondBridgeName(String secondBridgeName)
    {
        this.secondBridgeName = secondBridgeName;
    }

    public String getSecondBridgeNumChannels()
    {
        return secondBridgeNumChannels;
    }

    public void setSecondBridgeNumChannels(String secondBridgeNumChannels)
    {
        this.secondBridgeNumChannels = secondBridgeNumChannels;
    }

    public String getDestType()
    {
        return destType;
    }

    public void setDestType(String destType)
    {
        this.destType = destType;
    }

    public String getDestBridgeUniqueid()
    {
        return destBridgeUniqueid;
    }

    public void setDestBridgeUniqueid(String destBridgeUniqueid)
    {
        this.destBridgeUniqueid = destBridgeUniqueid;
    }

    public String getDestApp()
    {
        return destApp;
    }

    public void setDestApp(String destApp)
    {
        this.destApp = destApp;
    }

    public String getLocalOneChannel()
    {
        return localOneChannel;
    }

    public void setLocalOneChannel(String localOneChannel)
    {
        this.localOneChannel = localOneChannel;
    }

    public String getLocalOneChannelState()
    {
        return localOneChannelState;
    }

    public void setLocalOneChannelState(String localOneChannelState)
    {
        this.localOneChannelState = localOneChannelState;
    }

    public String getLocalOneChannelStateDesc()
    {
        return localOneChannelStateDesc;
    }

    public void setLocalOneChannelStateDesc(String localOneChannelStateDesc)
    {
        this.localOneChannelStateDesc = localOneChannelStateDesc;
    }

    public String getLocalOneCallerIDNum()
    {
        return localOneCallerIDNum;
    }

    public void setLocalOneCallerIDNum(String localOneCallerIDNum)
    {
        this.localOneCallerIDNum = localOneCallerIDNum;
    }

    public String getLocalOneCallerIDName()
    {
        return localOneCallerIDName;
    }

    public void setLocalOneCallerIDName(String localOneCallerIDName)
    {
        this.localOneCallerIDName = localOneCallerIDName;
    }

    public String getLocalOneConnectedLineNum()
    {
        return localOneConnectedLineNum;
    }

    public void setLocalOneConnectedLineNum(String localOneConnectedLineNum)
    {
        this.localOneConnectedLineNum = localOneConnectedLineNum;
    }

    public String getLocalOneConnectedLineName()
    {
        return localOneConnectedLineName;
    }

    public void setLocalOneConnectedLineName(String localOneConnectedLineName)
    {
        this.localOneConnectedLineName = localOneConnectedLineName;
    }

    public String getLocalOneAccountCode()
    {
        return localOneAccountCode;
    }

    public void setLocalOneAccountCode(String localOneAccountCode)
    {
        this.localOneAccountCode = localOneAccountCode;
    }

    public String getLocalOneContext()
    {
        return localOneContext;
    }

    public void setLocalOneContext(String localOneContext)
    {
        this.localOneContext = localOneContext;
    }

    public String getLocalOneExten()
    {
        return localOneExten;
    }

    public void setLocalOneExten(String localOneExten)
    {
        this.localOneExten = localOneExten;
    }

    public String getLocalOnePriority()
    {
        return localOnePriority;
    }

    public void setLocalOnePriority(String localOnePriority)
    {
        this.localOnePriority = localOnePriority;
    }

    public String getLocalOneUniqueid()
    {
        return localOneUniqueid;
    }

    public void setLocalOneUniqueid(String localOneUniqueid)
    {
        this.localOneUniqueid = localOneUniqueid;
    }

    public String getLocalTwoChannel()
    {
        return localTwoChannel;
    }

    public void setLocalTwoChannel(String localTwoChannel)
    {
        this.localTwoChannel = localTwoChannel;
    }

    public String getLocalTwoChannelState()
    {
        return localTwoChannelState;
    }

    public void setLocalTwoChannelState(String localTwoChannelState)
    {
        this.localTwoChannelState = localTwoChannelState;
    }

    public String getLocalTwoChannelStateDesc()
    {
        return localTwoChannelStateDesc;
    }

    public void setLocalTwoChannelStateDesc(String localTwoChannelStateDesc)
    {
        this.localTwoChannelStateDesc = localTwoChannelStateDesc;
    }

    public String getLocalTwoCallerIDNum()
    {
        return localTwoCallerIDNum;
    }

    public void setLocalTwoCallerIDNum(String localTwoCallerIDNum)
    {
        this.localTwoCallerIDNum = localTwoCallerIDNum;
    }

    public String getLocalTwoCallerIDName()
    {
        return localTwoCallerIDName;
    }

    public void setLocalTwoCallerIDName(String localTwoCallerIDName)
    {
        this.localTwoCallerIDName = localTwoCallerIDName;
    }

    public String getLocalTwoConnectedLineNum()
    {
        return localTwoConnectedLineNum;
    }

    public void setLocalTwoConnectedLineNum(String localTwoConnectedLineNum)
    {
        this.localTwoConnectedLineNum = localTwoConnectedLineNum;
    }

    public String getLocalTwoConnectedLineName()
    {
        return localTwoConnectedLineName;
    }

    public void setLocalTwoConnectedLineName(String localTwoConnectedLineName)
    {
        this.localTwoConnectedLineName = localTwoConnectedLineName;
    }

    public String getLocalTwoAccountCode()
    {
        return localTwoAccountCode;
    }

    public void setLocalTwoAccountCode(String localTwoAccountCode)
    {
        this.localTwoAccountCode = localTwoAccountCode;
    }

    public String getLocalTwoContext()
    {
        return localTwoContext;
    }

    public void setLocalTwoContext(String localTwoContext)
    {
        this.localTwoContext = localTwoContext;
    }

    public String getLocalTwoExten()
    {
        return localTwoExten;
    }

    public void setLocalTwoExten(String localTwoExten)
    {
        this.localTwoExten = localTwoExten;
    }

    public String getLocalTwoPriority()
    {
        return localTwoPriority;
    }

    public void setLocalTwoPriority(String localTwoPriority)
    {
        this.localTwoPriority = localTwoPriority;
    }

    public String getLocalTwoUniqueid()
    {
        return localTwoUniqueid;
    }

    public void setLocalTwoUniqueid(String localTwoUniqueid)
    {
        this.localTwoUniqueid = localTwoUniqueid;
    }

    public String getDestTransfererChannel()
    {
        return destTransfererChannel;
    }

    public void setDestTransfererChannel(String destTransfererChannel)
    {
        this.destTransfererChannel = destTransfererChannel;
    }

    public String getTransfereeChannel()
    {
        return transfereeChannel;
    }

    public void setTransfereeChannel(String transfereeChannel)
    {
        this.transfereeChannel = transfereeChannel;
    }

    public String getTransfereeChannelState()
    {
        return transfereeChannelState;
    }

    public void setTransfereeChannelState(String transfereeChannelState)
    {
        this.transfereeChannelState = transfereeChannelState;
    }

    public String getTransfereeChannelStateDesc()
    {
        return transfereeChannelStateDesc;
    }

    public void setTransfereeChannelStateDesc(String transfereeChannelStateDesc)
    {
        this.transfereeChannelStateDesc = transfereeChannelStateDesc;
    }

    public String getTransfereeCallerIDNum()
    {
        return transfereeCallerIDNum;
    }

    public void setTransfereeCallerIDNum(String transfereeCallerIDNum)
    {
        this.transfereeCallerIDNum = transfereeCallerIDNum;
    }

    public String getTransfereeCallerIDName()
    {
        return transfereeCallerIDName;
    }

    public void setTransfereeCallerIDName(String transfereeCallerIDName)
    {
        this.transfereeCallerIDName = transfereeCallerIDName;
    }

    public String getTransfereeConnectedLineNum()
    {
        return transfereeConnectedLineNum;
    }

    public void setTransfereeConnectedLineNum(String transfereeConnectedLineNum)
    {
        this.transfereeConnectedLineNum = transfereeConnectedLineNum;
    }

    public String getTransfereeConnectedLineName()
    {
        return transfereeConnectedLineName;
    }

    public void setTransfereeConnectedLineName(String transfereeConnectedLineName)
    {
        this.transfereeConnectedLineName = transfereeConnectedLineName;
    }

    public String getTransfereeAccountCode()
    {
        return transfereeAccountCode;
    }

    public void setTransfereeAccountCode(String transfereeAccountCode)
    {
        this.transfereeAccountCode = transfereeAccountCode;
    }

    public String getTransfereeContext()
    {
        return transfereeContext;
    }

    public void setTransfereeContext(String transfereeContext)
    {
        this.transfereeContext = transfereeContext;
    }

    public String getTransfereeExten()
    {
        return transfereeExten;
    }

    public void setTransfereeExten(String transfereeExten)
    {
        this.transfereeExten = transfereeExten;
    }

    public String getTransfereePriority()
    {
        return transfereePriority;
    }

    public void setTransfereePriority(String transfereePriority)
    {
        this.transfereePriority = transfereePriority;
    }

    public String getTransfereeUniqueid()
    {
        return transfereeUniqueid;
    }

    public void setTransfereeUniqueid(String transfereeUniqueid)
    {
        this.transfereeUniqueid = transfereeUniqueid;
    }

    public String getTransfereeLinkedId()
    {
        return transfereeLinkedId;
    }

    public void setTransfereeLinkedId(String transfereeLinkedId)
    {
        this.transfereeLinkedId = transfereeLinkedId;
    }

    public String getTransfereeLanguage()
    {
        return transfereeLanguage;
    }

    public void setTransfereeLanguage(String transfereeLanguage)
    {
        this.transfereeLanguage = transfereeLanguage;
    }

    public String getOrigTransfererLinkedId()
    {
        return origTransfererLinkedId;
    }

    public void setOrigTransfererLinkedId(String origTransfererLinkedId)
    {
        this.origTransfererLinkedId = origTransfererLinkedId;
    }

    public String getSecondTransfererLanguage()
    {
        return secondTransfererLanguage;
    }

    public void setSecondTransfererLanguage(String secondTransfererLanguage)
    {
        this.secondTransfererLanguage = secondTransfererLanguage;
    }

    public String getIsexternal()
    {
        return isexternal;
    }

    public void setIsexternal(String isexternal)
    {
        this.isexternal = isexternal;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getSecondTransfererLinkedId()
    {
        return secondTransfererLinkedId;
    }

    public void setSecondTransfererLinkedId(String secondTransfererLinkedId)
    {
        this.secondTransfererLinkedId = secondTransfererLinkedId;
    }

    public String getOrigTransfererLanguage()
    {
        return origTransfererLanguage;
    }

    public void setOrigTransfererLanguage(String origTransfererLanguage)
    {
        this.origTransfererLanguage = origTransfererLanguage;
    }
    
}
