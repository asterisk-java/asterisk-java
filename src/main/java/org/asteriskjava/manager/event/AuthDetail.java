package org.asteriskjava.manager.event;


import org.asteriskjava.ami.action.api.response.event.ResponseEvent;

/**
 * An AuthDetail event is triggered in response to a
 * {@link org.asteriskjava.manager.action.PJSipShowEndpoint},
 * and contains information about a PJSIP Contact
 * <p>
 *
 * @author Steve Sether
 * @version $Id$
 * @since 12
 */

public class AuthDetail extends ResponseEvent {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -4998727723768836212L;

    private String event;
    private String objectType;
    private String objectName;
    private String username;
    private String password;
    private String md5Cred;
    private String realm;
    private int nonceLifetime;
    private String authType;
    private String EndpointName;

    public AuthDetail(Object source) {
        super(source);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMd5Cred() {
        return md5Cred;
    }

    public void setMd5Cred(String md5Cred) {
        this.md5Cred = md5Cred;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public int getNonceLifetime() {
        return nonceLifetime;
    }

    public void setNonceLifetime(int nonceLifetime) {
        this.nonceLifetime = nonceLifetime;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getEndpointName() {
        return EndpointName;
    }

    public void setEndpointName(String endpointName) {
        EndpointName = endpointName;
    }


}
