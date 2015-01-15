package org.asteriskjava.manager.event;

public class DeviceStateChangeEvent extends ManagerEvent {
    private String state;
    private String device;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public DeviceStateChangeEvent(Object source) { super(source); }
}
