package org.asteriskjava.pbx.internal.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ChannelHangupListener;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.agi.AgiChannelActivityHold;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * The ChannelProxy exists to deal with the fact that Asterisk will often
 * replace a channel during a call. This is referred to as Masquerading. When a
 * channel is to be Masqueraded we get a Masquerade event. The ChannelProxy
 * class acts as a level of indirection for an iChannel. Rather than holding an
 * iChannel directly you can instead hold a ChannelProxy. If the underlying
 * iChannel is replaced the ChannelProxy will be updated with the new iChannel.
 * 
 * @author bsutton
 */
public class ChannelProxy implements Channel, ChannelHangupListener
{

    private static final Log logger = LogFactory.getLog(ChannelProxy.class);

    /**
     * We give each proxy a unique identity to help track them when debugging.
     */
    private static final AtomicInteger _nextIdentity = new AtomicInteger();

    private int _identity = _nextIdentity.incrementAndGet();

    private ChannelImpl _channel;

    private List<ChannelHangupListener> listeners = new CopyOnWriteArrayList<>();

    public ChannelProxy(ChannelImpl channel)
    {
        this._channel = channel;
        currentActivityAction.set(new AgiChannelActivityHold());

        channel.addHangupListener(this);

    }

    /**
     * returns the current channel
     * 
     * @return
     */
    public Channel getChannel()
    {
        return this._channel;
    }

    @Override
    public boolean isSame(Channel rhs)
    {
        return this._channel.isSame(rhs);
    }

    @Override
    public boolean isSame(String extendedChannelName, String uniqueID)
    {
        return this._channel.isSame(extendedChannelName, uniqueID);
    }

    public boolean sameUniqueID(String uniqueID)
    {
        return this._channel.sameUniqueID(uniqueID);
    }

    @Override
    public boolean sameEndPoint(Channel rhs)
    {
        return this._channel.sameEndPoint(rhs);
    }

    @Override
    public boolean sameEndPoint(EndPoint extensionRoaming)
    {
        return this._channel.sameEndPoint(extensionRoaming);
    }

    @Override
    public boolean sameExtenededChannelName(String channelName)
    {
        return this._channel.sameExtenededChannelName(channelName);
    }

    @Override
    public void setParked(boolean parked)
    {
        this._channel.setParked(parked);
    }

    @Override
    public void setMute(boolean muteState)
    {
        this._channel.setMute(muteState);

    }

    @Override
    public long getChannelId()
    {
        return this._channel.getChannelId();
    }

    @Override
    public boolean isLive()
    {
        return this._channel.isLive();
    }

    @Override
    public void addHangupListener(ChannelHangupListener listener)
    {
        if (!listeners.contains(listener))
        {
            this.listeners.add(listener);
        }

    }

    @Override
    public void removeListener(ChannelHangupListener listener)
    {

        this.listeners.remove(listener);

    }

    @Override
    public boolean isConnectedTo(EndPoint endPoint)
    {
        return this._channel.isConnectedTo(endPoint);
    }

    @Override
    public String getChannelName()
    {
        return this._channel.getChannelName();
    }

    @Override
    public EndPoint getEndPoint()
    {
        return this._channel.getEndPoint();
    }

    @Override
    public boolean isMute()
    {
        return this._channel.isMute();
    }

    @Override
    public boolean isLocal()
    {
        return this._channel.isLocal();
    }

    @Override
    public boolean isZombie()
    {
        return this._channel.isZombie();
    }

    @Override
    public boolean isConsole()
    {
        return this._channel.isConsole();
    }

    @Override
    public CallerID getCallerID()
    {
        return this._channel.getCallerID();
    }

    @Override
    public void rename(String newName, String uniqueId) throws InvalidChannelName
    {
        this._channel.rename(newName, uniqueId);
    }

    @Override
    public boolean isParked()
    {
        return this._channel.isParked();
    }

    /**
     * Used to handle a MasqueradeEvent. We essentially swap the two underlying
     * channels between the two proxies.
     * 
     * @param cloneProxy
     * @throws InvalidChannelName
     */
    public void masquerade(ChannelProxy cloneProxy) throws InvalidChannelName
    {
        ChannelImpl originalChannel = this._channel;
        ChannelImpl cloneChannel = cloneProxy._channel;

        cloneChannel.masquerade(this._channel);

        // detach the hangup listeners
        originalChannel.removeListener(this);
        cloneChannel.removeListener(cloneProxy);

        // Now we swap the channels.
        this._channel = cloneChannel;
        cloneProxy._channel = originalChannel;

        // Now re-attach the hangup listeners
        this._channel.addHangupListener(this);
        cloneProxy._channel.addHangupListener(cloneProxy);

        logger.debug(originalChannel + " Channel proxy now points to " + this._channel);
    }

    public ChannelImpl getRealChannel()
    {
        return this._channel;
    }

    @Override
    public String getExtendedChannelName()
    {
        return this._channel.getExtendedChannelName();
    }

    @Override
    public void notifyHangupListeners(Integer cause, String causeText)
    {
        for (ChannelHangupListener listener : this.listeners)
        {
            listener.channelHangup(this, cause, causeText);
        }

    }

    @Override
    public String toString()
    {
        return "(proxy=" + this._identity + ") " + this._channel.toString(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void channelHangup(Channel channel, Integer cause, String causeText)
    {
        // When the underlying channel hangs up we need to notify all of the
        // proxy listeners.
        if (channel == this._channel)
            notifyHangupListeners(cause, causeText);

    }

    @Override
    public boolean canDetectHangup()
    {
        return this._channel.canDetectHangup();
    }

    @Override
    public boolean isQuiescent()
    {
        return this._channel.isQuiescent();
    }

    @Override
    public boolean hasCallerID()
    {
        return this._channel.hasCallerID();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + _identity;
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof ChannelProxy))
        {
            return false;
        }
        ChannelProxy other = (ChannelProxy) obj;
        if (_identity != other._identity)
        {
            return false;
        }
        return true;
    }

    private AtomicReference<AgiChannelActivityAction> currentActivityAction = new AtomicReference<>();
    volatile private boolean isInAgi;

    @Override
    public AgiChannelActivityAction getCurrentActivityAction()
    {
        return currentActivityAction.get();
    }

    @Override
    public void setCurrentActivityAction(AgiChannelActivityAction action)
    {
        AgiChannelActivityAction previousAction = currentActivityAction.get();

        logger.debug("Setting action to " + action.getClass().getSimpleName() + " for " + this);

        // Exception e = new Exception("Setting action to " +
        // action.getClass().getSimpleName() + " for " + this);
        // logger.warn(e, e);

        currentActivityAction.set(action);
        if (previousAction != null)
        {
            // when we cancel the previous action, the new one will be invoked
            previousAction.cancel();
        }
    }

    @Override
    public void setIsInAgi(boolean b)
    {
        if (b)
        {
            hasReachedAgi.countDown();
        }
        isInAgi = b;
        logger.debug("Setting is in agi to " + b + " for channel " + this);

    }

    @Override
    public boolean isInAgi()
    {
        return isInAgi;
    }

    final CountDownLatch hasReachedAgi = new CountDownLatch(1);

    @Override
    public boolean waitForChannelToReachAgi(long timeout, TimeUnit timeunit) throws InterruptedException
    {
        return hasReachedAgi.await(timeout, timeunit);
    }

    @Override
    public String getUniqueId()
    {
        return _channel.getUniqueId();
    }

    public int getIdentity()
    {
        return _identity;
    }

    @Override
    public void setCallerId(CallerID callerId)
    {
        _channel.setCallerId(callerId);

    }

}
