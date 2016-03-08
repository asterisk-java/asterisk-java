package org.asteriskjava.manager.event;
/**
 * Raised when a Channel Event Log is generated for a channel.
 * https://wiki.asterisk.org/wiki/display/AST/Asterisk+13+ManagerEvent_CEL
 */
public class CelEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;
    public static final String CEL_EVENT_CHAN_START = "CHAN_START";
    public static final String CEL_EVENT_CHAN_END = "CHAN_END";
    public static final String CEL_EVENT_ANSWER = "ANSWER";
    public static final String CEL_EVENT_HANGUP = "HANGUP";
    public static final String CEL_EVENT_BRIDGE_ENTER = "BRIDGE_ENTER";
    public static final String CEL_EVENT_BRIDGE_EXIT = "BRIDGE_EXIT";
    public static final String CEL_EVENT_APP_START = "APP_START";
    public static final String CEL_EVENT_APP_END = "APP_END";
    public static final String CEL_EVENT_PARK_START = "PARK_START";
    public static final String CEL_EVENT_PARK_END = "PARK_END";
    public static final String CEL_EVENT_BLINDTRANSFER = "BLINDTRANSFER";
    public static final String CEL_EVENT_ATTENDEDTRANSFER = "ATTENDEDTRANSFER";
    public static final String CEL_EVENT_PICKUP = "PICKUP";
    public static final String CEL_EVENT_FORWARD = "FORWARD";
    public static final String CEL_EVENT_LINKEDID_END = "LINKEDID_END";
    public static final String CEL_EVENT_LOCAL_OPTIMIZE = "LOCAL_OPTIMIZE";
    public static final String CEL_EVENT_USER_DEFINED = "USER_DEFINED";

    private String eventName;
    private String accountCode;
    private String callerIDani;
    private String callerIDrdnis;
    private String callerIDdnid;
    private String exten;
    private String context;
    private String application;
    private String appData;
    private String eventTime;
    private String amaFlags;
    private String uniqueID;
    private String linkedID;
    private String userField;
    private String peer;
    private String peerAccount;
    private String extra;
    private String channel;

    public CelEvent(Object source)
    {
        super(source);
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }    

    public String getCallerIDani()
    {
        return callerIDani;
    }

    public void setCallerIDani(String callerIDani)
    {
        this.callerIDani = callerIDani;
    }

    public String getCallerIDrdnis()
    {
        return callerIDrdnis;
    }

    public void setCallerIDrdnis(String callerIDrdnis)
    {
        this.callerIDrdnis = callerIDrdnis;
    }

    public String getCallerIDdnid()
    {
        return callerIDdnid;
    }

    public void setCallerIDdnid(String callerIDdnid)
    {
        this.callerIDdnid = callerIDdnid;
    }

    public String getExten()
    {
        return exten;
    }

    public void setExten(String exten)
    {
        this.exten = exten;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    public String getApplication()
    {
        return application;
    }

    public void setApplication(String application)
    {
        this.application = application;
    }

    public String getAppData()
    {
        return appData;
    }

    public void setAppData(String appData)
    {
        this.appData = appData;
    }

    public String getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(String eventTime)
    {
        this.eventTime = eventTime;
    }

    public String getAmaFlags()
    {
        return amaFlags;
    }

    public void setAmaFlags(String amaFlags)
    {
        this.amaFlags = amaFlags;
    }

    public String getUniqueID()
    {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID)
    {
        this.uniqueID = uniqueID;
    }

    public String getLinkedID()
    {
        return linkedID;
    }

    public void setLinkedID(String linkedID)
    {
        this.linkedID = linkedID;
    }

    public String getUserField()
    {
        return userField;
    }

    public void setUserField(String userField)
    {
        this.userField = userField;
    }

    public String getPeer()
    {
        return peer;
    }

    public void setPeer(String peer)
    {
        this.peer = peer;
    }

    public String getPeerAccount()
    {
        return peerAccount;
    }

    public void setPeerAccount(String peerAccount)
    {
        this.peerAccount = peerAccount;
    }

    public String getExtra()
    {
        return extra;
    }

    public void setExtra(String extra)
    {
        this.extra = extra;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }
}
