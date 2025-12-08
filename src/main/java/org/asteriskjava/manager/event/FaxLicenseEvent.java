package org.asteriskjava.manager.event;

public final class FaxLicenseEvent extends ResponseEvent {
    private String file;
    private String key;
    private String product;
    private String hostId;
    private Integer ports;
    private String status;

    public FaxLicenseEvent(Object source) {
        super(source);
    }

    @Override
    public String getFile() {
        return file;
    }

    @Override
    public void setFile(String file) {
        this.file = file;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPorts() {
        return ports;
    }

    public void setPorts(Integer ports) {
        this.ports = ports;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
