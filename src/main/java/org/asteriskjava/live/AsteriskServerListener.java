package org.asteriskjava.live;

/**
 * You can register an AsteriskServerListener with an
 * {@link org.asteriskjava.live.AsteriskServer} to be notified about new
 * channels and MeetMe users.
 * <p>
 * Usually it is better to extend {@link AbstractAsteriskServerListener} than to
 * implement this interface directly as additonal methods will probably be added
 * in future versions of Asterisk-Java.
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public interface AsteriskServerListener
{
    /**
     * Called whenever a new channel appears on the Asterisk server.
     * 
     * @param channel the new channel.
     */
    void onNewAsteriskChannel(AsteriskChannel channel);

    /**
     * Called whenever a user joins a {@link MeetMeRoom}.
     * 
     * @param user the user that joined.
     */
    void onNewMeetMeUser(MeetMeUser user);

    /**
     * Called whenever a new agent will be registered at Asterisk server.
     * 
     * @param agent
     */
    void onNewAgent(AsteriskAgent agent);
    
    /**
     * Called whenever a queue entry ( ~ wapper over channel) joins a {@link org.asteriskjava.live.AsteriskQueue}.
     * 
     * @param entry the queue entry that joined.
     */
    void onNewQueueEntry(AsteriskQueueEntry entry);
}
