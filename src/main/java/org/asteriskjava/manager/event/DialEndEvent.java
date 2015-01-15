package org.asteriskjava.manager.event;

public class DialEndEvent extends DialEvent {
    public DialEndEvent(Object source) { super(source); setSubEvent("End"); }
}
