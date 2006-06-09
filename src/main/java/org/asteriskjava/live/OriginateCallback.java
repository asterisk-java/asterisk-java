package org.asteriskjava.live;

/**
 * Callback interface for asynchronous originates.
 *
 * @see org.asteriskjava.live.AsteriskServer#ori
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public interface OriginateCallback
{
    /**
     * Called if the originate was successful and the called party answered the call.
     * 
     * @param channel the channel created.
     */
    void onSuccess(AsteriskChannel channel);

    /**
     * Called if the originate was unsuccessful because the called party did not answer the call.
     * 
     * @param channel  the channel created.
     */
    void onNoAnswer(AsteriskChannel channel);

    /**
     * Called if the originate was unsuccessful because the called party was busy.
     * 
     * @param channel  the channel created.
     */
    void onBusy(AsteriskChannel channel);

    /**
     * Called if the originate failed for example due to an invalid channel name.
     * 
     * @param cause the exception that caused the failure.
     */
    void onFailure(LiveException cause);
}
