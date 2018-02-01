package org.asteriskjava.pbx;

import java.util.concurrent.TimeUnit;

/**
 * Provides an abstraction for an channel object which is independant of the
 * underlying PBX.
 * 
 * @author bsutton
 */
public interface Channel
{
    /**
     * Compares if two channels are the same logical channel on the pbx. This
     * comparison uses the full unique name of the channel not just the
     * abbreviated peer name. e.g. SIP/100-00000100
     * 
     * @param rhs
     * @return
     */
    boolean isSame(Channel rhs);

    boolean isSame(String channelName, String uniqueID);

    /*
     * This comparision uses just the peer name. e.g. SIP/100
     */
    boolean sameEndPoint(Channel rhs);

    /*
     * This comparision uses just the peer name. e.g. SIP/100
     */
    boolean sameEndPoint(EndPoint extensionRoaming);

    /**
     * Compares if this channel is the same as the named channel.
     * 
     * @param channelName
     * @return
     */
    boolean sameExtenededChannelName(String channelName);

    /**
     * This method does not actually change the state of the channel, rather it
     * is intended to be used to record the fact that the state has changed.
     * 
     * @see public iPBX.iParkCallActivity park(iChannel channel, Direction
     *      direction, boolean parkState) for a method which actually parks the
     *      channel.
     */
    void setParked(boolean parked);

    /**
     * This method does not actually change the state of the channel, rather it
     * is intended to be used to record the fact that the state has changed.
     * 
     * @see public iPBX.iMuteCallActivity mute(iChannel channel, Direction
     *      direction, boolean muteState) for a method which actually mutes the
     *      channel.
     */
    void setMute(boolean muteState);

    /**
     * Each channel is assigned a unique PBX independent id to help track the
     * channels when logging.
     * 
     * @return
     */
    long getChannelId();

    /**
     * Returns true if the channel is alive. A channel is considered to be alive
     * from the moment it is created until we get a notification that it has
     * been hungup.
     * 
     * @return
     */
    boolean isLive();

    /**
     * Adds a listener to the channel. The channel will send a hungup
     * notification to the listener when it hungup. A channel must be able to
     * support multiple listeners.
     * 
     * @param call
     */
    void addHangupListener(ChannelHangupListener listener);

    void removeListener(ChannelHangupListener listener);

    /**
     * Returns true if the given endpont is currently connected to this channel.
     * 
     * @param extension
     * @return
     */
    boolean isConnectedTo(EndPoint endPoint);

    /**
     * returns a fully qualifed channel name which includes the tech. e.g.
     * SIP/100-00000342
     * 
     * @return
     */
    String getChannelName();

    EndPoint getEndPoint();

    /**
     * Checks if this channel is currently mute. A channel is mute if the party
     * connected to this channel cannot hear any audio.
     * 
     * @return
     */
    boolean isMute();

    /**
     * In Asterisk speak, this method returns true if it is a LOCAL/ channel Not
     * quite certain how this will map to other pbx's
     * 
     * @return
     */
    boolean isLocal();

    /**
     * @return true if the channel is currently parked.
     */
    boolean isParked();

    /**
     * Returns true if the channel has been marked as in a zombie state.
     * 
     * @return
     */
    boolean isZombie();

    /**
     * @return true if the channel was originated from the console
     *         (Console/DSP).
     */
    boolean isConsole();

    /**
     * Returns the current callerid for this channel. Note: on some systems this
     * involves a round trip to the pbx.
     * 
     * @return
     */
    CallerID getCallerID();

    /**
     * Returns a PBX specific version of the Channel name.
     * 
     * @return
     */
    String getExtendedChannelName();

    void notifyHangupListeners(Integer cause, String causeText);

    boolean canDetectHangup();

    boolean isQuiescent();

    boolean hasCallerID();

    AgiChannelActivityAction getCurrentActivityAction();

    void setCurrentActivityAction(AgiChannelActivityAction action);

    void setIsInAgi(boolean b);

    boolean isInAgi();

    String getUniqueId();

    boolean waitForChannelToReachAgi(long timeout, TimeUnit timeunit) throws InterruptedException;

    void setCallerId(CallerID buildCallerID);

    /**
     * Called to rename a channel
     * 
     * @param newname the new name of the channel
     * @throws InvalidChannelName
     */

    void rename(String newName, String uniqueId) throws InvalidChannelName;

}
