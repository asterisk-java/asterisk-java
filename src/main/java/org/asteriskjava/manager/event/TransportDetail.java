package org.asteriskjava.manager.event;


import org.asteriskjava.ami.action.api.response.event.ResponseEvent;

/**
 * A TransportDetail event is triggered in response to a
 * {@link org.asteriskjava.manager.action.PJSipShowEndpoint}
 * <p>
 *
 * @author Steve Sether
 * @version $Id$
 * @since 12
 */

public class TransportDetail extends ResponseEvent {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 8558130768089350575L;
    private String Event;
    private String objectType;
    private String pbjectName;
    private String protocol;
    private String bind;
    private int asycOperations;
    private String caListFile;
    private String caListPath;
    private String certFile;
    private String privKeyFile;
    private String password;
    private String externalSignalingAddress;
    private int externalSignalingPort;
    private String externalMediaAddress;
    private String domain;
    private String verifyServer;
    private String verifyClient;
    private Boolean requireClientCert;
    private String method;
    private String cipher;
    private String localNet;
    private String tos;
    private int cos;
    private int websocketWriteTimeout;
    private String endpointName;


    /**
     * Serial version identifier.
     */
    public TransportDetail(Object source) {
        super(source);
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getPbjectName() {
        return pbjectName;
    }

    public void setPbjectName(String pbjectName) {
        this.pbjectName = pbjectName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public int getAsycOperations() {
        return asycOperations;
    }

    public void setAsycOperations(int asycOperations) {
        this.asycOperations = asycOperations;
    }

    public String getCaListFile() {
        return caListFile;
    }

    public void setCaListFile(String caListFile) {
        this.caListFile = caListFile;
    }

    public String getCaListPath() {
        return caListPath;
    }

    public void setCaListPath(String caListPath) {
        this.caListPath = caListPath;
    }

    public String getCertFile() {
        return certFile;
    }

    public void setCertFile(String certFile) {
        this.certFile = certFile;
    }

    public String getPrivKeyFile() {
        return privKeyFile;
    }

    public void setPrivKeyFile(String privKeyFile) {
        this.privKeyFile = privKeyFile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExternalSignalingAddress() {
        return externalSignalingAddress;
    }

    public void setExternalSignalingAddress(String externalSignalingAddress) {
        this.externalSignalingAddress = externalSignalingAddress;
    }

    public int getExternalSignalingPort() {
        return externalSignalingPort;
    }

    public void setExternalSignalingPort(int externalSignalingPort) {
        this.externalSignalingPort = externalSignalingPort;
    }

    public String getExternalMediaAddress() {
        return externalMediaAddress;
    }

    public void setExternalMediaAddress(String externalMediaAddress) {
        this.externalMediaAddress = externalMediaAddress;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getVerifyServer() {
        return verifyServer;
    }

    public void setVerifyServer(String verifyServer) {
        this.verifyServer = verifyServer;
    }

    public String getVerifyClient() {
        return verifyClient;
    }

    public void setVerifyClient(String verifyClient) {
        this.verifyClient = verifyClient;
    }

    public Boolean getRequireClientCert() {
        return requireClientCert;
    }

    public void setRequireClientCert(Boolean requireClientCert) {
        this.requireClientCert = requireClientCert;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getLocalNet() {
        return localNet;
    }

    public void setLocalNet(String localNet) {
        this.localNet = localNet;
    }

    public String getTos() {
        return tos;
    }

    public void setTos(String tos) {
        this.tos = tos;
    }

    public int getCos() {
        return cos;
    }

    public void setCos(int cos) {
        this.cos = cos;
    }

    public int getWebsocketWriteTimeout() {
        return websocketWriteTimeout;
    }

    public void setWebsocketWriteTimeout(int websocketWriteTimeout) {
        this.websocketWriteTimeout = websocketWriteTimeout;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
