package org.asteriskjava.manager.event;

import org.asteriskjava.util.AstUtil;

/**
 * A EndpointDetail event is triggered in response to a
 * {@link org.asteriskjava.manager.action.PJSipShowEndpoint},
 * and contains information about a PJSIP endpoint
 * <p>
 *
 * @author Steve Sether
 * @version $Id$
 * @since 12
 */

public class EndpointDetail extends ResponseEvent {

    /**
     * Serial version identifier.
     */
	private static final long serialVersionUID = 77481930059189731L;
    private Integer listItems;
    private String eventList;
	
	public Integer getListItems() {
		return listItems;
	}

	public void setListItems(Integer listItems) {
		this.listItems = listItems;
	}

	public String getEventList() {
		return eventList;
	}

	public void setEventList(String eventList) {
		this.eventList = eventList;
	}

	private String objectName;
	private String objectType;
	private Boolean rpidImmediate;
	private Boolean webrtc;
	private Boolean ignorel83WithoutSdp;
	private int deviceStateBusyAt;
	private int t38UdptlMaxdatagram;
	private int dtlsRekey;
	private String namedPickupGroup; 
	private String directMediaMethod;
	private Boolean sendRpid;
	private String pickupGroup; 
	private String sdpSession;
	private String dtlsVerify;
	private String messageContext;
	private String mailboxes;
	private String recordOnFeature;
	private String dtlsPrivateKey; 
	private String dtlsFingerprint;
	private String fromDomain; 
	private int timersSessExpires;
	private String namedCallGroup; 
	private String dtlsCipher; 
	private Boolean mediaEncryptionOptimistic;
	private Boolean suppressQ850ReasonHeaders;
	private String aors;
	private String identifyBy;
	private String calleridPrivacy;
	private String mwiSubscribeReplacesUnsolicited;
	private String cosAudio;
	private Boolean followEarlyMediaFork;
	private String context;
	private Boolean rtpSymmetric;
	private String transport; 
	private String mohSuggest;
	private Boolean t38Udptl;
	private Boolean faxDetect;
	private String tosVideo;
	private Boolean srtpTag32;
	private Boolean referBlindProgress;
	private int maxAudioStreams;
	private Boolean bundle;
	private Boolean useAvpf;
	private String callGroup; 
	private Boolean sendConnectedLine;
	private int faxDetectTimeout;
	private String sdpOwner;
	private Boolean forceRport;
	private String calleridTag; 
	private int rtpTimeoutHold;
	private Boolean usePtime;
	private String mediaAddress; 
	private String voicemailExtension; 
	private int rtpTimeout;
	private String setVar;
	private String contactAcl; 
	private Boolean preferredCodecOnly;
	private Boolean forceAvp;
	private String recordOffFeature;
	private String fromUser; 
	private Boolean sendDiversion;
	private Boolean t38UdptlIpv6;
	private String toneZone; 
	private String language; 
	private Boolean allowSubscribe;
	private Boolean rtpIpv6;
	private String callerid;
	private Boolean mohPassthrough;
	private String cosVideo;
	private String dtlsAutoGenerateCert;
	private Boolean asymmetricRtpCodec;
	private Boolean iceSupport;
	private Boolean aggregateMwi;
	private Boolean oneTouchRecording;
	private String mwiFromUser; 
	private String accountcode;
	private String allow;
	private Boolean rewriteContact;
	private Boolean userEqPhone;
	private String rtpEngine;
	private String subscribeContext;
	private Boolean notifyEarlyInuseRinging;
	private String incomingMwiMailbox; 
	private String auth; 
	private String directMediaGlareMitigation;
	private Boolean trustIdInbound;
	private Boolean bindRtpToMediaAddress;
	private Boolean disableDirectMediaOnNat;
	private Boolean mediaEncryption;
	private Boolean mediaUseReceivedTransport;
	private Boolean allowOverlap;
	private String dtmfMode;
	private String outboundAuth; 
	private String tosAudio;
	private String dtlsCertFile; 
	private String dtlsCaPath; 
	private String dtlsSetup;
	private String connectedLineMethod;
	private Boolean g726NonStandard;
	private String _100rel;
	private String timers;
	private Boolean directMedia;
	private String acl;
	private int timersMinSe;
	private Boolean trustIdOutbound;
	private int subMinExpiry;
	private Boolean rtcpMux;
	private int maxVideoStreams;
	private Boolean acceptMultipleSdpAnswers;
	private Boolean trustConnectedLine;
	private Boolean sendPai;
	private int rtpKeepalive;
	private String t38UdptlEc;
	private Boolean t38UdptlNat;
	private Boolean allowTransfer;
	private String dtlsCaFile; 
	private String outboundProxy;
	private Boolean inbandProgress;
	private String deviceState;
	private String activeChannels;
	private Boolean ignore183withoutsdp;
	
	public int getMaxVideoStreams() {
		return maxVideoStreams;
	}

	public void setMaxVideoStreams(int maxVideoStreams) {
		this.maxVideoStreams = maxVideoStreams;
	}
	
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Boolean isRpidImmediate() {
		return rpidImmediate;
	}

	public void setRpidImmediate(Boolean rpidImmediate) {
		this.rpidImmediate = rpidImmediate;
	}
	
	public Boolean isWebrtc() {
		return webrtc;
	}

	public void setWebrtc(Boolean webrtc) {
		this.webrtc = webrtc;
	}

	public Boolean isIgnorel83WithoutSdp() {
		return ignorel83WithoutSdp;
	}

	public void setIgnorel83WithoutSdp(Boolean ignorel83WithoutSdp) {
		this.ignorel83WithoutSdp = ignorel83WithoutSdp;
	}

	public int getDeviceStateBusyAt() {
		return deviceStateBusyAt;
	}

	public void setDeviceStateBusyAt(int deviceStateBusyAt) {
		this.deviceStateBusyAt = deviceStateBusyAt;
	}

	public int getT38UdptlMaxdatagram() {
		return t38UdptlMaxdatagram;
	}

	public void setT38UdptlMaxdatagram(int t38UdptlMaxdatagram) {
		this.t38UdptlMaxdatagram = t38UdptlMaxdatagram;
	}

	public int getDtlsRekey() {
		return dtlsRekey;
	}

	public void setDtlsRekey(int dtlsRekey) {
		this.dtlsRekey = dtlsRekey;
	}

	public String getNamedPickupGroup() {
		return namedPickupGroup;
	}

	public void setNamedPickupGroup(String namedPickupGroup) {
		this.namedPickupGroup = namedPickupGroup;
	}

	public String getDirectMediaMethod() {
		return directMediaMethod;
	}

	public void setDirectMediaMethod(String directMediaMethod) {
		this.directMediaMethod = directMediaMethod;
	}

	public Boolean isSendRpid() {
		return sendRpid;
	}

	public void setSendRpid(Boolean sendRpid) {
		this.sendRpid = sendRpid;
	}

	public String getPickupGroup() {
		return pickupGroup;
	}

	public void setPickupGroup(String pickupGroup) {
		this.pickupGroup = pickupGroup;
	}

	public String getSdpSession() {
		return sdpSession;
	}

	public void setSdpSession(String sdpSession) {
		this.sdpSession = sdpSession;
	}

	public String getDtlsVerify() {
		return dtlsVerify;
	}

	public void setDtlsVerify(String dtlsVerify) {
		this.dtlsVerify = dtlsVerify;
	}

	public String getMessageContext() {
		return messageContext;
	}

	public void setMessageContext(String messageContext) {
		this.messageContext = messageContext;
	}

	public String getMailboxes() {
		return mailboxes;
	}

	public void setMailboxes(String mailboxes) {
		this.mailboxes = mailboxes;
	}

	public String getRecordOnFeature() {
		return recordOnFeature;
	}

	public void setRecordOnFeature(String recordOnFeature) {
		this.recordOnFeature = recordOnFeature;
	}

	public String getDtlsPrivateKey() {
		return dtlsPrivateKey;
	}

	public void setDtlsPrivateKey(String dtlsPrivateKey) {
		this.dtlsPrivateKey = dtlsPrivateKey;
	}

	public String getDtlsFingerprint() {
		return dtlsFingerprint;
	}

	public void setDtlsFingerprint(String dtlsFingerprint) {
		this.dtlsFingerprint = dtlsFingerprint;
	}

	public String getFromDomain() {
		return fromDomain;
	}

	public void setFromDomain(String fromDomain) {
		this.fromDomain = fromDomain;
	}

	public int getTimersSessExpires() {
		return timersSessExpires;
	}

	public void setTimersSessExpires(int timersSessExpires) {
		this.timersSessExpires = timersSessExpires;
	}

	public String getNamedCallGroup() {
		return namedCallGroup;
	}

	public void setNamedCallGroup(String namedCallGroup) {
		this.namedCallGroup = namedCallGroup;
	}

	public String getDtlsCipher() {
		return dtlsCipher;
	}

	public void setDtlsCipher(String dtlsCipher) {
		this.dtlsCipher = dtlsCipher;
	}

	public Boolean isMediaEncryptionOptimistic() {
		return mediaEncryptionOptimistic;
	}

	public void setMediaEncryptionOptimistic(Boolean mediaEncryptionOptimistic) {
		this.mediaEncryptionOptimistic = mediaEncryptionOptimistic;
	}

	public Boolean isSuppressQ850ReasonHeaders() {
		return suppressQ850ReasonHeaders;
	}

	public void setSuppressQ850ReasonHeaders(Boolean suppressQ850ReasonHeaders) {
		this.suppressQ850ReasonHeaders = suppressQ850ReasonHeaders;
	}

	public String getAors() {
		return aors;
	}

	public void setAors(String aors) {
		this.aors = aors;
	}

	public String getIdentifyBy() {
		return identifyBy;
	}

	public void setIdentifyBy(String identifyBy) {
		this.identifyBy = identifyBy;
	}

	public String getCalleridPrivacy() {
		return calleridPrivacy;
	}

	public void setCalleridPrivacy(String calleridPrivacy) {
		this.calleridPrivacy = calleridPrivacy;
	}

	public String getMwiSubscribeReplacesUnsolicited() {
		return mwiSubscribeReplacesUnsolicited;
	}

	public void setMwiSubscribeReplacesUnsolicited(String mwiSubscribeReplacesUnsolicited) {
		this.mwiSubscribeReplacesUnsolicited = mwiSubscribeReplacesUnsolicited;
	}

	public Boolean isFollowEarlyMediaFork() {
		return followEarlyMediaFork;
	}

	public void setFollowEarlyMediaFork(Boolean followEarlyMediaFork) {
		this.followEarlyMediaFork = followEarlyMediaFork;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Boolean isRtpSymmetric() {
		return rtpSymmetric;
	}

	public void setRtpSymmetric(Boolean rtpSymmetric) {
		this.rtpSymmetric = rtpSymmetric;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getMohSuggest() {
		return mohSuggest;
	}

	public void setMohSuggest(String mohSuggest) {
		this.mohSuggest = mohSuggest;
	}

	public Boolean isT38Udptl() {
		return t38Udptl;
	}

	public void setT38Udptl(Boolean t38Udptl) {
		this.t38Udptl = t38Udptl;
	}

	public Boolean isFaxDetect() {
		return faxDetect;
	}

	public void setFaxDetect(Boolean faxDetect) {
		this.faxDetect = faxDetect;
	}

	public Boolean getSrtpTag32() {
		return srtpTag32;
	}

	public void setSrtpTag32(Boolean srtpTag32) {
		this.srtpTag32 = srtpTag32;
	}

	public Boolean isReferBlindProgress() {
		return referBlindProgress;
	}

	public void setReferBlindProgress(Boolean referBlindProgress) {
		this.referBlindProgress = referBlindProgress;
	}
	
	public int getMaxAudioStreams() {
		return maxAudioStreams;
	}

	public void setMaxAudioStreams(int maxAudioStreams) {
		this.maxAudioStreams = maxAudioStreams;
	}

	public Boolean isBundle() {
		return bundle;
	}

	public void setBundle(Boolean bundle) {
		this.bundle = bundle;
	}
	

	public Boolean isUseAvpf() {
		return useAvpf;
	}

	public void setUseAvpf(Boolean useAvpf) {
		this.useAvpf = useAvpf;
	}
	

	public String getCallGroup() {
		return callGroup;
	}

	public void setCallGroup(String callGroup) {
		this.callGroup = callGroup;
	}

	public Boolean isSendConnectedLine() {
		return sendConnectedLine;
	}

	public void setSendConnectedLine(Boolean sendConnectedLine) {
		this.sendConnectedLine = sendConnectedLine;
	}
	

	public int getFaxDetectTimeout() {
		return faxDetectTimeout;
	}

	public void setFaxDetectTimeout(int faxDetectTimeout) {
		this.faxDetectTimeout = faxDetectTimeout;
	}

	public String getSdpOwner() {
		return sdpOwner;
	}

	public void setSdpOwner(String sdpOwner) {
		this.sdpOwner = sdpOwner;
	}

	public Boolean isForceRport() {
		return forceRport;
	}

	public void setForceRport(Boolean forceRport) {
		this.forceRport = forceRport;
	}
	

	public String getCalleridTag() {
		return calleridTag;
	}

	public void setCalleridTag(String calleridTag) {
		this.calleridTag = calleridTag;
	}

	public int getRtpTimeoutHold() {
		return rtpTimeoutHold;
	}

	public void setRtpTimeoutHold(int rtpTimeoutHold) {
		this.rtpTimeoutHold = rtpTimeoutHold;
	}

	public Boolean isUsePtime() {
		return usePtime;
	}

	public void setUsePtime(Boolean usePtime) {
		this.usePtime = usePtime;
	}
	

	public String getMediaAddress() {
		return mediaAddress;
	}

	public void setMediaAddress(String mediaAddress) {
		this.mediaAddress = mediaAddress;
	}

	public String getVoicemailExtension() {
		return voicemailExtension;
	}

	public void setVoicemailExtension(String voicemailExtension) {
		this.voicemailExtension = voicemailExtension;
	}

	public int getRtpTimeout() {
		return rtpTimeout;
	}

	public void setRtpTimeout(int rtpTimeout) {
		this.rtpTimeout = rtpTimeout;
	}

	public String getSetVar() {
		return setVar;
	}

	public void setSetVar(String setVar) {
		this.setVar = setVar;
	}

	public String getContactAcl() {
		return contactAcl;
	}

	public void setContactAcl(String contactAcl) {
		this.contactAcl = contactAcl;
	}

	public Boolean isPreferredCodecOnly() {
		return preferredCodecOnly;
	}

	public void setPreferredCodecOnly(Boolean preferredCodecOnly) {
		this.preferredCodecOnly = preferredCodecOnly;
	}
	
	public Boolean isForceAvp() {
		return forceAvp;
	}

	public void setForceAvp(Boolean forceAvp) {
		this.forceAvp = forceAvp;
	}
	

	public String getRecordOffFeature() {
		return recordOffFeature;
	}

	public void setRecordOffFeature(String recordOffFeature) {
		this.recordOffFeature = recordOffFeature;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public Boolean isSendDiversion() {
		return sendDiversion;
	}

	public void setSendDiversion(Boolean sendDiversion) {
		this.sendDiversion = sendDiversion;
	}
	

	public Boolean isT38UdptlIpv6() {
		return t38UdptlIpv6;
	}

	public void setT38UdptlIpv6(Boolean t38UdptlIpv6) {
		this.t38UdptlIpv6 = t38UdptlIpv6;
	}
	

	public String getToneZone() {
		return toneZone;
	}

	public void setToneZone(String toneZone) {
		this.toneZone = toneZone;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Boolean isAllowSubscribe() {
		return allowSubscribe;
	}

	public void setAllowSubscribe(Boolean allowSubscribe) {
		this.allowSubscribe = allowSubscribe;
	}
	
	public Boolean isRtpIpv6() {
		return rtpIpv6;
	}

	public void setRtpIpv6(Boolean rtpIpv6) {
		this.rtpIpv6 = rtpIpv6;
	}
	

	public String getCallerid() {
		return callerid;
	}

	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}

	public Boolean isMohPassthrough() {
		return mohPassthrough;
	}

	public void setMohPassthrough(Boolean mohPassthrough) {
		this.mohPassthrough = mohPassthrough;
	}
	
	public String getCosAudio() {
		return cosAudio;
	}

	public void setCosAudio(String cosAudio) {
		this.cosAudio = cosAudio;
	}

	public String getCosVideo() {
		return cosVideo;
	}

	public void setCosVideo(String cosVideo) {
		this.cosVideo = cosVideo;
	}

	public String getDtlsAutoGenerateCert() {
		return dtlsAutoGenerateCert;
	}

	public void setDtlsAutoGenerateCert(String dtlsAutoGenerateCert) {
		this.dtlsAutoGenerateCert = dtlsAutoGenerateCert;
	}

	public Boolean isAsymmetricRtpCodec() {
		return asymmetricRtpCodec;
	}

	public void setAsymmetricRtpCodec(Boolean asymmetricRtpCodec) {
		this.asymmetricRtpCodec = asymmetricRtpCodec;
	}
	

	public Boolean isIceSupport() {
		return iceSupport;
	}

	public void setIceSupport(Boolean iceSupport) {
		this.iceSupport = iceSupport;
	}

	
	public Boolean isAggregateMwi() {
		return aggregateMwi;
	}

	public void setAggregateMwi(Boolean aggregateMwi) {
		this.aggregateMwi = aggregateMwi;
	}
	

	public Boolean isOneTouchRecording() {
		return oneTouchRecording;
	}

	public void setOneTouchRecording(Boolean oneTouchRecording) {
		this.oneTouchRecording = oneTouchRecording;
	}
	

	public String getMwiFromUser() {
		return mwiFromUser;
	}

	public void setMwiFromUser(String mwiFromUser) {
		this.mwiFromUser = mwiFromUser;
	}

	public String getAccountcode() {
		return accountcode;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public Boolean isRewriteContact() {
		return rewriteContact;
	}

	public void setRewriteContact(Boolean rewriteContact) {
		this.rewriteContact = rewriteContact;
	}
	
	public Boolean isUserEqPhone() {
		return userEqPhone;
	}

	public void setUserEqPhone(Boolean userEqPhone) {
		this.userEqPhone = userEqPhone;
	}
	
	public String getRtpEngine() {
		return rtpEngine;
	}

	public void setRtpEngine(String rtpEngine) {
		this.rtpEngine = rtpEngine;
	}

	public String getSubscribeContext() {
		return subscribeContext;
	}

	public void setSubscribeContext(String subscribeContext) {
		this.subscribeContext = subscribeContext;
	}

	public Boolean isNotifyEarlyInuseRinging() {
		return notifyEarlyInuseRinging;
	}

	public void setNotifyEarlyInuseRinging(Boolean notifyEarlyInuseRinging) {
		this.notifyEarlyInuseRinging = notifyEarlyInuseRinging;
	}
	
	public String getIncomingMwiMailbox() {
		return incomingMwiMailbox;
	}

	public void setIncomingMwiMailbox(String incomingMwiMailbox) {
		this.incomingMwiMailbox = incomingMwiMailbox;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getDirectMediaGlareMitigation() {
		return directMediaGlareMitigation;
	}

	public void setDirectMediaGlareMitigation(String directMediaGlareMitigation) {
		this.directMediaGlareMitigation = directMediaGlareMitigation;
	}

	public Boolean isTrustIdInbound() {
		return trustIdInbound;
	}

	public void setTrustIdInbound(Boolean trustIdInbound) {
		this.trustIdInbound = trustIdInbound;
	}
	
	public Boolean isBindRtpToMediaAddress() {
		return bindRtpToMediaAddress;
	}

	public void setBindRtpToMediaAddress(Boolean bindRtpToMediaAddress) {
		this.bindRtpToMediaAddress = bindRtpToMediaAddress;
	}
	
	public Boolean isDisableDirectMediaOnNat() {
		return disableDirectMediaOnNat;
	}

	public void setDisableDirectMediaOnNat(Boolean disableDirectMediaOnNat) {
		this.disableDirectMediaOnNat = disableDirectMediaOnNat;
	}
	
	public Boolean isMediaEncryption() {
		return mediaEncryption;
	}

	public void setMediaEncryption(Boolean mediaEncryption) {
		this.mediaEncryption = mediaEncryption;
	}
	
	public Boolean isMediaUseReceivedTransport() {
		return mediaUseReceivedTransport;
	}

	public void setMediaUseReceivedTransport(Boolean mediaUseReceivedTransport) {
		this.mediaUseReceivedTransport = mediaUseReceivedTransport;
	}
	
	public Boolean isAllowOverlap() {
		return allowOverlap;
	}

	public void setAllowOverlap(Boolean allowOverlap) {
		this.allowOverlap = allowOverlap;
	}
	
	public String getDtmfMode() {
		return dtmfMode;
	}

	public void setDtmfMode(String dtmfMode) {
		this.dtmfMode = dtmfMode;
	}

	public String getOutboundAuth() {
		return outboundAuth;
	}

	public void setOutboundAuth(String outboundAuth) {
		this.outboundAuth = outboundAuth;
	}
	
	public String getTosVideo() {
		return tosVideo;
	}

	public void setTosVideo(String tosVideo) {
		this.tosVideo = tosVideo;
	}

	public String getTosAudio() {
		return tosAudio;
	}

	public void setTosAudio(String tosAudio) {
		this.tosAudio = tosAudio;
	}

	public String getDtlsCertFile() {
		return dtlsCertFile;
	}

	public void setDtlsCertFile(String dtlsCertFile) {
		this.dtlsCertFile = dtlsCertFile;
	}

	public String getDtlsCaPath() {
		return dtlsCaPath;
	}

	public void setDtlsCaPath(String dtlsCaPath) {
		this.dtlsCaPath = dtlsCaPath;
	}

	public String getDtlsSetup() {
		return dtlsSetup;
	}

	public void setDtlsSetup(String dtlsSetup) {
		this.dtlsSetup = dtlsSetup;
	}

	public String getConnectedLineMethod() {
		return connectedLineMethod;
	}

	public void setConnectedLineMethod(String connectedLineMethod) {
		this.connectedLineMethod = connectedLineMethod;
	}

	public Boolean isG726NonStandard() {
		return g726NonStandard;
	}

	public void setG726NonStandard(Boolean g726NonStandard) {
		this.g726NonStandard = g726NonStandard;
	}

	public String get100rel() {
		return _100rel;
	}

	public void set100rel(String _100rel) {
		this._100rel = _100rel;
	}

	public String getTimers() {
		return timers;
	}

	public void setTimers(String timers) {
		this.timers = timers;
	}

	public Boolean isDirectMedia() {
		return directMedia;
	}

	public void setDirectMedia(Boolean directMedia) {
		this.directMedia = directMedia;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public int getTimersMinSe() {
		return timersMinSe;
	}

	public void setTimersMinSe(int timersMinSe) {
		this.timersMinSe = timersMinSe;
	}

	public Boolean isTrustIdOutbound() {
		return trustIdOutbound;
	}

	public void setTrustIdOutbound(Boolean trustIdOutbound) {
		this.trustIdOutbound = trustIdOutbound;
	}

	public int getSubMinExpiry() {
		return subMinExpiry;
	}

	public void setSubMinExpiry(int subMinExpiry) {
		this.subMinExpiry = subMinExpiry;
	}

	public Boolean isRtcpMux() {
		return rtcpMux;
	}

	public void setRtcpMux(Boolean rtcpMux) {
		this.rtcpMux = rtcpMux;
	}

	public Boolean isAcceptMultipleSdpAnswers() {
		return acceptMultipleSdpAnswers;
	}

	public void setAcceptMultipleSdpAnswers(Boolean acceptMultipleSdpAnswers) {
		this.acceptMultipleSdpAnswers = acceptMultipleSdpAnswers;
	}

	public Boolean isTrustConnectedLine() {
		return trustConnectedLine;
	}

	public void setTrustConnectedLine(Boolean trustConnectedLine) {
		this.trustConnectedLine = trustConnectedLine;
	}

	public Boolean isSendPai() {
		return sendPai;
	}

	public void setSendPai(Boolean sendPai) {
		this.sendPai = sendPai;
	}

	public int getRtpKeepalive() {
		return rtpKeepalive;
	}

	public void setRtpKeepalive(int rtpKeepalive) {
		this.rtpKeepalive = rtpKeepalive;
	}

	public String getT38UdptlEc() {
		return t38UdptlEc;
	}

	public void setT38UdptlEc(String t38UdptlEc) {
		this.t38UdptlEc = t38UdptlEc;
	}

	public Boolean isT38UdptlNat() {
		return t38UdptlNat;
	}

	public void setT38UdptlNat(Boolean t38UdptlNat) {
		this.t38UdptlNat = t38UdptlNat;
	}

	public Boolean isAllowTransfer() {
		return allowTransfer;
	}

	public void setAllowTransfer(Boolean allowTransfer) {
		this.allowTransfer = allowTransfer;
	}

	public String getDtlsCaFile() {
		return dtlsCaFile;
	}

	public void setDtlsCaFile(String dtlsCaFile) {
		this.dtlsCaFile = dtlsCaFile;
	}

	public String getOutboundProxy() {
		return outboundProxy;
	}

	public void setOutboundProxy(String outboundProxy) {
		this.outboundProxy = outboundProxy;
	}

	public Boolean isInbandProgress() {
		return inbandProgress;
	}

	public void setInbandProgress(Boolean inbandProgress) {
		this.inbandProgress = inbandProgress;
	}

	public String getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}

	public String getActiveChannels() {
		return activeChannels;
	}

	public void setActiveChannels(String activeChannels) {
		this.activeChannels = activeChannels;
	}

	public EndpointDetail(Object source) {
		super(source);
	}

	public Boolean getIgnore183withoutsdp() {
		return ignore183withoutsdp;
	}

	public void setIgnore183withoutsdp(Boolean ignore183withoutsdp) {
		this.ignore183withoutsdp = ignore183withoutsdp;
	}

}
