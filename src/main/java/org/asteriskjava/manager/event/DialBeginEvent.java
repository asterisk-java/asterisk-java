package org.asteriskjava.manager.event;

public class DialBeginEvent extends DialEvent {
    private String destCallerIdNum;

    public DialBeginEvent(Object source) { super(source); setSubEvent("Begin"); }

    public String getDestCallerIdNum() {
        return destCallerIdNum;
    }

    public void setDestCallerIdNum(String destCallerIdNum) {
        this.destCallerIdNum = destCallerIdNum;
    }
}
