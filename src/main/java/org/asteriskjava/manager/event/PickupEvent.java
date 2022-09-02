package org.asteriskjava.manager.event;

public class PickupEvent extends ManagerEvent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String accountcode;
    private String channel;
    private String language;
    private String linkedid;
    private String targetaccountcode;
    private String targetcalleridname;
    private String targetcalleridnum;
    private String targetchannel;
    private String targetchannelstate;
    private String targetchannelstatedesc;
    private String targetconnectedlinename;
    private String targetconnectedlinenum;
    private String targetcontext;
    private String targetexten;
    private String targetlanguage;
    private String targetlinkedid;
    private String targetpriority;
    private String targetuniqueid;
    private String uniqueid;

    public PickupEvent(Object source) {
        super(source);
    }

    public String getAccountcode() {
        return accountcode;
    }

    public String getChannel() {
        return channel;
    }

    public String getLanguage() {
        return language;
    }

    public String getLinkedid() {
        return linkedid;
    }

    public String getTargetaccountcode() {
        return targetaccountcode;
    }

    public String getTargetcalleridname() {
        return targetcalleridname;
    }

    public String getTargetcalleridnum() {
        return targetcalleridnum;
    }

    public String getTargetchannel() {
        return targetchannel;
    }

    public String getTargetchannelstate() {
        return targetchannelstate;
    }

    public String getTargetchannelstatedesc() {
        return targetchannelstatedesc;
    }

    public String getTargetconnectedlinename() {
        return targetconnectedlinename;
    }

    public String getTargetconnectedlinenum() {
        return targetconnectedlinenum;
    }

    public String getTargetcontext() {
        return targetcontext;
    }

    public String getTargetexten() {
        return targetexten;
    }

    public String getTargetlanguage() {
        return targetlanguage;
    }

    public String getTargetlinkedid() {
        return targetlinkedid;
    }

    public String getTargetpriority() {
        return targetpriority;
    }

    public String getTargetuniqueid() {
        return targetuniqueid;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLinkedid(String linkedid) {
        this.linkedid = linkedid;
    }

    public void setTargetaccountcode(String targetaccountcode) {
        this.targetaccountcode = targetaccountcode;
    }

    public void setTargetcalleridname(String targetcalleridname) {
        this.targetcalleridname = targetcalleridname;
    }

    public void setTargetcalleridnum(String targetcalleridnum) {
        this.targetcalleridnum = targetcalleridnum;
    }

    public void setTargetchannel(String targetchannel) {
        this.targetchannel = targetchannel;
    }

    public void setTargetchannelstate(String targetchannelstate) {
        this.targetchannelstate = targetchannelstate;
    }

    public void setTargetchannelstatedesc(String targetchannelstatedesc) {
        this.targetchannelstatedesc = targetchannelstatedesc;
    }

    public void setTargetconnectedlinename(String targetconnectedlinename) {
        this.targetconnectedlinename = targetconnectedlinename;
    }

    public void setTargetconnectedlinenum(String targetconnectedlinenum) {
        this.targetconnectedlinenum = targetconnectedlinenum;
    }

    public void setTargetcontext(String targetcontext) {
        this.targetcontext = targetcontext;
    }

    public void setTargetexten(String targetexten) {
        this.targetexten = targetexten;
    }

    public void setTargetlanguage(String targetlanguage) {
        this.targetlanguage = targetlanguage;
    }

    public void setTargetlinkedid(String targetlinkedid) {
        this.targetlinkedid = targetlinkedid;
    }

    public void setTargetpriority(String targetpriority) {
        this.targetpriority = targetpriority;
    }

    public void setTargetuniqueid(String targetuniqueid) {
        this.targetuniqueid = targetuniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

}
