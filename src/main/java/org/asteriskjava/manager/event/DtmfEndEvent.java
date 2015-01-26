package org.asteriskjava.manager.event;

/**
 * Created by Alexander Polakov <apolyakov@beget.ru> on 1/26/15.
 */
public class DtmfEndEvent extends DtmfEvent {
    public DtmfEndEvent(Object source) { super(source); setBegin(false); setEnd(true); }
}
