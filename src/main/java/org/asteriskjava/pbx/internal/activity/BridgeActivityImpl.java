package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.BridgeActivity;
import org.asteriskjava.pbx.agi.AgiChannelActivityBridge;
import org.asteriskjava.pbx.agi.AgiChannelActivityHoldForBridge;
import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.LinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusCompleteEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusEvent;
import org.asteriskjava.pbx.internal.asterisk.DurationRoomOwner;
import org.asteriskjava.pbx.internal.asterisk.MeetmeRoom;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.managerAPI.RedirectToMeetMe;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class BridgeActivityImpl extends ActivityHelper<BridgeActivity> implements BridgeActivity
{
    private static final Log logger = LogFactory.getLog(BridgeActivityImpl.class);
    private final Channel _lhsChannel;
    private final Channel _rhsChannel;

    private CountDownLatch _bridgeLatch;

    public BridgeActivityImpl(final Channel lhsChannel, final Channel rhsChannel,
            final ActivityCallback<BridgeActivity> callback)
    {
        super("Bridge", callback);

        this._lhsChannel = lhsChannel;
        this._rhsChannel = rhsChannel;
        this.startActivity(true);
    }

    @Override
    public boolean doActivity() throws PBXException
    {
        boolean success = false;

        BridgeActivityImpl.logger.debug("*******************************************************************************");
        BridgeActivityImpl.logger.info("***********                    begin Bridge                ****************");
        BridgeActivityImpl.logger.info("***********                  lhs:" + this._lhsChannel + " ****************");
        BridgeActivityImpl.logger.debug("***********                  rhs:" + this._rhsChannel + " ****************");
        BridgeActivityImpl.logger.debug("*******************************************************************************");
        try
        {

            final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

            if (!pbx.moveChannelToAgi(_lhsChannel))
            {
                throw new PBXException("Channel: " + _lhsChannel + " couldn't be moved to agi");
            }
            if (!pbx.moveChannelToAgi(_rhsChannel))
            {
                throw new PBXException("Channel: " + _rhsChannel + " couldn't be moved to agi");
            }

            List<Channel> channels = new LinkedList<>();
            channels.add(this._lhsChannel);
            channels.add(_rhsChannel);
            if (!pbx.waitForChannelsToQuiescent(channels, 3000))
                throw new PBXException("Channel: " + this._lhsChannel + " cannot be held as it is still in transition.");
            try
            {
                if (pbx.isBridgeSupported())
                {

                    this._bridgeLatch = new CountDownLatch(1);
                    final AgiChannelActivityBridge action = new AgiChannelActivityBridge(_lhsChannel);
                    _lhsChannel.setCurrentActivityAction(new AgiChannelActivityHoldForBridge(action));
                    _rhsChannel.setCurrentActivityAction(action);

                    // now wait for the bridge event to arrive.
                    success = this._bridgeLatch.await(3, TimeUnit.SECONDS);

                }
                else if (pbx.isMeetmeInstalled())
                {
                    // As bridge isn't supported we need to do the bridge using
                    // a
                    // meetme
                    // room

                    final RedirectToMeetMe meetmeTransfer = new RedirectToMeetMe();
                    // meetme room is empty
                    // find a new available room.
                    MeetmeRoom room = pbx.acquireMeetmeRoom(new DurationRoomOwner(30, TimeUnit.SECONDS));

                    // redirect the lhs to meetme
                    meetmeTransfer.redirectToMeetme(_lhsChannel, room.getRoomNumber(), true, false);

                    // redirect destination this is not the marked call
                    // as we want this to be hung up if no other calls are left
                    // in
                    // this
                    // room
                    final boolean markedCall = !_rhsChannel.canDetectHangup();
                    meetmeTransfer.redirectToMeetme(_rhsChannel, room.getRoomNumber(), markedCall, false);

                    // TODO put some logic to check that the meetme actually
                    // completes.
                    success = true;

                }
                else
                    throw new PBXException("Cannot bridge two calls as neither the BridgeAction nor Meetme is available");
            }
            catch (IllegalArgumentException | IllegalStateException | InterruptedException e)
            {
                throw new PBXException(e);
            }

        }
        catch (IllegalArgumentException | IllegalStateException e)
        {
            BridgeActivityImpl.logger.error(e, e);
            BridgeActivityImpl.logger.error(e, e);
            throw new PBXException(e);
        }
        return success;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();
        required.add(StatusCompleteEvent.class);
        required.add(StatusEvent.class);
        required.add(BridgeEvent.class);
        required.add(LinkEvent.class);
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

        if (event instanceof BridgeEvent)
        {
            BridgeEvent bridge = (BridgeEvent) event;
            if (bridge.isLink()
                    && (bridge.getChannel1().isSame(this._lhsChannel) || bridge.getChannel1().isSame(this._rhsChannel))
                    && (bridge.getChannel2().isSame(this._lhsChannel) || bridge.getChannel2().isSame(this._rhsChannel)))
                this._bridgeLatch.countDown();
        }
    }
}
