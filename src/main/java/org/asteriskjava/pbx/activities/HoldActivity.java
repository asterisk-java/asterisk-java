package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.Channel;

public interface HoldActivity extends Activity
{
	// Returns the Channel that is on hold
	Channel getChannel();
}
