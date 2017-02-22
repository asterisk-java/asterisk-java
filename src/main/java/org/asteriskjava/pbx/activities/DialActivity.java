package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;

public interface DialActivity extends Activity
{
	/**
	 * Returns the end point which originated the dial.
	 */
	EndPoint getOriginatingEndPoint();

	/**
	 * Returns the end point which is being dialed. This is available even
	 * before the dial commences.
	 */
	EndPoint getAcceptingEndPoint();

	/**
	 * Returns the 'originating' channel which is brought up as part of the dial
	 * activity. The 'originating' channel may return null if the channel is not
	 * up.
	 * 
	 * @return the 'originating' channel if it has been brought up otherwise
	 *         null.
	 */
	Channel getOriginatingChannel();

	/**
	 * Returns the 'accepting' channel which is brought up as part of the dial
	 * activity. The accepting party is the party that answers the call. The
	 * 'accepting' channel may return null if the channel is not up.
	 * 
	 * @return the 'accepting' channel if it has been brought up otherwise null
	 */
	Channel getAcceptingChannel();

	/**
	 * Call markCancelled if you want to stop a dial which is in progress from
	 * completing. This allows a user to cancel a dial.
	 */
	void markCancelled();

	/**
	 * Returns true if markCancelled was called during the dial attempt.
	 * @return
	 */
	boolean cancelledByOperator();

	Call getNewCall();
}
