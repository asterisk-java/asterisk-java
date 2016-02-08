package org.asteriskjava.manager.event;

public class DongleDeviceEntryEvent extends ResponseEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String Device;
    private String AudioSetting;
    private String DataSetting;
    private String IMEISetting;
    private String IMSISetting;
    private String ChannelLanguage;
    private String Group;
    private String RXGain;
    private String TXGain;
    private String U2DIAG;
    private String UseCallingPres;
    private String DefaultCallingPres;
    private String AutoDeleteSMS;
    private String DisableSMS;
    private String ResetDongle;
    private String SMSPDU;
    private String CallWaitingSetting;
    private String DTMF;
    private String MinimalDTMFGap;
    private String MinimalDTMFDuration;
    private String MinimalDTMFInterval;

    private String State;
    private String AudioState;
    private String DataState;
    private String Voice;
    private String SMS;
    private String Manufacturer;
    private String Model;
    private String Firmware;
    private String IMEIState;
    private String GSMRegistrationStatus;
    private String RSSI;
    private String Mode;
    private String Submode;
    private String ProviderName;
    private String LocationAreaCode;
    private String CellID;
    private String SubscriberNumber;
    private String SMSServiceCenter;
    private String UseUCS2Encoding;
    private String USSDUse7BitEncoding;
    private String USSDUseUCS2Decoding;
    private String TasksInQueue;
    private String CommandsInQueue;
    private String CallWaitingState;
    private String CurrentDeviceState;
    private String DesiredDeviceState;
    private String CallsChannels;
    private String Active;
    private String Held;
    private String Dialing;
    private String Alerting;
    private String Incoming;
    private String Waiting;
    private String Releasing;
    private String Initializing;

    public DongleDeviceEntryEvent(Object source)
    {
        super(source);
    }

    public String getDevice()
    {
        return Device;
    }

    public void setDevice(String Device)
    {
        this.Device = Device;
    }

    public String getAudioSetting()
    {
        return AudioSetting;
    }

    public void setAudioSetting(String AudioSetting)
    {
        this.AudioSetting = AudioSetting;
    }

    public String getDataSetting()
    {
        return DataSetting;
    }

    public void setDataSetting(String DataSetting)
    {
        this.DataSetting = DataSetting;
    }

    public String getIMEISetting()
    {
        return IMEISetting;
    }

    public void setIMEISetting(String IMEISetting)
    {
        this.IMEISetting = IMEISetting;
    }

    public String getIMSISetting()
    {
        return IMSISetting;
    }

    public void setIMSISetting(String IMSISetting)
    {
        this.IMSISetting = IMSISetting;
    }

    public String getChannelLanguage()
    {
        return ChannelLanguage;
    }

    public void setChannelLanguage(String ChannelLanguage)
    {
        this.ChannelLanguage = ChannelLanguage;
    }

    public String getGroup()
    {
        return Group;
    }

    public void setGroup(String Group)
    {
        this.Group = Group;
    }

    public String getRXGain()
    {
        return RXGain;
    }

    public void setRXGain(String RXGain)
    {
        this.RXGain = RXGain;
    }

    public String getTXGain()
    {
        return TXGain;
    }

    public void setTXGain(String TXGain)
    {
        this.TXGain = TXGain;
    }

    public String getU2DIAG()
    {
        return U2DIAG;
    }

    public void setU2DIAG(String U2DIAG)
    {
        this.U2DIAG = U2DIAG;
    }

    public String getUseCallingPres()
    {
        return UseCallingPres;
    }

    public void setUseCallingPres(String UseCallingPres)
    {
        this.UseCallingPres = UseCallingPres;
    }

    public String getDefaultCallingPres()
    {
        return DefaultCallingPres;
    }

    public void setDefaultCallingPres(String DefaultCallingPres)
    {
        this.DefaultCallingPres = DefaultCallingPres;
    }

    public String getAutoDeleteSMS()
    {
        return AutoDeleteSMS;
    }

    public void setAutoDeleteSMS(String AutoDeleteSMS)
    {
        this.AutoDeleteSMS = AutoDeleteSMS;
    }

    public String getDisableSMS()
    {
        return DisableSMS;
    }

    public void setDisableSMS(String DisableSMS)
    {
        this.DisableSMS = DisableSMS;
    }

    public String getResetDongle()
    {
        return ResetDongle;
    }

    public void setResetDongle(String ResetDongle)
    {
        this.ResetDongle = ResetDongle;
    }

    public String getSMSPDU()
    {
        return SMSPDU;
    }

    public void setSMSPDU(String SMSPDU)
    {
        this.SMSPDU = SMSPDU;
    }

    public String getCallWaitingSetting()
    {
        return CallWaitingSetting;
    }

    public void setCallWaitingSetting(String CallWaitingSetting)
    {
        this.CallWaitingSetting = CallWaitingSetting;
    }

    public String getDTMF()
    {
        return DTMF;
    }

    public void setDTMF(String DTMF)
    {
        this.DTMF = DTMF;
    }

    public String getMinimalDTMFGap()
    {
        return MinimalDTMFGap;
    }

    public void setMinimalDTMFGap(String MinimalDTMFGap)
    {
        this.MinimalDTMFGap = MinimalDTMFGap;
    }

    public String getMinimalDTMFDuration()
    {
        return MinimalDTMFDuration;
    }

    public void setMinimalDTMFDuration(String MinimalDTMFDuration)
    {
        this.MinimalDTMFDuration = MinimalDTMFDuration;
    }

    public String getMinimalDTMFInterval()
    {
        return MinimalDTMFInterval;
    }

    public void setMinimalDTMFInterval(String MinimalDTMFInterval)
    {
        this.MinimalDTMFInterval = MinimalDTMFInterval;
    }

    public String getState()
    {
        return State;
    }

    public void setState(String State)
    {
        this.State = State;
    }

    public String getAudioState()
    {
        return AudioState;
    }

    public void setAudioState(String AudioState)
    {
        this.AudioState = AudioState;
    }

    public String getDataState()
    {
        return DataState;
    }

    public void setDataState(String DataState)
    {
        this.DataState = DataState;
    }

    public String getVoice()
    {
        return Voice;
    }

    public void setVoice(String Voice)
    {
        this.Voice = Voice;
    }

    public String getSMS()
    {
        return SMS;
    }

    public void setSMS(String SMS)
    {
        this.SMS = SMS;
    }

    public String getManufacturer()
    {
        return Manufacturer;
    }

    public void setManufacturer(String Manufacturer)
    {
        this.Manufacturer = Manufacturer;
    }

    public String getModel()
    {
        return Model;
    }

    public void setModel(String Model)
    {
        this.Model = Model;
    }

    public String getFirmware()
    {
        return Firmware;
    }

    public void setFirmware(String Firmware)
    {
        this.Firmware = Firmware;
    }

    public String getIMEIState()
    {
        return IMEIState;
    }

    public void setIMEIState(String IMEIState)
    {
        this.IMEIState = IMEIState;
    }

    public String getGSMRegistrationStatus()
    {
        return GSMRegistrationStatus;
    }

    public void setGSMRegistrationStatus(String GSMRegistrationStatus)
    {
        this.GSMRegistrationStatus = GSMRegistrationStatus;
    }

    public String getRSSI()
    {
        return RSSI;
    }

    public void setRSSI(String RSSI)
    {
        this.RSSI = RSSI;
    }

    public String getMode()
    {
        return Mode;
    }

    public void setMode(String Mode)
    {
        this.Mode = Mode;
    }

    public String getSubmode()
    {
        return Submode;
    }

    public void setSubmode(String Submode)
    {
        this.Submode = Submode;
    }

    public String getProviderName()
    {
        return ProviderName;
    }

    public void setProviderName(String ProviderName)
    {
        this.ProviderName = ProviderName;
    }

    public String getLocationAreaCode()
    {
        return LocationAreaCode;
    }

    public void setLocationAreaCode(String LocationAreaCode)
    {
        this.LocationAreaCode = LocationAreaCode;
    }

    public String getCellID()
    {
        return CellID;
    }

    public void setCellID(String CellID)
    {
        this.CellID = CellID;
    }

    public String getSubscriberNumber()
    {
        return SubscriberNumber;
    }

    public void setSubscriberNumber(String SubscriberNumber)
    {
        this.SubscriberNumber = SubscriberNumber;
    }

    public String getSMSServiceCenter()
    {
        return SMSServiceCenter;
    }

    public void setSMSServiceCenter(String SMSServiceCenter)
    {
        this.SMSServiceCenter = SMSServiceCenter;
    }

    public String getUseUCS2Encoding()
    {
        return UseUCS2Encoding;
    }

    public void setUseUCS2Encoding(String UseUCS2Encoding)
    {
        this.UseUCS2Encoding = UseUCS2Encoding;
    }

    public String getUSSDUse7BitEncoding()
    {
        return USSDUse7BitEncoding;
    }

    public void setUSSDUse7BitEncoding(String USSDUse7BitEncoding)
    {
        this.USSDUse7BitEncoding = USSDUse7BitEncoding;
    }

    public String getUSSDUseUCS2Decoding()
    {
        return USSDUseUCS2Decoding;
    }

    public void setUSSDUseUCS2Decoding(String USSDUseUCS2Decoding)
    {
        this.USSDUseUCS2Decoding = USSDUseUCS2Decoding;
    }

    public String getTasksInQueue()
    {
        return TasksInQueue;
    }

    public void setTasksInQueue(String TasksInQueue)
    {
        this.TasksInQueue = TasksInQueue;
    }

    public String getCommandsInQueue()
    {
        return CommandsInQueue;
    }

    public void setCommandsInQueue(String CommandsInQueue)
    {
        this.CommandsInQueue = CommandsInQueue;
    }

    public String getCallWaitingState()
    {
        return CallWaitingState;
    }

    public void setCallWaitingState(String CallWaitingState)
    {
        this.CallWaitingState = CallWaitingState;
    }

    public String getCurrentDeviceState()
    {
        return CurrentDeviceState;
    }

    public void setCurrentDeviceState(String CurrentDeviceState)
    {
        this.CurrentDeviceState = CurrentDeviceState;
    }

    public String getDesiredDeviceState()
    {
        return DesiredDeviceState;
    }

    public void setDesiredDeviceState(String DesiredDeviceState)
    {
        this.DesiredDeviceState = DesiredDeviceState;
    }

    public String getCallsChannels()
    {
        return CallsChannels;
    }

    public void setCallsChannels(String CallsChannels)
    {
        this.CallsChannels = CallsChannels;
    }

    public String getActive()
    {
        return Active;
    }

    public void setActive(String Active)
    {
        this.Active = Active;
    }

    public String getHeld()
    {
        return Held;
    }

    public void setHeld(String Held)
    {
        this.Held = Held;
    }

    public String getDialing()
    {
        return Dialing;
    }

    public void setDialing(String Dialing)
    {
        this.Dialing = Dialing;
    }

    public String getAlerting()
    {
        return Alerting;
    }

    public void setAlerting(String Alerting)
    {
        this.Alerting = Alerting;
    }

    public String getIncoming()
    {
        return Incoming;
    }

    public void setIncoming(String Incoming)
    {
        this.Incoming = Incoming;
    }

    public String getWaiting()
    {
        return Waiting;
    }

    public void setWaiting(String Waiting)
    {
        this.Waiting = Waiting;
    }

    public String getReleasing()
    {
        return Releasing;
    }

    public void setReleasing(String Releasing)
    {
        this.Releasing = Releasing;
    }

    public String getInitializing()
    {
        return Initializing;
    }

    public void setInitializing(String Initializing)
    {
        this.Initializing = Initializing;
    }

}
