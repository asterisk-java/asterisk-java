package org.asteriskjava.live;

/**
 * The lifecycle status of a {@link org.asteriskjava.live.QueueEntry}.
 * 
 * @author gmi
 */
public enum QueueEntryState
{
    /**
     * The user joined the queue.
     */
    JOINED,

    /**
     * The user left the queue.
     */
    LEFT
}
