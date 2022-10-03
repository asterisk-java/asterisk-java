package org.asteriskjava.manager.event;

/**
 * Response to a {@link org.asteriskjava.manager.action.ConfbridgeListAction}.
 *
 * @see org.asteriskjava.manager.action.ConfbridgeListAction
 * @since 1.0.0
 */
public class ConfbridgeListEvent extends ResponseEvent {
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    private String conference;
    private Boolean admin;
    private Boolean markedUser;
    private String channel;
    private String linkedid;
    private String waiting;
    private String language;
    private String talking;
    private String muted;
    private String uniqueid;
    private int answeredtime;
    private String waitmarked;
    private String endmarked;
    private String accountcode;

    public ConfbridgeListEvent(Object source) {
        super(source);
    }

    /**
     * Sets the id of the conference to be listed.
     *
     * @param conference the id of the conference to be listed.
     */
    public void setConference(String conference) {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference to be listed.
     *
     * @return the id of the conference to be listed.
     */
    public String getConference() {
        return conference;
    }

    /**
     * Sets the role of the caller in the list admin = yes or no of the
     * conference.
     *
     * @param admin = yes or no the role of the caller in the list of the
     *              conference.
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * Returns the role of the caller in the list admin = yes or no of the
     * conference.
     *
     * @return the role of the caller in the list admin = yes or no of the
     * conference.
     */
    public Boolean getAdmin() {
        return admin;
    }

    public void setMarkedUser(Boolean markedUser) {
        this.markedUser = markedUser;
    }

    public Boolean getMarkedUser() {
        return markedUser;
    }

    /**
     * Sets the name of the channel in the list.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Returns the name of the channel in the list.
     */
    public String getChannel() {
        return channel;
    }

    public String getLinkedid() {
        return linkedid;
    }

    public void setLinkedid(String linkedid) {
        this.linkedid = linkedid;
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTalking() {
        return talking;
    }

    public void setTalking(String talking) {
        this.talking = talking;
    }

    public String getMuted() {
        return muted;
    }

    public void setMuted(String muted) {
        this.muted = muted;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public int getAnsweredtime() {
        return answeredtime;
    }

    public void setAnsweredtime(int answeredtime) {
        this.answeredtime = answeredtime;
    }

    public String getWaitmarked() {
        return waitmarked;
    }

    public void setWaitmarked(String waitmarked) {
        this.waitmarked = waitmarked;
    }

    public String getEndmarked() {
        return endmarked;
    }

    public void setEndmarked(String endmarked) {
        this.endmarked = endmarked;
    }

    public String getAccountcode() {
        return accountcode;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }
}
