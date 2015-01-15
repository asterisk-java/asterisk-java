package org.asteriskjava.manager.event;

public class DialBeginEvent extends DialEvent {
    public DialBeginEvent(Object source) { super(source); setSubEvent("Begin"); }
}
