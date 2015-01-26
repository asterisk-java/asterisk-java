package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class BlindTransferEvent extends AbstractBridgeEvent {
    private String transfererConnectedLineNum;
    private String transfereeChannel;

    public BlindTransferEvent(Object source) { super(source); }
}
