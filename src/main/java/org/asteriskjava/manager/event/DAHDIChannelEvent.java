package org.asteriskjava.manager.event;

/**
 * @author Sebastian
 */
public class DAHDIChannelEvent extends ManagerEvent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String dahdichannel;
    private String dahdispan;
    private String uniqueid;
    private String channel;
    private String linkedid;
    private String language;
    private Integer dahdigroup;
    private String accountcode;

    public void setDahdichannel(String dahdichannel) {
        this.dahdichannel = dahdichannel;
    }

    public String getDahdispan() {
        return dahdispan;
    }

    public void setDahdispan(String dahdispan) {
        this.dahdispan = dahdispan;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public DAHDIChannelEvent(Object source) {
        super(source);
    }

    public String getLinkedId() {
        return this.linkedid;
    }

    public void setLinkedId(String linkedid) {
        this.linkedid = linkedid;
    }

    public Integer getDadhiGroup() {
        return this.dahdigroup;
    }

    public void setDadhiGroup(Integer dahdigroup) {
        this.dahdigroup = dahdigroup;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAccountCode() {
        return this.accountcode;
    }

    public void setAccountCode(String accountcode) {
        this.accountcode = accountcode;
    }
}
