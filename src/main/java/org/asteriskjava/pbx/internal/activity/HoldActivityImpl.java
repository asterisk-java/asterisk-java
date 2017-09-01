package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.HoldActivity;
import org.asteriskjava.pbx.agi.AgiChannelActivityHold;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class HoldActivityImpl extends ActivityHelper<HoldActivity> implements HoldActivity
{
    private static final Log logger = LogFactory.getLog(HoldActivityImpl.class);
    private final Channel _channel;

    public HoldActivityImpl(final Channel channel, final ActivityCallback<HoldActivity> callback)
    {
        super("HoldCall", callback);

        this._channel = channel;
        this.startActivity(false);
    }

    @Override
    public boolean doActivity() throws PBXException
    {
        boolean success = false;

        HoldActivityImpl.logger.debug("*******************************************************************************");
        HoldActivityImpl.logger.info("***********                    begin Hold Audio               ****************");
        HoldActivityImpl.logger.info("***********                  " + this._channel + " ****************");
        HoldActivityImpl.logger.debug("*******************************************************************************");
        try
        {
            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

            if (!pbx.moveChannelToAgi(_channel))
            {
                throw new PBXException("Channel: " + _channel + " couldn't be moved to agi");
            }

            _channel.setCurrentActivityAction(new AgiChannelActivityHold());
            success = true;

        }
        catch (IllegalArgumentException | IllegalStateException e)
        {
            HoldActivityImpl.logger.error(e, e);
            HoldActivityImpl.logger.error(e, e);
            throw new PBXException(e);
        }
        return success;
    }

    public Channel getChannel()
    {
        return this._channel;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();
        // no events requried.

        return required;
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

    @Override
    public void onManagerEvent(ManagerEvent event)
    {
        // NOOP

    }

}
