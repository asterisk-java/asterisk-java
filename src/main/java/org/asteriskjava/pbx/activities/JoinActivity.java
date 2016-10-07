package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.Call;

public interface JoinActivity extends Activity
{
	Call getJoinedCall();

}
