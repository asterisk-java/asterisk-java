package org.asteriskjava.pbx.internal.core;

/**
 * Used to notify a listener that a Call (CallTracker) has ended.
 * <p>
 * This generally means the last channel has hungup.
 *
 * @author bsutton
 */
public interface CallEndedListener {
    void callEnded(CallTracker listener);
}
