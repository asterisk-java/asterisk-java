package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.BridgeActivity;
import org.asteriskjava.pbx.agi.AgiChannelActivityBridge;
import org.asteriskjava.pbx.agi.AgiChannelActivityHoldForBridge;
import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusCompleteEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusEvent;
import org.asteriskjava.pbx.internal.asterisk.MeetmeRoom;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.managerAPI.RedirectToMeetMe;

public class BridgeActivityImpl extends ActivityHelper<BridgeActivity> implements BridgeActivity
{
	static Logger logger = Logger.getLogger(BridgeActivityImpl.class);
	private final Channel _lhsChannel;
	private final Channel _rhsChannel;

	private CountDownLatch _bridgeLatch;

	public BridgeActivityImpl(final Channel lhsChannel, final Channel rhsChannel,
			final ActivityCallback<BridgeActivity> callback)
	{
		super("Bridge", callback); //$NON-NLS-1$

		this._lhsChannel = lhsChannel;
		this._rhsChannel = rhsChannel;
		this.startActivity(true);
	}

	@Override
	public boolean doActivity() throws PBXException
	{
		boolean success = false;

		BridgeActivityImpl.logger.info("*******************************************************************************"); //$NON-NLS-1$
		BridgeActivityImpl.logger.info("***********                    begin Bridge                ****************"); //$NON-NLS-1$
		BridgeActivityImpl.logger.info("***********                  lhs:" + this._lhsChannel + " ****************"); //$NON-NLS-1$ //$NON-NLS-2$
		BridgeActivityImpl.logger.info("***********                  rhs:" + this._rhsChannel + " ****************"); //$NON-NLS-1$ //$NON-NLS-2$
		BridgeActivityImpl.logger.info("*******************************************************************************"); //$NON-NLS-1$
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
				throw new PBXException(
						"Channel: " + this._lhsChannel + " cannot be held as it is still in transition.");
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
					MeetmeRoom room = pbx.acquireMeetmeRoom();

					// redirect the lhs to meetme
					meetmeTransfer.redirectToMeetme(_lhsChannel, room.getRoomNumber(), true);

					// redirect destination this is not the marked call
					// as we want this to be hung up if no other calls are left
					// in
					// this
					// room
					final boolean markedCall = !_rhsChannel.canDetectHangup();
					meetmeTransfer.redirectToMeetme(_rhsChannel, room.getRoomNumber(), markedCall);

					// TODO put some logic to check that the meetme actually
					// completes.
					success = true;

				}
				else
					throw new PBXException(
							"Cannot bridge two calls as neither the BridgeAction nor Meetme is available"); //$NON-NLS-1$
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
	public HashSet<Class<? extends ManagerEvent>> requiredEvents()
	{
		HashSet<Class<? extends ManagerEvent>> required = new HashSet<>();
		required.add(StatusCompleteEvent.class);
		required.add(StatusEvent.class);
		required.add(BridgeEvent.class);
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
