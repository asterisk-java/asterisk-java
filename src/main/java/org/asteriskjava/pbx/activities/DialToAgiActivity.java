package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;

public interface DialToAgiActivity extends Activity
{

	void markCancelled();

	boolean cancelledByOperator();

	EndPoint getOriginatingEndPoint();

	Channel getOriginatingChannel();

}
