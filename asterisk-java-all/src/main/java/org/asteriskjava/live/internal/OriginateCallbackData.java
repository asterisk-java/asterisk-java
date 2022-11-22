/**
 *
 */
package org.asteriskjava.live.internal;

import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.action.OriginateAction;

import java.util.Date;

/**
 * Wrapper class for OriginateCallbacks.
 *
 * @author srt
 * @version $Id$
 */
class OriginateCallbackData {
    private OriginateAction originateAction;
    private Date dateSent;
    private OriginateCallback callback;
    private AsteriskChannelImpl channel;

    /**
     * Creates a new instance.
     *
     * @param originateAction the action that has been sent to the Asterisk
     *                        server
     * @param dateSent        date when the the action has been sent
     * @param callback        callback to notify about result
     */
    OriginateCallbackData(OriginateAction originateAction, Date dateSent, OriginateCallback callback) {
        super();
        this.originateAction = originateAction;
        this.dateSent = dateSent;
        this.callback = callback;
    }

    OriginateAction getOriginateAction() {
        return originateAction;
    }

    Date getDateSent() {
        return dateSent;
    }

    OriginateCallback getCallback() {
        return callback;
    }

    AsteriskChannelImpl getChannel() {
        return channel;
    }

    void setChannel(AsteriskChannelImpl channel) {
        this.channel = channel;
    }
}
