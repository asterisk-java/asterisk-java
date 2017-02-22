package org.asteriskjava.manager.response;

import java.util.Map;

/**
 * Response to a {@link org.asteriskjava.manager.action.SipShowPeerAction}.
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 * @see org.asteriskjava.manager.action.SipShowPeerAction
 */
public class SipShowPeerResponse extends ManagerResponse
{
    private static final long serialVersionUID = 1L;

    private String channelType;
    private String objectName;
    private String chanObjectType;
    private Boolean secretExist;
    private Boolean md5SecretExist;
    private Boolean remoteSecretExist;
    private String context;
    private String language;
    private String accountCode;
    private String amaFlags;
    private String cidCallingPres;
    private String sipFromUser;
    private String sipFromDomain;
    private String callGroup;
    private String pickupGroup;
    private String voiceMailbox;
    private String transferMode;
    private Integer lastMsgsSent;
    private Integer callLimit;
    private Integer busyLevel;
    private Integer maxCallBr; // "%d kbps"
    private Boolean dynamic;
    private String callerId;
    private Long regExpire; // "%ld seconds"
    private Boolean sipAuthInsecure;
    private Boolean sipNatSupport;
    private Boolean acl;
    private Boolean sipT38support;
    private String sipT38ec;
    private Long sipT38MaxDtgrm;
    private Boolean sipDirectMedia;
    private Boolean sipCanReinvite;
    private Boolean sipPromiscRedir;
    private Boolean sipUserPhone;
    private Boolean sipVideoSupport;
    private Boolean sipTextSupport;
    private String sipSessTimers;
    private String sipSessRefresh;
    private Integer sipSessExpires;
    private Integer sipSessMin;
    private String sipDtmfMode;
    private String toHost;
    private String addressIp;
    private Integer addressPort;
    private String defaultAddrIp;
    private Integer defaultAddrPort;
    private String defaultUsername;
    private String regExtension;
    private String codecs;
    private String codecOrder;
    private String status;
    private String sipUserAgent;
    private String regContact;
    private Integer qualifyFreq; // "%d ms"
    private String parkingLot;
    private Integer maxForwards;

    private String toneZone;
    private String sipUseReasonHeader;
    private String sipEncryption;
    private String sipForcerport;
    private String sipRtpEngine;
    private String sipComedia;
    private String mohsuggest;

    private Map<String, String> chanVariable;

    public String getChannelType()
    {
        return channelType;
    }

    public void setChannelType(String channelType)
    {
        this.channelType = channelType;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    public String getChanObjectType()
    {
        return chanObjectType;
    }

    public void setChanObjectType(String chanObjectType)
    {
        this.chanObjectType = chanObjectType;
    }

    public Boolean getSecretExist()
    {
        return secretExist;
    }

    public void setSecretExist(Boolean secretExist)
    {
        this.secretExist = secretExist;
    }

    public Boolean getMd5SecretExist()
    {
        return md5SecretExist;
    }

    public void setMd5SecretExist(Boolean md5SecretExist)
    {
        this.md5SecretExist = md5SecretExist;
    }

    public Boolean getRemoteSecretExist()
    {
        return remoteSecretExist;
    }

    public void setRemoteSecretExist(Boolean remoteSecretExist)
    {
        this.remoteSecretExist = remoteSecretExist;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getAmaFlags()
    {
        return amaFlags;
    }

    public void setAmaFlags(String amaFlags)
    {
        this.amaFlags = amaFlags;
    }

    public String getCidCallingPres()
    {
        return cidCallingPres;
    }

    public void setCidCallingPres(String cidCallingPres)
    {
        this.cidCallingPres = cidCallingPres;
    }

    public String getSipFromUser()
    {
        return sipFromUser;
    }

    public void setSipFromUser(String sipFromUser)
    {
        this.sipFromUser = sipFromUser;
    }

    public String getSipFromDomain()
    {
        return sipFromDomain;
    }

    public void setSipFromDomain(String sipFromDomain)
    {
        this.sipFromDomain = sipFromDomain;
    }

    public String getCallGroup()
    {
        return callGroup;
    }

    public void setCallGroup(String callGroup)
    {
        this.callGroup = callGroup;
    }

    public String getPickupGroup()
    {
        return pickupGroup;
    }

    public void setPickupGroup(String pickupGroup)
    {
        this.pickupGroup = pickupGroup;
    }

    public String getVoiceMailbox()
    {
        return voiceMailbox;
    }

    public void setVoiceMailbox(String voiceMailbox)
    {
        this.voiceMailbox = voiceMailbox;
    }

    public String getTransferMode()
    {
        return transferMode;
    }

    public void setTransferMode(String transferMode)
    {
        this.transferMode = transferMode;
    }

    public Integer getLastMsgsSent()
    {
        return lastMsgsSent;
    }

    public void setLastMsgsSent(Integer lastMsgsSent)
    {
        this.lastMsgsSent = lastMsgsSent;
    }

    public Integer getCallLimit()
    {
        return callLimit;
    }

    public void setCallLimit(Integer callLimit)
    {
        this.callLimit = callLimit;
    }

    public Integer getBusyLevel()
    {
        return busyLevel;
    }

    public void setBusyLevel(Integer busyLevel)
    {
        this.busyLevel = busyLevel;
    }

    public Integer getMaxCallBr()
    {
        return maxCallBr;
    }

    public void setMaxCallBr(String maxCallBr)
    {
        this.maxCallBr = stringToInteger(maxCallBr, "kbps");
    }

    public Boolean getDynamic()
    {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic)
    {
        this.dynamic = dynamic;
    }

    public String getCallerId()
    {
        return callerId;
    }

    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    public Long getRegExpire()
    {
        return regExpire;
    }

    public void setRegExpire(String regExpire)
    {
        this.regExpire = stringToLong(regExpire, "seconds");
    }

    public Boolean getSipAuthInsecure()
    {
        return sipAuthInsecure;
    }

    public void setSipAuthInsecure(Boolean sipAuthInsecure)
    {
        this.sipAuthInsecure = sipAuthInsecure;
    }

    public Boolean getSipNatSupport()
    {
        return sipNatSupport;
    }

    public void setSipNatSupport(Boolean sipNatSupport)
    {
        this.sipNatSupport = sipNatSupport;
    }

    public Boolean getAcl()
    {
        return acl;
    }

    public void setAcl(Boolean acl)
    {
        this.acl = acl;
    }

    public Boolean getSipT38support()
    {
        return sipT38support;
    }

    public void setSipT38support(Boolean sipT38support)
    {
        this.sipT38support = sipT38support;
    }

    public String getSipT38ec()
    {
        return sipT38ec;
    }

    public void setSipT38ec(String sipT38ec)
    {
        this.sipT38ec = sipT38ec;
    }

    public Long getSipT38MaxDtgrm()
    {
        return sipT38MaxDtgrm;
    }

    public void setSipT38MaxDtgrm(Long sipT38MaxDtgrm)
    {

        /**
         * asterisk returns sipT38MaxDtgrm as 4394967295L when the value is -1
         * so I'm taking a long and then changing it to -1 if required
         */
        if (sipT38MaxDtgrm == 4294967295L)
        {
            this.sipT38MaxDtgrm = -1L;
        }
        else
        {
            if (sipT38MaxDtgrm < Integer.MAX_VALUE && sipT38MaxDtgrm > Integer.MIN_VALUE)
            {
                this.sipT38MaxDtgrm = sipT38MaxDtgrm;
            }
        }
    }

    public Boolean getSipDirectMedia()
    {
        return sipDirectMedia;
    }

    public void setSipDirectMedia(Boolean sipDirectMedia)
    {
        this.sipDirectMedia = sipDirectMedia;
    }

    public Boolean getSipCanReinvite()
    {
        return sipCanReinvite;
    }

    public void setSipCanReinvite(Boolean sipCanReinvite)
    {
        this.sipCanReinvite = sipCanReinvite;
    }

    public Boolean getSipPromiscRedir()
    {
        return sipPromiscRedir;
    }

    public void setSipPromiscRedir(Boolean sipPromiscRedir)
    {
        this.sipPromiscRedir = sipPromiscRedir;
    }

    public Boolean getSipUserPhone()
    {
        return sipUserPhone;
    }

    public void setSipUserPhone(Boolean sipUserPhone)
    {
        this.sipUserPhone = sipUserPhone;
    }

    public Boolean getSipVideoSupport()
    {
        return sipVideoSupport;
    }

    public void setSipVideoSupport(Boolean sipVideoSupport)
    {
        this.sipVideoSupport = sipVideoSupport;
    }

    public Boolean getSipTextSupport()
    {
        return sipTextSupport;
    }

    public void setSipTextSupport(Boolean sipTextSupport)
    {
        this.sipTextSupport = sipTextSupport;
    }

    public String getSipSessTimers()
    {
        return sipSessTimers;
    }

    public void setSipSessTimers(String sipSessTimers)
    {
        this.sipSessTimers = sipSessTimers;
    }

    public String getSipSessRefresh()
    {
        return sipSessRefresh;
    }

    public void setSipSessRefresh(String sipSessRefresh)
    {
        this.sipSessRefresh = sipSessRefresh;
    }

    public Integer getSipSessExpires()
    {
        return sipSessExpires;
    }

    public void setSipSessExpires(Integer sipSessExpires)
    {
        this.sipSessExpires = sipSessExpires;
    }

    public Integer getSipSessMin()
    {
        return sipSessMin;
    }

    public void setSipSessMin(Integer sipSessMin)
    {
        this.sipSessMin = sipSessMin;
    }

    public String getSipDtmfMode()
    {
        return sipDtmfMode;
    }

    public void setSipDtmfMode(String sipDtmfMode)
    {
        this.sipDtmfMode = sipDtmfMode;
    }

    public String getToHost()
    {
        return toHost;
    }

    public void setToHost(String toHost)
    {
        this.toHost = toHost;
    }

    public String getAddressIp()
    {
        return addressIp;
    }

    public void setAddressIp(String addressIp)
    {
        this.addressIp = addressIp;
    }

    public Integer getAddressPort()
    {
        return addressPort;
    }

    public void setAddressPort(Integer addressPort)
    {
        this.addressPort = addressPort;
    }

    public String getDefaultAddrIp()
    {
        return defaultAddrIp;
    }

    public void setDefaultAddrIp(String defaultAddrIp)
    {
        this.defaultAddrIp = defaultAddrIp;
    }

    public Integer getDefaultAddrPort()
    {
        return defaultAddrPort;
    }

    public void setDefaultAddrPort(Integer defaultAddrPort)
    {
        this.defaultAddrPort = defaultAddrPort;
    }

    public String getDefaultUsername()
    {
        return defaultUsername;
    }

    public void setDefaultUsername(String defaultUsername)
    {
        this.defaultUsername = defaultUsername;
    }

    public String getRegExtension()
    {
        return regExtension;
    }

    public void setRegExtension(String regExtension)
    {
        this.regExtension = regExtension;
    }

    public String getCodecs()
    {
        return codecs;
    }

    public void setCodecs(String codecs)
    {
        this.codecs = codecs;
    }

    public String getCodecOrder()
    {
        return codecOrder;
    }

    public void setCodecOrder(String codecOrder)
    {
        this.codecOrder = codecOrder;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getSipUserAgent()
    {
        return sipUserAgent;
    }

    public void setSipUserAgent(String sipUserAgent)
    {
        this.sipUserAgent = sipUserAgent;
    }

    public String getParkingLot()
    {
        return parkingLot;
    }

    public void setParkingLot(String parkingLot)
    {
        this.parkingLot = parkingLot;
    }

    public String getRegContact()
    {
        return regContact;
    }

    public void setRegContact(String regContact)
    {
        // workaround for Asterisk bug:
        if (regContact.startsWith(": "))
        {
            regContact = regContact.substring(2);
        }
        this.regContact = regContact;
    }

    public Integer getQualifyFreq()
    {
        return qualifyFreq;
    }

    public void setQualifyFreq(String qualifyFreq)
    {
        // workaround for Asterisk bugs:
        if (qualifyFreq.startsWith(": "))
        {
            qualifyFreq = qualifyFreq.substring(2);
        }
        if (qualifyFreq.indexOf('\n') > -1)
        {
            qualifyFreq = qualifyFreq.substring(0, qualifyFreq.indexOf('\n'));
        }
        this.qualifyFreq = stringToInteger(qualifyFreq, "ms");
    }

    public Map<String, String> getChanVariable()
    {
        return chanVariable;
    }

    public void setChanVariable(final Map<String, String> chanVariable)
    {
        this.chanVariable = chanVariable;
    }

    public Integer getMaxForwards()
    {
        return maxForwards;
    }

    public void setMaxForwards(Integer maxForwards)
    {
        this.maxForwards = maxForwards;
    }

    public String getToneZone()
    {
        return toneZone;
    }

    public void setToneZone(String toneZone)
    {
        this.toneZone = toneZone;
    }

    public String getSipUseReasonHeader()
    {
        return sipUseReasonHeader;
    }

    public void setSipUseReasonHeader(String sipUseReasonHeader)
    {
        this.sipUseReasonHeader = sipUseReasonHeader;
    }

    public String getSipEncryption()
    {
        return sipEncryption;
    }

    public void setSipEncryption(String sipEncryption)
    {
        this.sipEncryption = sipEncryption;
    }

    public String getSipForcerport()
    {
        return sipForcerport;
    }

    public void setSipForcerport(String sipForcerport)
    {
        this.sipForcerport = sipForcerport;
    }

    public String getSipRtpEngine()
    {
        return sipRtpEngine;
    }

    public void setSipRtpEngine(String sipRtpEngine)
    {
        this.sipRtpEngine = sipRtpEngine;
    }

    public String getSipComedia()
    {
        return sipComedia;
    }

    public void setSipComedia(String sipComedia)
    {
        this.sipComedia = sipComedia;
    }

    public String getMohsuggest()
    {
        return mohsuggest;
    }

    public void setMohsuggest(String mohsuggest)
    {
        this.mohsuggest = mohsuggest;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SipShowPeerResponse [channelType=");
        builder.append(channelType);
        builder.append(", objectName=");
        builder.append(objectName);
        builder.append(", chanObjectType=");
        builder.append(chanObjectType);
        builder.append(", secretExist=");
        builder.append(secretExist);
        builder.append(", md5SecretExist=");
        builder.append(md5SecretExist);
        builder.append(", remoteSecretExist=");
        builder.append(remoteSecretExist);
        builder.append(", context=");
        builder.append(context);
        builder.append(", language=");
        builder.append(language);
        builder.append(", accountCode=");
        builder.append(accountCode);
        builder.append(", amaFlags=");
        builder.append(amaFlags);
        builder.append(", cidCallingPres=");
        builder.append(cidCallingPres);
        builder.append(", sipFromUser=");
        builder.append(sipFromUser);
        builder.append(", sipFromDomain=");
        builder.append(sipFromDomain);
        builder.append(", callGroup=");
        builder.append(callGroup);
        builder.append(", pickupGroup=");
        builder.append(pickupGroup);
        builder.append(", voiceMailbox=");
        builder.append(voiceMailbox);
        builder.append(", transferMode=");
        builder.append(transferMode);
        builder.append(", lastMsgsSent=");
        builder.append(lastMsgsSent);
        builder.append(", callLimit=");
        builder.append(callLimit);
        builder.append(", busyLevel=");
        builder.append(busyLevel);
        builder.append(", maxCallBr=");
        builder.append(maxCallBr);
        builder.append(", dynamic=");
        builder.append(dynamic);
        builder.append(", callerId=");
        builder.append(callerId);
        builder.append(", regExpire=");
        builder.append(regExpire);
        builder.append(", sipAuthInsecure=");
        builder.append(sipAuthInsecure);
        builder.append(", sipNatSupport=");
        builder.append(sipNatSupport);
        builder.append(", acl=");
        builder.append(acl);
        builder.append(", sipT38support=");
        builder.append(sipT38support);
        builder.append(", sipT38ec=");
        builder.append(sipT38ec);
        builder.append(", sipT38MaxDtgrm=");
        builder.append(sipT38MaxDtgrm);
        builder.append(", sipDirectMedia=");
        builder.append(sipDirectMedia);
        builder.append(", sipCanReinvite=");
        builder.append(sipCanReinvite);
        builder.append(", sipPromiscRedir=");
        builder.append(sipPromiscRedir);
        builder.append(", sipUserPhone=");
        builder.append(sipUserPhone);
        builder.append(", sipVideoSupport=");
        builder.append(sipVideoSupport);
        builder.append(", sipTextSupport=");
        builder.append(sipTextSupport);
        builder.append(", sipSessTimers=");
        builder.append(sipSessTimers);
        builder.append(", sipSessRefresh=");
        builder.append(sipSessRefresh);
        builder.append(", sipSessExpires=");
        builder.append(sipSessExpires);
        builder.append(", sipSessMin=");
        builder.append(sipSessMin);
        builder.append(", sipDtmfMode=");
        builder.append(sipDtmfMode);
        builder.append(", toHost=");
        builder.append(toHost);
        builder.append(", addressIp=");
        builder.append(addressIp);
        builder.append(", addressPort=");
        builder.append(addressPort);
        builder.append(", defaultAddrIp=");
        builder.append(defaultAddrIp);
        builder.append(", defaultAddrPort=");
        builder.append(defaultAddrPort);
        builder.append(", defaultUsername=");
        builder.append(defaultUsername);
        builder.append(", regExtension=");
        builder.append(regExtension);
        builder.append(", codecs=");
        builder.append(codecs);
        builder.append(", codecOrder=");
        builder.append(codecOrder);
        builder.append(", status=");
        builder.append(status);
        builder.append(", sipUserAgent=");
        builder.append(sipUserAgent);
        builder.append(", regContact=");
        builder.append(regContact);
        builder.append(", qualifyFreq=");
        builder.append(qualifyFreq);
        builder.append(", parkingLot=");
        builder.append(parkingLot);
        builder.append(", maxForwards=");
        builder.append(maxForwards);
        builder.append(", toneZone=");
        builder.append(toneZone);
        builder.append(", sipUseReasonHeader=");
        builder.append(sipUseReasonHeader);
        builder.append(", sipEncryption=");
        builder.append(sipEncryption);
        builder.append(", sipForcerport=");
        builder.append(sipForcerport);
        builder.append(", sipRtpEngine=");
        builder.append(sipRtpEngine);
        builder.append(", sipComedia=");
        builder.append(sipComedia);
        builder.append(", chanVariable=");
        builder.append(chanVariable);
        builder.append(", mohSuggest=");
        builder.append(mohsuggest);
        builder.append("]");
        return builder.toString();
    }

}
