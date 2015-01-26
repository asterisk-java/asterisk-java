package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BridgeEnterEvent extends ManagerEvent {
    private String bridgeUniqueId;

    public BridgeEnterEvent(Object source) { super(source); }
}
