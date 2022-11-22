package org.asteriskjava.manager.event;

/**
 * A ContactStatusDetail event is triggered in response to a
 * {@link org.asteriskjava.manager.action.PJSipShowEndpoint}, and contains
 * information about a PJSIP Contact
 * <p>
 *
 * @author Steve Sether
 * @version $Id$
 * @since 12
 */

public class AorDetail extends ResponseEvent {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 2634171854313358591L;

    public AorDetail(Object source) {
        super(source);
    }

    private String objectType;
    private String objectName;
    private int minimumExpiration;
    private int defaultExpiration;
    private Float qualifyTimeout;
    private String mailboxes;
    private Boolean supportPath;
    private String voicemailExtension;
    private int maxContacts;
    private Boolean authenticateQualify;
    private String contacts;
    private int maximumExpiration;
    private int qualifyFrequency;
    private Boolean removeExisting;
    private String outboundProxy;
    private int totalContacts;
    private int contactsRegistered;
    private String endpointName;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getMinimumExpiration() {
        return minimumExpiration;
    }

    public void setMinimumExpiration(int minimumExpiration) {
        this.minimumExpiration = minimumExpiration;
    }

    public int getDefaultExpiration() {
        return defaultExpiration;
    }

    public void setDefaultExpiration(int defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    public Float getQualifyTimeout() {
        return qualifyTimeout;
    }

    public void setQualifyTimeout(Float qualifyTimeout) {
        this.qualifyTimeout = qualifyTimeout;
    }

    public String getMailboxes() {
        return mailboxes;
    }

    public void setMailboxes(String mailboxes) {
        this.mailboxes = mailboxes;
    }

    public Boolean isSupportPath() {
        return supportPath;
    }

    public void setSupportPath(Boolean supportPath) {
        this.supportPath = supportPath;
    }

    public String getVoicemailExtension() {
        return voicemailExtension;
    }

    public void setVoicemailExtension(String voicemailExtension) {
        this.voicemailExtension = voicemailExtension;
    }

    public int getMaxContacts() {
        return maxContacts;
    }

    public void setMaxContacts(int maxContacts) {
        this.maxContacts = maxContacts;
    }

    public Boolean isAuthenticateQualify() {
        return authenticateQualify;
    }

    public void setAuthenticateQualify(Boolean authenticateQualify) {
        this.authenticateQualify = authenticateQualify;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public int getMaximumExpiration() {
        return maximumExpiration;
    }

    public void setMaximumExpiration(int maximumExpiration) {
        this.maximumExpiration = maximumExpiration;
    }

    public int getQualifyFrequency() {
        return qualifyFrequency;
    }

    public void setQualifyFrequency(int qualifyFrequency) {
        this.qualifyFrequency = qualifyFrequency;
    }

    public Boolean isRemoveExisting() {
        return removeExisting;
    }

    public void setRemoveExisting(Boolean removeExisting) {
        this.removeExisting = removeExisting;
    }

    public String getOutboundProxy() {
        return outboundProxy;
    }

    public void setOutboundProxy(String outboundProxy) {
        this.outboundProxy = outboundProxy;
    }

    public int getTotalContacts() {
        return totalContacts;
    }

    public void setTotalContacts(int totalContacts) {
        this.totalContacts = totalContacts;
    }

    public int getContactsRegistered() {
        return contactsRegistered;
    }

    public void setContactsRegistered(int contactsRegistered) {
        this.contactsRegistered = contactsRegistered;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

}
