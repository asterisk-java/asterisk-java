package org.asteriskjava.live;

/**
 * You can register an AsteriskQueueListener with an
 * {@link org.asteriskjava.live.AsteriskQueue} to be notified about new calls in
 * and out of the queue
 * 
 * @author gmi
 * @since 0.3
 */
public interface AsteriskQueueListener
{
    /**
     * Called whenever a new channel (entry) appears in the queue
     * 
     * @param channel the new channel.
     */
    void onNewEntry(AsteriskChannel channel);

    /**
     * Called whenever a channel (entry) leaves the queue
     * 
     * @param channel the channel that leaves the queue.
     */
    void onEntryLeave(AsteriskChannel channel);

    /**
     * Called whenever a member changes his state
     * @param member the member that changes his state.
     * @since 0.3.1
     * @author pbreucking
     */
    void onMemberStateChange(AsteriskQueueMember member);

    /**
     * @param channel
     */
    void onEntryServiceLevelExceeded(AsteriskChannel channel);
}
