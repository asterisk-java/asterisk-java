package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BridgeLeaveEvent extends ManagerEvent {
    private String bridgeUniqueId;

    public BridgeLeaveEvent(Object source) { super(source); }
}
