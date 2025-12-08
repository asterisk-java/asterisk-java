package org.asteriskjava.manager.response;

public final class FaxLicenseStatusResponse extends ManagerResponse {
    private Integer portsLicensed;

    public Integer getPortsLicensed() {
        return portsLicensed;
    }

    public void setPortsLicensed(Integer portsLicensed) {
        this.portsLicensed = portsLicensed;
    }
}
