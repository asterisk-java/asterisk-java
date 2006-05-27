package org.asteriskjava.live;

/**
 * Empty implementation of the {@link org.asteriskjava.live.AsteriskServerListener}
 * interface. Use this class as a base class for your own listeners if you only want
 * to implement a subset of the methods in {@link org.asteriskjava.live.AsteriskServerListener}.  
 * 
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public abstract class AbstractAsteriskServerListener implements AsteriskServerListener
{
    public void onNewAsteriskChannel(AsteriskChannel channel)
    {

    }

    public void onNewMeetMeUser(MeetMeUser user)
    {

    }
}
