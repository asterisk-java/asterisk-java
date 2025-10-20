/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.event;

import org.asteriskjava.ami.action.api.response.event.ResponseEvent;

/**
 * A ContactListCompleteEvent is triggered after the details of all peers has
 * been reported in response to an PJSipShowContactsAction.
 * <p>
 * Available since Asterisk 16?
 *
 * @author srt
 * @version $Id$
 * @since 3.0
 */
public class ContactList extends ResponseEvent {
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1177773673509373296L;

    Double qualifyTimeout;
    String callid;
    String regserver;

    // roundtripusec when it contains a value is a long, but when it doesn't
    // asterisk reports "N/A"
    String roundtripusec;
    Long expirationtime;
    String authenticatequalify;
    String objectname;
    String useragent;
    String uri;
    String viaaddr;
    Long qualifyfrequency;
    String path;
    String endpoint;
    String viaport;
    String outboundproxy;

    String objecttype;
    String pruneonboot;
    ContactStatusEnum status;

    /**
     * Creates a new instance.
     *
     * @param source
     */
    public ContactList(Object source) {
        super(source);
    }

    public double getQualifyTimeout() {
        return qualifyTimeout;
    }

    public void setQualifyTimeout(double qualifyTimeout) {
        this.qualifyTimeout = qualifyTimeout;
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }

    public String getRegserver() {
        return regserver;
    }

    public void setRegserver(String regserver) {
        this.regserver = regserver;
    }

    public String getRoundtripusec() {
        return roundtripusec;
    }

    public void setRoundtripusec(String roundtripusec) {
        this.roundtripusec = roundtripusec;
    }

    public long getExpirationtime() {
        return expirationtime;
    }

    public void setExpirationtime(long expirationtime) {
        this.expirationtime = expirationtime;
    }

    public String getAuthenticatequalify() {
        return authenticatequalify;
    }

    public void setAuthenticatequalify(String authenticatequalify) {
        this.authenticatequalify = authenticatequalify;
    }

    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getViaaddr() {
        return viaaddr;
    }

    public void setViaaddr(String viaaddr) {
        this.viaaddr = viaaddr;
    }

    public long getQualifyfrequency() {
        return qualifyfrequency;
    }

    public void setQualifyfrequency(Long qualifyfrequency) {
        this.qualifyfrequency = qualifyfrequency;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getViaport() {
        return viaport;
    }

    public void setViaport(String viaport) {
        this.viaport = viaport;
    }

    public String getOutboundproxy() {
        return outboundproxy;
    }

    public void setOutboundproxy(String outboundproxy) {
        this.outboundproxy = outboundproxy;
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }

    public String getPruneonboot() {
        return pruneonboot;
    }

    public void setPruneonboot(String pruneonboot) {
        this.pruneonboot = pruneonboot;
    }

    public ContactStatusEnum getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = ContactStatusEnum.UNKNOWN;

        if (status != null && status.length() > 0) {
            this.status = ContactStatusEnum.valueOf(status.toUpperCase());
        }

    }

    @Override
    public String toString() {
        return "ContactList [qualifyTimeout=" + qualifyTimeout + ", callid=" + callid + ", regserver=" + regserver
                + ", roundtripusec=" + roundtripusec + ", expirationtime=" + expirationtime + ", authenticatequalify="
                + authenticatequalify + ", objectname=" + objectname + ", useragent=" + useragent + ", uri=" + uri
                + ", viaaddr=" + viaaddr + ", qualifyfrequency=" + qualifyfrequency + ", path=" + path + ", endpoint="
                + endpoint + ", viaport=" + viaport + ", outboundproxy=" + outboundproxy + ", actionId=" + getActionId()
                + ", objecttype=" + objecttype + ", pruneonboot=" + pruneonboot + ", status=" + status + "]\n";
    }

}
